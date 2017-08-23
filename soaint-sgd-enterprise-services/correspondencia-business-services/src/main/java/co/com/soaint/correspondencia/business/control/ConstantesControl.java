package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
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
@NoArgsConstructor
@BusinessControl
@Log4j2
public class ConstantesControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;


    // ----------------------

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByEstado(String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAll", ConstanteDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codigo
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodigoAndEstado(String codigo, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodigoAndEstado", ConstanteDTO.class)
                    .setParameter("CODIGO", codigo)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codPadre
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodPadreAndEstado(String codPadre, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodPadreAndEstado", ConstanteDTO.class)
                    .setParameter("COD_PADRE", codPadre)
                    .setParameter("ESTADO", estado)
                    .getResultList();
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
     * @param codigos
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ConstantesDTO listarConstantesByCodigo(String[] codigos) throws SystemException {
        List<ConstanteDTO> constanteDTOList = new ArrayList<>();
        try {
            constanteDTOList = em.createNamedQuery("TvsConstantes.findAllByCodigo", ConstanteDTO.class)
                    .setParameter("CODIGOS", Arrays.asList(codigos))
                    .getResultList();
            return ConstantesDTO.newInstance().constantes(constanteDTOList).build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
