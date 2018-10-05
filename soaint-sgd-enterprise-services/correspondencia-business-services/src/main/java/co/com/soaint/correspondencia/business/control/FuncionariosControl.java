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
import java.util.Arrays;
import java.util.Date;
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
     * @param codDependencia
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FuncionarioDTO> listarFuncionariosByCodDependencia(String codDependencia) throws SystemException {
        List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            em.createNamedQuery("Funcionarios.findAllByCodOrgaAdmi", FuncionarioDTO.class)
                    .setParameter("COD_ORGA_ADMI", codDependencia)
                    .getResultList()
                    .stream()
                    .forEach(funcionarioDTO -> {
                        funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
                        funcionarioDTOList.add(funcionarioDTO);
                    });

            return funcionarioDTOList;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param loginName
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName) throws BusinessException, SystemException {
        try {
            log.info("login name:"+ loginName);
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

    /**
     * @param loginNames
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO listarFuncionariosByLoginNameList(String[] loginNames) throws SystemException {
        try {
            FuncionariosDTO funcionarios = FuncionariosDTO.newInstance().funcionarios(em.createNamedQuery("Funcionarios.findByLoginNamList", FuncionarioDTO.class)
                    .setParameter("LOGIN_NAMES", Arrays.asList(loginNames))
                    .getResultList())
                    .build();

            for (FuncionarioDTO funcionarioDTO : funcionarios.getFuncionarios()) {
                funcionarioDTO.setDependencias(dependenciaControl.obtenerDependenciasByFuncionario(funcionarioDTO.getIdeFunci()));
            }

            return funcionarios;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean existFuncionarioByIdeFunci(BigInteger ideFunci) throws SystemException {
        try {
            return em.createNamedQuery("Funcionarios.existFuncionarioByIdeFunci", Long.class)
                    .setParameter("IDE_FUNCI", ideFunci)
                    .getSingleResult()>0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
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

    /**
     * @param nroIdentificacion
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<FuncionarioDTO> consultarFuncionarioByNroIdentificacion(String nroIdentificacion) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("Funcionarios.findByNroIdentificacion", FuncionarioDTO.class)
                    .setParameter("NRO_IDENTIFICACION", nroIdentificacion)
                    .getResultList();
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
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarCredencialesByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
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

    /**
     * @param ideFunci
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarLoginNameByIdeFunci(BigInteger ideFunci) throws BusinessException, SystemException {
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
     * @param funcionarioDTO
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionarioDTO) throws SystemException {
        try {
            funcionarioDTO.setEstado("A");
            Funcionarios funcionario = funcionarioTransform(funcionarioDTO);
            funcionario.setTvsOrgaAdminXFunciPkList(new ArrayList<>());
            for (DependenciaDTO dependenciaDTO : funcionarioDTO.getDependencias()) {
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

    /**
     * @param funcionario
     * @return
     * @throws SystemException
     */
    public String actualizarFuncionario(FuncionarioDTO funcionario) throws SystemException {
        try {
            em.createNamedQuery("Funcionarios.update")
                    .setParameter("IDE_FUNCI", funcionario.getIdeFunci())
                    .setParameter("COD_TIP_DOC_IDENT", funcionario.getCodTipDocIdent())
                    .setParameter("NRO_IDENTIFICACION", funcionario.getNroIdentificacion())
                    .setParameter("NOM_FUNCIONARIO", funcionario.getNomFuncionario())
                    .setParameter("VAL_APELLIDO1", funcionario.getValApellido1())
                    .setParameter("VAL_APELLIDO2", funcionario.getValApellido2())
                    .setParameter("CORR_ELECTRONICO", funcionario.getCorrElectronico())
                    .setParameter("ESTADO", funcionario.getEstado())
                    .setParameter("FECHA", new Date())
                    .setParameter("CREDENCIALES", java.util.Base64.getEncoder().encodeToString((funcionario.getLoginName() + ":" + funcionario.getPassword()).getBytes()))
                    .executeUpdate();
            return "1";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param funcionario
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FuncionariosDTO buscarFuncionario(FuncionarioDTO funcionario) throws SystemException {
        List<FuncionarioDTO> funcionarioDTOList = new ArrayList<>();
        try {
            em.createNamedQuery("Funcionarios.filter", FuncionarioDTO.class)
                    .setParameter("COD_TIP_DOC_IDENT", funcionario.getCodTipDocIdent())
                    .setParameter("NRO_IDENTIFICACION", funcionario.getNroIdentificacion())
                    .setParameter("NOM_FUNCIONARIO", funcionario.getNomFuncionario())
                    .setParameter("VAL_APELLIDO1", funcionario.getValApellido1())
                    .setParameter("VAL_APELLIDO2", funcionario.getValApellido2())
                    .setParameter("CORR_ELECTRONICO", funcionario.getCorrElectronico())
                    .setParameter("LOGIN_NAME", funcionario.getLoginName())
                    .setParameter("ESTADO", funcionario.getEstado())
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
     * @param funcionarioDTO
     * @return
     */
    public Funcionarios funcionarioTransform(FuncionarioDTO funcionarioDTO) {
        AuditColumns auditColumns = new AuditColumns();
        auditColumns.setEstado(funcionarioDTO.getEstado());
        auditColumns.setCodUsuarioCrea(funcionarioDTO.getUsuarioCrea());
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
