package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;
import co.com.soaint.ecm.domain.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public abstract Conexion obtenerConexion() throws SystemException ;

    /* -- Obtener dominio -- */

    public abstract Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather) throws SystemException ;

    public abstract String  formatearNombre(String[] informationArray, String formatoConfig) throws SystemException;

    public abstract boolean actualizarNombreCarpeta(Carpeta carpeta, String nombre)throws SystemException;

    public abstract  MensajeRespuesta movDocumento(Session session,String documento, String CarpetaFuente, String carpetaDestino ) throws SystemException;

    public abstract MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException ;

    public abstract String subirDocumento(Session session, String nombreDocumento, MultipartFile documento, String tipoComunicacion) throws SystemException,IOException ;


}
