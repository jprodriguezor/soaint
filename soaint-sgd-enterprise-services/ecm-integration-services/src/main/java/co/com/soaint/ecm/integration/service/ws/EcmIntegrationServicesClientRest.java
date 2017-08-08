package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Dasiel on 19/06/2017.
 */


@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcmIntegrationServicesClientRest {

    private static Logger LOGGER = LogManager.getLogger(EcmIntegrationServicesClientRest.class.getName());

    public EcmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Autowired
    EcmManagerMediator fEcmManager;

    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException, BusinessException, IOException {
        LOGGER.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM (structure);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/subirDocumentoECM/")
    @Consumes("multipart/form-data")
    public String subirDocumentoECM(@QueryParam("nombreDocumento") final String nombreDocumento,
                                    @RequestPart("documento") final MultipartFormDataInput documento1,
                                    @QueryParam("tipoComunicacion") final String tipoComunicacion) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Subir Documento ECM");

        //Codigo new
        String fileName = "";

        try {

            Map<String, List<InputPart>> uploadForm = documento1.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("uploadedFile");

            for (InputPart inputPart : inputParts) {
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    fileName = getFileName(header);

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);

                //constructs upload file path
                fileName = "/home/wildfly/" + fileName;
                LOGGER.info("Ruta del fichero: " + fileName);

                writeFile(bytes,fileName);

                LOGGER.info("Fichero escrito");

            }
            LOGGER.info("Nombre del fichero: " + fileName);

            return "subida exitosa";
            //Fin codigo new



            //return fEcmManager.subirDocumento (nombreDocumento,documento,tipoComunicacion );
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam ("moverDocumento") final String moverDocumento,
                                              @QueryParam ("carpetaFuente") final String carpetaFuente,
                                              @QueryParam ("carpetaDestino") final String carpetaDestino) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Mover Documento ECM");
        try {
            return fEcmManager.moverDocumento (moverDocumento,carpetaFuente,carpetaDestino );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * header sample
     * {
     * 	Content-Type=[image/png],
     * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     **/
    //get uploaded filename, is there a easy way in RESTEasy?
    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            LOGGER.info("contenido del file: " + filename);
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }


    //save to somewhere
    private void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileOutputStream fop = new FileOutputStream(file)) {

            fop.write(content);
            fop.flush();
            fop.close();
        }

    }

}

