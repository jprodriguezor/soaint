package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import org.apache.chemistry.opencmis.client.api.Session;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public abstract class ContentControl {

    public ContentControl() {
    }

    /* -- Obtener coneccion -- */
    public abstract Conexion obtenerConexion();

    /* -- Obtener dominio -- */

    public abstract Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather);

    public abstract String formatearNombre(String[] informationArray, String formatoConfig);

    public abstract boolean actualizarNombreCarpeta(Carpeta carpeta, String nombre);

    public abstract MensajeRespuesta movDocumento(Session session, String documento, String CarpetaFuente, String carpetaDestino);

    public abstract MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder);

    public abstract String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException;


}
