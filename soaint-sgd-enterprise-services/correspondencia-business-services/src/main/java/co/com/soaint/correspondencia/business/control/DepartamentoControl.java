package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
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
public class DepartamentoControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PaisControl paisControl;

    // ----------------------

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByEstado(String estado) throws SystemException {
        try {
            List<DepartamentoDTO> departamentos =  em.createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
            return conformarDepartamentosConPais(departamentos);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codPais
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByCodPaisAndEstado(String codPais, String estado) throws SystemException {
        try {
            List<DepartamentoDTO> departamentos = em.createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class)
                    .setParameter("COD_PAIS", codPais)
                    .setParameter("ESTADO", estado)
                    .getResultList();
            return departamentos;
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
     * @param codMunic
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DepartamentoDTO consultarDepartamentoByCodMunic(String codMunic) throws SystemException, BusinessException {
        try {
            DepartamentoDTO departamento = em.createNamedQuery("TvsDepartamento.findByCodMunic", DepartamentoDTO.class)
                    .setParameter("COD_MUNIC", codMunic)
                    .getSingleResult();
            departamento.setPais(paisControl.consultarPaisByCodDepar(departamento.getCodDepar()));
            return departamento;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("departamento.departamento_not_exist_by_codMunic")
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
     * @param cod
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DepartamentoDTO consultarDepartamentoByCod(String cod) throws SystemException, BusinessException {
        try {
            DepartamentoDTO departamento = em.createNamedQuery("TvsDepartamento.findByCodDep", DepartamentoDTO.class)
                    .setParameter("COD_DEP", cod)
                    .getSingleResult();
            return departamento;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("departamento.departamento_not_exist_by_codMunic")
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

    private List<DepartamentoDTO> conformarDepartamentosConPais(List<DepartamentoDTO> departamentos)throws SystemException, BusinessException{
        for(DepartamentoDTO departamento : departamentos){
            departamento.setPais(paisControl.consultarPaisByCodDepar(departamento.getCodDepar()));
        }
        return departamentos;
    }
}
