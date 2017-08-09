package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.domain.entity.Dominio;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Dasiel on 11/11/2016.
 */
@BusinessBoundary
@NoArgsConstructor
public class ContentControlAlfresco extends ContentControl {

    private static Logger LOGGER = Logger.getLogger (ContentControlAlfresco.class.getName ( ));
    private static Session conexion = null;
    private static Dominio dominio = null;
    private final String ID_ORG_ADM = "0";
    private final String ID_ORG_OFC = "1";
    private final String COD_SERIE = "2";
    private final String NOM_SERIE = "3";
    private final String COD_SUBSERIE = "4";
    private final String NOM_SUBSERIE = "5";

//    @Value( "${ALFRSCO_ATOMPUB_URL}" )
//    private String propiedadALFRSCO_ATOMPUB_URL ;
//    @Value( "${REPOSITORY_ID}" )
//    private String propiedadREPOSITORY_ID ;
//    @Value( "${ALFRESCO_USER}" )
//    private String propiedadALFRESCO_USER ;
//    @Value( "${ALFRESCO_PASS}" )
//    private String propiedadALFRESCO_PASS ;

    private String propiedadALFRSCO_ATOMPUB_URL = "http://192.168.1.82:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
    private String propiedadREPOSITORY_ID = "-default-";
    private String propiedadALFRESCO_USER = "admin";
    private String propiedadALFRESCO_PASS = "admin";

