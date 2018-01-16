package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
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
public class PlantillaControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlantillaMetadatoControl plantillaMetadatoControl;

    /**
     * @param codTipoDoc
     * @param estado
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PlantillaDTO consultarPlantillaByCodClasificacionAndEstaddo(String codTipoDoc, String estado) throws SystemException, BusinessException {
        try {
            PlantillaDTO plantilla = em.createNamedQuery("TvsPlantilla.findByCodClasificacionAndEstado", PlantillaDTO.class)
                    .setParameter("COD_TIPO_DOC", codTipoDoc)
                    .setParameter("ESTADO", estado)
                    .getSingleResult();
            plantilla.setMetadatos(plantillaMetadatoControl.listarMetadatos(plantilla.getIdePlantilla()));
            return plantilla;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("plantilla.plantilla_not_exist_by_codClasificacion_and_estado")
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
     *
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarTipologiasDocumentalesByEstado(String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsPlantilla.findTipoDocByEstado", ConstanteDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
