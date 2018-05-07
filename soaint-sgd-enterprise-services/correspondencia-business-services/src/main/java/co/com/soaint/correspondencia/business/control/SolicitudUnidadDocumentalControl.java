package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

            if (fechaIni != null && fechaIni != null) {
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

            if (solicitudUnidadDocumentalDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.not_exist_by_periodo_and_dependencia_and_sede")
                        .buildBusinessException();
            }

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

    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizarSolicitudUnidadDocumental");

        try{
            TvsSolicitudUnidadDocumental unidadDocumental = TvsSolicitudUnidadDocumental.newInstance()
                    .ideSolicitud(solicitudUnidadDocumentalDTO.getIdSolicitud())
                    .id(solicitudUnidadDocumentalDTO.getId())
                    .nro(solicitudUnidadDocumentalDTO.getNro())
                    .idSolicitante(solicitudUnidadDocumentalDTO.getIdSolicitante())
                    .idConstante(solicitudUnidadDocumentalDTO.getIdConstante())
                    .fecHora(solicitudUnidadDocumentalDTO.getFechaHora())
                    .estado(solicitudUnidadDocumentalDTO.getEstado())
                    .descriptor2(solicitudUnidadDocumentalDTO.getDescriptor2())
                    .descriptor1(solicitudUnidadDocumentalDTO.getDescriptor1())
                    .codSubserie(solicitudUnidadDocumentalDTO.getCodigoSubSerie())
                    .codSerie(solicitudUnidadDocumentalDTO.getCodigoSerie())
                    .codSede(solicitudUnidadDocumentalDTO.getCodigoSede())
                    .codDependencia(solicitudUnidadDocumentalDTO.getCodigoDependencia())
                    .accion(solicitudUnidadDocumentalDTO.getAccion())
                    .nombreUD(solicitudUnidadDocumentalDTO.getNombreUnidadDocumental())
                    .observaciones(solicitudUnidadDocumentalDTO.getObservaciones())
                    .build();

            em.merge(unidadDocumental);
            em.flush();

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
