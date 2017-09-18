package co.com.soaint.funcionario.business.control;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.apis.delegator.funcionarios.FuncionariosWebApiClient;
import co.com.soaint.funcionario.apis.delegator.security.SecurityApiClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
     * 
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
        FuncionariosDTO funcionariosDTO = FuncionariosDTO
                .newInstance()
                .funcionarios(new ArrayList<>())
                .build();
        try {
            List<FuncionarioDTO> usuariosRol = listarUsuariosByRol(rol);

            List<FuncionarioDTO> funcionariosDepenendencia = listarFuncionariosByCodDependenciaAndEstado(codDependencia, codEstado);

            funcionariosDepenendencia.stream().forEach(funcionario -> {
                FuncionarioDTO usuarioRol = usuariosRol.stream().filter(x -> x.getLoginName().equals(funcionario.getLoginName())).findFirst().get();
                if (usuarioRol != null) {
                    funcionario.setRoles(usuarioRol.getRoles());
                    funcionariosDTO.getFuncionarios().add(funcionario);
                }
            });
            return funcionariosDTO;
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
     *
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
}