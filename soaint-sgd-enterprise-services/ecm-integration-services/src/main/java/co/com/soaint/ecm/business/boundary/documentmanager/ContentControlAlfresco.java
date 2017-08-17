package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.NoArgsConstructor;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */

@BusinessControl
@NoArgsConstructor
public class ContentControlAlfresco implements ContentControl {
    @Autowired
    Configuracion configuracion;


    @Value("${claseSubserie}")
    private static String aclaseSubserie;
    private static final Logger logger = LogManager.getLogger (ContentControlAlfresco.class.getName ( ));

    private static final String CLASE_BASE = "claseBase";
    private static final String CLASE_DEPENDENCIA = "claseDependencia";
    private static final String CLASE_SERIE = "claseSerie";
    private static final String CLASE_SUBSERIE = "claseSubserie";
    private static final String CMCOR="cmcor:";
    private static final String CMCOR_CODIGOUNIDADAMINPADRE="cmcor:CodigoUnidadAdminPadre";
    private static final String CMCOR_CODIGODEPENDENCIA="cmcor:CodigoDependencia";

    /**
     * Metodo que obtiene el objeto de conexion que produce Alfresco en conexion
     *
     * @return Objeto de tipo Conexion en este caso devuevle un objeto Session
     */
    @Override
    public Conexion obtenerConexion() {
        Conexion conexion = new Conexion ( );

        logger.info ("*** obtenerConexion ***");

        try {
            Map <String, String> parameter = new HashMap <> ( );

            // Credenciales del usuario
            parameter.put (SessionParameter.USER, configuracion.getPropiedad ("ALFRESCO_USER"));
            parameter.put (SessionParameter.PASSWORD, configuracion.getPropiedad ("ALFRESCO_PASS"));

            // configuracion de conexion
            parameter.put (SessionParameter.ATOMPUB_URL, configuracion.getPropiedad ("ALFRESCO_ATOMPUB_URL"));
            parameter.put (SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value ( ));
            parameter.put (SessionParameter.REPOSITORY_ID, configuracion.getPropiedad ("REPOSITORY_ID"));

            // Object factory de Alfresco
            parameter.put (SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

            // Crear Sesion
            SessionFactory factory = SessionFactoryImpl.newInstance ( );
            conexion.setSession (factory.getRepositories (parameter).get (0).createSession ( ));


        } catch (Exception e) {
            logger.error ("*** Error al obtener conexion *** ", e);
        }

        return conexion;
    }

    /**
     * Metodo que crea carpetas dentro de Alfresco
     *
     * @param folder          Objeto carpeta que contiene un Folder de Alfresco
     * @param nameOrg         Nombre de la carpeta
     * @param codOrg          Codigo de la carpeta que se va a crear
     * @param classDocumental Clase documental que tiene la carpeta que se va a crar.
     * @param folderFather    Carpeta dentro de la cual se va a crear la carpeta
     * @return Devuelve la carpeta creada dentro del objeto Carpeta
     */
    private Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather) {
        Carpeta newFolder = null;
        try {

            logger.info ("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map <String, String> props = new HashMap <> ( );
            //Se define como nombre de la carpeta nameOrg
            props.put (PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre

            switch (classDocumental) {
                case CLASE_BASE:
                    llenarPropiedadesCarpeta ("cmcor:CodigoBase", CLASE_BASE, props, codOrg);
                    break;
                case CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta (CMCOR_CODIGODEPENDENCIA, CLASE_DEPENDENCIA, props, codOrg);
                    //En dependencia de la clase documental que venga por parametro se crea el tipo de carpeta
                    if (folderFather != null) {
                        props.put (CMCOR_CODIGOUNIDADAMINPADRE, folderFather.getFolder ( ).getPropertyValue (CMCOR_CODIGODEPENDENCIA));
                    } else {
                        props.put (CMCOR_CODIGOUNIDADAMINPADRE, codOrg);
                    }
                    break;
                case CLASE_SERIE:
                    llenarPropiedadesCarpeta ("cmcor:CodigoSerie", CLASE_SERIE, props, codOrg);
                    if (folderFather != null) {
                        props.put (CMCOR_CODIGOUNIDADAMINPADRE, folderFather.getFolder ( ).getPropertyValue (CMCOR_CODIGODEPENDENCIA));
                    } else {
                        props.put (CMCOR_CODIGOUNIDADAMINPADRE, codOrg);
                    }
                    break;
                case CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta ("cmcor:CodigoSubserie", CLASE_SUBSERIE, props, codOrg);
                    if (folderFather != null) {
                        props.put (CMCOR_CODIGOUNIDADAMINPADRE, folderFather.getFolder ( ).getPropertyValue ("cmcor:CodigoSerie"));
                    }
                    break;
                default:
                    break;
            }
            //Se crea la carpeta dentro de la carpeta folder
            logger.info ("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta ( );
            newFolder.setFolder (folder.getFolder ( ).createFolder (props));
            logger.info ("---------------------Carpeta: " + newFolder.getFolder ( ).getName ( ) + " creada--------------");
        } catch (Exception e) {
            logger.error ("*** Error al crear carpeta *** ", e);
        }
        return newFolder;

    }

    /**
     * @param informationArray Arreglo que trae el nombre de la carpeta para formatearlo para ser usado por el ECM
     * @param formatoConfig    Contiene el formato que se le dara al nombre
     * @return Nombre formateado
     */
    private String formatearNombre(String[] informationArray, String formatoConfig) {
        String formatoCadena;
        StringBuilder formatoFinal = new StringBuilder ( );
        try {
            formatoCadena = configuracion.getPropiedad (formatoConfig);
            String[] formatoCadenaArray = formatoCadena.split ("");
            for (String aFormatoCadenaArray : formatoCadenaArray) {

                String nomSerie = "3";
                String idOrgAdm = "0";
                String idOrgOfc = "1";
                String codSerie = "2";
                String codSubserie = "4";
                String nomSubserie = "5";
                if (aFormatoCadenaArray.equals (idOrgAdm)) {
                    formatoFinal.append (informationArray[Integer.parseInt (idOrgAdm)]);
                } else if (aFormatoCadenaArray.equals (idOrgOfc)) {
                    formatoFinal.append (informationArray[Integer.parseInt (idOrgOfc)]);
                } else if (aFormatoCadenaArray.equals (codSerie)) {
                    formatoFinal.append (informationArray[Integer.parseInt (codSerie)]);
                } else if (aFormatoCadenaArray.equals (nomSerie)) {
                    formatoFinal.append (informationArray[Integer.parseInt (nomSerie)]);
                } else if (aFormatoCadenaArray.equals (codSubserie)) {
                    formatoFinal.append (informationArray[Integer.parseInt (codSubserie)]);
                } else if (aFormatoCadenaArray.equals (nomSubserie)) {
                    formatoFinal.append (informationArray[Integer.parseInt (nomSubserie)]);
                } else if (isNumeric (aFormatoCadenaArray)) {
                    //El formato no cumple con los requerimientos minimos
                    logger.info ("El formato no cumple con los requerimientos.");
                    formatoFinal = null;
                    break;
                } else {
                    formatoFinal.append (aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            logger.error ("*** Error al formatear nombre *** ", e);
        }
        return formatoFinal != null ? formatoFinal.toString ( ) : "";
    }

    /**
     * Metodo que retorna true en caso de que la cadena que se le pasa es numerica y false si no.
     *
     * @param cadena Cadena de texto que se le pasa al metodo
     * @return Retorna true o false
     */
    private boolean isNumeric(String cadena) {
            return cadena.matches("[+-]?\\d*(\\.\\d+)?") && cadena.equals("")==Boolean.FALSE;
    }

    /**
     * Metodo que obtiene la carpeta dado el nombre
     *
     * @param nombreCarpeta NOmbre de la carpeta que se va a buscar
     * @param session       objeto de conexion al Alfresco
     * @return Retorna la Carpeta que se busca
     */
    private Carpeta obtenerCarpetaPorNombre(String nombreCarpeta, Session session) {
        Carpeta folder = new Carpeta ( );
        try {

            String queryString = "SELECT cmis:objectId FROM cmis:folder WHERE cmis:name = '" + nombreCarpeta + "'";
            ItemIterable <QueryResult> results = session.query (queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName ("cmis:objectId");
                folder.setFolder ((Folder) session.getObject (session.createObjectId (objectId)));
            }
        } catch (Exception e) {
            logger.error ("*** Error al obtenerCarpetas *** ", e);
        }

        return folder;

    }

    /**
     * Metodo que devuelve las carpetas hijas de una carpeta
     *
     * @param carpetaPadre Carpeta a la cual se le van a buscar las carpetas hijas
     * @return Lista de carpetas resultantes de la busqueda
     */
    private List <Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) {
        logger.info ("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder ( ).getName ( ));
        List <Carpeta> listaCarpetas = null;

        try {
            ItemIterable <CmisObject> listaObjetos = carpetaPadre.getFolder ( ).getChildren ( );

            listaCarpetas = new ArrayList <> ( );
            //Lista de carpetas hijas
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder) {
                    Carpeta folder = new Carpeta ( );
                    folder.setFolder ((Folder) contentItem);

                    listaCarpetas.add (folder);
                }
            }

        } catch (Exception e) {
            logger.error ("*** Error al obtener Carpetas Hijas dado padre*** ", e);
        }
        return listaCarpetas;
    }

    /**
     * Metodo para actualizar el nombre de la carpeta
     *
     * @param carpeta Carpeta a la cual se le va a actualizar el nombre
     * @param nombre  Nuevo nombre de la carpeta
     */
    private void actualizarNombreCarpeta(Carpeta carpeta, String nombre) {
        logger.info ("### Actualizando nombre folder: " + nombre);
        try {
            carpeta.getFolder ( ).rename (nombre);
        } catch (Exception e) {
            logger.error ("*** Error al actualizar nombre folder *** ", e);
        }
    }

    /**
     * Metodo que devuelve la carpeta padre de la carpeta que se le pase.
     *
     * @param folderFather Carpeta padre
     * @param codFolder    Codigo de la carpeta que se le va a chequear la carpeta padre
     * @return Carpeta padre
     */
    private Carpeta chequearCapetaPadre(Carpeta folderFather, String codFolder) {
        Carpeta folderReturn = null;
        List <Carpeta> listaCarpeta;
        Conexion conexion = obtenerConexion ( );
        listaCarpeta = obtenerCarpetasHijasDadoPadre (folderFather);
        Iterator <Carpeta> iterator;
        if (listaCarpeta != null) {
            iterator = listaCarpeta.iterator ( );
            while (iterator.hasNext ( )) {
                Carpeta aux = iterator.next ( );

                Carpeta carpeta = obtenerCarpetaPorNombre (aux.getFolder ( ).getName ( ), conexion.getSession ( ));
                String description = carpeta.getFolder ( ).getDescription ( );
                if (description.equals (configuracion.getPropiedad (CLASE_DEPENDENCIA))) {
                    if (aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodDependencia")) != null &&
                            aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodDependencia")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals (configuracion.getPropiedad (CLASE_SERIE))) {
                    if (aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodSerie")) != null &&
                            aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodSerie")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals (configuracion.getPropiedad (CLASE_SUBSERIE))) {
                    logger.info ("Entro a clase subserie cargando los valores");
                    if (aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodSubserie")) != null &&
                            aux.getFolder ( ).getPropertyValue (CMCOR + configuracion.getPropiedad ("metadatoCodSubserie")).equals (codFolder)) {
                        folderReturn = aux;
                    }
                }
            }
        }
        return folderReturn;
    }

    /**
     * Metodo para mover carpetas dentro de Alfresco
     *
     * @param session        Objeto de conexion al Alfresco
     * @param documento      Nombre del documento que se va a mover
     * @param carpetaFuente  Carpeta desde donde se va a mover el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento
     * @return Mensaje de respuesta del metodo(codigo y mensaje)
     */
    @Override
    public MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino) {
        logger.info ("### Mover documento: " + documento);
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {

            new Carpeta ( );
            Carpeta carpetaF;
            new Carpeta ( );
            Carpeta carpetaD;

            carpetaF = obtenerCarpetaPorNombre (carpetaFuente, session);
            carpetaD = obtenerCarpetaPorNombre (carpetaDestino, session);

            CmisObject object = session.getObjectByPath (carpetaF.getFolder ( ).getPath ( ) + "/" + documento);
            Document mvndocument = (Document) object;
            mvndocument.move (carpetaF.getFolder ( ), carpetaD.getFolder ( ));
            response.setMensaje ("OK");
            response.setCodMensaje ("0000");

        } catch (CmisObjectNotFoundException e) {
            logger.error ("*** Error al mover el documento *** ", e);
            response.setMensaje ("Documento no encontrado");
            response.setCodMensaje ("00006");
        }
        return response;
    }

    /**
     * Metodo para generar el arbol de carpetas dentro del Alfresco
     *
     * @param estructuraList Estrcutura que sera creada dentro del Alfresco
     * @param folder         Carpeta padre de la estructura
     * @return Mensaje de respuesta (codigo y mensaje)
     */
    @Override
    public MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder) {
        logger.info ("### Generando arbol");

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
                Carpeta folderSon;
                Carpeta folderFatherContainer = null;
                //Recorremos la lista organigrama
                for (OrganigramaDTO organigrama : organigramaList)
                    if (bandera == 0) {
                        folderFather = chequearCapetaPadre (folder, organigrama.getCodOrg ( ));
                        if (folderFather == null) {
                            logger.info ("Organigrama --  Creando folder: " + organigrama.getNomOrg ( ));
                            folderFather = crearCarpeta (folder, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ), CLASE_BASE, null);
                        } else {
                            logger.info ("Organigrama --  La carpeta ya esta creado: " + folderFather.getFolder ( ).getName ( ));
                            //Actualización de folder
                            if (!(organigrama.getNomOrg ( ).equals (folderFather.getFolder ( ).getName ( )))) {
                                logger.info ("Se debe actualizar al nombre: " + organigrama.getNomOrg ( ));
                                actualizarNombreCarpeta (folderFather, organigrama.getNomOrg ( ));
                            } else {
                                logger.info ("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg ( ));
                            }
                        }
                        bandera++;
                        folderFatherContainer = folderFather;

                    } else {
                        folderSon = chequearCapetaPadre (folderFather, organigrama.getCodOrg ( ));
                        if (folderSon == null) {
                            logger.info ("Organigrama --  Creando folder: " + organigrama.getNomOrg ( ));
                            folderSon = crearCarpeta (folderFather, organigrama.getNomOrg ( ), organigrama.getCodOrg ( ), CLASE_DEPENDENCIA, folderFather);
                        } else {
                            logger.info ("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder ( ).getName ( ));
                            //Actualización de folder
                            if (!(organigrama.getNomOrg ( ).equals (folderSon.getFolder ( ).getName ( )))) {
                                logger.info ("Se debe actualizar al nombre: " + organigrama.getNomOrg ( ));
                                actualizarNombreCarpeta (folderSon, organigrama.getNomOrg ( ));
                            } else {
                                logger.info ("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg ( ));
                            }
                        }
                        folderFather = folderSon;
                        folderFatherContainer = folderSon;
                        bandera++;

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
                    folderSon = chequearCapetaPadre (folderFatherContainer, dependencias.getCodSerie ( ));
                    if (folderSon == null) {
                        if (!"".equals (nombreSerie)) {
                            logger.info ("TRD --  Creando folder: " + nombreSerie);
                            folderSon = crearCarpeta (folderFatherContainer, nombreSerie, dependencias.getCodSerie ( ), CLASE_SERIE, folderFather);
                        } else {
                            logger.info ("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals (folderSon.getFolder ( ).getName ( )))) {
                            logger.info ("Se debe cambiar el nombre: " + nombreSerie);
                            actualizarNombreCarpeta (folderSon, nombreSerie);
                        } else {
                            logger.info ("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (dependencias.getCodSubSerie ( ) != null && !"".equals (dependencias.getCodSubSerie ( ))) {
                        String nombreSubserie = formatearNombre (dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre (folderFather, dependencias.getCodSubSerie ( ));
                        if (folderSon == null) {
                            if (!"".equals (nombreSubserie)) {
                                logger.info ("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta (folderFather, nombreSubserie, dependencias.getCodSubSerie ( ), "claseSubserie", folderFather);
                            } else {
                                logger.info ("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals (folderSon.getFolder ( ).getName ( )))) {
                                logger.info ("Se debe cambiar el nombre: " + nombreSubserie);
                                actualizarNombreCarpeta (folderSon, nombreSubserie);
                            } else {
                                logger.info ("TRD --  El folder ya esta creado: " + nombreSubserie);
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
            logger.error ("Error al crear arbol content ", e);
            response.setCodMensaje ("Error al crear el arbol");
            response.setMensaje ("111112");
        }
        logger.info ("### Estructura creada------..");
        return response;
    }

    /**
     * Metodo para subir documentos al Alfresco
     *
     * @param session          Objeto de conexion a Alfresco
     * @param nombreDocumento  Nombre del documento que se va a crear
     * @param documento        Documento que se va a subir
     * @param tipoComunicacion Tipo de comunicacion que puede ser Externa o Interna
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException {

        logger.info ("Se entra al metodo subirDocumento");

        String idDocumento = "";
        Map <String, List <InputPart>> uploadForm = documento.getFormDataMap ( );
        List <InputPart> inputParts = uploadForm.get ("documento");


        logger.info ("Debug------------------------------" + inputParts);

        String fileName;
        String mimeType = "application/pdf";
        for (InputPart inputPart : inputParts) {

            // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
            MultivaluedMap <String, String> headers = inputPart.getHeaders ( );
            String[] contentDispositionHeader = headers.getFirst ("Content-Disposition").split (";");
            for (String name : contentDispositionHeader) {
                if (name.trim ( ).startsWith ("filename")) {
                    String[] tmp = name.split ("=");
                    fileName = tmp[1].trim ( ).replaceAll ("\"", "");
                    logger.info ("El nombre del fichera es: " + fileName);
                }
            }
            InputStream inputStream = null;

            try {
                inputStream = inputPart.getBody (InputStream.class, null);
            } catch (IOException e) {
                logger.error ("### Error..------", e);
            }

            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray (inputStream);

            //Se definen las propiedades del documento a subir
            Map <String, Object> properties = new HashMap <> ( );

            properties.put (PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
            properties.put (PropertyIds.NAME, nombreDocumento);

            try {
                //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
                new Carpeta ( );
                Carpeta folderAlfresco;
                logger.info ("### Se elige la carpeta donde se va a guardar el documento a radicar..");

                if ("TP-CMCOE".equals (tipoComunicacion)) {

                    folderAlfresco = obtenerCarpetaPorNombre ("100101.00302_COMUNICACION_EXTERNA", session);
                } else {

                    folderAlfresco = obtenerCarpetaPorNombre ("100100.00301_COMUNICACION_INTERNA", session);
                }
                logger.info ("### Se elige la carpeta donde se va a guardar el documento a radicar.." + folderAlfresco.getFolder ( ).getName ( ));
                VersioningState vs = VersioningState.MAJOR;
                ContentStream contentStream = new ContentStreamImpl (nombreDocumento, BigInteger.valueOf (bytes.length), mimeType, new ByteArrayInputStream (bytes));
                //Se crea el documento
                logger.info ("### Se va a crear el documento..");
                Document newDocument = folderAlfresco.getFolder ( ).createDocument (properties, contentStream, vs);
                idDocumento = newDocument.getId ( );
                logger.info ("### Documento creado con id " + idDocumento);
            } catch (CmisContentAlreadyExistsException ccaee) {
                logger.error ("### Error tipo CmisContentAlreadyExistsException----------------------------- :", ccaee);
            } catch (CmisConstraintException cce) {
                logger.error ("### Error tipo CmisConstraintException----------------------------- :", cce);
            } catch (Exception e) {
                logger.error ("### Error tipo Exception----------------------------- :", e);
            }
        }
        return idDocumento;
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param tipoCarpeta tipo de carpeta
     * @param clase       clase de la carpeta
     * @param props       objeto de propiedades
     * @param codOrg      codigo
     */
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map <String, String> props, String codOrg) {
        props.put (PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad (clase));
        props.put (PropertyIds.DESCRIPTION, configuracion.getPropiedad (clase));
        props.put (tipoCarpeta, codOrg);
    }

}




