package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
@Log4j2
public class AgenteControl {

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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    public List<AgenteDTO> listarRemitentesByIdeDocumento(BigInteger ideDocumento)throws SystemException{
        try{
            return em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.REMITENTE.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param ideDocumento
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public List<AgenteDTO> listarDestinatarioByIdeDocumentoAndCodDependenciaAndCodEstado(BigInteger ideDocumento,
                                                                                   String codDependencia,
                                                                                   String codEstado)throws SystemException{
        try{
        return em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
                .setParameter("COD_ESTADO", codEstado)
                .setParameter("COD_DEPENDENCIA", codDependencia)
                .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                .setParameter("IDE_DOCUMENTO", ideDocumento)
                .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public AgenteDTO consultarAgenteByIdeAgente(BigInteger ideAgente)throws BusinessException, SystemException{
        try{
            return em.createNamedQuery("CorAgente.findByIdeAgente", AgenteDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("agente.agente_not_exist_by_ideAgente")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByIdeAgente(agenteDTO.getIdeAgente())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();
            }

            em.createNamedQuery("CorAgente.updateEstado")
                    .setParameter("COD_ESTADO", agenteDTO.getCodEstado())
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .executeUpdate();

            if (agenteDTO.getCodEstado().equals(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(agenteDTO.getIdeAgente());
                correspondencia.setCodEstado(EstadoCorrespondenciaEnum.ASIGNACION.getCodigo());
                correspondenciaControl.actualizarEstadoCorrespondencia(correspondencia);
            }
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param agentesDTO
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(AgentesDTO agentesDTO) throws SystemException {
        try {
            String codSede;
            String codDependencia;

            for (AgenteDTO agenteDTO : agentesDTO.getAgentes()) {
                codSede = agenteDTO.getCodSede();
                codDependencia = agenteDTO.getCodDependencia();
                AsignacionDTO asignacion = asignacionControl.consultarAsignacionByIdeAgente(agenteDTO.getIdeAgente());
                if (!puedeRedireccionar(asignacion)) {
                    CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByNroRadicado(asignacion.getNroRadicado());
                    codSede = correspondencia.getCodSede();
                    codDependencia = correspondencia.getCodDependencia();
                }

                em.createNamedQuery("CorAgente.redireccionarCorrespondencia")
                        .setParameter("COD_SEDE", codSede)
                        .setParameter("COD_DEPENDENCIA", codDependencia)
                        .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                        .executeUpdate();
            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
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
     * @param corAgente
     * @param datosContactoDTOList
     */
    public static void asignarDatosContacto(CorAgente corAgente, List<DatosContactoDTO> datosContactoDTOList) {
        DatosContactoControl datosContactoControl = new DatosContactoControl();
        for (DatosContactoDTO datosContactoDTO : datosContactoDTOList) {
            TvsDatosContacto datosContacto = datosContactoControl.datosContactoTransform(datosContactoDTO);
            datosContacto.setCorAgente(corAgente);
            corAgente.getTvsDatosContactoList().add(datosContacto);
        }
    }

    /**
     * @param idDocumento
     * @return
     */
    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) {
        return em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
    }

    /**
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

    /**
     * @param asignacion
     * @return
     */
    private boolean puedeRedireccionar(AsignacionDTO asignacion) throws SystemException {
        if (asignacion != null) {
            int numRedirecciones = 0;
            if (asignacion.getNumRedirecciones() != null)
                numRedirecciones = Integer.parseInt(asignacion.getNumRedirecciones());
            numRedirecciones++;
            asignacionControl.actualizarNumRedirecciones(Integer.toString(numRedirecciones), asignacion.getIdeAsigUltimo());
            return numRedirecciones <= numMaxRedirecciones;
        } else return true;
    }
}
