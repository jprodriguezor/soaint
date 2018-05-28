package co.com.soaint.funcionario.business.control;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.apis.delegator.funcionarios.FuncionariosWebApiClient;
import co.com.soaint.funcionario.apis.delegator.security.SecurityApiClient;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by Manuel Martorell Gonzalez
 * on 5/24/2018 12:23 PM
 */
@Log4j2
public class FuncionariosControlTest {

    private final String crudFuncionarioResult = "1";

    //Class to be tested
    private FuncionariosControl funcionariosControl;

    //Dependencies (will be mocked)
    private SecurityApiClient securityApiClient;
    private FuncionariosWebApiClient funcionariosWebApiClient;

    //Test data
    private FuncionarioDTO funcionario;
    private List<FuncionarioDTO> listaFuncionarios;
    private FuncionariosDTO funcionarios;
    private List<RolDTO> roles;

    @Before
    public void setup() {
        funcionariosControl = new FuncionariosControl();
        securityApiClient = Mockito.mock(SecurityApiClient.class);
        funcionariosWebApiClient = Mockito.mock(FuncionariosWebApiClient.class);
        ReflectionTestUtils.setField(funcionariosControl, "funcionariosWebApiClient", funcionariosWebApiClient);
        ReflectionTestUtils.setField(funcionariosControl, "securityApiClient", securityApiClient);
        funcionario = new FuncionarioDTO(BigInteger.ONE, "CI", "12345678",
                "Manuel", "Martorell", "Gonzalez",
                "mmartorellg@gmail.com", "mmartorellg", "activo");
        listaFuncionarios = new ArrayList<>();
        listaFuncionarios.add(funcionario);
        RolDTO rol = new RolDTO("Administrador");
        roles = new ArrayList<>();
        roles.add(rol);
        funcionarios = new FuncionariosDTO();
        funcionarios.setFuncionarios(listaFuncionarios);
    }

    @Test
    public void verificarCredenciales() throws Exception {
        when(securityApiClient.verificarCredenciales(any(CredencialesDTO.class))).thenReturn(funcionario);
        when(funcionariosWebApiClient.listarFuncionarioByLoginName(anyString())).thenReturn(funcionario);
        FuncionarioDTO funcionarioDTO = funcionariosControl.verificarCredenciales(new CredencialesDTO());
        assertEquals(funcionarioDTO, funcionario);
    }

    @Test(expected = BusinessException.class)
    public void verificarCredencialesBusinessError() throws Exception {
        when(securityApiClient.verificarCredenciales(any(CredencialesDTO.class))).thenReturn(funcionario);
        doAnswer(invocation -> {
            throw new BusinessException();
        }).when(funcionariosWebApiClient).listarFuncionarioByLoginName(anyString());
        funcionariosControl.verificarCredenciales(new CredencialesDTO());
    }

    @Test(expected = Exception.class)
    public void verificarCredencialesError() throws Exception {
        when(securityApiClient.verificarCredenciales(any(CredencialesDTO.class))).thenReturn(funcionario);
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).listarFuncionarioByLoginName(anyString());
        funcionariosControl.verificarCredenciales(new CredencialesDTO());
    }

    @Test
    public void listarFuncionariosByDependenciaAndRolAndEstado() throws Exception {
        when(securityApiClient.listarUsusriosByRol(anyString())).thenReturn(listaFuncionarios);
        when(funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(anyString(), anyString()))
                .thenReturn(funcionarios);
        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
        FuncionariosDTO funcionariosDTO = funcionariosControl.
                listarFuncionariosByDependenciaAndRolAndEstado("", "", "");
        assertNotNull(funcionariosDTO);
        assertNotNull(funcionariosDTO.getFuncionarios());
        assertEquals(funcionariosDTO.getFuncionarios().size(), 1);
    }

