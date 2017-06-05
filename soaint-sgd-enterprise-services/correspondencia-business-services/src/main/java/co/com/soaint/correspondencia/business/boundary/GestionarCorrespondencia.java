package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.*;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
            Date fecha = new Date();
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
                    .corAgenteList(new ArrayList<>())
                    .ppdDocumentoList(new ArrayList<>())
                    .corReferidoList(new ArrayList<>())
                    .build();

            correspondenciaDTO.getCorAgenteList().stream().forEach((corAgenteDTO) -> {
                CorAgente corAgente = CorAgente.newInstance()
                        .codTipoRemite(corAgenteDTO.getCodTipoRemite())
                        .codTipoPers(corAgenteDTO.getCodTipoPers())
                        .nombre(corAgenteDTO.getNombre())
                        .nroDocumentoIden(corAgenteDTO.getNroDocumentoIden())
                        .razonSocial(corAgenteDTO.getRazonSocial())
                        .nit(corAgenteDTO.getNit())
                        .codCortesia(corAgenteDTO.getCodCortesia())
                        .codCargo(corAgenteDTO.getCodCargo())
                        .codEnCalidad(corAgenteDTO.getCodEnCalidad())
                        .codTipDocIdent(corAgenteDTO.getCodTipDocIdent())
                        .nroDocuIdentidad(corAgenteDTO.getNroDocuIdentidad())
                        .codSede(corAgenteDTO.getCodSede())
                        .codDependencia(corAgenteDTO.getCodDependencia())
                        .codFuncRemite(corAgenteDTO.getCodFuncRemite())
                        .fecAsignacion(corAgenteDTO.getFecAsignacion())
                        .ideContacto(corAgenteDTO.getIdeContacto())
                        .codTipAgent(corAgenteDTO.getCodTipAgent())
                        .indOriginal(corAgenteDTO.getIndOriginal())
                        .corCorrespondencia(correspondencia)
                        .build();
                correspondencia.getCorAgenteList().add(corAgente);
            });
            correspondenciaDTO.getPpdDocumentoList().stream().forEach((ppdDocumentoDTO) -> {
                PpdDocumento ppdDocumento = PpdDocumento.newInstance()
                        .codTipoDoc(ppdDocumentoDTO.getCodTipoDoc())
                        .fecDocumento(ppdDocumentoDTO.getFecDocumento())
                        .codAsunto(ppdDocumentoDTO.getCodAsunto())
                        .nroFolios(ppdDocumentoDTO.getNroFolios())
                        .nroAnexos(ppdDocumentoDTO.getNroAnexos())
                        .codEstDoc(ppdDocumentoDTO.getCodEstDoc())
                        .ideEcm(ppdDocumentoDTO.getIdeEcm())
                        .codTipoSoporte(ppdDocumentoDTO.getCodTipoSoporte())
                        .codEstArchivado(ppdDocumentoDTO.getCodEstArchivado())
                        .fecCreacion(fecha)
                        .corCorrespondencia(correspondencia)
                        .build();
                ppdDocumento.setCorAnexoList(new ArrayList<>());
                ppdDocumentoDTO.getCorAnexoList().stream().forEach((corAnexoDTO) -> {
                    CorAnexo corAnexo = CorAnexo.newInstance()
                            .codAnexo(corAnexoDTO.getCodAnexo())
                            .descripcion(corAnexoDTO.getDescripcion())
                            .ppdDocumento(ppdDocumento)
                            .build();
                    ppdDocumento.getCorAnexoList().add(corAnexo);
                });
                correspondencia.getPpdDocumentoList().add(ppdDocumento);
            });
            correspondenciaDTO.getCorReferidoList().stream().forEach((corReferidoDTO)->{
                CorReferido corReferido = CorReferido.newInstance()
                        .nroRadRef(corReferidoDTO.getNroRadRef())
                        .corCorrespondencia(correspondencia)
                        .build();
                correspondencia.getCorReferidoList().add(corReferido);
            });

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
