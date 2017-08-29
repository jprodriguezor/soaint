package co.com.soaint.funcionario.business.control;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
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
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(String codDependencia, String rol, String codEstado) throws SystemException {
        FuncionariosDTO funcionariosDTO = FuncionariosDTO
                .newInstance()
                .funcionarios(new ArrayList<>())
                .build();
        try {
            List<FuncionarioDTO> usuariosRol = securityApiClient.listarUsusriosByRol(rol);
            funcionariosWebApiClient.listarFuncionariosByDependenciaAndEstado(codDependencia, codEstado).getFuncionarios().stream().forEach(funcionario -> {
                FuncionarioDTO usuarioRol = usuariosRol.stream().filter(x -> x.getLoginName().equals(funcionario.getLoginName())).findFirst().get();
                if (usuarioRol != null) {
                    funcionario.setRoles(usuarioRol.getRoles());
                    funcionariosDTO.getFuncionarios().add(funcionario);
                }
            });
            return funcionariosDTO;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
