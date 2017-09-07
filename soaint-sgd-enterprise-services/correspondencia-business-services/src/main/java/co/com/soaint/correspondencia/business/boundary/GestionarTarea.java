package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.TareaControl;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarTarea {

    @Autowired
    TareaControl control;

    /**
     *
     * @param tarea
     * @throws SystemException
     */
    public void guardarEstadoTarea(TareaDTO tarea)throws SystemException{
        control.guardarEstadoTarea(tarea);
    }

    /**
     *
     * @param idInstanciaProceso
     * @param idTareaProceso
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public TareaDTO listarEstadoTarea(String idInstanciaProceso, String idTareaProceso)throws BusinessException, SystemException{
        return control.listarEstadoTarea(idInstanciaProceso, idTareaProceso);
    }
}
