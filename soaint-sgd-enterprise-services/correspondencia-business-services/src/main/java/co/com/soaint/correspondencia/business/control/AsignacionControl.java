package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
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
@Log4j2
public class AsignacionControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DctAsignacionControl dctAsignacionControl;

    @Autowired
    DctAsigUltimoControl dctAsigUltimoControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;
    // ----------------------

    /**
     * @param asignacionesDTO
     * @return
     * @throws SystemException
     */
    public AsignacionesDTO asignarCorrespondencia(AsignacionesDTO asignacionesDTO) throws BusinessException, SystemException {
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
                            .build();
                }

                dctAsigUltimo.setDctAsignacion(dctAsignacion);
                dctAsigUltimo.setCorAgente(corAgente);
                dctAsigUltimo.setCorCorrespondencia(corCorrespondencia);
                dctAsigUltimo.setIdeUsuarioCambio(usuarioCambio);

                dctAsignacion.setFecAsignacion(fecha);
                dctAsignacion.setCorAgente(corAgente);
                dctAsignacion.setCorCorrespondencia(corCorrespondencia);
                dctAsignacion.setIdeUsuarioCreo(usuarioCreo);

                em.persist(dctAsignacion);
                em.merge(dctAsigUltimo);
                em.flush();

                em.createNamedQuery("CorAgente.updateAsignacion")
                        .setParameter("FECHA_ASIGNACION", fecha)
                        .setParameter("COD_ESTADO", EstadoAgenteEnum.ASIGNADO.getCodigo())
                        .setParameter("IDE_AGENTE", corAgente.getIdeAgente())
                        .executeUpdate();

                AsignacionDTO asignacionDTOResult = em.createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class)
                        .setParameter("IDE_AGENTE", corAgente.getIdeAgente())
                        .setParameter("COD_ESTADO", EstadoAgenteEnum.ASIGNADO.getCodigo())
                        .getSingleResult();
                asignacionDTOResult.setLoginName(asignacionDTO.getLoginName());
                asignacionDTOResult.setAlertaVencimiento(calcularAlertaVencimiento(
                        correspondenciaControl.consultarFechaVencimientoByIdeDocumento(asignacionDTO.getIdeDocumento()),
                        fecha)
                );
                asignacionesDTOResult.getAsignaciones().add(asignacionDTOResult);
            }
            return asignacionesDTOResult;
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
     * @param asignacion
     * @throws BusinessException
     * @throws SystemException
     */
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
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("asignacion.asignacion_not_exist_by_ideAsignacion")
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
     * @param ideFunci
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
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
     * @param numRedirecciones
     * @param ideAsigUltimo
     */
    public void actualizarNumRedirecciones(String numRedirecciones, BigInteger ideAsigUltimo) throws SystemException {
        try {
            em.createNamedQuery("DctAsigUltimo.updateNumRedirecciones")
                    .setParameter("NUM_REDIRECCIONES", numRedirecciones)
                    .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                    .executeUpdate();
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
     * @param asignacion
     * @throws SystemException
     */
    public void actualizarTipoProceso(AsignacionDTO asignacion) throws SystemException {
        try {
            em.createNamedQuery("DctAsigUltimo.updateTipoProceso")
                    .setParameter("COD_TIPO_PROCESO", asignacion.getCodTipProceso())
                    .setParameter("IDE_ASIG_ULTIMO", asignacion.getIdeAsigUltimo())
                    .executeUpdate();
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
    public AsignacionDTO consultarAsignacionByIdeAgente(BigInteger ideAgente) throws SystemException {
        try {
            List<AsignacionDTO> asignacionDTOList = em.createNamedQuery("DctAsigUltimo.consultarByIdeAgente", AsignacionDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getResultList();
            return asignacionDTOList.isEmpty() ? null : asignacionDTOList.get(0);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private String calcularAlertaVencimiento(Date fechaVencimientoTramite, Date fechaAsignacion){
        int diferenciaMinutos = (int) ChronoUnit.MINUTES.between(convertToLocalDateTime(fechaAsignacion),
                convertToLocalDateTime(fechaVencimientoTramite));
        return  String.valueOf(diferenciaMinutos / 60).concat("h").concat(String.valueOf(diferenciaMinutos % 60)).concat("m");
    }

    private LocalDateTime convertToLocalDateTime(Date fecha){
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        return LocalDateTime.of(calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DATE),
                calendario.get(Calendar.HOUR),
                calendario.get(Calendar.MINUTE),
                calendario.get(Calendar.SECOND)
        );
    }
}
