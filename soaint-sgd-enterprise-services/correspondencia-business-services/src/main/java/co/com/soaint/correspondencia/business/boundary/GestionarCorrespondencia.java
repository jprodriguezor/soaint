package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.common.MessageUtil;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 31-May-2017
 * Author: esanchez
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

    public CorrespondenciaDTO radicarCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            String nroRadicado = generarNumeroRadicado(correspondenciaDTO.getCodSede(), correspondenciaDTO.getCodTipoCmc());
            correspondenciaDTO.setNroRadicado(nroRadicado);
            CorCorrespondencia correspondencia = CorCorrespondencia.newInstance()
                    .descripcion(correspondenciaDTO.getDescripcion())
                    .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta().toString())
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
                    .build();

            em.persist(correspondencia);
            em.flush();

            return correspondenciaDTO;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getInstance("system.generic.error").getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public String generarNumeroRadicado(String sede, String tipoComunicacion) {//TODO
        String consecutivo = "00001";
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
