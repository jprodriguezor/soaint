package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.FuncionariosControl;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarFuncionarios {
    // [fields] -----------------------------------

    @Autowired
    FuncionariosControl control;
    // ----------------------

    /**
     *
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        return control.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }

    /**
     *
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws SystemException {
        return control.listarFuncionariosByCodDependenciaAndCodEstado(codDependencia, codEstado);
    }

    /**
     *
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws BusinessException, SystemException {
        return control.listarFuncionarioByLoginName(loginName);
    }

    /**
     *
     * @param funcionarioDTO
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionarioDTO)throws SystemException{
        control.crearFuncionario(funcionarioDTO);
    }
}
