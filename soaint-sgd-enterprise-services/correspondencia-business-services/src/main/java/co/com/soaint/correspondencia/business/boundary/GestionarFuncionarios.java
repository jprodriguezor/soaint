package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.OrganigramaAdministrativoControl;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
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

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;
    // ----------------------

    public GestionarFuncionarios() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(String loginName, String estado) throws BusinessException, SystemException {
        try {
            List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
            em.createNamedQuery("Funcionarios.findByLoginNameAndEstado", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .setParameter("ESTADO", estado)
                    .getResultList()
                    .stream()
                    .forEach(funcionarioDTO -> {
                        OrganigramaItemDTO dependencia = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", OrganigramaItemDTO.class)
                                .setParameter("IDE_ORGA_ADMIN", BigInteger.valueOf(Long.parseLong(funcionarioDTO.getCodOrgaAdmi())))
                                .getSingleResult();
                        funcionarioDTO.setDependencia(dependencia);
                        OrganigramaItemDTO sede = organigramaAdministrativoControl.consultarPadreDeSegundoNivel(BigInteger.valueOf(Long.parseLong(funcionarioDTO.getCodOrgaAdmi())));
                        funcionarioDTO.setSede(sede);
                        funcionarioDTOList.add(funcionarioDTO);
                    });
            if (funcionarioDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_loginName_and_estado")
                        .buildBusinessException();
            }
            return funcionarioDTOList.get(0);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(String codDependencia, String codEstado) throws BusinessException, SystemException {
        try {
            List<FuncionarioDTO> funcionarioDTOList = em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmiAndEstado")
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .setParameter("ESTADO", codEstado)
                    .getResultList();
            if (funcionarioDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionario.funcionario_not_exist_by_codDependencia_and_estado")
                        .buildBusinessException();
            }
            return FuncionariosDTO.newInstance().funcionarios(funcionarioDTOList).build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
