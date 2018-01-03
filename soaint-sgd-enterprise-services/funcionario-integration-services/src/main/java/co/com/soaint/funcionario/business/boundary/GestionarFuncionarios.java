package co.com.soaint.funcionario.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.business.control.FuncionariosControl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

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
    public void crearFuncionario(FuncionarioDTO funcionario)throws SystemException{
        control.crearFuncionario(funcionario);
    }

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public void actualizarFuncionario(FuncionarioDTO funcionario)throws SystemException{
        control.actualizarFuncionario(funcionario);
    }

    /**
     *
     * @param idFuncionario
     * @throws SystemException
     */
    public void eliminarFuncionario(BigInteger idFuncionario)throws SystemException{
        control.eliminarFuncionario(idFuncionario);
    }
}
