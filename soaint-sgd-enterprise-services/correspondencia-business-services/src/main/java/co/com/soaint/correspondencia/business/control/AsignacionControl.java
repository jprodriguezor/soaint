package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    FuncionariosControl funcionariosControl;

    @Autowired
    AgenteControl agenteControl;

    @Value("${radicado.cant.horas.para.activar.alerta.vencimiento}")
    private int cantHorasParaActivarAlertaVencimiento;

    // ----------------------

    /**
     * @param asignacionTramite
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public AsignacionesDTO asignarCorrespondencia(AsignacionTramiteDTO asignacionTramite) throws BusinessException, SystemException {
        AsignacionesDTO asignacionesDTOResult = AsignacionesDTO.newInstance()
                .asignaciones(new ArrayList<>())
                .build();
        try {
            for (AsignacionDTO asignacionDTO : asignacionTramite.getAsignaciones().getAsignaciones()) {
                Date fecha = new Date();

                DctAsignacionDTO dctAsignacion = em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)
                        .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                        .getSingleResult();

                em.createNamedQuery("DctAsignacion.asignarToFuncionario")
                        .setParameter("IDE_FUNCI", asignacionDTO.getIdeFunci())
                        .setParameter("IDE_ASIGNACION", dctAsignacion.getIdeAsignacion())
                        .executeUpdate();

                agenteControl.actualizarEstadoAgente(AgenteDTO.newInstance()
                        .ideAgente(asignacionDTO.getIdeAgente())
                        .codEstado(EstadoAgenteEnum.ASIGNADO.getCodigo())
                        .build());

                AsignacionDTO asignacionDTOResult = em.createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class)
                        .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncAsigDTO consultarAsignacionReasignarByIdeAgente(BigInteger ideAgente) throws SystemException {
        try {
            AsignacionDTO asignacion = em.createNamedQuery("DctAsigUltimo.consultarByIdeAgente", AsignacionDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
            asignacion.setLoginName(funcionariosControl.consultarLoginNameByIdeFunci(asignacion.getIdeFunci()));
            return FuncAsigDTO.newInstance()
                    .asignacion(asignacion)
                    .credenciales(funcionariosControl.consultarCredencialesByIdeFunci(asignacion.getIdeFunci()))
                    .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private String calcularAlertaVencimiento(Date fechaVencimientoTramite, Date fechaAsignacion) {
        int diferenciaMinutos = (int) ChronoUnit.MINUTES.between(convertToLocalDateTime(fechaAsignacion), convertToLocalDateTime(fechaVencimientoTramite));

        diferenciaMinutos -= (cantHorasParaActivarAlertaVencimiento * 60);

        return diferenciaMinutos > 0 ? String.valueOf(diferenciaMinutos / 60).concat("h").concat(String.valueOf(diferenciaMinutos % 60)).concat("m") : "0h0m";
    }

    private LocalDateTime convertToLocalDateTime(Date fecha) {
        return LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @param agentes
     * @param ideDocumento
     * @param codTipoAsignacion
     * @param ideFuncionario
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    public AsignacionesDTO conformarAsignaciones(List<AgenteDTO> agentes, BigInteger ideDocumento, String codTipoAsignacion,
                                                 BigInteger ideFuncionario) {
        AsignacionesDTO asignaciones = AsignacionesDTO.newInstance()
                .asignaciones(new ArrayList<>())
                .build();
        agentes.stream().forEach(agente ->
                        asignaciones.getAsignaciones().add(transfromAgenteToAsignacion(agente, ideDocumento, codTipoAsignacion, ideFuncionario))
        );
        return asignaciones;
    }

    private AsignacionDTO transfromAgenteToAsignacion(AgenteDTO agente, BigInteger ideDocumento, String codTipoAsignacion, BigInteger ideFuncionario) {
        return AsignacionDTO.newInstance()
                .ideAgente(agente.getIdeAgente())
                .codDependencia(agente.getCodDependencia())
                .ideDocumento(ideDocumento)
                .codTipAsignacion(codTipoAsignacion)
                .ideFunci(ideFuncionario)
                .build();
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    public void asignarDocumentoByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByNroRadicado(nroRadicado);
        List<AgenteDTO> destinatarios = agenteControl.listarDestinatariosByIdeDocumento(correspondencia.getIdeDocumento());
        AsignacionesDTO asignaciones = conformarAsignaciones(destinatarios, correspondencia.getIdeDocumento(), "CTA",
                new BigInteger(correspondencia.getCodFuncRadica()));
        asignarCorrespondencia(AsignacionTramiteDTO.newInstance()
                .asignaciones(asignaciones)
                .build());
    }

    /**
     * @param ideAgente
     * @param ideDocumento
     * @param codDependencia
     * @param ideFunci
     * @throws SystemException
     */
    public void actualizarAsignacion(BigInteger ideAgente, BigInteger ideDocumento, String codDependencia, BigInteger ideFunci) throws SystemException {
        try {
            log.error("ideAgente: " + ideAgente);
            log.error("ideDocumento: " + ideDocumento);
            log.error("codDependencia: " + codDependencia);
            log.error("ideFunci: " + ideFunci);
            DctAsigUltimo asignacionUltimo = getAsignacionUltimoByIdeAgente(ideAgente);

            CorCorrespondencia correspondencia = CorCorrespondencia.newInstance()
                    .ideDocumento(ideDocumento)
                    .build();

            CorAgente agente = CorAgente.newInstance()
                    .ideAgente(ideAgente)
                    .build();

            DctAsignacion asignacion = DctAsignacion.newInstance()
                    .corCorrespondencia(correspondencia)
                    .ideUsuarioCreo(String.valueOf(ideFunci))
                    .ideFunci(ideFunci)
                    .codDependencia(codDependencia)
                    .codTipAsignacion("CTA")
                    .fecAsignacion(new Date())
                    .corAgente(agente)
                    .build();

            asignacionUltimo.setNumRedirecciones(asignacionUltimo.getNumRedirecciones() + 1);
            asignacionUltimo.setIdeUsuarioCambio(ideFunci);
            asignacionUltimo.setFecCambio(new Date());
            asignacionUltimo.setDctAsignacion(asignacion);
            asignacionUltimo.setCorCorrespondencia(correspondencia);
            asignacionUltimo.setCorAgente(agente);

            em.persist(asignacion);
            em.merge(asignacionUltimo);
            em.flush();
            log.error("Actualizo la asignacion correctamente");

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
     * @param ideAgente
     * @param ideDocumento
     * @param ideFunci
     * @param causalDevolucion
     * @param observaciones
     * @throws SystemException
     */
    public void actualizarAsignacionFromDevolucion(BigInteger ideAgente, BigInteger ideDocumento, BigInteger ideFunci,
                                                   String causalDevolucion, String observaciones) throws SystemException {
        try {
            DctAsigUltimo asignacionUltimo = getAsignacionUltimoByIdeAgente(ideAgente);
            CorCorrespondencia correspondencia = CorCorrespondencia.newInstance()
                    .ideDocumento(ideDocumento)
                    .build();

            CorAgente agente = CorAgente.newInstance()
                    .ideAgente(ideAgente)
                    .build();

            DctAsignacion asignacion = em.createNamedQuery("DctAsignacion.findByIdeAsigUltimo", DctAsignacion.class)
                    .setParameter("IDE_ASIG_ULTIMO", asignacionUltimo.getIdeAsigUltimo())
                    .getSingleResult();

            asignacion.setCorAgente(agente);
            asignacion.setCorCorrespondencia(correspondencia);
            asignacion.setCodTipCausal(causalDevolucion);
            asignacion.setObservaciones(observaciones);

            asignacionUltimo.setNumDevoluciones(asignacionUltimo.getNumDevoluciones() + 1);
            asignacionUltimo.setIdeUsuarioCambio(ideFunci);
            asignacionUltimo.setFecCambio(new Date());
            asignacionUltimo.setDctAsignacion(asignacion);
            asignacionUltimo.setCorCorrespondencia(correspondencia);
            asignacionUltimo.setCorAgente(agente);

            em.merge(asignacion);
            em.merge(asignacionUltimo);
            em.flush();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public DctAsigUltimo getAsignacionUltimoByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("DctAsigUltimo.findByIdeAgente", DctAsigUltimo.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("asignacion.asignacion_not_exist_by_ideAgente")
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
}