//    @Test
//    public void listarFuncionariosByDependenciaAndRolAndEstadoNoUsuario() throws Exception {
//        when(securityApiClient.listarUsusriosByRol(anyString())).thenReturn(listaFuncionarios);
//        when(funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(anyString(), anyString()))
//                .thenReturn(funcionarios);
//        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
//        FuncionariosDTO funcionariosDTO = funcionariosControl.
//                listarFuncionariosByDependenciaAndRolAndEstado("", "", "");
//        assertNotNull(funcionariosDTO);
//        assertNotNull(funcionariosDTO.getFuncionarios());
//        assertEquals(funcionariosDTO.getFuncionarios().size(), 0);
//    }

    @Test
    public void listarFuncionariosByDependenciaAndRolAndEstadoNoRoles() throws Exception {
        FuncionarioDTO otroFuncionario = new FuncionarioDTO(BigInteger.ONE, "CI", "12345678",
                "Manuel", "Martorell", "Gonzalez",
                "mmartorellg@gmail.com", "otro", "activo");
        List<FuncionarioDTO> otraLista = new ArrayList<>();
        otraLista.add(otroFuncionario);
        when(securityApiClient.listarUsusriosByRol(anyString())).thenReturn(otraLista);
        when(funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(anyString(), anyString()))
                .thenReturn(funcionarios);
        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
        FuncionariosDTO funcionariosDTO = funcionariosControl.
                listarFuncionariosByDependenciaAndRolAndEstado("", "", "");
        assertNotNull(funcionariosDTO);
        assertNotNull(funcionariosDTO.getFuncionarios());
        assertEquals(funcionariosDTO.getFuncionarios().size(), 0);
    }

    @Test
    public void listarFuncionariosByCodDependenciaAndEstado() throws Exception {
        when(funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(anyString(), anyString()))
                .thenReturn(funcionarios);
        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
        List<FuncionarioDTO> funcionarios = funcionariosControl.listarFuncionariosByCodDependenciaAndEstado(anyString(), anyString());
        assertEquals(funcionarios, listaFuncionarios);
    }

    @Test(expected = Exception.class)
    public void listarFuncionariosByCodDependenciaAndEstadoNoFuncionario() throws Exception {
        FuncionariosDTO funcionariosEmpty = new FuncionariosDTO(new ArrayList<>());
        when(funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(anyString(), anyString()))
                .thenReturn(funcionariosEmpty);
        funcionariosControl.listarFuncionariosByCodDependenciaAndEstado(anyString(), anyString());
    }

    @Test(expected = Exception.class)
    public void listarFuncionariosByCodDependenciaAndEstadoSystemError() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).listarFuncionariosByDependenciaAndEstado(anyString(), anyString());
        funcionariosControl.listarFuncionariosByCodDependenciaAndEstado(anyString(), anyString());
    }

    @Test
    public void listarUsuariosByRol() throws Exception {
        when(securityApiClient.listarUsusriosByRol(anyString())).thenReturn(listaFuncionarios);
        List<FuncionarioDTO> funcionarios = funcionariosControl.listarUsuariosByRol("");
        assertEquals(funcionarios, listaFuncionarios);
    }

    @Test(expected = Exception.class)
    public void listarUsuariosByRolSystemError() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(securityApiClient).listarUsusriosByRol(anyString());
        funcionariosControl.listarUsuariosByRol("");
    }

    @Test(expected = Exception.class)
    public void listarUsuariosByRolNoFuncionario() throws Exception {
        when(securityApiClient.listarUsusriosByRol(anyString())).thenReturn(new ArrayList<>());
        funcionariosControl.listarUsuariosByRol("");
    }

    @Test
    public void crearFuncionario() throws SystemException, BusinessException {
        when(funcionariosWebApiClient.crearFuncionario(funcionario)).thenReturn(crudFuncionarioResult);
        String result = funcionariosControl.crearFuncionario(funcionario);
        Mockito.verify(securityApiClient).crearFuncionario(funcionario);
        assertEquals(result, crudFuncionarioResult);
    }

    @Test(expected = Exception.class)
    public void crearFuncionarioCatch() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).crearFuncionario(funcionario);
        funcionariosControl.crearFuncionario(funcionario);
    }

    @Test
    public void actualizarFuncionario() throws Exception {
        when(funcionariosWebApiClient.actualizarFuncionario(funcionario)).thenReturn(crudFuncionarioResult);
        funcionario.setPassword("");
        String result = funcionariosControl.actualizarFuncionario(funcionario);
        Mockito.verify(securityApiClient).actualizarFuncionario(funcionario);
        assertEquals(result, crudFuncionarioResult);
    }

    @Test
    public void actualizarFuncionarioWidthPassword() throws Exception {
        when(funcionariosWebApiClient.actualizarFuncionario(funcionario)).thenReturn(crudFuncionarioResult);
        funcionario.setPassword("test");
        String result = funcionariosControl.actualizarFuncionario(funcionario);
        Mockito.verify(securityApiClient).actualizarFuncionario(funcionario);
        assertEquals(result, crudFuncionarioResult);
    }

    @Test(expected = Exception.class)
    public void actualizarFuncionarioCatch() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).actualizarFuncionario(funcionario);
        funcionariosControl.actualizarFuncionario(funcionario);
    }

    @Test
    public void eliminarFuncionario() throws Exception {
        String result = funcionariosControl.eliminarFuncionario(BigInteger.ONE);
        assertEquals(result, "1");
    }

    @Test
    public void obtenerRoles() throws Exception {
        when(securityApiClient.obtenerRoles()).thenReturn(roles);
        List<RolDTO> rolDTOS = funcionariosControl.obtenerRoles();
        assertEquals(rolDTOS, roles);
    }

    @Test(expected = BusinessException.class)
    public void obtenerRolesBusinessError() throws Exception {
        when(securityApiClient.obtenerRoles()).thenReturn(roles);
        doAnswer(invocation -> {
            throw new BusinessException();
        }).when(securityApiClient).obtenerRoles();
        funcionariosControl.obtenerRoles();
    }

    @Test(expected = Exception.class)
    public void obtenerRolesError() throws Exception {
        when(securityApiClient.obtenerRoles()).thenReturn(roles);
        doAnswer(invocation -> {
            throw new Exception();
        }).when(securityApiClient).obtenerRoles();
        funcionariosControl.obtenerRoles();
    }

    @Test
    public void buscarFuncionario() throws Exception {
        when(funcionariosWebApiClient.buscarFuncionario(funcionario))
                .thenReturn(funcionarios);
        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
        FuncionariosDTO funcionariosDTO = funcionariosControl.buscarFuncionario(funcionario);
        assertNotNull(funcionariosDTO);
        assertNotNull(funcionariosDTO.getFuncionarios());
        assertEquals(funcionariosDTO.getFuncionarios().size(), 1);
    }

    @Test(expected = Exception.class)
    public void buscarFuncionarioCatch() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).buscarFuncionario(funcionario);
        funcionariosControl.buscarFuncionario(funcionario);
    }

    @Test
    public void consultarFuncionarioByIdeFunci() throws Exception {
        when(funcionariosWebApiClient.consultarFuncionarioByIdeFunci(BigInteger.ONE))
                .thenReturn(funcionario);
        FuncionarioDTO funcionarioDTO = funcionariosControl.consultarFuncionarioByIdeFunci(BigInteger.ONE);
        assertEquals(funcionarioDTO, funcionario);
    }

    @Test(expected = Exception.class)
    public void consultarFuncionarioByIdeFunciCatch() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).consultarFuncionarioByIdeFunci(BigInteger.ONE);
        funcionariosControl.consultarFuncionarioByIdeFunci(BigInteger.ONE);
    }

    @Test
    public void listarFuncionariosByLoginNameList() throws Exception {
        when(funcionariosWebApiClient.listarFuncionariosByLoginNameList(anyString()))
                .thenReturn(funcionarios);
        when(securityApiClient.obtenerRolesUsuario(anyString())).thenReturn(roles);
        FuncionariosDTO funcionariosDTO = funcionariosControl.listarFuncionariosByLoginNameList("");
        assertNotNull(funcionariosDTO);
        assertNotNull(funcionariosDTO.getFuncionarios());
        assertEquals(funcionariosDTO.getFuncionarios().size(), 1);
    }

    @Test(expected = Exception.class)
    public void listarFuncionariosByLoginNameListCatch() throws Exception {
        doAnswer(invocation -> {
            throw new Exception();
        }).when(funcionariosWebApiClient).listarFuncionariosByLoginNameList(anyString());
        funcionariosControl.listarFuncionariosByLoginNameList("");
    }
}