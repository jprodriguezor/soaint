package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.PlantillaMetadatoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaMetadatosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Ene-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PlantillaMetadatoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param idePlantilla
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PlantillaMetadatosDTO listarMetadatos(BigInteger idePlantilla)throws SystemException {
        try {
            List<PlantillaMetadatoDTO> metadatos = em.createNamedQuery("TvsPlantillaMestadato.findByIdePlantilla", PlantillaMetadatoDTO.class)
                    .setParameter("IDE_PLANTILLA", idePlantilla)
                    .getResultList();
            return PlantillaMetadatosDTO.newInstance()
                    .metadato(metadatos)
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
