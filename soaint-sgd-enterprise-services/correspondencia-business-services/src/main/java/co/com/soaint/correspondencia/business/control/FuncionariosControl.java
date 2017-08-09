package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 03-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class FuncionariosControl {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(FuncionariosControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DependenciaControl dependenciaControl;
    // ----------------------

    /**
     *
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        try {
            FuncionarioDTO funcionario = em.createNamedQuery("Funcionarios.findByLoginNameAndEstado", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .setParameter("ESTADO", estado)
                    .getSingleResult();

            funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
            return funcionario;
        } catch (NoResultException n) {
            logger.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_loginName_and_estado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            logger.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws SystemException {
        List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmiAndEstado", FuncionarioDTO.class)
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .setParameter("ESTADO", codEstado)
                    .getResultList()
                    .stream()
                    .forEach(funcionarioDTO -> {
                        funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
                        funcionarioDTOList.add(funcionarioDTO);
                    });

            return FuncionariosDTO.newInstance().funcionarios(funcionarioDTOList).build();
        } catch (Exception ex) {
            logger.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
