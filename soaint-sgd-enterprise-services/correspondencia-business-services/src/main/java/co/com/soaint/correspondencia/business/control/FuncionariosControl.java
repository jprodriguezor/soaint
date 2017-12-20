package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.AuditColumns;
import co.com.soaint.correspondencia.domain.entity.Funcionarios;
import co.com.soaint.correspondencia.domain.entity.TvsOrgaAdminXFunciPk;
import co.com.soaint.correspondencia.domain.entity.TvsOrgaAdminXFunciPkPk;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
@Log4j2
public class FuncionariosControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DependenciaControl dependenciaControl;
    // ----------------------

    /**
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
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_loginName_and_estado")
                    .withRootException(n)
                    .buildBusinessException();
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
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws BusinessException, SystemException {
        try {
            FuncionarioDTO funcionario = em.createNamedQuery("Funcionarios.findByLoginName", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAME", loginName)
                    .getSingleResult();

            funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
            return funcionario;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_loginName")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO consultarFuncionarioByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
        try {
            FuncionarioDTO funcionario = em.createNamedQuery("Funcionarios.findByIdeFunci", FuncionarioDTO.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult();

            funcionario.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionario.getIdeFunci()));
            return funcionario;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarCredencialesByIdeFunci(BigInteger ideFunci)throws BusinessException, SystemException{
        try {
        return em.createNamedQuery("Funcionarios.consultarCredencialesByIdeFunci", String.class)
                .setParameter("IDE_FUNCI", ideFunci)
                .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarLoginNameByIdeFunci(BigInteger ideFunci)throws BusinessException, SystemException{
        try {
            return em.createNamedQuery("Funcionarios.consultarLoginNameByIdeFunci", String.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("funcionarios.funcionario_not_exist_by_ideFunci")
                    .withRootException(n)
                    .buildBusinessException();
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
     * @param funcionarioDTO
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionarioDTO)throws SystemException{
        try {
            Funcionarios funcionario = funcionarioTransform(funcionarioDTO);
            funcionario.setTvsOrgaAdminXFunciPkList(new ArrayList<>());
            for (DependenciaDTO dependenciaDTO : funcionarioDTO.getDependencias()){
                TvsOrgaAdminXFunciPkPk tvsOrgaAdminXFunciPkPk = new TvsOrgaAdminXFunciPkPk();
                tvsOrgaAdminXFunciPkPk.setCodOrgaAdmi(dependenciaDTO.getCodDependencia());
                tvsOrgaAdminXFunciPkPk.setFuncionario(funcionario);
                funcionario.getTvsOrgaAdminXFunciPkList().add(TvsOrgaAdminXFunciPk.newInstance()
                        .tvsOrgaAdminXFunciPkPk(tvsOrgaAdminXFunciPkPk)
                        .build());
            }
            em.persist(funcionario);
            em.flush();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public Funcionarios funcionarioTransform(FuncionarioDTO funcionarioDTO){
        AuditColumns auditColumns = new AuditColumns();
        auditColumns.setEstado(funcionarioDTO.getEstado());
        return Funcionarios.newInstance()
                .codTipDocIdent(funcionarioDTO.getCodTipDocIdent())
                .nroIdentificacion(funcionarioDTO.getNroIdentificacion())
                .nomFuncionario(funcionarioDTO.getNomFuncionario())
                .valApellido1(funcionarioDTO.getValApellido1())
                .valApellido2(funcionarioDTO.getValApellido2())
                .corrElectronico(funcionarioDTO.getCorrElectronico())
                .loginName(funcionarioDTO.getLoginName())
                .auditColumns(auditColumns)
                .credenciales(java.util.Base64.getEncoder().encodeToString((funcionarioDTO.getLoginName() + ":" + funcionarioDTO.getPassword()).getBytes()))
                .build();
    }
}
