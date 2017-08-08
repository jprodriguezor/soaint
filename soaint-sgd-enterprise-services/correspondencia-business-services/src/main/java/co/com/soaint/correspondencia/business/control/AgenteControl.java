package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class AgenteControl {

    private static Logger logger = LogManager.getLogger(AgenteControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AsignacionControl asignacionControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Value("${radicado.max.num.redirecciones}")
    private int numMaxRedirecciones;

    /**
     *
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByIdeAgente(agenteDTO.getIdeAgente())){
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorAgente.updateEstado")
                    .setParameter("COD_ESTADO", agenteDTO.getCodEstado())
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .executeUpdate();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param agentesDTO
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(AgentesDTO agentesDTO) throws SystemException {
        try {
            String codSede;
            String codDependencia;
            int numRedirecciones = 0;

            for (AgenteDTO agenteDTO : agentesDTO.getAgentes()) {
                codSede = agenteDTO.getCodSede();
                codDependencia = agenteDTO.getCodDependencia();
                AsignacionDTO asignacion = asignacionControl.consultarAsignacionByIdeAgente(agenteDTO.getIdeAgente());
                if (asignacion != null) {
                    if (asignacion.getNumRedirecciones() != null)
                        numRedirecciones = Integer.parseInt(asignacion.getNumRedirecciones());
                    if (numRedirecciones == numMaxRedirecciones) {
                        CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByNroRadicado(asignacion.getNroRadicado());
                        codSede = correspondencia.getCodSede();
                        codDependencia = correspondencia.getCodDependencia();
                    }
                    numRedirecciones++;
                    asignacionControl.actualizarNumRedirecciones(Integer.toString(numRedirecciones), asignacion.getIdeAsigUltimo());
                }

                em.createNamedQuery("CorAgente.redireccionarCorrespondencia")
                        .setParameter("COD_SEDE", codSede)
                        .setParameter("COD_DEPENDENCIA", codDependencia)
                        .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                        .executeUpdate();
            }
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param ideAgente
     * @return
     */
    public Boolean verificarByIdeAgente(BigInteger ideAgente) {
        long cantidad = em.createNamedQuery("CorAgente.countByIdeAgente", Long.class)
                .setParameter("IDE_AGENTE", ideAgente)
                .getSingleResult();
        return cantidad > 0;
    }

    /**
     *
     * @param corAgente
     * @param datosContactoDTOList
     */
    public static void asignarDatosContacto(CorAgente corAgente, List<DatosContactoDTO> datosContactoDTOList){
        DatosContactoControl datosContactoControl = new DatosContactoControl();
        for (DatosContactoDTO datosContactoDTO : datosContactoDTOList) {
            TvsDatosContacto datosContacto = datosContactoControl.datosContactoTransform(datosContactoDTO);
            datosContacto.setCorAgente(corAgente);
            corAgente.getTvsDatosContactoList().add(datosContacto);
        }
    }

    /**
     *
     * @param idDocumento
     * @return
     */
    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) {
        return em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
    }

    /**
     *
     * @param agenteDTO
     * @return
     */
    public CorAgente corAgenteTransform(AgenteDTO agenteDTO) {
        return CorAgente.newInstance()
                .ideAgente(agenteDTO.getIdeAgente())
                .codTipoRemite(agenteDTO.getCodTipoRemite())
                .codTipoPers(agenteDTO.getCodTipoPers())
                .nombre(agenteDTO.getNombre())
                .razonSocial(agenteDTO.getRazonSocial())
                .nit(agenteDTO.getNit())
                .codCortesia(agenteDTO.getCodCortesia())
                .codEnCalidad(agenteDTO.getCodEnCalidad())
                .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                .codSede(agenteDTO.getCodSede())
                .codDependencia(agenteDTO.getCodDependencia())
                .codEstado(agenteDTO.getCodEstado())
                .fecAsignacion(agenteDTO.getFecAsignacion())
                .codTipAgent(agenteDTO.getCodTipAgent())
                .indOriginal(agenteDTO.getIndOriginal())
                .tvsDatosContactoList(new ArrayList<>())
                .build();
    }
}
