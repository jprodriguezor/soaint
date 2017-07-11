package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DctAsigUltimoControl;
import co.com.soaint.correspondencia.business.control.DctAsignacionControl;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarAsignacion {
    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarAsignacion.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DctAsignacionControl dctAsignacionControl;

    @Autowired
    DctAsigUltimoControl dctAsigUltimoControl;
    // ----------------------

    public void asignarCorrespondencia(AsignacionDTO asignacionDTO) throws BusinessException, SystemException {
        try {
            CorAgente corAgente = CorAgente.newInstance()
                    .ideAgente(asignacionDTO.getIdeAgente())
                    .build();
            CorCorrespondencia corCorrespondencia = CorCorrespondencia.newInstance()
                    .ideDocumento(asignacionDTO.getIdeDocumento())
                    .build();

            Date fecha = new Date();
            String usuarioCreo = "0"; //TODO
            Long usuarioCambio = Long.parseLong("0"); //TODO

            DctAsignacion dctAsignacion = dctAsignacionControl.dctAsignacionTransform(asignacionDTO.getDctAsignacion());
            DctAsigUltimo dctAsigUltimo;

            List<DctAsigUltimo> dctAsigUltimoList = em.createNamedQuery("DctAsigUltimo.findByIdeAgente", DctAsigUltimo.class)
                    .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                    .getResultList();

            if (dctAsigUltimoList.size() > 0) {
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

            dctAsignacion.setCorAgente(corAgente);
            dctAsignacion.setCorCorrespondencia(corCorrespondencia);
            dctAsignacion.setIdeUsuarioCreo(usuarioCreo);
            dctAsignacion.setFecCreo(fecha);//TODO

            em.persist(dctAsignacion);
            em.merge(dctAsigUltimo);
            em.flush();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void actualizarIdInstancia(Long ideAsignacion, String idInstancia) throws BusinessException, SystemException {
        try {
            DctAsigUltimo dctAsigUltimo = em.createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class)
                    .setParameter("IDE_ASIGNACION", ideAsignacion)
                    .getSingleResult();
            em.createNamedQuery("DctAsigUltimo.updateIdInstancia")
                    .setParameter("IDE_ASIG_ULTIMO", dctAsigUltimo.getIdeAsigUltimo())
                    .setParameter("ID_INSTANCIA", idInstancia)
                    .executeUpdate();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("asignacion.asignacion_not_exist_by_ideAsignacion")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(BigInteger ideFunci, String nroRadicado) throws BusinessException, SystemException {
        List<AsignacionDTO> asignacionDTOList = new ArrayList<>();
        try {
            List<DctAsigUltimoDTO> dctAsigUltimoDTOList = em.createNamedQuery("DctAsigUltimo.findByIdeFunciAndNroRadicado", DctAsigUltimoDTO.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? nroRadicado : "%" + nroRadicado + "%")
                    .getResultList();
            if (dctAsigUltimoDTOList.size() == 0){
                throw ExceptionBuilder.newBuilder()
                        .withMessage("asignacion.not_exist_by_idefuncionario_and_nroradicado")
                        .buildBusinessException();
            }
            for (DctAsigUltimoDTO dctAsigUltimoDTO : dctAsigUltimoDTOList){

                DctAsignacionDTO dctAsignacionDTO = em.createNamedQuery("DctAsignacion.findByIdeAsignacion", DctAsignacionDTO.class)
                        .setParameter("IDE_ASIGNACION", dctAsigUltimoDTO.getIdeAsignacion())
                        .getSingleResult();

                CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("DctAsigUltimo.findCorrespondenciaByIdeAsigUltimo", CorrespondenciaDTO.class)
                        .setParameter("IDE_ASIG_ULTIMO", dctAsigUltimoDTO.getIdeAsigUltimo())
                        .getSingleResult();

                AgenteDTO agenteDTO = em.createNamedQuery("DctAsigUltimo.findAgenteByIdeAsigUltimo", AgenteDTO.class)
                        .setParameter("IDE_ASIG_ULTIMO", dctAsigUltimoDTO.getIdeAsigUltimo())
                        .getSingleResult();

                AsignacionDTO asignacionDTO = AsignacionDTO.newInstance()
                        .dctAsignacion(dctAsignacionDTO)
                        .dctAsigUltimo(dctAsigUltimoDTO)
                        .ideAgente(agenteDTO.getIdeAgente())
                        .ideDocumento(correspondenciaDTO.getIdeDocumento())
                        .nroRadicado(correspondenciaDTO.getNroRadicado())
                        .build();
                asignacionDTOList.add(asignacionDTO);
            }
            return AsignacionesDTO.newInstance().asignaciones(asignacionDTOList).build();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
