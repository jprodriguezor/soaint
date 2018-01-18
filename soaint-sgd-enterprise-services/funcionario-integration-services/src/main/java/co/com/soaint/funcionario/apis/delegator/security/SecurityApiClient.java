package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import com.soaint.services.security_cartridge._1_0.*;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import java.net.MalformedURLException;
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
            SecurityAPIService securityApiService = getSecutrityApiService();
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
            SecurityAPIService securityApiService = getSecutrityApiService();
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

    /**
     *
     * @param funcionario
     * @throws BusinessException
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionario)throws BusinessException, SystemException{
        try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationPrincipalStatusContext respuesta = securityApiService.getSecurityAPIPort().crearUsuario(transformToPrincipalContext(funcionario));
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.creation_failed")
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

    /**
     *
     * @param funcionario
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarFuncionario(FuncionarioDTO funcionario)throws BusinessException, SystemException{
        try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationStatus respuesta = securityApiService.getSecurityAPIPort().actualizarUsuario(transformToPrincipalContext(funcionario));
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.update_failed")
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

    /**
     *
     * @param uid
     * @throws BusinessException
     * @throws SystemException
     */
    public void eliminarFuncionario(String uid)throws BusinessException, SystemException{
        try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            OperationStatus respuesta = securityApiService.getSecurityAPIPort().eliminarUsuarioporNombre(uid);
            if (!respuesta.isSuccessful())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.delete_failed")
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

    /**
     *
     * @throws BusinessException
     * @throws SystemException
     */
    public List<RolDTO> obtenerRoles() throws BusinessException, SystemException{
        List<RolDTO> roles = new ArrayList<>();
        try {
            SecurityAPIService securityApiService = getSecutrityApiService();
             OperationRolesListStatus respuesta = securityApiService.getSecurityAPIPort().listadoDeRoles();
            if (respuesta.isSuccessful()) {
                List<Rol> rolesResp = respuesta.getRoles().getRol();
                for (Rol rolR: rolesResp){
                    RolDTO nrol = new RolDTO();
                    nrol.setRol(rolR.getName());
                    roles.add(nrol);
                }
                return roles;
            }
            else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.obtener-roles-failed")
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

    public List<RolDTO> obtenerRolesUsuario(String loginName) throws BusinessException, SystemException{
        List<RolDTO> roles = new ArrayList<>();
        try {
            SecurityAPIService securityApiService = getSecutrityApiService();
            ListadoRoles rolesUser = securityApiService.getSecurityAPIPort().getRolesbyUser(loginName);
            //if (rolesUser.) {
                List<Rol> rolesResp = rolesUser.getRoles().getRol();
                for (Rol rolR: rolesResp){
                    RolDTO nrol = new RolDTO();
                    nrol.setRol(rolR.getName());
                    roles.add(nrol);
                }
                return roles;
            /*
            }
            else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.obtener-roles-failed")
                        .buildBusinessException();


        } catch (BusinessException e) {
            log.error("Api Delegator - a business error has occurred", e);
            throw e;*/
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }

    private SecurityAPIService getSecutrityApiService()throws MalformedURLException{
        return new SecurityAPIService(new URL(endpoint));
    }

    private PrincipalContext transformToPrincipalContext(FuncionarioDTO funcionario){
        PrincipalContext usuario = new PrincipalContext();
        usuario.setUsername(funcionario.getLoginName());
        usuario.setFirstName(funcionario.getNomFuncionario());
        usuario.setLastName(StringUtils.defaultString(funcionario.getValApellido1(), "") + " " + StringUtils.defaultString(funcionario.getValApellido2(), ""));
        usuario.setEmail(funcionario.getCorrElectronico());
        usuario.setPassword(funcionario.getPassword());

        if(funcionario.getRoles() != null){
            Roles roles = new Roles();
            for(RolDTO rolDTO : funcionario.getRoles()){
                Rol rol = new Rol();
                rol.setName(rolDTO.getRol());
                roles.getRol().add(rol);
            }
            usuario.setRoles(roles);
        }

        return usuario;
    }



}
