package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class ProcessServiceTest {
    private ProcessService processService;
    private EntradaProcesoDTO entityManager;

    private ITaskServices tareaOperaciones;
    private EngineConexion engine = EngineConexion.getInstance();
    private Estados estadosOperaciones = new Estados();

    @Before
    public void setUp(){
        processService = mock(ProcessService.class);
        entityManager = mock(EntradaProcesoDTO.class);
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
        ReflectionTestUtils.setField(processService, "tareaOperaciones", tareaOperaciones);
        ReflectionTestUtils.setField(processService, "engine", engine);
        ReflectionTestUtils.setField(processService, "estadosOperaciones", estadosOperaciones);
        assertNotNull(entityManager);
        assertNotNull(processService);
//        RespuestaProcesoDTO result = processService.iniciarProceso(entityManager);
        assertNotNull(processService.iniciarProceso(entityManager));
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