package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 03-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class AsignacionControl {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(AsignacionControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DctAsignacionControl dctAsignacionControl;

    @Autowired
    DctAsigUltimoControl dctAsigUltimoControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;
    // ----------------------

    public AsignacionesDTO asignarCorrespondencia(AsignacionesDTO asignacionesDTO) throws SystemException {
        AsignacionesDTO asignacionesDTOResult = AsignacionesDTO.newInstance()
                .asignaciones(new ArrayList<>())
                .build();
        try {
            for (AsignacionDTO asignacionDTO : asignacionesDTO.getAsignaciones()) {
                CorAgente corAgente = CorAgente.newInstance()
                        .ideAgente(asignacionDTO.getIdeAgente())
                        .build();
                CorCorrespondencia corCorrespondencia = CorCorrespondencia.newInstance()
                        .ideDocumento(asignacionDTO.getIdeDocumento())
                        .build();

                Date fecha = new Date();
                String usuarioCreo = "0"; //TODO
                Long usuarioCambio = Long.parseLong("0"); //TODO

                DctAsignacion dctAsignacion = dctAsignacionControl.dctAsignacionTransform(asignacionDTO);
                DctAsigUltimo dctAsigUltimo;

                List<DctAsigUltimo> dctAsigUltimoList = em.createNamedQuery("DctAsigUltimo.findByIdeAgente", DctAsigUltimo.class)
                        .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                        .getResultList();

                if (!dctAsigUltimoList.isEmpty()) {
                    dctAsigUltimo = dctAsigUltimoList.get(0);
                } else {
                    dctAsigUltimo = DctAsigUltimo.newInstance()
                            .ideUsuarioCreo(usuarioCreo)
                            .fecCreo(fecha)
                            .build();
                }

                dctAsigUltimo.setDctAsignacion(dctAsignacion);
                dctAsigUltimo.setCorAgente(corAgente);
                dctAsigUltimo.setCorCorrespondencia(corCorrespondencia);
                dctAsigUltimo.setIdeUsuarioCambio(usuarioCambio);
                dctAsigUltimo.setFecCambio(fecha);

                dctAsignacion.setFecAsignacion(fecha);
                dctAsignacion.setCorAgente(corAgente);
                dctAsignacion.setCorCorrespondencia(corCorrespondencia);
                dctAsignacion.setIdeUsuarioCreo(usuarioCreo);
                dctAsignacion.setFecCreo(fecha);//TODO

                em.persist(dctAsignacion);
                em.merge(dctAsigUltimo);
                em.flush();

                em.createNamedQuery("CorAgente.updateAsignacion")
                        .setParameter("FECHA_ASIGNACION", fecha)
                        .setParameter("COD_ESTADO", EstadoCorrespondenciaEnum.ASIGNADO.getCodigo())
                        .setParameter("IDE_AGENTE", corAgente.getIdeAgente())
                        .executeUpdate();

                AsignacionDTO asignacionDTOResult = em.createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class)
                        .setParameter("IDE_AGENTE", corAgente.getIdeAgente())
                        .setParameter("COD_ESTADO", EstadoCorrespondenciaEnum.ASIGNADO.getCodigo())
                        .getSingleResult();
                asignacionDTOResult.setLoginName(asignacionDTO.getLoginName());
                asignacionesDTOResult.getAsignaciones().add(asignacionDTOResult);
            }
            return asignacionesDTOResult;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void actualizarIdInstancia(AsignacionDTO asignacion) throws BusinessException, SystemException {
        try {
            DctAsigUltimo dctAsigUltimo = em.createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class)
                    .setParameter("IDE_ASIGNACION", asignacion.getIdeAsignacion())
                    .getSingleResult();
            em.createNamedQuery("DctAsigUltimo.updateIdInstancia")
                    .setParameter("IDE_ASIG_ULTIMO", dctAsigUltimo.getIdeAsigUltimo())
                    .setParameter("ID_INSTANCIA", asignacion.getIdInstancia())
                    .executeUpdate();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("asignacion.asignacion_not_exist_by_ideAsignacion")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(BigInteger ideFunci, String nroRadicado) throws BusinessException, SystemException {
        try {
            List<AsignacionDTO> asignacionDTOList = em.createNamedQuery("DctAsigUltimo.findByIdeFunciAndNroRadicado", AsignacionDTO.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();
            if (asignacionDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("asignacion.not_exist_by_idefuncionario_and_nroradicado")
                        .buildBusinessException();
            }
            return AsignacionesDTO.newInstance().asignaciones(asignacionDTOList).build();
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


    public void actualizarNumRedirecciones(String numRedirecciones, BigInteger ideAsigUltimo) {
        em.createNamedQuery("DctAsigUltimo.updateNumRedirecciones")
                .setParameter("NUM_REDIRECCIONES", numRedirecciones)
                .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                .executeUpdate();
    }

    public AsignacionDTO consultarAsignacionByIdeAgente(BigInteger ideAgente){
        List<AsignacionDTO> asignacionDTOList = em.createNamedQuery("DctAsigUltimo.consultarByIdeAgente", AsignacionDTO.class)
                .setParameter("IDE_AGENTE", ideAgente)
                .getResultList();
        return asignacionDTOList.isEmpty() ? null : asignacionDTOList.get(0);
    }
}
