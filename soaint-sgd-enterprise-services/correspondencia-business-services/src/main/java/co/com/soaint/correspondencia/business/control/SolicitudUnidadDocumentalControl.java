package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
      * @param solicitudUnidadDocumental
      * @return
      * @throws SystemException
      * @throws BusinessException
      */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SolicitudUnidadDocumentalDTO crearSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        try {
            TvsSolicitudUnidadDocumental unidadDocumental = this.tvsSolicitudUnidadDocumentalTransform(solicitudUnidadDocumental);

            em.persist(unidadDocumental);
            em.flush();

            return solicitudUnidadDocumental;
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
     * @throws BusinessException
     * @throws SystemException
     */
    public TvsSolicitudUnidadDocumental tvsSolicitudUnidadDocumentalTransform(SolicitudUnidadDocumentalDTO solicitudUnidadDocumental) throws SystemException, BusinessException{
        try {
            return TvsSolicitudUnidadDocumental.newInstance()
                    .ideSolicitud(solicitudUnidadDocumental.getIdSolicitud())
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
                    .withMessage("system.generic.error")
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(Date fechaIni, Date fechaFin, String codSede, String codDependencia) throws BusinessException, SystemException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFin);
            cal.add(Calendar.DATE, 1);
            List<SolicitudUnidadDocumentalDTO> solicitudUnidadDocumentalDTOList = em.createNamedQuery("TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", SolicitudUnidadDocumentalDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", cal.getTime(), TemporalType.DATE)
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
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
