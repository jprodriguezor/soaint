package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

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

    private ITaskServices tareaOperaciones;
//    private EngineConexion engine = EngineConexion.getInstance();
//    private Estados estadosOperaciones;

    @Before
    public void setUp(){
        //seteando el objeto de entrada
        String idDespliegue= "co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT";
        String idProceso = "proceso.recibir-gestionar-doc";
        Long instanciaProceso = Long.valueOf(17613);
        Long idTarea = Long.valueOf(0);
        String usuario = "arce";
        String pass = "arce";
        Map<String, Object> parametros;
        List<EstadosEnum> estados;

        procesoDTO = EntradaProcesoDTO.newInstance().idProceso(idProceso).usuario(usuario).pass(pass)
                                                    .instanciaProceso(instanciaProceso).idDespliegue(idDespliegue)
                                                    .idTarea(idTarea).build();
//        ReflectionTestUtils.setField(processService, "tareaOperaciones", tareaOperaciones);
//        ReflectionTestUtils.setField(processService, "engine", engine);
//        ReflectionTestUtils.setField(processService, "estadosOperaciones", estadosOperaciones);

    }

    @Test
    public void listarProcesos() {
    }

    @Test
    public void listarVariablesProcesos() {
    }

    @Test
    public void listarProcesosInstanciaPorUsuarios() {
    }

    @Test
    public void abortarProceso() {
    }

    @Test
    public void iniciarProceso() throws SystemException {

        assertNotNull(procesoDTO);
        assertNotNull(processService);
        RespuestaProcesoDTO result = processService.iniciarProceso(procesoDTO);
        assertNotNull(processService.iniciarProceso(procesoDTO));
    }

    @Test
    public void iniciarProcesoPorTercero() {
    }

    @Test
    public void iniciarProcesoManual() {
    }

    @Test
    public void enviarSenalProceso() {
    }

    @Test
    public void senalInicioAutomatico() {
    }
}