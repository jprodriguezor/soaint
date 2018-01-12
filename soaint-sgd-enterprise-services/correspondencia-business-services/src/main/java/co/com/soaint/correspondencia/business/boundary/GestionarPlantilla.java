package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PlantillaControl;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 12-Ene-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarPlantilla {

    // [fields] -----------------------------------

    @Autowired
    PlantillaControl control;

    // ----------------------

    /**
     *
     * @param codClasificacion
     * @param estado
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    public PlantillaDTO consultarPlantillaByCodClasificacionAndEstaddo(String codClasificacion, String estado)throws SystemException, BusinessException {
        return control.consultarPlantillaByCodClasificacionAndEstaddo(codClasificacion, estado);
    }
}
