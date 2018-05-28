package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rules.ConnectionRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/core-config.xml", "/spring/db-persistence-integration-test.xml"})

public class ProcessServiceTest {
    @Autowired
    private ProcessService processService;
    private EntradaProcesoDTO procesoDTO;

    @Rule
    public ConnectionRule connectionRule = new ConnectionRule();

    @Before
    public void setUp() {
        //seteando el objeto de entrada
        String idDespliegue = "co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT";
        String idProceso = "proceso.recibir-gestionar-doc";
//        Long instanciaProceso = Long.valueOf(17613);
//        Long idTarea = Long.valueOf(0);
        String usuario = "arce";
        String pass = "arce";

        try {
//            String parametros = "{\"idAgente\":\"138\",\"idAsignacion\":\"49\",\"numeroRadicado\":\"1040TP-CMCOE2017000011\", \"usuario\":\"arce\"}";
            String parametros = "{}";
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            Map<String, String> map = new HashMap<String, String>();
            HashMap<String, Object> o = objectMapper.readValue(parametros, typeRef);
            map = objectMapper.readValue(parametros, new TypeReference<HashMap<String, String>>() {
            });

            List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
            estados.add(EstadosEnum.LISTO);

            procesoDTO = EntradaProcesoDTO.newInstance().idProceso(idProceso).usuario(usuario).pass(pass).
                    idDespliegue(idDespliegue).parametros(o).estados(estados).build();

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void iniciarProcesoTest() throws SystemException {

        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProceso(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getCodigoProceso());
    }

    @Test
    public void iniciarProcesoPorTerceroTest() throws SystemException {
        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProcesoPorTercero(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getCodigoProceso());
    }

    @Test
    public void iniciarProcesoManualTest() throws SystemException {
        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProcesoManual(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getCodigoProceso());
    }

//    @Test
//    public void enviarSenalProceso() {
//    }
//
//    @Test
//    public void senalInicioAutomatico() {
//    }

//    @Test
//    public void listarProcesos() {
//    }
//
//    @Test
//    public void listarVariablesProcesos() {
//    }
//
//    @Test
//    public void listarProcesosInstanciaPorUsuarios() {
//    }
//
//    @Test
//    public void abortarProceso() {
//    }
}