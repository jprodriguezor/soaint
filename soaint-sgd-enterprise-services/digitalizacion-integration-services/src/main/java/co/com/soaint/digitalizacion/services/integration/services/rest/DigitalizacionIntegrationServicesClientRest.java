package co.com.soaint.digitalizacion.services.integration.services.rest;

import co.com.soaint.digitalizacion.services.integration.services.IProcesarFichero;
import co.com.soaint.foundation.canonical.digitalizar.MensajeGenericoDigitalizarDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by amartinez on 12/03/2018.
 */
@Path("/digitalizar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class DigitalizacionIntegrationServicesClientRest {

    @Autowired
    private IProcesarFichero digitalizar;

    /**
     * Contructor de la clase
     */
    public DigitalizacionIntegrationServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Permite listar procesar lotes de ficheros
     *
     * @param entradaDigitalizar Objeto que define los parametros de entrada para la digitalizacion
     * @return numero de radicado del lote procesado y el fichero pdf en formato base64
     * @throws Throwable
     */
    @POST
    @Path("/procesar-lote/")
    public MensajeGenericoDigitalizarDTO procesarLote(MensajeGenericoDigitalizarDTO entradaDigitalizar) throws SystemException, FormatException, ChecksumException, IOException {
        log.info("processing rest request - listar procesos");
        return digitalizar.leerDirectorio(entradaDigitalizar);
    }

}
