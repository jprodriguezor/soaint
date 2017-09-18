package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import com.soaint.services.security_cartridge._1_0.AuthenticationResponseContext;
import com.soaint.services.security_cartridge._1_0.OperationPrincipalContextListStatus;
import com.soaint.services.security_cartridge._1_0.SecurityAPIService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class SecurityApiClient {

    @Value("${securityapi.endpoint.url}")
    private String endpoint = "";

    /**
     * @param rol
     * @return
     * @throws SystemException
     */
    public List<FuncionarioDTO> listarUsusriosByRol(String rol) throws SystemException {
        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        try {
            SecurityAPIService securityApiService = new SecurityAPIService(new URL(endpoint));
            OperationPrincipalContextListStatus respuesta = securityApiService.getSecurityAPIPort().obtenerUsuariosporRol(rol);
            if (respuesta.isSuccessful())
                respuesta.getUsuarios().getUsuario().stream().forEach(usuario -> {
                    FuncionarioDTO funcionario = FuncionarioDTO.newInstance()
                            .loginName(usuario.getUsername())
                            .roles(new ArrayList<>())
                            .build();
                    usuario.getRoles().getRol().stream().forEach(uRol -> funcionario.getRoles().add(RolDTO.newInstance().rol(uRol.getName()).build()));
                    funcionarios.add(funcionario);
                });
            return funcionarios;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

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
            SecurityAPIService securityApiService = new SecurityAPIService(new URL(endpoint));
            AuthenticationResponseContext respuesta = securityApiService.getSecurityAPIPort().verifyCredentials(credenciales.getLoginName(), credenciales.getPassword());
            if (respuesta.isSuccessful()) {
                funcionario = FuncionarioDTO.newInstance()
                        .loginName(respuesta.getPrincipalContext().getUsername())
                        .roles(new ArrayList<>())
                        .build();
                respuesta.getPrincipalContext().getRoles().getRol()
                        .stream().forEach(rol -> funcionario.getRoles().add(RolDTO.newInstance().rol(rol.getName()).build()));
                return funcionario;
            }
            else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.autentication_failed")
                        .buildBusinessException();
        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}