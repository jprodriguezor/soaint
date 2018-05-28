package co.com.soaint.bpm.services.integration.services.impl;

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

            procesoDTO = EntradaProcesoDTO.newInstance().idProceso(idProceso).usuario(usuario).pass(pass).
                    idDespliegue(idDespliegue).parametros(o).build();

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void iniciarProcesoTest() throws SystemException {

        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);

        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProceso(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getIdDespliegue());
    }

    @Test
    public void iniciarProcesoPorTerceroTest() throws SystemException {
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);

        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProcesoPorTercero(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getIdDespliegue());
    }

    @Test
    public void iniciarProcesoManualTest() throws SystemException {
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);

        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProcesoManual(procesoDTO);
        assertNotNull(result);
        System.out.println(result.getIdDespliegue());
    }

    @Test
    public void senalInicioAutomaticoTest() throws SystemException{
        procesoDTO.setIdProceso("proceso.archivar-documento");
        procesoDTO.setIdDespliegue("co.com.soaint.sgd.process:proceso-archivar-documento:1.0.0-SNAPSHOT");
        procesoDTO.setIdTarea(Long.valueOf(4931));
        try {
            String parametros = "{\"nombreSennal\":\"archivarDocumento\",\"numeroRadicado\":\"CS-CTC-2017-000002\",\"codDependencia\":\"1040.1040\"}";
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            Map<String, String> map = new HashMap<String, String>();
            HashMap<String, Object> o = objectMapper.readValue(parametros, typeRef);
            map = objectMapper.readValue(parametros, new TypeReference<HashMap<String, String>>() {
            });
            procesoDTO.setParametros(o);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.senalInicioAutomatico(procesoDTO);
        assertNotNull(result);
        System.out.print(result.getIdDespliegue());
    }

    @Test
    public void abortarProcesoTest() throws SystemException{
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);
        RespuestaProcesoDTO result = processService.iniciarProcesoManual(procesoDTO);
        assertNotNull(result);
        procesoDTO.setIdDespliegue(result.getIdDespliegue());
        procesoDTO.setInstanciaProceso(Long.valueOf(result.getCodigoProceso()));
        RespuestaProcesoDTO result1 = processService.abortarProceso(procesoDTO);
        assertNotNull(result1);
        System.out.print(result1.getEstado());

    }

    @Test
    public void listarVariablesProcesosTest() throws SystemException {
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);
        assertNotNull(procesoDTO);
        RespuestaProcesoDTO result = processService.iniciarProcesoManual(procesoDTO);
        assertNotNull(result);
        String result1 = processService.listarVariablesProcesos(procesoDTO);
        System.out.print(result1);

    }

    @Test
    public void listarProcesosTest() throws SystemException{
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);
        assertNotNull(procesoDTO);
        List<RespuestaProcesoDTO> listaProcesos =processService.listarProcesos(procesoDTO);
        for (int i = 0; i< listaProcesos.size(); i ++){
            System.out.print(listaProcesos.get(i).getCodigoProceso() + "\n");
        }

    }

    @Test
    public void listarProcesosInstanciaPorUsuariosTest() throws SystemException{
        List<EstadosEnum> estados = new ArrayList<EstadosEnum>();
        estados.add(EstadosEnum.LISTO);
        procesoDTO.setEstados(estados);
        assertNotNull(procesoDTO);
        List<RespuestaProcesoDTO> listaProcesos =processService.listarProcesosInstanciaPorUsuarios(procesoDTO);
        for (int i = 0; i< listaProcesos.size(); i ++){
            System.out.print(listaProcesos.get(i).getCodigoProceso() + "\n");
        }
    }


}