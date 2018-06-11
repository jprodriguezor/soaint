package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class OrganigramaAdministrativoControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
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
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
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
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama() throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getSingleResult();

            List<OrganigramaItemDTO> organigramaItemDTOList = consultarElementosDeNivelInferior(raiz.getIdeOrgaAdmin());
            organigramaItemDTOList.add(raiz);

            return organigramaItemDTOList;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
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
     * @param ideOrgaAdmin
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(BigInteger ideOrgaAdmin) throws SystemException {
        try {
            return consultarElementosDeNivelInferior(ideOrgaAdmin);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideOrgaAdmin
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaItemDTO listarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO organigramaItem = consultarPadreDeSegundoNivel(ideOrgaAdmin);
            if (organigramaItem == null) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("organigrama.no_padre_segundo_nivel")
                        .buildBusinessException();
            }
            return organigramaItem;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param data
     * @param storage
     */
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

    /**
     * @param ideOrgaAdmin
     * @return
     */
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) {
        log.info(ideOrgaAdmin);
        OrganigramaItemDTO organigramaItem = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", OrganigramaItemDTO.class)
                .setParameter("IDE_ORGA_ADMIN", ideOrgaAdmin)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        if (organigramaItem.getIdOrgaAdminPadre() == null) {
            return null;
        }
        log.info(organigramaItem.getIdOrgaAdminPadre());
        Boolean esPadreSegundoNivel = false;

        while (!esPadreSegundoNivel) {
            OrganigramaItemDTO padre = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", OrganigramaItemDTO.class)
                    .setParameter("IDE_ORGA_ADMIN", new BigInteger(organigramaItem.getIdOrgaAdminPadre()))
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
            if (padre.getIdOrgaAdminPadre() == null) {
                esPadreSegundoNivel = true;
            } else {
                organigramaItem = padre;
            }
        }
        return organigramaItem;
    }

    /**
     * @param ideOrgaAdmin
     * @return
     */
    public List<OrganigramaItemDTO> consultarElementosDeNivelInferior(final BigInteger ideOrgaAdmin) {
        List<OrganigramaItemDTO> data = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("ID_PADRE", String.valueOf(ideOrgaAdmin))
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        this.consultarElementosRecursivamente(new ArrayList<>(data), data);
        return data;
    }

    /**
     *
     * @param codOrg
     * @return
     */
    public OrganigramaItemDTO consultarElementoByCodOrg(String codOrg){
        return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByCodOrg", OrganigramaItemDTO.class)
                .setParameter("COD_ORG", codOrg)
                .getSingleResult();
    }

    /**
     *
     * @param codOrg
     * @return
     */
    public String consultarNombreElementoByCodOrg(String codOrg){
        if (codOrg == null) {
            return null;
        }
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarNombreElementoByCodOrg", String.class)
                    .setParameter("COD_ORG", codOrg)
                    .getSingleResult();
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
        }
        return null;

    }

    /**
     *
     * @param codOrg
     * @return
     */
    public List<String> consultarNombreFuncionarioByCodOrg(String codOrg) throws SystemException {
        try {
            return em.createNamedQuery("TvsOrgaAdminXFunciPk.findFuncByCodOrgaAdmi", String.class)
                    .setParameter("COD_ORG", codOrg)
                    .getResultList();

        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.error consultando nombre del funcionario del Organo Administrativo - consultarNombreFuncionarioByCodOrg")
                    .buildSystemException();
        }
    }

    /**
     *
     * @param codigosOrg
     * @return
     */
    public List<OrganigramaItemDTO> consultarElementosByCodOrg(String[] codigosOrg){
        return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementosByCodigosOrg", OrganigramaItemDTO.class)
                .setParameter("CODIGOS_ORG", Arrays.asList(codigosOrg))
                .getResultList();
    }
}
