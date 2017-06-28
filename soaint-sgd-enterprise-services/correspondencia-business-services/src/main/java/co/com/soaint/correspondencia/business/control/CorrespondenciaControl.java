package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Calendar;

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
    @PersistenceContext
    private EntityManager em;

    public CorCorrespondencia corCorrespondenciaTransform(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        return CorCorrespondencia.newInstance()
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
    }

    public Boolean verificarByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        long cantidad = em.createNamedQuery("CorCorrespondencia.countByNroRadicado", Long.class)
                .setParameter("NRO_RADICADO", nroRadicado)
                .getSingleResult();
        return cantidad > 0;
    }

    public String generarNumeroRadicado(CorrespondenciaDTO correspondencia) throws BusinessException, SystemException {//TODO
        String nroR = em.createNamedQuery("CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC", String.class)
                .setParameter("COD_SEDE", correspondencia.getCodSede())
                .setParameter("COD_TIPO_CMC", correspondencia.getCodTipoCmc())
                .getSingleResult();
        int consecRadicado = 0;
        if (nroR != null) {
            CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("NRO_RADICADO", nroR)
                    .getSingleResult();
            Calendar calendar = Calendar.getInstance();
            int anno = calendar.get(Calendar.YEAR);
            calendar.setTime(correspondenciaDTO.getFecRadicado());
            if (anno == calendar.get(Calendar.YEAR)) {
                consecRadicado = Integer.parseInt(nroR.substring(nroR.length() - 6));
            }
        }
        consecRadicado++;
        return correspondencia.getCodSede()
                .concat(correspondencia.getCodTipoCmc())
                .concat(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                .concat(String.format("%06d", consecRadicado));
    }

}