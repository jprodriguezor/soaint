package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class CorrespondenciaControl {
    public CorCorrespondencia corCorrespondenciaTransform(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        CorCorrespondencia correspondencia = CorCorrespondencia.newInstance()
                .descripcion(correspondenciaDTO.getDescripcion())
                .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta())
                .codUnidadTiempo(correspondenciaDTO.getCodUnidadTiempo())
                .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                .fecRadicado(correspondenciaDTO.getFecRadicado())
                .nroRadicado(correspondenciaDTO.getNroRadicado())
                .codTipoCmc(correspondenciaDTO.getCodTipoCmc())
                .ideInstancia(correspondenciaDTO.getIdeInstancia())
                .reqDistFisica(correspondenciaDTO.getReqDistFisica())
                .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                .codSede(correspondenciaDTO.getCodSede())
                .codDependencia(correspondenciaDTO.getCodDependencia())
                .reqDigita(correspondenciaDTO.getReqDigita())
                .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                .nroGuia(correspondenciaDTO.getNroGuia())
                .fecVenGestion(correspondenciaDTO.getFecVenGestion())
                .codEstado(correspondenciaDTO.getCodEstado())
                .corAgenteList(new ArrayList<>())
                .ppdDocumentoList(new ArrayList<>())
                .corReferidoList(new ArrayList<>())
                .build();
        return correspondencia;
    }

    public String generarNumeroRadicado(String sede, String tipoComunicacion) {//TODO
        Random rand = new Random();
        int  n = rand.nextInt(99999) + 1;
        String consecutivo = String.format("%05d", n);
        int anno = Calendar.getInstance().get(Calendar.YEAR);
        String numeroRadicado = "";
        numeroRadicado = numeroRadicado
                .concat(sede)
                .concat("-")
                .concat(tipoComunicacion)
                .concat("-")
                .concat(String.valueOf(anno))
                .concat("-")
                .concat(consecutivo);
        return numeroRadicado;
    }
}
