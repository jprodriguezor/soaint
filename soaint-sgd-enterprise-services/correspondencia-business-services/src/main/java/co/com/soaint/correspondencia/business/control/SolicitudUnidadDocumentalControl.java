package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
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
import java.util.ArrayList;
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
}
