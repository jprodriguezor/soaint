package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class DatosContactoControl {
    public TvsDatosContacto datosContactoTransform(DatosContactoDTO datosContactoDTO){
        return TvsDatosContacto.newInstance()
                .ideContacto(datosContactoDTO.getIdeContacto())
                .nroViaGeneradora(datosContactoDTO.getNroViaGeneradora())
                .nroPlaca(datosContactoDTO.getNroPlaca())
                .codTipoVia(datosContactoDTO.getCodTipoVia())
                .codPrefijoCuadrant(datosContactoDTO.getCodPrefijoCuadrant())
                .codPostal(datosContactoDTO.getCodPostal())
                .direccion(datosContactoDTO.getDireccion())
                .celular(datosContactoDTO.getCelular())
                .telFijo1(datosContactoDTO.getTelFijo1())
                .telFijo2(datosContactoDTO.getTelFijo2())
                .extension1(datosContactoDTO.getExtension1())
                .extension2(datosContactoDTO.getExtension2())
                .corrElectronico(datosContactoDTO.getCorrElectronico())
                .codPais(datosContactoDTO.getCodPais())
                .codDepartamento(datosContactoDTO.getCodDepartamento())
                .codMunicipio(datosContactoDTO.getCodMunicipio())
                .provEstado(datosContactoDTO.getProvEstado())
                .ciudad(datosContactoDTO.getCiudad())
                .build();
    }
}
