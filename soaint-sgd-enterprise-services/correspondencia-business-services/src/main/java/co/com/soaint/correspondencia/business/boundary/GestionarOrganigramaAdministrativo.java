package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.OrganigramaAdministrativoControl;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 22-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarOrganigramaAdministrativo {
    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrganigramaAdministrativoControl organigramaAdministrativoControl;

    // ----------------------

    public GestionarOrganigramaAdministrativo() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarDescendientesDirectosDeElementoRayz() throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getSingleResult();
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                    .setParameter("ID_PADRE", String.valueOf(raiz.getIdeOrgaAdmin()))
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama() throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getSingleResult();

            List<OrganigramaItemDTO> organigramaItemDTOList = organigramaAdministrativoControl.listarElementosDeNivelInferior(raiz.getIdeOrgaAdmin());
            organigramaItemDTOList.add(raiz);

            return organigramaItemDTOList;
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(BigInteger ideOrgaAdmin) throws BusinessException, SystemException {
        try {
            return organigramaAdministrativoControl.listarElementosDeNivelInferior(ideOrgaAdmin);
        } catch (Throwable ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO organigramaItem = organigramaAdministrativoControl.consultarPadreDeSegundoNivel(ideOrgaAdmin);
            if (organigramaItem == null) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("organigrama.no_padre_segundo_nivel")
                        .buildBusinessException();
            }
            return organigramaItem;
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
