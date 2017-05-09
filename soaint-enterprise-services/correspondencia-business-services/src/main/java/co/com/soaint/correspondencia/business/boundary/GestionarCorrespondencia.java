package co.com.soaint.correspondencia.business.boundary;

/**
 * Created by jrodriguez on 09/05/2017.
 */

import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint SGD Generic Artifact
 * Created: 4-May-2017
 * Author: jprodriguezor
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@BusinessBoundary
public class GestionarCorrespondencia {


    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------
    public GestionarCorrespondencia() {
        super();
    }

    public void crearCorrespondencia(final CorrespondenciaDTO correspondencia){

        CorCorrespondencia corCorrespondencia =CorCorrespondencia.newInstance()
                .ideDocumento(correspondencia.getIdeDocumento())
                .descripcion(correspondencia.getDescripcion())
                .tiempoRespuesta(correspondencia.getTiempoRespuesta())
                .codUnidadTiempo(correspondencia.getCodUnidadTiempo())
                .codMedioRecepcion(correspondencia.getCodMedioRecepcion())
                .fecRadicado(correspondencia.getFecRadicado())
                .nroRadicado(correspondencia.getNroRadicado())
                .fecDocumento(correspondencia.getFecDocumento())
                .codTipoDoc(correspondencia.getCodTipoDoc())
                .codTipoCmc(correspondencia.getCodTipoCmc())
                .ideInstancia(correspondencia.getIdeInstancia())
                .reqDistFisica(correspondencia.getReqDistFisica())
                .codFuncRadica(correspondencia.getCodFuncRadica())
                .codSede(correspondencia.getCodSede())
                .codDependencia(correspondencia.getCodDependencia())
                .reqDigita(correspondencia.getReqDigita())
                .codEmpMsj(correspondencia.getCodEmpMsj())
                .nroGuia(correspondencia.getNroGuia())
                .fecVenGestion(correspondencia.getFecVenGestion())
                .codEstado(correspondencia.getCodEstado())
                .build();
        em.persist(corCorrespondencia);

    }


}
