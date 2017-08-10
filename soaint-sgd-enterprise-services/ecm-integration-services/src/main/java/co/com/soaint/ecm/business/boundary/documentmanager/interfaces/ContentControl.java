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

/**
 * Clase para el control del ECM
 */
@Service
public interface ContentControl {
    /**
     * Metodo que retorna un objeto de conexion al ECM
     * @return Objeto de conexion
     */
    Conexion obtenerConexion();

    /**
     * Metodo que crea carpetas dentro del ECM
     * @param folder Objeto carpeta que contiene un Folder de Alfresco
     * @param nameOrg         Nombre de la carpeta
     * @param codOrg          Codigo de la carpeta que se va a crear
     * @param classDocumental Clase documental que tiene la carpeta que se va a crar.
     * @param folderFather    Carpeta dentro de la cual se va a crear la carpeta
     * @return Devuelve la carpeta creada dentro del objeto Carpeta
     */
    Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather);

    /**
     * @param informationArray Arreglo que trae el nombre de la carpeta para formatearlo para ser usado por el ECM
     * @param formatoConfig    Contiene el formato que se le dara al nombre
     * @return Nombre formateado
     */
    String formatearNombre(String[] informationArray, String formatoConfig);

    /**
     * Metodo para actualizar el nombre de la carpeta
     *
     * @param carpeta Carpeta a la cual se le va a actualizar el nombre
     * @param nombre  Nuevo nombre de la carpeta
     */
    void actualizarNombreCarpeta(Carpeta carpeta, String nombre);
    /**
     * Metodo para mover carpetas dentro de Alfresco
     *
     * @param session        Objeto de conexion al ECM
     * @param documento      Nombre del documento que se va a mover
     * @param carpetaFuente  Carpeta desde donde se va a mover el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento
     * @return Mensaje de respuesta del metodo(codigo y mensaje)
     */
    MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino);
    /**
     * Metodo para generar el arbol de carpetas dentro del Alfresco
     *
     * @param estructuraList Estrcutura que sera creada dentro del Alfresco
     * @param folder         Carpeta padre de la estructura
     * @return Mensaje de respuesta (codigo y mensaje)
     */
    MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder);
    /**
     * Metodo para subir documentos al ECM
     *
     * @param session          Objeto de conexion al ECM
     * @param nombreDocumento  Nombre del documento que se va a crear
     * @param documento        Documento que se va a subir
     * @param tipoComunicacion Tipo de comunicacion que puede ser Externa o Interna
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException;


}