    public MensajeRespuesta establecerConexiones() throws SystemException {


        MensajeRespuesta response = new MensajeRespuesta ( );

        try {
            LOGGER.info ("### Llenando datos de conexion..");
            LOGGER.info ("### Usuario.." + propiedadALFRESCO_USER);
            Properties props = new Properties ( );

            Map <String, String> parameter = new HashMap <String, String> ( );

            // Credenciales del usuario
            parameter.put (SessionParameter.USER, propiedadALFRESCO_USER);
            parameter.put (SessionParameter.PASSWORD, propiedadALFRESCO_PASS);

            // Configuracion de conexion
            parameter.put (SessionParameter.ATOMPUB_URL, propiedadALFRSCO_ATOMPUB_URL);
            parameter.put (SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value ( ));
            parameter.put (SessionParameter.REPOSITORY_ID, propiedadREPOSITORY_ID);

            // Object factory de Alfresco
            parameter.put (SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

            // Crear Sesion
            SessionFactory factory = SessionFactoryImpl.newInstance ( );
            Session session = factory.getRepositories (parameter).get (0).createSession ( );

            response.setMensaje ("OK");
        } catch (Exception e) {
            e.printStackTrace ( );
            LOGGER.info ("Error obteniendo conexion");
            response.setCodMensaje ("Error al establecer Conexiones");
            response.setMensaje ("000002");
        }

        return response;
    }

    /* -- Obtener conexion -- */
    public Conexion obtenerConexion() throws SystemException {

        Conexion conexion = new Conexion ( );

        LOGGER.info ("*** obtenerConexion ***");
        try {
            if (conexion != null) {

                try {
                    Properties props = new Properties ( );
                    FileInputStream file = null;

                    Map <String, String> parameter = new HashMap <String, String> ( );

                    // Credenciales del usuario
                    parameter.put (SessionParameter.USER, propiedadALFRESCO_USER);
                    parameter.put (SessionParameter.PASSWORD, propiedadALFRESCO_PASS);

                    // Configuracion de conexion
                    parameter.put (SessionParameter.ATOMPUB_URL, propiedadALFRSCO_ATOMPUB_URL);
                    parameter.put (SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value ( ));
                    parameter.put (SessionParameter.REPOSITORY_ID, propiedadREPOSITORY_ID);

                    // Object factory de Alfresco
                    parameter.put (SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

                    // Crear Sesion
                    SessionFactory factory = SessionFactoryImpl.newInstance ( );
                    conexion.setSession (factory.getRepositories (parameter).get (0).createSession ( ));


                } catch (Exception e) {
                    LOGGER.info ("*** Error al obtener conexion ***");
                }


            }

        } catch (Exception e) {
            LOGGER.info ("*** Error al obtener conexion ***");
        }
        return conexion;
    }

    /**
     * Metodo que, dado el nombre de la carpeta padre y la nueva Carpeta, crea el documento
     *
     * @param folder
     * @param nameOrg
     * @param codOrg
     * @param classDocumental
     * @return newly created folder(newFolder)
     */
    public Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather) throws SystemException {
        Carpeta newFolder = null;
        try {

            LOGGER.info ("### Creando Carpeta.. con clase docuemntal:" + classDocumental);
            Map <String, String> props = new HashMap <> ( );
            //Se define como nombre de la carpeta nameOrg
            props.put (PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre

            if (classDocumental.equals ("claseBase")) {
                props.put (PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + Configuracion.getPropiedad ("claseBase"));
                props.put (PropertyIds.DESCRIPTION, Configuracion.getPropiedad ("claseBase"));
                props.put ("cmcor:CodigoBase", codOrg);

            } else if (classDocumental.equals ("claseDependencia")) {
                props.put (PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + Configuracion.getPropiedad ("claseDependencia"));
                props.put (PropertyIds.DESCRIPTION, Configuracion.getPropiedad ("claseDependencia"));
                props.put ("cmcor:CodigoDependencia", codOrg);
                //En dependencia de la clase documental que venga por parametro se crea el tipo de carpeta
                if (folderFather != null) {
                    props.put ("cmcor:CodigoUnidadAdminPadre", folderFather.getFolder ( ).getPropertyValue ("cmcor:CodigoDependencia"));
                } else {
                    props.put ("cmcor:CodigoUnidadAdminPadre", codOrg);
                }
            } else if (classDocumental.equals ("claseSerie")) {
                props.put (PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + Configuracion.getPropiedad ("claseSerie"));
                props.put (PropertyIds.DESCRIPTION, Configuracion.getPropiedad ("claseSerie"));
                props.put ("cmcor:CodigoSerie", codOrg);
                if (folderFather != null) {
                    props.put ("cmcor:CodigoUnidadAdminPadre", folderFather.getFolder ( ).getPropertyValue ("cmcor:CodigoDependencia"));
                } else {
                    props.put ("cmcor:CodigoUnidadAdminPadre", codOrg);
                }
            } else if (classDocumental.equals ("claseSubserie")) {
                props.put (PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + Configuracion.getPropiedad ("claseSubserie"));
                props.put (PropertyIds.DESCRIPTION, Configuracion.getPropiedad ("claseSubserie"));
                props.put ("cmcor:CodigoSubserie", codOrg);
                if (folderFather != null) {
                    props.put ("cmcor:CodigoUnidadAdminPadre", folderFather.getFolder ( ).getPropertyValue ("cmcor:CodigoSerie"));
                }
            }
            //Se crea la carpeta dentro de la carpeta folder
            LOGGER.info ("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta ( );
            LOGGER.info ("*** despues de aqui se va a crear la nueva c arpeta dentro d ela carpeta: ***" + Configuracion.getPropiedad ("claseSubserie") + Configuracion.getPropiedad ("claseBase"));
            newFolder.setFolder (folder.getFolder ( ).createFolder (props));
        } catch (Exception e) {
            LOGGER.info ("*** Error al crear folder ***");
        }
        return newFolder;

    }

    /**
     * Metodo que, dado el nombre de un espacio, devuelve su Id (nodeRef)
     *
     * @param session
     * @param nombre
     * @return nodeRef (id)
     */
    public String obtenerIdDocumento(Session session, String nombre) {

        String queryString = "SELECT cmis:objectId FROM cmis:document WHERE cmis:name = '" + nombre + "'";

        ItemIterable <QueryResult> results = session.query (queryString, false);

        String id = "";

        for (QueryResult qResult : results) {
            String objectId = qResult.getPropertyValueByQueryName ("cmis:objectId");
            Document document = (Document) session.getObject (session.createObjectId (objectId));
            id = document.getId ( );
            System.out.println ("Listado de carpetas: " + document.getId ( ));
        }
        return id;

    }

    /**
     * Metodo que, dado la sesion y el nombre del documento, devuelve su contenido (InputStream)
     *
     * @param session
     * @param nombre
     * @return InputStream(stream)
     */
    public InputStream obtenerContenidoDocumento(Session session, String nombre) {
        CmisObject object = session.getObject (session.createObjectId (obtenerIdDocumento (session, nombre)));
        Document document;
        InputStream stream = null;
        if (object instanceof Document) {
            document = (Document) object;
            String filename = document.getName ( );
            stream = document.getContentStream ( ).getStream ( );
        } else {
            System.out.println ("No es de tipo document");
        }
        return stream;
    }

    /**
     * Metodo que, dado el nombre de una Carpeta y un documento, elimina el docuemtno(carpeta)
     *
     * @param target
     * @param delDocName
     * @param session
     */
    private void eliminarDocumento(Folder target, String delDocName, Session session) {


        try {

            CmisObject object = session.getObjectByPath (target.getPath ( ) + delDocName);
            Document delDoc = (Document) object;
            delDoc.delete (true);

        } catch (CmisObjectNotFoundException e) {
            System.err.println ("Document is not found: " + delDocName);

        }
    }

    public String formatearNombre(String[] informationArray, String formatoConfig) throws SystemException {
        String formatoCadena;
        String formatoFinal = "";
        try {
            formatoCadena = Configuracion.getPropiedad (formatoConfig);
            String[] formatoCadenaArray = formatoCadena.split ("");
            int bandera = 000;
            for (int i = 0; i < formatoCadenaArray.length; i++) {

                if (formatoCadenaArray[i].equals (ID_ORG_ADM)) {
                    formatoFinal += informationArray[Integer.parseInt (ID_ORG_ADM)];
                    bandera = Integer.parseInt (ID_ORG_ADM);
                } else if (formatoCadenaArray[i].equals (ID_ORG_OFC)) {
                    formatoFinal += informationArray[Integer.parseInt (ID_ORG_OFC)];
                    bandera = Integer.parseInt (ID_ORG_OFC);
                } else if (formatoCadenaArray[i].equals (COD_SERIE)) {
                    formatoFinal += informationArray[Integer.parseInt (COD_SERIE)];
                    bandera = Integer.parseInt (COD_SERIE);
                } else if (formatoCadenaArray[i].equals (NOM_SERIE)) {
                    formatoFinal += informationArray[Integer.parseInt (NOM_SERIE)];
                    bandera = Integer.parseInt (NOM_SERIE);
                } else if (formatoCadenaArray[i].equals (COD_SUBSERIE)) {
                    formatoFinal += informationArray[Integer.parseInt (COD_SUBSERIE)];
                    bandera = Integer.parseInt (COD_SUBSERIE);
                } else if (formatoCadenaArray[i].equals (NOM_SUBSERIE)) {
                    formatoFinal += informationArray[Integer.parseInt (NOM_SUBSERIE)];
                    bandera = Integer.parseInt (NOM_SUBSERIE);
                } else if (isNumeric (formatoCadenaArray[i])) {
                    //El formato no cumple con los requerimientos minimos
                    LOGGER.info ("El formato no cumple con los requerimientos.");
                    formatoFinal = null;
                    break;
                } else {
                    if (bandera == 000) {
                        formatoFinal += formatoCadenaArray[i];
                    } else {
                        formatoFinal += formatoCadenaArray[i];
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info ("*** Error al formatear nombre ***");
        }
        return formatoFinal;
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt (cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static Carpeta obtenerCarpetaPorNombre(String nombreCarpeta, Session session) throws SystemException {
        LOGGER.info ("Se entra al metodo obtener carpeta por nombre");
        Carpeta folder = new Carpeta ( );
        try {

            String queryString = "SELECT cmis:objectId FROM cmis:folder WHERE cmis:name = '" + nombreCarpeta + "'";
            LOGGER.info ("Se obtiene la query:" + queryString);
            ItemIterable <QueryResult> results = session.query (queryString, false);
            LOGGER.info ("Se obtiene el resultado:" + results);
            for (QueryResult qResult : results) {
                LOGGER.info ("Se obtiene la qResult:" + qResult);
                String objectId = qResult.getPropertyValueByQueryName ("cmis:objectId");
                LOGGER.info ("Se obtiene la objectId:" + objectId);
                folder.setFolder ((Folder) session.getObject (session.createObjectId (objectId)));
                LOGGER.info ("Se obtiene la folder:" + folder);
            }
        } catch (Exception e) {
            LOGGER.info ("*** Error al obtenerCarpetas ***");
        }

        return folder;

    }

    public static List <Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) throws SystemException {
        LOGGER.info ("### Obtener Carpetas Hijas Dado Padre");
        List <Carpeta> listaCarpetas = null;

        try {
            LOGGER.info ("### Antes de los primero con ALFRESCO");
            ItemIterable <CmisObject> listaObjetos = carpetaPadre.getFolder ( ).getChildren ( );

            listaCarpetas = new ArrayList <> ( );
            //Lista de carpetas hijas
            LOGGER.info ("### Antes del ciclo sobre CMISOBJECT con ALFRESCO");
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder) {
                    Carpeta folder = new Carpeta ( );
                    folder.setFolder ((Folder) contentItem);

                    listaCarpetas.add (folder);
                }
            }

        } catch (Exception e) {
            LOGGER.info ("*** Error al obtener Carpetas Hijas dado padre***");
        }
        return listaCarpetas;
    }

    public boolean actualizarNombreCarpeta(Carpeta carpeta, String nombre) throws SystemException {
        LOGGER.info ("### Actualizando nombre folder: " + nombre);
        boolean estado;
        try {
            carpeta.getFolder ( ).rename (nombre);
            estado = true;
        } catch (Exception e) {
            estado = false;
            LOGGER.info ("*** Error al actualizar nombre folder ***");
        }
        return estado;
    }

    public Carpeta chequearCapetaPadre(Carpeta folderFather, String nameFolder, String codFolder) throws BusinessException, IOException, SystemException {
        Carpeta folderReturn = null;
        List <Carpeta> listaCarpeta = new ArrayList <Carpeta> ( );
        Conexion conexion = obtenerConexion ( );
        listaCarpeta = obtenerCarpetasHijasDadoPadre (folderFather);
        Iterator <Carpeta> iterator = null;
        if (listaCarpeta != null) {
            iterator = listaCarpeta.iterator ( );
            while (iterator.hasNext ( )) {
                Carpeta aux = iterator.next ( );
                //Aqui da Error porque esta tratando de obtener una carpeta que esta mas profunda en el arbol
//                Folder carpeta = (Folder) conexion.getSession ( ).getObjectByPath (conexion.getSession ( ).getRootFolder ( ).getPath ( ) + aux.getFolder ( ).getName ( ));

                //Mi solucion al problema de mas arriba
                Carpeta carpeta = obtenerCarpetaPorNombre (aux.getFolder ( ).getName ( ), conexion.getSession ( ));
                String description = carpeta.getFolder ( ).getDescription ( );
                if (description.equals (Configuracion.getPropiedad ("claseDependencia"))) {
                    if (aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodDependencia")) != null &&
                            aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodDependencia")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals (Configuracion.getPropiedad ("claseSerie"))) {
                    if (aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodSerie")) != null &&
                            aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodSerie")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals (Configuracion.getPropiedad ("claseSubserie"))) {
                    if (aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodSubserie")) != null &&
                            aux.getFolder ( ).getPropertyValue ("cmcor:" + Configuracion.getPropiedad ("metadatoCodSubserie")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                }


            }
        }

        return folderReturn;
    }

    public MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino) throws SystemException {
        LOGGER.info ("### Mover documento: " + documento);

        LOGGER.info ("### Obtener carpeta fuente: " + carpetaFuente);
        MensajeRespuesta response=new MensajeRespuesta ( );
        try {

            Carpeta carpetaF = new Carpeta ( );
            Carpeta carpetaD = new Carpeta ( );

            carpetaF = (obtenerCarpetaPorNombre (carpetaFuente, session));
            carpetaD = (obtenerCarpetaPorNombre (carpetaDestino, session));

            CmisObject object = session.getObjectByPath (carpetaF.getFolder ( ).getPath ( ) + "/" + documento);
            Document mvndocument = (Document) object;
            mvndocument.move (carpetaF.getFolder ( ), carpetaD.getFolder ( ));
            response.setMensaje ("OK");
            response.setCodMensaje ("0000");

        } catch (CmisObjectNotFoundException e) {
            System.err.println ("Document is not found: " + documento);
            LOGGER.info ("*** Error al mover el documento ***");
            response.setMensaje ("Documento no encontrado");
            response.setCodMensaje ("00006");
        }

        return response;
    }

    public MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder) throws SystemException {
        LOGGER.info ("### Generando arbol");

        MensajeRespuesta response = new MensajeRespuesta ( );
        try {
            int bandera = 0;
            //Recorremos la lista general

            for (EstructuraTrdDTO estructura : estructuraList) {
                List <OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList ( );
                List <ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList ( );
                Utilities utils = new Utilities ( );

                //Grantizar que el orden de la estructura sea de menor a mayor, ideOrgaAdmin
                utils.ordenarListaOrganigrama (organigramaList);
                Carpeta folderFather = null;
                Carpeta folderSon = null;
                Carpeta folderContainer = null;
                Carpeta folderFatherContainer = null;
                //Recorremos la lista organigrama
                for (OrganigramaDTO organigrama : organigramaList) {
                    switch (bandera) {
                        case 0:
                            folderFather = chequearCapetaPadre (folder, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ));
                            if (folderFather == null) {
                                LOGGER.info ("Organigrama --  Creando folder: " + organigrama.getNomOrg ( ));
                                folderFather = crearCarpeta (folder, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ), "claseBase", folderFather);
                            } else {
                                LOGGER.info ("Organigrama --  El folder ya esta creado: " + folderFather.getFolder ( ).getName ( ));
                                //Actualización de folder
                                if (!(organigrama.getNomOrg ( ).equals (folderFather.getFolder ( ).getName ( )))) {
                                    LOGGER.info ("Se debe actualizar al nombre: " + organigrama.getNomOrg ( ));
                                    actualizarNombreCarpeta (folderFather, organigrama.getNomOrg ( ));
                                } else {
                                    LOGGER.info ("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg ( ));
                                }
                            }
                            bandera++;
                            folderFatherContainer = folderFather;
                            break;

                        default:
                            folderSon = chequearCapetaPadre (folderFather, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ));
                            if (folderSon == null) {
                                LOGGER.info ("Organigrama --  Creando folder: " + organigrama.getNomOrg ( ));
                                folderSon = crearCarpeta (folderFather, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ), "claseDependencia", folderFather);
                            } else {
                                LOGGER.info ("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder ( ).getName ( ));
                                //Actualización de folder
                                if (!(organigrama.getNomOrg ( ).equals (folderSon.getFolder ( ).getName ( )))) {
                                    LOGGER.info ("Se debe actualizar al nombre: " + organigrama.getNomOrg ( ));
                                    actualizarNombreCarpeta (folderSon, organigrama.getNomOrg ( ));
                                } else {
                                    LOGGER.info ("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg ( ));
                                }
                            }
                            folderFather = folderSon;
                            folderFatherContainer = folderSon;
                            folderContainer = folderFather;
                            bandera++;
                            break;
                    }
                }
                //Recorremos la lista de Dependencias TRD
                for (ContenidoDependenciaTrdDTO dependencias : trdList) {
                    String[] dependenciasArray = {dependencias.getIdOrgAdm ( ),
                            dependencias.getIdOrgOfc ( ),
                            dependencias.getCodSerie ( ),
                            dependencias.getNomSerie ( ),
                            dependencias.getCodSubSerie ( ),
                            dependencias.getNomSubSerie ( ),
                    };
                    String nombreSerie = formatearNombre (dependenciasArray, "formatoNombreSerie");
                    folderSon = chequearCapetaPadre (folderFatherContainer, nombreSerie, dependencias.getCodSerie ( ));
                    if (folderSon == null) {
                        if (nombreSerie != null) {
                            LOGGER.info ("TRD --  Creando folder: " + nombreSerie);
                            folderSon = crearCarpeta (folderFatherContainer, nombreSerie, dependencias.getCodSerie ( ), "claseSerie", folderFather);
                        } else {
                            LOGGER.info ("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals (folderSon.getFolder ( ).getName ( )))) {
                            LOGGER.info ("Se debe cambiar el nombre: " + nombreSerie);
                            actualizarNombreCarpeta (folderSon, nombreSerie);
                        } else {
                            LOGGER.info ("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (dependencias.getCodSubSerie ( ) != null && !dependencias.getCodSubSerie ( ).equals ("")) {
                        folderContainer = folderFather;
                        String nombreSubserie = formatearNombre (dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre (folderFather, nombreSubserie, dependencias.getCodSubSerie ( ));
                        if (folderSon == null) {
                            if (nombreSubserie != null) {
                                LOGGER.info ("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta (folderFather, nombreSubserie, dependencias.getCodSubSerie ( ), "claseSubserie", folderFather);
                            } else {
                                LOGGER.info ("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals (folderSon.getFolder ( ).getName ( )))) {
                                LOGGER.info ("Se debe cambiar el nombre: " + nombreSubserie);
                                actualizarNombreCarpeta (folderSon, nombreSubserie);
                            } else {
                                LOGGER.info ("TRD --  El folder ya esta creado: " + nombreSubserie);
                            }
                        }
                        folderFather = folderSon;
                    }
                }
                bandera = 0;
                response.setCodMensaje ("OK");
                response.setMensaje ("00000");
            }
        } catch (Exception e) {
            LOGGER.info ("Error al crear arbol content");
            e.printStackTrace ( );
            //TODO revisar el tema del response
            response.setCodMensaje ("Error al crear el arbol");
            response.setMensaje ("111112");
        }

        return response;
    }

    public String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException, SystemException {
        LOGGER.info ("Se entra al metodo subirDocumento");

        String idDocumento = "";
        Map <String, List <InputPart>> uploadForm = documento.getFormDataMap ( );
        List <InputPart> inputParts = uploadForm.get ("documento");


        LOGGER.info ("Debug------------------------------"+inputParts);

        String fileName = "";
        String mimeType="application/pdf";
        for (InputPart inputPart : inputParts) {
            MultivaluedMap <String, String> header = inputPart.getHeaders ( );
            InputStream inputStream = null;
            try {
                inputStream = inputPart.getBody (InputStream.class, null);
            } catch (IOException e) {
                e.printStackTrace ( );
            }

            byte[] bytes = IOUtils.toByteArray (inputStream);

            //Se definen las propiedades del documento a subir
            Map <String, Object> properties = new HashMap <String, Object> ( );

            properties.put (PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
            properties.put (PropertyIds.NAME, nombreDocumento);

        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            Carpeta folderAlfresco = new Carpeta ( );
            LOGGER.info ("### Se elige la carpeta donde se va a guardar el documento a radicar..");

            if ("TP-CMCOE".equals (tipoComunicacion)) {

                folderAlfresco = obtenerCarpetaPorNombre ("100101.00302_COMUNICACION_EXTERNA", session);
            } else {

                folderAlfresco = obtenerCarpetaPorNombre ("100100.00301_COMUNICACION_INTERNA", session);
            }
            LOGGER.info ("### Se elige la carpeta donde se va a guardar el documento a radicar.."+folderAlfresco.getFolder ().getName ());
            VersioningState vs = VersioningState.MAJOR;
            ContentStream contentStream = new ContentStreamImpl (nombreDocumento, BigInteger.valueOf (bytes.length), mimeType, new ByteArrayInputStream (bytes));
            //Se crea el documento
            LOGGER.info ("### Se va a crear el documento..");
            Document newDocument = folderAlfresco.getFolder ( ).createDocument (properties, contentStream, vs);
            idDocumento = newDocument.getId ( );
            LOGGER.info ("### Documento creado con id " + idDocumento);
        } catch (CmisContentAlreadyExistsException ccaee) {
            System.out.println ("ERROR: Unable to Load - CmisContentAlreadyExistsException: ");
            LOGGER.info ("### Error tipo CmisContentAlreadyExistsException----------------------------- :"+ccaee);
        } catch (CmisConstraintException cce) {
            System.out.println ("ERROR: Unable to Load - CmisConstraintException: ");
            LOGGER.info ("### Error tipo CmisConstraintException----------------------------- :"+cce);
        }
        catch (SystemException se) {
            se.printStackTrace ( );
            LOGGER.info ("### Error tipo SystemException----------------------------- :"+se);
        }
        catch (Exception e){
            e.printStackTrace ();
            LOGGER.info ("### Error tipo Exception----------------------------- :"+e);
        }

        }return idDocumento;
    }
}



