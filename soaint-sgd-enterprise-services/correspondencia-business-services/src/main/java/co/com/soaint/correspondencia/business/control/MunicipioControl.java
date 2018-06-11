package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
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
@BusinessControl
@Log4j2
public class MunicipioControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DepartamentoControl departamentoControl;

    // ----------------------

    /**
     * @param codDepar
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByCodDeparAndEstado(String codDepar, String estado) throws SystemException {
        try {
            List<MunicipioDTO> municipios = em.createNamedQuery("TvsMunicipio.findAllByCodDeparAndEstado", MunicipioDTO.class)
                    .setParameter("COD_DEPAR", codDepar)
                    .setParameter("ESTADO", estado)
                    .getResultList();

            return conformarMunicipiosConDepartamento(municipios);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByEstado(String estado) throws SystemException {
        try {
            List<MunicipioDTO> municipios = em.createNamedQuery("TvsMunicipio.findAll", MunicipioDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
            return conformarMunicipiosConDepartamento(municipios);
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
    public List<MunicipioDTO> listarMunicipiosByCodidos(String[] codigos) throws SystemException {
        try {
            List<MunicipioDTO> municipios = em.createNamedQuery("TvsMunicipio.findAllByCodigos", MunicipioDTO.class)
                    .setParameter("CODIGOS", Arrays.asList(codigos))
                    .getResultList();
            return conformarMunicipiosConDepartamento(municipios);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private List<MunicipioDTO> conformarMunicipiosConDepartamento(List<MunicipioDTO> municipios)throws SystemException, BusinessException{
        for (MunicipioDTO municipio : municipios){
            municipio.setDepartamento(departamentoControl.consultarDepartamentoByCodMunic(municipio.getCodMunic()));
        }
        return municipios;
    }

    /**
     *
     * @param codMunic
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public MunicipioDTO consultarMunicipioByCodMunic(String codMunic) throws SystemException, BusinessException {
        if (codMunic == null) {
            return null;
        }
        try {
            MunicipioDTO municipioDTO = em.createNamedQuery("TvsMunicipio.findByCodMun", MunicipioDTO.class)
                    .setParameter("COD_MUN", codMunic)
                    .getSingleResult();
            return municipioDTO;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("municipio.municipio_not_exist_by_codMunic")
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
}
