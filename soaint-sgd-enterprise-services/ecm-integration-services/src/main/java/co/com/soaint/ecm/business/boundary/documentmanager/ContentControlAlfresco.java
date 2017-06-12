package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;


import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by sarias on 11/11/2016.
 */
@Service
public class ContentControlAlfresco extends ContentControl {

    private static Logger LOGGER = Logger.getLogger(ContentControlAlfresco.class.getName());
    private static Session conexion = null;
//    private static ObjectStore os = null;
    private static Dominio dominio = null;
    private final String ID_ORG_ADM = "0";
    private final String ID_ORG_OFC = "1";
    private final String COD_SERIE = "2";
    private final String NOM_SERIE = "3";
    private final String COD_SUBSERIE ="4";
    private final String NOM_SUBSERIE = "5";

    public ContentControlAlfresco()  {
    }

    public MensajeRespuesta establecerConexiones() throws SystemException {


        MensajeRespuesta response = new MensajeRespuesta();

        try {
            Properties props = new Properties();
            FileInputStream file = null;
            String propiedadALFRSCO_ATOMPUB_URL = null;
            String propiedadREPOSITORY_ID = null;
            String propiedadALFRESCO_USER = null;
            String propiedadALFRESCO_PASS = null;
            try {
                String path = new File("ecm-integration-services/src/main/resources/connectionAlfresco.properties").getAbsolutePath();
              
                file = new FileInputStream(path);

                props.load(file);
                propiedadALFRSCO_ATOMPUB_URL = props.getProperty("ALFRSCO_ATOMPUB_URL");
                propiedadREPOSITORY_ID = props.getProperty("REPOSITORY_ID");
                propiedadALFRESCO_USER = props.getProperty("ALFRESCO_USER");
                propiedadALFRESCO_PASS = props.getProperty("ALFRESCO_PASS");
                file.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, String> parameter = new HashMap<String, String>();

            // Credenciales del usuario
            parameter.put(SessionParameter.USER, propiedadALFRESCO_USER);
            parameter.put(SessionParameter.PASSWORD, propiedadALFRESCO_PASS);

            // Configuracion de conexion
            parameter.put(SessionParameter.ATOMPUB_URL, propiedadALFRSCO_ATOMPUB_URL);
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            parameter.put(SessionParameter.REPOSITORY_ID, propiedadREPOSITORY_ID);

            // Object factory de Alfresco
            parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

            // Crear Sesion
            SessionFactory factory = SessionFactoryImpl.newInstance();
            Session session = factory.getRepositories(parameter).get(0).createSession();

            response.setMensaje("OK");
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("Error obteniendo conexion");
//            response.setCodMensaje(MessageUtil.getMessage("cod01"));
//            response.setMensaje(MessageUtil.getMessage("msj01"));
        }

        return response;
    }

    /* -- Obtener conexion -- */
    public Conexion obtenerConexion() throws SystemException {

        Conexion conexion= new Conexion();

        LOGGER.info("*** obtenerConexion ***");
        try {
            if (conexion != null) {

                try {
                    Properties props = new Properties();
                    FileInputStream file = null;
                    String propiedadALFRSCO_ATOMPUB_URL = null;
                    String propiedadREPOSITORY_ID = null;
                    String propiedadALFRESCO_USER = null;
                    String propiedadALFRESCO_PASS = null;
                    try {
                        String path = new File("ecm-integration-services/src/main/resources/connectionAlfresco.properties").getAbsolutePath();

                        file = new FileInputStream(path);

                        props.load(file);
                        propiedadALFRSCO_ATOMPUB_URL = props.getProperty("ALFRSCO_ATOMPUB_URL");
                        propiedadREPOSITORY_ID = props.getProperty("REPOSITORY_ID");
                        propiedadALFRESCO_USER = props.getProperty("ALFRESCO_USER");
                        propiedadALFRESCO_PASS = props.getProperty("ALFRESCO_PASS");
                        file.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Map<String, String> parameter = new HashMap<String, String>();

                    // Credenciales del usuario
                    parameter.put(SessionParameter.USER, propiedadALFRESCO_USER);
                    parameter.put(SessionParameter.PASSWORD, propiedadALFRESCO_PASS);

                    // Configuracion de conexion
                    parameter.put(SessionParameter.ATOMPUB_URL, propiedadALFRSCO_ATOMPUB_URL);
                    parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                    parameter.put(SessionParameter.REPOSITORY_ID, propiedadREPOSITORY_ID);

                    // Object factory de Alfresco
                    parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

                    // Crear Sesion
                    SessionFactory factory = SessionFactoryImpl.newInstance();
                    conexion.setSession( factory.getRepositories(parameter).get(0).createSession());


                }catch (Exception e){
                    LOGGER.info("*** Error al obtener conexion ***");
                }


            }

        }
        catch (Exception e){
        LOGGER.info("*** Error al obtener conexion ***");
        }
        return conexion;
    }

    /* -- Obtener dominio -- */
    public Dominio obtenerDominio(Conexion con) throws SystemException {

//        LOGGER.info("*** obtenerDominio ***");
//        try {
//            if (dominio == null) {
//                dominio = Factory.Domain.fetchInstance(con,Configuracion.getPropiedad("repositorioContent"), null);
//            }
//        } catch (Exception e) {
//            LOGGER.info("*** Error al obtener dominio ***");
//        }
//        System.out.println("*** " + dominio);
//        return dominio;
        return null;

    }

//    public ObjectStore obtenerObjectStore(Dominio dominio) throws ECMIntegrationException {
//        LOGGER.info("*** obtenerObjectStore ***");
//        try {
//            if (os == null) {
//                os = Factory.ObjectStore.fetchInstance(dominio,Configuracion.getPropiedad("ecm"), null);
//            }
//        } catch (Exception e) {
//            LOGGER.info("*** Error al obtener objectstore ***");
//        }
//        return os;
//
//    }

    public void cerrarConexionContent() {
//        try {
//            LOGGER.info("*** Cerrando conexiones... ***");
//            conexion = null;
//            os = null;
//            dominio = null;
//        } catch (Exception e) {
//            LOGGER.info("*** Error al cerrar conexiones ***");
//        }
    }

    public  Carpeta chequearCarpetaPadre(Carpeta folderFather, String nameFolder, String codFolder) throws SystemException, IOException {
//            Folder folderReturn = null;
//            folderFather.save(RefreshMode.REFRESH);
//            FolderSet subFolders = folderFather.get_SubFolders();
//            Iterator<Folder> iterator = subFolders.iterator();
//            while (iterator.hasNext()) {
//                Folder aux = iterator.next();
//                Folder carpeta = Factory.Folder.fetchInstance(os, folderFather.get_PathName() + "/" + aux.get_Name(), null);
//                String description = carpeta.get_ClassDescription().get_Name();
//                if (description.equals(Configuracion.getPropiedad("claseDependencia"))) {
//                    if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodDependencia")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodDependencia")).equals(codFolder)) {
//                        folderReturn = aux;
//                    }
//                } else if (description.equals(Configuracion.getPropiedad("claseSerie"))) {
//                    if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSerie")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSerie")).equals(codFolder)) {
//                        folderReturn = aux;
//                    }
//                } else if (description.equals(Configuracion.getPropiedad("claseSubserie"))) {
//                    if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSubserie")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSubserie")).equals(codFolder)) {
//                        folderReturn = aux;
//                    }
//                }
//            }
//        return folderReturn;
        return null;
    }

    /**
     * Metodo que, dado el nombre de la carpeta padre y la nueva Carpeta, crea el documento
     * @param folder
     * @param nameOrg
     * @param  codOrg
     * @param classDocumental
     * @return newly created folder(newFolder)
     */
    public  Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental) throws SystemException {
        Carpeta newFolder = null;
        try {
            Map <String, String> props = new HashMap <> ( );

            props.put (PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            //Se define como nombre de la carpeta nameOrg
            props.put (PropertyIds.NAME, nameOrg);
            //Se crea la carpeta dentro de la carpeta folder
            newFolder = new Carpeta ( );
            newFolder.setFolder (folder.getFolder ( ).createFolder (props));

        } catch (Exception e) {
            LOGGER.info ("*** Error al crear folder ***");
        }
        return newFolder;
    }

    /**
     * Metodo que, dado el nombre de un espacio, devuelve su Id (nodeRef)
     * @param session
     * @param nombre
     * @return nodeRef (id)
     */
    public String obtenerIdDocumento(Session session, String nombre){

        String queryString = "SELECT cmis:objectId FROM cmis:document WHERE cmis:name = '" + nombre+"'";

        ItemIterable<QueryResult> results = session.query(queryString, false);

        String id = "";

        for (QueryResult qResult : results) {
            String objectId = qResult.getPropertyValueByQueryName("cmis:objectId");
            Document document = (Document) session.getObject(session.createObjectId(objectId));
            id = document.getId();
            System.out.println("Listado de carpetas: "+document.getId());
        }
        return id;

    }

    /**
     * Metodo que, dado la sesion y el nombre del documento, devuelve su contenido (InputStream)
     * @param session
     * @param nombre
     * @return InputStream(stream)
     */
    public InputStream obtenerContenidoDocumento(Session session, String nombre){
        CmisObject object = session.getObject(session.createObjectId(obtenerIdDocumento (session,nombre)));
        Document document;
        InputStream stream=null;
        if (object instanceof Document){
            document = (Document) object;
            String filename = document.getName();
            stream = document.getContentStream().getStream();
        }
        else{
            System.out.println("No es de tipo document");
        }
        return stream;
    }

    /**
     * Metodo que, dado el nombre de una Carpeta, devuelve la carpeta(carpeta)
     * @param session
     * @param nombre
     * @return Folder (carpeta)
     */
    public static Folder obtenerCarpetaPorNombre(Session session, String nombre)    {



        String queryString = "SELECT cmis:objectId FROM cmis:folder WHERE cmis:name = '" + nombre+"'";

        ItemIterable<QueryResult> results = session.query(queryString, false);

        Folder carpeta = null;



        for (QueryResult qResult : results) {
            String objectId = qResult.getPropertyValueByQueryName("cmis:objectId");
            carpeta = (Folder) session.getObject(session.createObjectId(objectId));

            System.out.println("Listado de carpetas: "+carpeta.getId());
        }


        return carpeta;

    }

    /**
     * Metodo que, dado una Carpeta y la profundidad necesaria, devuelve una lista de las carpetas (carpeta)
     * @param depth
     * @param target
     * @return
     */
    private static void obtenerlistaCarpetas(int depth, Folder target) {
        String indent = StringUtils.repeat("\t", depth);
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext();) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                System.out.println(indent + "[Docment] " + o.getName());
            } else if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {
                System.out.println(indent + "[Folder] " + o.getName());
                obtenerlistaCarpetas(++depth, (Folder) o);
            }
        }

    }

    /**
     * Metodo que, dado el nombre de una Carpeta y un documento, elimina el docuemtno(carpeta)
     * @param target
     * @param delDocName
     * @param session
     */
    private void eliminarDocumento(Folder target, String delDocName,Session session) {


        try {

            CmisObject object = session.getObjectByPath(target.getPath()+ delDocName);
            Document delDoc = (Document) object;
            delDoc.delete(true);

        } catch (CmisObjectNotFoundException e) {
            System.err.println("Document is not found: " + delDocName);

        }
    }


    /**
     * Metodo para mover un documento de una carpeta a otra
     * @param targetFolder
     * @param sourceFolder
     * @param document
     */
    public void moverDocumento(String document, Folder sourceFolder, Folder targetFolder, Session session) {
        try {
            CmisObject object = session.getObjectByPath(sourceFolder.getPath()+"/"+document);
            Document mvndocument = (Document) object;
            mvndocument.move(sourceFolder,targetFolder);
        } catch (CmisObjectNotFoundException e) {
            System.err.println("Document is not found: " + document);
        }

    }

    public String  formatearNombre(String[] informationArray, String formatoConfig) throws SystemException {
//        String formatoCadena;
//        String formatoFinal  = "";
//        try {
//            formatoCadena = Configuracion.getPropiedad(formatoConfig);
//            String[] formatoCadenaArray = formatoCadena.split("");
//            int bandera = 000;
//            for(int i = 0; i < formatoCadenaArray.length; i++){
//
//                if(formatoCadenaArray[i].equals(ID_ORG_ADM)){
//                    formatoFinal += informationArray[Integer.parseInt(ID_ORG_ADM)];
//                    bandera = Integer.parseInt(ID_ORG_ADM);
//                }else if(formatoCadenaArray[i].equals(ID_ORG_OFC)){
//                    formatoFinal += informationArray[Integer.parseInt(ID_ORG_OFC)];
//                    bandera = Integer.parseInt(ID_ORG_OFC);
//                }else if(formatoCadenaArray[i].equals(COD_SERIE)){
//                    formatoFinal += informationArray[Integer.parseInt(COD_SERIE)];
//                    bandera = Integer.parseInt(COD_SERIE);
//                }else if(formatoCadenaArray[i].equals(NOM_SERIE)){
//                    formatoFinal += informationArray[Integer.parseInt(NOM_SERIE)];
//                    bandera = Integer.parseInt(NOM_SERIE);
//                }else if(formatoCadenaArray[i].equals(COD_SUBSERIE)){
//                    formatoFinal += informationArray[Integer.parseInt(COD_SUBSERIE)];
//                    bandera = Integer.parseInt(COD_SUBSERIE);
//                } else if (formatoCadenaArray[i].equals(NOM_SUBSERIE)) {
//                    formatoFinal += informationArray[Integer.parseInt(NOM_SUBSERIE)];
//                    bandera = Integer.parseInt(NOM_SUBSERIE);
//                }else if(isNumeric(formatoCadenaArray[i])) {
//                    //El formato no cumple con los requerimientos minimos
//                    LOGGER.info("El formato no cumple con los requerimientos.");
//                    formatoFinal = null;
//                    break;
//                }else{
//                    if(bandera == 000){
//                        formatoFinal+= formatoCadenaArray[i];
//                    }else{
//                        formatoFinal+=formatoCadenaArray[i];
//                    }
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.info("*** Error al formatear nombre ***");
//        }
//        return formatoFinal;
return null;
    }

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public Carpeta crearCarpeta(String nombreCarpeta) {
            LOGGER.info("### Creando carpeta" + nombreCarpeta);
        Carpeta carpeta=new Carpeta ();
        try {

        Map<String, String> props = new HashMap<String, String>();
        props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        props.put(PropertyIds.NAME, nombreCarpeta);
        Conexion conexion= obtenerConexion ();
        carpeta.setFolder (conexion.getSession ( ).getRootFolder ().createFolder (props));
        } catch (Exception e) {
            LOGGER.info("Error al crear la carpeta "+nombreCarpeta);
        }
        LOGGER.info("### Carpeta" + nombreCarpeta +" creada");
        return carpeta;
    }

    public Carpeta verificarCarpetaPadre(String nombre) throws SystemException {
        LOGGER.info("### Verificando carpeta padre");
        Carpeta carpetaPadre = null;
        List<Folder> listaCarpetas = new ArrayList <> ();

        ItemIterable<CmisObject> it = obtenerConexion ().getSession ().getRootFolder ().getChildren ();
        for (CmisObject obj : it) {
//            writer.addChild(obj.getId(), obj.getName());
            Folder folder = (Folder) obj;



        for (Folder lista : listaCarpetas) {
            if (lista.getName ().equals(nombre)) {
                carpetaPadre.setFolder (lista);
            }
        }

         }
        return carpetaPadre;
    }

    public   List<Carpeta> obtenerCarpetas(String path, ObjetoECM os) throws SystemException {
        List<Folder> listaCarpetas = new ArrayList<Folder>();
        try {
            Folder carpeta = null;
/*
Folder currentFolder = os.getSession().getRootFolder ().fetchInstance(os, path, null);
FolderSet subFolders = currentFolder.get_SubFolders();
Iterator<Folder> iterator = subFolders.iterator();
while (iterator.hasNext()) {
Folder subFolder = iterator.next();
carpeta = subFolder;
listaCarpetas.add(carpeta);
}
*/
        } catch (Exception e) {
            LOGGER.info("*** Error al obtenerCarpetas ***");
        }
//TODO Arrelgar este metodo        return listaCarpetas;
return null;
    }


    public  boolean actualizarNombreCarpeta(Carpeta carpeta, String nombre)throws SystemException{
//        LOGGER.info("### Actualizando nombre folder: "+nombre);
//        boolean estado;
//        try{
//            carpeta.set_FolderName(nombre);
//            carpeta.save(RefreshMode.REFRESH);
//            estado = true;
//        }catch(Exception e){
//            estado = false;
//            LOGGER.info("*** Error al actualizar nombre folder ***");
//        }
//        return estado;
        return true;
    }

    public static Folder chequearCapetaPadre(Carpeta folderFather, String nameFolder, String codFolder) throws BusinessException, IOException {
        Folder folderReturn = null;
//        folderFather.save(RefreshMode.REFRESH);
        //TODO obtener carpetas hijas a partir de la carpeta padre
//        ItemIterable<CmisObject> subFolders=folderFather.getChildren ();
        ItemIterable<CmisObject> listaObjetos = folderFather.getFolder ().getChildren ();

        while (listaObjetos.iterator ().hasNext ()) {
            CmisObject aux = listaObjetos.iterator ().next();
//            aux.get
//            Folder carpeta = Factory.Folder.fetchInstance(os, folderFather.get_PathName() + "/" + aux.get_Name(), null);
//            String description = carpeta.get_ClassDescription().get_Name();
//            if (description.equals(Configuracion.getPropiedad("claseDependencia"))) {
//                if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodDependencia")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodDependencia")).equals(codFolder)) {
//                    folderReturn = aux;
//                }
//            } else if (description.equals(Configuracion.getPropiedad("claseSerie"))) {
//                if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSerie")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSerie")).equals(codFolder)) {
//                    folderReturn = aux;
//                }
//            } else if (description.equals(Configuracion.getPropiedad("claseSubserie"))) {
//                if (aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSubserie")) != null &&
//                        aux.getProperties().getStringValue(Configuracion.getPropiedad("metadatoCodSubserie")).equals(codFolder)) {
//                    folderReturn = aux;
//                }
//            }
//        }
        folderReturn=folderFather.getFolder ();

    }
    return folderReturn;
    }

    public static boolean actualizarNombreFolder(Folder carpeta, String nombre)throws SystemException{
        LOGGER.info("### Actualizando nombre folder: "+nombre);
        boolean estado;

        try{
            carpeta.rename (nombre);
            estado = true;
        }catch(Exception e){
            estado = false;
            LOGGER.info("*** Error al actualizar nombre folder ***");
        }
        return estado;
    }
    public MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException {
        LOGGER.info ("### Generando arbol");
        MensajeRespuesta response = new MensajeRespuesta ( );
       
        return response;
    }
    }



