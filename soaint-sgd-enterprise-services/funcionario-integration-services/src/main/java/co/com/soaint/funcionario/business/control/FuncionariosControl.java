package co.com.soaint.funcionario.business.control;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.apis.delegator.funcionarios.FuncionariosWebApiClient;
import co.com.soaint.funcionario.apis.delegator.security.SecurityApiClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class FuncionariosControl {

    @Autowired
    FuncionariosWebApiClient funcionariosWebApiClient;

    @Autowired
    SecurityApiClient securityApiClient;

    /**
     * @param credenciales
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO verificarCredenciales(CredencialesDTO credenciales) throws BusinessException, SystemException {
        FuncionarioDTO funcionario;
        try {
            FuncionarioDTO usuario = securityApiClient.verificarCredenciales(credenciales);
            funcionario = funcionariosWebApiClient.listarFuncionarioByLoginName(usuario.getLoginName());
            funcionario.setRoles(usuario.getRoles());
            return funcionario;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(String codDependencia, String rol, String codEstado) throws BusinessException, SystemException {
        List<FuncionarioDTO> usuariosRol = listarUsuariosByRol(rol);
        List<FuncionarioDTO> funcionariosDependencia = listarFuncionariosByCodDependenciaAndEstado(codDependencia, codEstado);
        List<FuncionarioDTO> resultado = new ArrayList<>();
        if (!usuariosRol.isEmpty() && !funcionariosDependencia.isEmpty()) {
            funcionariosDependencia.stream().forEach(funcionarioDTO -> {
                List<RolDTO> roles = obtenerRolesFromList(usuariosRol, funcionarioDTO.getLoginName());
                if (roles != null) {
                    funcionarioDTO.setRoles(roles);
                    resultado.add(funcionarioDTO);
                }
            });
        }
        return FuncionariosDTO
                .newInstance()
                .funcionarios(resultado)
                .build();

    }

    private List<RolDTO> obtenerRolesFromList(List<FuncionarioDTO> funcionarios, String loginName) {
        List<RolDTO> roles = null;
        FuncionarioDTO funcionario = funcionarios.stream().filter(x -> x.getLoginName().equals(loginName)).findFirst().orElse(null);
        if (funcionario != null)
            roles = funcionario.getRoles();
        return roles;
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarFuncionariosByCodDependenciaAndEstado(String codDependencia, String codEstado) throws BusinessException, SystemException {
        try {
            List<FuncionarioDTO> funcionariosDepenendencia = funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(codDependencia, codEstado).getFuncionarios();
            if (funcionariosDepenendencia.isEmpty())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_codDependencia")
                        .buildBusinessException();
            return funcionariosDepenendencia;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param rol
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarUsuariosByRol(String rol) throws BusinessException, SystemException {
        try {
            List<FuncionarioDTO> usuariosRol = securityApiClient.listarUsusriosByRol(rol);
            if (usuariosRol.isEmpty())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_rol")
                        .buildBusinessException();
            return usuariosRol;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public String crearFuncionario(FuncionarioDTO funcionario)throws SystemException{
        try {
            securityApiClient.crearFuncionario(funcionario);
            return funcionariosWebApiClient.crearFuncionario(funcionario);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funcionario)throws SystemException{
        try {
            if (funcionario.getPassword().isEmpty()){
                funcionario.setPassword(null);
            }
            securityApiClient.actualizarFuncionario(funcionario);
            return funcionariosWebApiClient.actualizarFuncionario(funcionario);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param idFuncionario
     * @throws SystemException
     */
    public String eliminarFuncionario(BigInteger idFuncionario)throws SystemException{
        try {
            //securityApiClient.crearFuncionario();
            FuncionarioDTO funcionario = funcionariosWebApiClient.consultarFuncionarioByIdeFunci(idFuncionario);
            funcionario.setEstado("I");
            return funcionariosWebApiClient.actualizarFuncionario(funcionario);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @throws SystemException
     */
    public List<RolDTO> obtenerRoles() throws BusinessException, SystemException{
        try {
            return securityApiClient.obtenerRoles();
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO)throws SystemException{
        try {
            return funcionariosWebApiClient.buscarFuncionario(funcionarioDTO);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci)throws SystemException{
        try {
            return funcionariosWebApiClient.consultarFuncionarioByIdeFunci(ideFunci);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
