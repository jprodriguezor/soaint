package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/correspondencia-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class CorrespondenciaWebApi {
    private static Logger LOGGER = LogManager.getLogger(CorrespondenciaWebApi.class.getName());

    @Autowired
    private GestionarCorrespondencia boundary;

    public CorrespondenciaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/correspondencia")
    public CorrespondenciaDTO radicarComunicacionesOficiales(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        LOGGER.info("processing rest request - radicar correspondencia");
        return boundary.radicarCorrespondencia(correspondenciaDTO);
    }

    @GET
    @Path("/correspondencia/{estado}")
    public CorrespondenciaDTO obtenerCorrespondencia(@PathParam("estado")final String estado) throws BusinessException, SystemException {
        Date fecha = new Date();
        CorrespondenciaDTO correspondenciaDTO = CorrespondenciaDTO.newInstance()
                .descripcion("Correspondencia DTO test")
                .tiempoRespuesta(BigInteger.valueOf(1))
                .codUnidadTiempo("UT")
                .codMedioRecepcion("CMR")
                .fecRadicado(fecha)
                .nroRadicado("nroRadicado")
                .codTipoCmc("CTC")
                .ideInstancia("II")
                .reqDistFisica("DF")
                .codFuncRadica("CDR")
                .codSede("CS")
                .codDependencia("CD")
                .reqDigita("RD")
                .codEmpMsj("CEM")
                .nroGuia("NG")
                .fecVenGestion(fecha)
                .codEstado("CE")
                .corAgenteList(new ArrayList<>())
                .ppdDocumentoList(new ArrayList<>())
                .corReferidoList(new ArrayList<>())
                .build();

        CorAgenteDTO corAgenteDTO = CorAgenteDTO.newInstance()
                .codTipoRemite("CTR")
                .codTipoPers("CTP")
                .nombre("NOMBRE")
                .nroDocumentoIden("NDI")
                .razonSocial("RS")
                .nit("NIT")
                .codCortesia("CC")
                .codCargo("CCG")
                .codEnCalidad("CEC")
                .codTipDocIdent("CTDI")
                .nroDocuIdentidad("NDI")
                .codSede("CS")
                .codDependencia("CD")
                .codFuncRemite("CFR")
                .fecAsignacion(new SimpleDateFormat("yyyy-MM-dd").format(fecha))
                .ideContacto(Long.parseLong("1"))
                .codTipAgent("CTA")
                .indOriginal("IO")
                .build();
        correspondenciaDTO.getCorAgenteList().add(corAgenteDTO);

        PpdDocumentoDTO ppdDocumentoDTO = PpdDocumentoDTO.newInstance()
                .codTipoDoc("CTD")
                .fecDocumento(fecha)
                .codAsunto("CA")
                .nroFolios(Long.parseLong("1"))
                .nroAnexos(Long.parseLong("1"))
                .codEstDoc("CED")
                .ideEcm("IE")
                .codTipoSoporte("CTS")
                .codEstArchivado("CEA")
                .build();
        ppdDocumentoDTO.setCorAnexoList(new ArrayList<>());
        CorAnexoDTO corAnexoDTO = CorAnexoDTO.newInstance()
                .codAnexo("CA")
                .descripcion("Descripcion Anexo")
                .build();
        ppdDocumentoDTO.getCorAnexoList().add(corAnexoDTO);

        correspondenciaDTO.getPpdDocumentoList().add(ppdDocumentoDTO);

            CorReferidoDTO corReferidoDTO = CorReferidoDTO.newInstance()
                    .nroRadRef("NRR")
                    .build();
            correspondenciaDTO.getCorReferidoList().add(corReferidoDTO);
        return correspondenciaDTO;
    }
}
