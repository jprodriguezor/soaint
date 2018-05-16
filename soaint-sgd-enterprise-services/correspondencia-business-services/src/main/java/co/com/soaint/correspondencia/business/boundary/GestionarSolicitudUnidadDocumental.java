package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.SolicitudUnidadDocumentalControl;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarSolicitudUnidadDocumental {
    // [fields] -----------------------------------

    @Autowired
    SolicitudUnidadDocumentalControl control;
    // ----------------------

    /**
     *
     * @param unidadesDocumentalDTO
     * * @throws BusinessException
     * @throws SystemException
     */
    public Boolean crearSolicitudUnidadDocumental(SolicitudesUnidadDocumentalDTO unidadesDocumentalDTO)throws SystemException, BusinessException{
       return control.crearSolicitudUnidadDocumental(unidadesDocumentalDTO);
    }

    /**
     *
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(Date fechaIni, Date fechaFin, String codSede, String codDependencia) throws BusinessException, SystemException {
        return control.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaIni, fechaFin, codSede, codDependencia);
    }

    /**
     *
     * @param fechaIni
     * @param ideSolicitante
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(Date fechaIni, String ideSolicitante, String codSede, String codDependencia) throws BusinessException, SystemException {
        return control.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(fechaIni, ideSolicitante, codSede, codDependencia);
    }

    /**
     *
     * @param fechaIni
     * @param ideSolicitante
     * @param codDependencia
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(Date fechaIni, String ideSolicitante, String codSede, String codDependencia) throws BusinessException, SystemException {
        return control.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(fechaIni, ideSolicitante, codSede, codDependencia);
    }

    /**
     * @param solicitudUnidadDocumentalDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        return control.actualizarSolicitudUnidadDocumental(solicitudUnidadDocumentalDTO);
    }

}
