package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarFuncionarios {
    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(GestionarFuncionarios.class.getName());

    @PersistenceContext
    private EntityManager em;
    // ----------------------

    public GestionarFuncionarios() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.findByLoginNameAndEstado", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .setParameter("ESTADO", estado)
                    .getSingleResult();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_loginName_and_estado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws SystemException {
        try {
            List<FuncionarioDTO> funcionarioDTOList = em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmiAndEstado", FuncionarioDTO.class)
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .setParameter("ESTADO", codEstado)
                    .getResultList();

            return FuncionariosDTO.newInstance().funcionarios(funcionarioDTOList).build();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
