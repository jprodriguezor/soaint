package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.oracle.jrockit.jfr.ContentType.Timestamp;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 17-Oct-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class SolicitudUnidadDocumentalControl {
    @PersistenceContext
    private EntityManager em;

    /**
      * @param solicitudesUnidadDocumentalDTO
      * @return
      * @throws SystemException
      * @throws BusinessException
      */
    public Boolean crearSolicitudUnidadDocumental(SolicitudesUnidadDocumentalDTO solicitudesUnidadDocumentalDTO) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        try {
            for (SolicitudUnidadDocumentalDTO s : solicitudesUnidadDocumentalDTO.getSolicitudesUnidadDocumentalDTOS()) {
                this.insertarSolicitudUnidadDocumental(s);
            }
            em.flush();
            return true;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
      * @param solicitudUnidadDocumental
      * @return
      * @throws SystemException
      * @throws BusinessException
      */
    public void insertarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        try {
            solicitudUnidadDocumental.setFechaHora(new Date());
            TvsSolicitudUnidadDocumental unidadDocumental = this.tvsSolicitudUnidadDocumentalTransform(solicitudUnidadDocumental);
            em.persist(unidadDocumental);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Error insertando Solicitud de Unidad Documental.")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param solicitudUnidadDocumental
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public TvsSolicitudUnidadDocumental tvsSolicitudUnidadDocumentalTransform(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws SystemException, BusinessException{
        try {
            return TvsSolicitudUnidadDocumental.newInstance()
                    .id(solicitudUnidadDocumental.getId())
                    .nombreUD(solicitudUnidadDocumental.getNombreUnidadDocumental())
                    .accion(solicitudUnidadDocumental.getAccion())
                    .codDependencia(solicitudUnidadDocumental.getCodigoDependencia())
                    .codSede(solicitudUnidadDocumental.getCodigoSede())
                    .codSerie(solicitudUnidadDocumental.getCodigoSerie())
                    .codSubserie(solicitudUnidadDocumental.getCodigoSubSerie())
                    .descriptor1(solicitudUnidadDocumental.getDescriptor1())
                    .descriptor2(solicitudUnidadDocumental.getDescriptor2())
                    .estado(solicitudUnidadDocumental.getEstado())
                    .fecHora(solicitudUnidadDocumental.getFechaHora())
                    .idConstante(solicitudUnidadDocumental.getIdConstante())
                    .idSolicitante(solicitudUnidadDocumental.getIdSolicitante())
                    .nro(solicitudUnidadDocumental.getNro())
                    .observaciones(solicitudUnidadDocumental.getObservaciones())
                    .motivo(solicitudUnidadDocumental.getMotivo())
                    .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("Error convirtiendo Solicitud de Unidad Documental.")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codSede
     * @param codDependencia
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(Date fechaIni, Date fechaFin, String codSede, String codDependencia) throws BusinessException, SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo");

            if (fechaIni != null && fechaFin != null) {
                if(fechaIni.getTime() > fechaFin.getTime() || fechaIni.getTime() == fechaFin.getTime())
                    throw ExceptionBuilder.newBuilder()
                            .withMessage("La fecha final no puede ser igual o menor que la fecha inicial.")
                            .buildBusinessException();
            }

            Timestamp fecIn = fechaIni == null ? null : new Timestamp(fechaIni.getTime());
            Timestamp fecFin = fechaFin == null ? null : new Timestamp(fechaFin.getTime());

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", SolicitudUnidadDocumentalDTO.class)
                    .setParameter("FECHA_INI", fecIn)
                    .setParameter("FECHA_FIN", fecFin)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_DEP", codDependencia)
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("BD query Error obteniendo las solicitudes.")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param fechaSolicitud
     * @param ideSolicitante
     * @param codSede
     * @param codDependencia
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(Date fechaSolicitud, String ideSolicitante,String codSede, String codDependencia) throws BusinessException, SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante");


            Date fechaIni = null;
            Date fechaFin = null;

            if(fechaSolicitud != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaSolicitud);
                calendar.add(Calendar.DATE, 1);

                fechaIni = new Date(fechaSolicitud.getTime());;
                fechaFin = calendar.getTime();;
            }

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitante", SolicitudUnidadDocumentalDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.TIMESTAMP)
                    .setParameter("FECHA_FIN", fechaFin, TemporalType.TIMESTAMP)
                    .setParameter("ID_SOL", ideSolicitante)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_DEP", codDependencia)
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("BD query Error obteniendo las solicitudes.")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param fechaSolicitud
     * @param ideSolicitante
     * @param codSede
     * @param codDependencia
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(Date fechaSolicitud, String ideSolicitante,String codSede, String codDependencia) throws BusinessException, SystemException {
        try {
            log.info("Se entra al metodo obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante");


            Date fechaIni = null;
            Date fechaFin = null;

            if(fechaSolicitud != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaSolicitud);
                calendar.add(Calendar.DATE, 1);

                fechaIni = new Date(fechaSolicitud.getTime());;
                fechaFin = calendar.getTime();;
            }

            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaSolicitanteSinTramitar", SolicitudUnidadDocumentalDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.TIMESTAMP)
                    .setParameter("FECHA_FIN", fechaFin, TemporalType.TIMESTAMP)
                    .setParameter("ID_SOL", ideSolicitante)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_DEP", codDependencia)
                    .getResultList();

            return SolicitudesUnidadDocumentalDTO.newInstance().solicitudesUnidadDocumentalDTOS(solicitudUnidadDocumentalDTOList).build();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("BD query Error obteniendo las solicitudes.")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param IdSolicitud
     * @return
     */
    public Boolean verificarByIdeSolicitud(BigInteger IdSolicitud) throws SystemException {
        try {
            long cantidad = em.createNamedQuery("TvsSolicitudUM.countByIdSolicitud", Long.class)
                    .setParameter("IDE_SOL", IdSolicitud)
                    .getSingleResult();
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param IdSolicitud
     * @return
     */
    public Boolean verificarByIdNombreUMSolicitud(BigInteger IdSolicitud, String nombreUD) throws SystemException {
        try {
            long cantidad = em.createNamedQuery("TvsSolicitudUM.countByIdNombreUDSolicitud", Long.class)
                    .setParameter("IDE_SOL", IdSolicitud)
                    .setParameter("NOM_UD", nombreUD)
                    .getSingleResult();
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizarSolicitudUnidadDocumental");

        if (!verificarByIdeSolicitud(solicitudUnidadDocumentalDTO.getIdSolicitud())) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("solicitud.solicitud_not_exist_by_id")
                    .buildBusinessException();
        }
        try{
                em.createNamedQuery("TvsSolicitudUM.actualizarSolicitudUnidadDocumental")
                .setParameter("IDE_SOL", solicitudUnidadDocumentalDTO.getIdSolicitud())
                .setParameter("ID", solicitudUnidadDocumentalDTO.getId())
                .setParameter("ID_CONST", solicitudUnidadDocumentalDTO.getIdConstante())
                .setParameter("FECH", solicitudUnidadDocumentalDTO.getFechaHora())
                .setParameter("NOMBREUD", solicitudUnidadDocumentalDTO.getNombreUnidadDocumental())
                .setParameter("DESCP1", solicitudUnidadDocumentalDTO.getDescriptor1())
                .setParameter("DESCP2", solicitudUnidadDocumentalDTO.getDescriptor2())
                .setParameter("NRO", solicitudUnidadDocumentalDTO.getNro())
                .setParameter("COD_SER", solicitudUnidadDocumentalDTO.getCodigoSerie())
                .setParameter("COD_SUBS", solicitudUnidadDocumentalDTO.getCodigoSubSerie())
                .setParameter("COD_SED", solicitudUnidadDocumentalDTO.getCodigoSede())
                .setParameter("COD_DEP", solicitudUnidadDocumentalDTO.getCodigoDependencia())
                .setParameter("ID_SOL", solicitudUnidadDocumentalDTO.getIdSolicitante())
                .setParameter("EST", solicitudUnidadDocumentalDTO.getEstado())
                .setParameter("ACC", solicitudUnidadDocumentalDTO.getAccion())
                .setParameter("OBS", solicitudUnidadDocumentalDTO.getObservaciones())
                .setParameter("MOT", solicitudUnidadDocumentalDTO.getMotivo())
                .executeUpdate();

                return solicitudUnidadDocumentalDTO;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
