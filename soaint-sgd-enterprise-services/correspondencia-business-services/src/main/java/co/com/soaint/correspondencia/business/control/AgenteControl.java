package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class AgenteControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AsignacionControl asignacionControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    PpdTrazDocumentoControl ppdTrazDocumentoControl;

    @Value("${radicado.max.num.redirecciones}")
    private int numMaxRedirecciones;

    @Value("${radicado.req.dist.fisica}")
    private String reqDistFisica;

    /**
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarRemitentesByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.REMITENTE.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
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
     * @param ideDocumento
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatarioByIdeDocumentoAndCodDependenciaAndCodEstado(BigInteger ideDocumento,
                                                                                         String codDependencia,
                                                                                         String codEstado) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
                    .setParameter("COD_ESTADO", codEstado)
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
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
     * @param ideAgente
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AgenteDTO consultarAgenteByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorAgente.findByIdeAgente", AgenteDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("agente.agente_not_exist_by_ideAgente")
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
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByIdeAgente(agenteDTO.getIdeAgente())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();
            }

            em.createNamedQuery("CorAgente.updateEstado")
                    .setParameter("COD_ESTADO", agenteDTO.getCodEstado())
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .executeUpdate();


            if (agenteDTO.getCodEstado().equals(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(agenteDTO.getIdeAgente());
                correspondencia.setCodEstado(EstadoCorrespondenciaEnum.ASIGNACION.getCodigo());
                correspondenciaControl.actualizarEstadoCorrespondencia(correspondencia);
            }
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
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
     * @param redireccion
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        try {
            for (AgenteDTO agente : redireccion.getAgentes()) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(agente.getIdeAgente());
                String estadoAgente = reqDistFisica.equals(correspondencia.getReqDistFisica()) ? EstadoAgenteEnum.DISTRIBUCION.getCodigo() : EstadoAgenteEnum.SIN_ASIGNAR.getCodigo();
                em.createNamedQuery("CorAgente.redireccionarCorrespondencia")
                        .setParameter("COD_SEDE", agente.getCodSede())
                        .setParameter("COD_DEPENDENCIA", agente.getCodDependencia())
                        .setParameter("IDE_AGENTE", agente.getIdeAgente())
                        .setParameter("COD_ESTADO", estadoAgente)
                        .executeUpdate();

                correspondencia.setCodEstado(EstadoCorrespondenciaEnum.ASIGNACION.getCodigo());
                correspondenciaControl.actualizarEstadoCorrespondencia(correspondencia);

                ppdTrazDocumentoControl.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                        .observacion(redireccion.getTraza().getObservacion())
                        .ideFunci(redireccion.getTraza().getIdeFunci())
                        .codEstado(redireccion.getTraza().getCodEstado())
                        .ideDocumento(correspondencia.getIdeDocumento())
                        .codOrgaAdmin(redireccion.getTraza().getCodOrgaAdmin())
                        .build());

            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ideAgente
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarNumDevoluciones(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            if (!verificarByIdeAgente(ideAgente))
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();

            em.createNamedQuery("CorAgente.updateNumDevoluciones")
                    .setParameter("IDE_AGENTE", ideAgente)
                    .executeUpdate();
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
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
     * @param ideAgente
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Boolean verificarByIdeAgente(BigInteger ideAgente) {
        long cantidad = em.createNamedQuery("CorAgente.countByIdeAgente", Long.class)
                .setParameter("IDE_AGENTE", ideAgente)
                .getSingleResult();
        return cantidad > 0;
    }

    /**
     * @param corAgente
     * @param datosContactoDTOList
     */
    public static void asignarDatosContacto(CorAgente corAgente, List<DatosContactoDTO> datosContactoDTOList) {
        DatosContactoControl datosContactoControl = new DatosContactoControl();
        for (DatosContactoDTO datosContactoDTO : datosContactoDTOList) {
            TvsDatosContacto datosContacto = datosContactoControl.datosContactoTransform(datosContactoDTO);
            datosContacto.setCorAgente(corAgente);
            corAgente.getTvsDatosContactoList().add(datosContacto);
        }
    }

    /**
     * @param idDocumento
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) {
        return em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
    }

    /**
     * @param agentes
     * @param datosContactoList
     * @param rDistFisica
     * @return
     */
    public List<CorAgente> conformarAgentes(List<AgenteDTO> agentes, List<DatosContactoDTO> datosContactoList, String rDistFisica) {
        long valorInicialRedirecciones = Long.parseLong("0");
        List<CorAgente> corAgentes = new ArrayList<>();
        for (AgenteDTO agenteDTO : agentes) {
            CorAgente corAgente = corAgenteTransform(agenteDTO);

            if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite()))
                AgenteControl.asignarDatosContacto(corAgente, datosContactoList);

            if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                corAgente.setCodEstado(reqDistFisica.equals(rDistFisica) ? EstadoAgenteEnum.DISTRIBUCION.getCodigo() : EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                corAgente.setNumRedirecciones(valorInicialRedirecciones);
                corAgente.setNumDevoluciones(valorInicialRedirecciones);
            }

            corAgentes.add(corAgente);
        }
        return corAgentes;
    }

    /**
     * @param agenteDTO
     * @return
     */
    public CorAgente corAgenteTransform(AgenteDTO agenteDTO) {
        return CorAgente.newInstance()
                .ideAgente(agenteDTO.getIdeAgente())
                .codTipoRemite(agenteDTO.getCodTipoRemite())
                .codTipoPers(agenteDTO.getCodTipoPers())
                .nombre(agenteDTO.getNombre())
                .razonSocial(agenteDTO.getRazonSocial())
                .nit(agenteDTO.getNit())
                .codCortesia(agenteDTO.getCodCortesia())
                .codEnCalidad(agenteDTO.getCodEnCalidad())
                .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                .codSede(agenteDTO.getCodSede())
                .codDependencia(agenteDTO.getCodDependencia())
                .codEstado(agenteDTO.getCodEstado())
                .fecAsignacion(agenteDTO.getFecAsignacion())
                .codTipAgent(agenteDTO.getCodTipAgent())
                .indOriginal(agenteDTO.getIndOriginal())
                .numRedirecciones(agenteDTO.getNumRedirecciones())
                .numDevoluciones(agenteDTO.getNumDevoluciones())
                .tvsDatosContactoList(new ArrayList<>())
                .build();
    }
}
