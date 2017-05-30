package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;
import co.com.soaint.ecm.domain.entity.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;


/**
 * Created by Dasiel on 29/05/2017.
 */
@Service
public abstract class ContentControl  {

    public ContentControl()  {
    }

    public abstract MensajeRespuesta establecerConexiones() throws SystemException;


    /* -- Obtener coneccion -- */
    public abstract Conexion obtenerConexion(String ceUri, String user, String pass, String stanza) throws SystemException ;

    /* -- Obtener dominio -- */
    public abstract Dominio obtenerDominio(Conexion con) throws SystemException ;

//    public abstract ObjectStore obtenerObjectStore(Dominio dominio) throws ECMIntegrationException ;

    public abstract void cerrarConexionContent();

    public abstract Carpeta chequearCarpetaPadre(Carpeta folderFather, String nameFolder, String codFolder) throws SystemException, IOException ;

    public abstract Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental) throws SystemException ;

    public abstract String  formatearNombre(String[] informationArray, String formatoConfig) throws SystemException;


    public abstract Carpeta crearCarpeta(String nombreCarpeta);

    public abstract Carpeta verificarCarpetaPadre(String nombre) throws SystemException ;

    public abstract  List<Carpeta> obtenerCarpetas(String path, ObjetoECM os) throws SystemException;

    public abstract boolean actualizarNombreCarpeta(Carpeta carpeta, String nombre)throws SystemException;

    public abstract MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException ;


}
