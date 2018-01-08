package co.com.soaint.funcionario.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.business.control.FuncionariosControl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarFuncionarios {

    @Autowired
    FuncionariosControl control;

    /**
     *
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(String codDependencia, String rol, String codEstado) throws BusinessException, SystemException {
        return control.listarFuncionariosByDependenciaAndRolAndEstado(codDependencia, rol, codEstado);
    }

    /**
     * 
     * @param credenciales
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO verificarCredenciales(CredencialesDTO credenciales) throws BusinessException, SystemException {
        return control.verificarCredenciales(credenciales);
    }

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public String crearFuncionario(FuncionarioDTO funcionario)throws SystemException{
        return control.crearFuncionario(funcionario);
    }

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funcionario)throws SystemException{
        return control.actualizarFuncionario(funcionario);
    }

    /**
     *
     * @param idFuncionario
     * @throws SystemException
     */
    public String eliminarFuncionario(BigInteger idFuncionario)throws SystemException{
        return control.eliminarFuncionario(idFuncionario);
    }

    /**
     *
     * @throws SystemException
     */
    public List<RolDTO> obtenerRoles() throws BusinessException,SystemException{
        return control.obtenerRoles();
    }

    /**
     *
     * @throws SystemException
     */
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException{
        return control.buscarFuncionario(funcionarioDTO);
    }

    /**
     * @param ideFunci
     * @throws SystemException
     */
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci)throws SystemException{
        return control.consultarFuncionarioByIdeFunci(ideFunci);
    }
}
