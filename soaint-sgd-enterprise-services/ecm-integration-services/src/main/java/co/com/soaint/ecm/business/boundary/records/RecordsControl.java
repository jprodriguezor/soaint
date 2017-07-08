///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package co.com.soaint.ecm.business.boundary.records;
//
//
//import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
//import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
//import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
//import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
//import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
//import co.com.soaint.foundation.framework.common.MessageUtil;
//import org.springframework.stereotype.Service;
//import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
//import java.io.IOException;
//import java.util.List;
//import java.util.logging.Logger;
//
//
//
///**
// * @author JSGONZALEZ
// */
//
//@Service
//public class RecordsControl {
//
//    private static Logger LOGGER = Logger.getLogger(RecordsControl.class.getName());
//    MensajeRespuesta response = new MensajeRespuesta();
////    private DomainConnection jarmConexion = null;
//    private final String ID_ORG_ADM = "0";
//    private final String ID_ORG_OFC = "1";
//    private final String COD_SERIE = "2";
//    private final String NOM_SERIE = "3";
//    private final String COD_SUBSERIE ="4";
//    private final String NOM_SUBSERIE = "5";
//
//    public RecordCategory returnRecordCategory(RecordCategoryContainer categoryFather,
//                                               String codRecordCategory) {
//        LOGGER.info("** returnRecordCategory ** " + categoryFather);
//        RecordCategory rcReturn = null;
//
//        if (categoryFather != null) {
//            PageableSet<RecordCategory> prueba = categoryFather.fetchRecordCategories(RMPropertyFilter.MinimumPropertySet, null);
//
//            for (RecordCategory rc : prueba) {
//
//                if (rc.getRecordCategoryIdentifier().equals(codRecordCategory)) {
//                    rcReturn = rc;
//                }
//            }
//        }
//        return rcReturn;
//    }
//
//    public RecordCategory checkRecordCategoriFilePlan(RecordCategoryContainer categoryFather,
//                                                               String nameRecordCategori, String codRecordCategori) {
//        LOGGER.info("** checkRecordCategoriFilePlan ** " + categoryFather);
//        RecordCategory rcReturn = null;
//        if (categoryFather != null) {
//            PageableSet<RecordCategory> prueba = categoryFather.fetchRecordCategories(RMPropertyFilter.MinimumPropertySet, null);
//            for (RecordCategory rc : prueba) {
//
//                if (rc.getRecordCategoryIdentifier().equals(codRecordCategori)) {
//                    rcReturn = rc;
//                    }
//            }
//        }
//
//        return rcReturn;
//    }
//
//    public static boolean actualizarNombreFolder(RecordCategory carpeta, String nombre)throws ECMIntegrationException{
//        LOGGER.info("*** Actualizando folder: "+nombre);
//        boolean estado;
//        try{
//            carpeta.getProperties().putStringValue("RecordCategoryName",nombre);
//            carpeta.save(RMRefreshMode.Refresh);
//            estado = true;
//        }catch (Exception e){
//            estado = false;
//            LOGGER.info("*** Error al formatear nombre ***");
//        }
//        return estado;
//    }
//
//    public MensajeRespuesta generateTree(List<EstructuraTrdDTO> structure, FilePlan fileplan) throws ECMIntegrationException {
//        LOGGER.info("** generateTree **" + structure.size());
//        int bandera = 0;
//        //Recorremos la lista que llega desde el servicio
//        for (EstructuraTrdDTO registro : structure) {
//            bandera = 0;
//            List<OrganigramaDTO> listaOrganigrama = registro.getOrganigramaItemList();
//            List<ContenidoDependenciaTrdDTO> listaTRD = registro.getContenidoDependenciaList();
//            Utilities utils = new Utilities();
//            utils.ordenarListaOrganigrama(listaOrganigrama);
//            RecordCategory categorySon = null;
//            RecordCategory categoryFather = null;
//            RecordCategoryContainer categoryFatherTRD = null;
//            RecordCategoryContainer recordCategoryContainerTRD = null;
//            RecordCategory recordCategoryTrd = null;
//            //Recorremos la lista del organigrama
//            for (OrganigramaDTO organigrama : listaOrganigrama) {
//                switch (bandera) {
//                    case 0:
//                        categoryFather = checkRecordCategoriFilePlan(fileplan, organigrama.getNomOrg(), organigrama.getCodOrg());
//                        if (categoryFather == null) {
//                            LOGGER.info("Organigrama Records --  Creando folder: " + organigrama.getNomOrg());
//                            categoryFather = crearCategoriaDeRegistro(fileplan, organigrama.getNomOrg(), organigrama.getCodOrg(), "");
//                        }else{
//                            //Actualización de folder
//                            if(!(organigrama.getNomOrg().equals(categoryFather.getName()))){
//                                LOGGER.info("Se debe actualizar al nombre: "+organigrama.getNomOrg());
//                                actualizarNombreFolder(categoryFather,organigrama.getNomOrg());
//                            }else{
//                                LOGGER.info("Organigrama Records --  El folder ya esta creado: " + organigrama.getNomOrg());
//                            }
//                        }
//                        bandera++;
//                        break;
//                    default:
//                        categorySon = checkRecordCategoriFilePlan(categoryFather, organigrama.getNomOrg(), organigrama.getCodOrg());
//                        if (categorySon == null) {
//                            LOGGER.info("Organigrama Records --  Creando folder: " + organigrama.getNomOrg());
//                            categorySon = crearCategoriaDeRegistroHijo(organigrama.getNomOrg(), organigrama.getCodOrg(), "", categoryFather);
//                        }else{
//                            //Actualización de folder
//                            if(!(organigrama.getNomOrg().equals(categorySon.getName()))){
//                                LOGGER.info("Se debe actualizar al nombre: "+organigrama.getNomOrg());
//                                actualizarNombreFolder(categorySon, organigrama.getNomOrg());
//                            }else{
//                                LOGGER.info("Organigrama Records --  El folder ya esta creado: " + organigrama.getNomOrg());
//                            }
//                        }
//                         categoryFather = categorySon;
//                        categoryFatherTRD = categoryFather;
//                        bandera++;
//                        break;
//                }
//            }
//            recordCategoryContainerTRD = categoryFather;
//            //recorremos la lista de la TRD
//            for (ContenidoDependenciaTrdDTO trd : listaTRD) {
//                String[] dependenciasArray = {trd.getIdOrgAdm(),
//                        trd.getIdOrgOfc(),
//                        trd.getCodSerie(),
//                        trd.getNomSerie(),
//                        trd.getCodSubSerie(),
//                        trd.getNomSubSerie(),
//                };
//                String nombreSerie =  formatearNombre(dependenciasArray, "formatoNombreSerie");
//                categorySon = checkRecordCategoriFilePlan(recordCategoryContainerTRD, nombreSerie, trd.getCodSerie());
//                if (categorySon == null) {
//                    if(nombreSerie != null){
//                        LOGGER.info("TRD --  Creando folder: " + nombreSerie);
//                        categorySon = crearCategoriaDeRegistroHijo(nombreSerie, trd.getCodSerie(), "", recordCategoryContainerTRD);
//                    }else{
//                        LOGGER.info("El formato para el nombre de la serie no es valido.");
//                        break;
//                    }
//                }else{
//                    //Actualización de folder
//                    if(!(nombreSerie.equals(categorySon.getName()))){
//                        LOGGER.info("Se debe cambiar el nombre: "+nombreSerie);
//                        actualizarNombreFolder(categorySon, nombreSerie);
//                    }else{
//                        LOGGER.info("TRD --  El folder ya esta creado: " + nombreSerie);
//                    }
//                }
//                categoryFather = categorySon;
//                if (!trd.getCodSubSerie().isEmpty()) {
//                    String nombreSubserie = formatearNombre(dependenciasArray, "formatoNombreSubserie");
//                    categorySon = checkRecordCategoriFilePlan(categoryFather, nombreSubserie, trd.getCodSubSerie());
//                    if (categorySon == null) {
//                        if(nombreSubserie != null) {
//                            LOGGER.info("TRD --  Creando folder: " + nombreSubserie);
//                            categorySon = crearCategoriaDeRegistroHijo(nombreSubserie, trd.getCodSubSerie(), "", categoryFather);
//                        }else{
//                            LOGGER.info("El formato para el nombre de la subserie no es valido.");
//                            break;
//                        }
//                    }else{
//                        //Actualización de folder
//                        if(!(nombreSubserie.equals(categorySon.getName()))){
//                            LOGGER.info("Se debe cambiar el nombre: "+nombreSubserie);
//                            actualizarNombreFolder(categorySon, nombreSubserie);
//                        }else{
//                            LOGGER.info("TRD --  El folder ya esta creado: " + nombreSubserie);
//                        }
//                    }
//                    recordCategoryTrd = returnRecordCategory(categoryFather, trd.getCodSubSerie());
//                } else {
//                    recordCategoryTrd = returnRecordCategory(recordCategoryContainerTRD, trd.getCodSerie());
//                }
//                String nameSchedule = "AG" + trd.getRetArchivoGestion() + "-AC" + trd.getRetArchivoCentral()
//                        + "-DF" + nameSchedule(trd.getDiposicionFinal());
//                String nameAG = "AG" + trd.getRetArchivoGestion();
//                String nameAC = "AC" + trd.getRetArchivoCentral();
//                int timeAG = Integer.parseInt(trd.getRetArchivoGestion().toString());
//                int timeAC = Integer.parseInt(trd.getRetArchivoCentral().toString());
//                if (recordCategoryTrd != null) {
//                    try {
//                        recordCategoryTrd.assignDispositionSchedule(associateSchedule(nameSchedule, trd.getProcedimiento(), nameAG, timeAG, nameAC, timeAC, trd.getDiposicionFinal()),
//                                SchedulePropagation.ToImmediateSubContainersAndAllInheritors);
//                    } catch (Exception e) {
//                        response.setCodMensaje(MessageUtil.getMessage("cod06"));
//                        response.setMensaje(MessageUtil.getMessage("msj06"));
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        return response;
//    }
//
//    public String  formatearNombre(String[] informationArray, String formatoConfig) throws ECMIntegrationException {
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
//
//    }
//
//    private static boolean isNumeric(String cadena){
//        try {
//            Integer.parseInt(cadena);
//            return true;
//        } catch (NumberFormatException nfe){
//            return false;
//        }
//    }
//
//    public String nameSchedule(int dispoFinal) {
//        String nameSche = "";
//        switch (dispoFinal) {
//            case 1:
//                nameSche = "MF";
//                break;
//            case 2:
//                nameSche = "CT";
//                break;
//            case 3:
//                nameSche = "S";
//                break;
//            case 4:
//                nameSche = "E";
//                break;
//            case 5:
//                nameSche = "D";
//                break;
//            default:
//                nameSche = "NA";
//                break;
//        }
//        return nameSche;
//    }
//
//    public DispositionSchedule associateSchedule(String scheduleName, String description,
//                                                 String nameAG, int timeAction1, String nameAC, int timeAction2, int typeDispo) throws ECMIntegrationException, IOException {
//        LOGGER.info("** associateSchedule **");
//        ScheduleRecordsControl srm = new ScheduleRecordsControl();
//        RMDomain domain = obtenerDominio();
//        FilePlanRepository fpRepository
//                = RMFactory.FilePlanRepository.getInstance(domain, Configuracion.getPropiedad("fpos"));
//
//        String action1 = Configuracion.getPropiedad("A_GESTION");
//        String action2 = Configuracion.getPropiedad("A_CENTRAL");
//
//        DispositionSchedule dispositionSchedule = srm.createDispositionTrigger(fpRepository, scheduleName, description,
//                action1, nameAG, timeAction1, action2, nameAC, timeAction2, typeDispo);
//
//        return dispositionSchedule;
//
//    }
//
//    public FilePlan checkFilePlan(String nameFilePlan) throws ECMIntegrationException {
//        LOGGER.info("** checkFilePlan ** " + nameFilePlan);
//        RMDomain jarmDomain = obtenerDominio();
//        RMPropertyFilter filt = RMPropertyFilter.MinimumPropertySet;
//        FilePlan filePlanExist = null;
//        if (jarmDomain == null) {
//            jarmDomain = obtenerDominio();
//        }
//
//        LOGGER.info("** //jarmDomain ** " + jarmDomain);
//        if (jarmDomain != null) {
//            List<FilePlanRepository> fpRepositories
//                    = jarmDomain.fetchFilePlanRepositories(filt);
//            //Recorremos el plan de repositorios de records
//            for (FilePlanRepository fP : fpRepositories) {
//                List<FilePlan> filePlans = fP.getFilePlans(filt);
//                //Recorremos los filePlan del repositorio
//                for (FilePlan fps : filePlans) {
//                    //Validamos que no esxista un filePlan con el nombre ingresado
//                    if (fps.getName().equals(nameFilePlan)) {
//                        filePlanExist = fps;
//                    }
//                }
//            }
//        }
//        LOGGER.info("** checkFilePlan ** " + filePlanExist);
//        return filePlanExist;
//    }
//
//    public FilePlan crearFilePlan(RMDomain dominio, String nombreRepositorio, String nombreFilePlan,
//                                  String descripcionFilePlan) throws ECMIntegrationException {
//        RMPropertyFilter filtro = null;
//        FilePlanRepository repos = null;
//        RMProperties jarmProps = null;
//        String claseIdentidad = null;
//        List<RMPermission> jarmPerms = null;
//        FilePlan nuevoFileplan = null;
//        try {
//            filtro = RMPropertyFilter.MinimumPropertySet;
//            repos = RMFactory.FilePlanRepository.fetchInstance(dominio, nombreRepositorio, filtro);
//            jarmProps = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
//            jarmProps.add(RMPropertyName.FilePlanName, // Nombre
//                    DataType.String, // Tipo de dato
//                    RMCardinality.Single, // Simple o multivalor
//                    nombreFilePlan); // Valor
//
//            // El mecanismo de ajuste de valor de la propiedad más comúnmente
//            // utilizado :
//            jarmProps.putStringValue(RMPropertyName.RMEntityDescription, descripcionFilePlan); // Valor.
//            jarmProps.putIntegerValue(RMPropertyName.RetainMetadata, RetainMetadata.NeverRetain.getIntValue());
//            claseIdentidad = RMClassName.FilePlan;
//            // El metodo add crea el plan de archivos
//            nuevoFileplan = repos.addFilePlan(claseIdentidad, jarmProps, jarmPerms);
//
//        } catch (Exception e) {
//            response.setMensaje(MessageUtil.getMessage("msj02"));
//            response.setCodMensaje(MessageUtil.getMessage("cod02"));
//            e.printStackTrace();
//        }
//
//        return nuevoFileplan;
//    }
//
//    public RecordCategory crearCategoriaDeRegistro(FilePlan plan, String nombreCategoria,
//                                                   String identificadorCategoria, String personaReviewer) throws ECMIntegrationException {
//        LOGGER.info("**** crearCategoriaDeRegistro ****");
//        RMProperties jarmPropes = null;
//        String claseIdentidad = null;
//        RecordCategory nuevaCategoria = null;
//
//        try {
//            RecordCategoryContainer parent = plan;
//            jarmPropes = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
//            jarmPropes.putStringValue(RMPropertyName.RecordCategoryName, nombreCategoria);
//            jarmPropes.putStringValue(RMPropertyName.RecordCategoryIdentifier, identificadorCategoria);
//            jarmPropes.putStringValue(RMPropertyName.Reviewer, personaReviewer);
//
//            claseIdentidad = RMClassName.RecordCategory;
//            // El metodo addRecordCategory crea la categoria
//            nuevaCategoria = parent.addRecordCategory(claseIdentidad, jarmPropes, null);
//
//        } catch (Exception e) {
//            response.setCodMensaje(MessageUtil.getMessage("cod04"));
//            response.setCodMensaje(MessageUtil.getMessage("msj04"));
//            e.printStackTrace();
//
//        }
//        return nuevaCategoria;
//    }
//
//    public RMDomain obtenerDominio() throws ECMIntegrationException {
//        LOGGER.info("**** obtenerDominio *****");
//        RMDomain dominio = null;
//
//        try {
//            if (jarmConexion == null) {
//                Conexion con = new Conexion();
//                jarmConexion = con.obtenerConexion();
//            }
//            if (jarmConexion == null) {
//                response.setCodMensaje(MessageUtil.getMessage("cod01"));
//                response.setMensaje(MessageUtil.getMessage("msj01"));
//            } else {
//                dominio = RMFactory.RMDomain.fetchInstance(jarmConexion, null, null);
//            }
//
//
//        } catch (Exception e) {
//            response.setCodMensaje(MessageUtil.getMessage("cod03"));
//            response.setMensaje(MessageUtil.getMessage("msj03"));
//            e.printStackTrace();
//        }
//        return dominio;
//    }
//
//    public RecordCategory crearCategoriaDeRegistroHijo(String nombreCategoria,
//                                                       String identificadorCategoria, String personaReviewer, RecordCategoryContainer inPadreRC) throws ECMIntegrationException {
//
//        LOGGER.info("**** crearCategoriaDeRegistroHijo **** ");
//
//        RMProperties jarmPropes = null;
//        String claseIdentidad = null;
//        RecordCategory padre = null;
//
//        try {
//
//            jarmPropes = RMFactory.RMProperties.createInstance(DomainType.P8_CE);
//            jarmPropes.putStringValue(RMPropertyName.RecordCategoryName, nombreCategoria);
//            jarmPropes.putStringValue(RMPropertyName.RecordCategoryIdentifier, identificadorCategoria);
//            jarmPropes.putStringValue(RMPropertyName.Reviewer, personaReviewer);
//
//            //RMClassName.RecordCategory
//            claseIdentidad = RMClassName.RecordCategory;
//
//            // El metodo addRecordCategory crea la categoria
//            padre = inPadreRC.addRecordCategory(claseIdentidad, jarmPropes, null);
//
//        } catch (Exception e) {
//            response.setCodMensaje(MessageUtil.getMessage("cod04"));
//            response.setMensaje(MessageUtil.getMessage("msj04"));
//            e.printStackTrace();
//        }
//
//        if (padre != null) {
//            padre.save(RMRefreshMode.Refresh);
//        }
//
//        return padre;
//    }
//
//    public RecordFolder obtenerRecordFolder(FilePlanRepository repositorio, String id) throws ECMIntegrationException {
//        RecordFolder carpetaRegistro = null;
//        RMPropertyFilter filter = null;
//
//        try {
//            carpetaRegistro = (RecordFolder) RMFactory.RecordFolder.fetchInstance(repositorio, id, filter);
//        } catch (Exception e) {
//            response.setCodMensaje(MessageUtil.getMessage("cod04"));
//            response.setMensaje (MessageUtil.getMessage("msj04"));
//            e.printStackTrace();
//        }
//
//        return carpetaRegistro;
//    }
//
//
//}
