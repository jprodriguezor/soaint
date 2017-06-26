package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
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
import java.math.BigInteger;
import java.util.ArrayList;
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

    private static Logger LOGGER = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    public GestionarOrganigramaAdministrativo() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama()throws BusinessException, SystemException{
        try{
            OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getSingleResult();

            List<OrganigramaItemDTO> organigramaItemDTOList = listarElementosDeNivelInferior(raiz.getIdeOrgaAdmin());
            organigramaItemDTOList.add(raiz);

            return organigramaItemDTOList;
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(final BigInteger ideOrgaAdmin) {
        List<OrganigramaItemDTO> data = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("ID_PADRE", String.valueOf(ideOrgaAdmin))
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        consultarElementosRecursivamente(new ArrayList<>(data), data);
        return data;
    }

    public void consultarElementosRecursivamente(final List<OrganigramaItemDTO> data, final List<OrganigramaItemDTO> storage) {

        for (OrganigramaItemDTO item : data) {
            List<OrganigramaItemDTO> hijos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                    .setParameter("ID_PADRE", String.valueOf(item.getIdeOrgaAdmin()))
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
            storage.addAll(hijos);
            consultarElementosRecursivamente(new ArrayList<>(hijos), storage);
        }

    }
}
