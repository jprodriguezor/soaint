///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package co.com.soaint.ecm.business.boundary.records;
//
//import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
//import co.com.soaint.ecm.business.boundary.records.interfaces.RecordManagerMediator;
//import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
//import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
//import co.com.soaint.foundation.framework.common.MessageUtil;
//import org.hibernate.secure.spi.IntegrationException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * @author JSGONZALEZ
// */
//@Service
//public class RecordsManager implements RecordManagerMediator {
//
//    Logger LOGGER = Logger.getLogger(RecordsManager.class.getName());
//
//    @Autowired
//    private RecordsControl control;
//
//    public RecordsManager() {
//    }
//
//    @Override
//    public MensajeRespuesta createStructureRecords(List<EstructuraTrdDTO> structure) throws IntegrationException {
//        /*Instanciamos el objeto de response*/
//        MensajeRespuesta response = new MensajeRespuesta();
//        try {
//            LOGGER.info("** createStructureRecords **");
//            //traemomos el nombre del filePlan del archivo de propiedades
//            String nameFilePlan = Configuracion.getPropiedad("dominio");
//            LOGGER.info("__ " + nameFilePlan);
//
//
//            LOGGER.info("________ ");
//            /*Con el checkFilePlan recorremos los filePlan para ver si existe uno con el mismo nombre
//        que tenemos en el archivo de propiedades
//             */
//            FilePlan fileplan = control.checkFilePlan(nameFilePlan);
//            /*Validamos que el filePlan no exista en records
//        si existe solo genera la estructura documental,
//        si no existe crea el filePlan y la estructura documental
//             */
//            LOGGER.info(fileplan + "-----------------------");
//            if (fileplan == null) {
//
//                String nameRepositori = Configuracion.getPropiedad("fpos");
//                String descFilePlan = Configuracion.getPropiedad("descFilePlan");
//
//                //Obtenemos el domino de records
//                RMDomain dominio = control.obtenerDominio();
//                //Creamos el filePlan
//                fileplan = control.crearFilePlan(dominio, nameRepositori, nameFilePlan, descFilePlan);
//                //Generamos la estructura documental
//                control.generateTree(structure, fileplan);
//
//            } else {
//                //Generamos la estructura documental
//                control.generateTree(structure, fileplan);
//
//            }
//            if (control.response.getCodMensaje().isEmpty()) {
//                response.setCodMensaje(MessageUtil.getMessage("cod00"));
//                response.setMensaje(MessageUtil.getMessage("msj00"));
//            }else{
//                response = control.response;
//            }
//
//        } catch (IntegrationException e) {
//            response.setCodMensaje(MessageUtil.getMessage("cod07"));
//            response.setMensaje(MessageUtil.getMessage("msj07"));
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//}
