package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.*;
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
            return em.createNamedQuery("CorAgente.findRemitentesByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
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
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(BigInteger ideDocumento,
                                                                                          String codDependencia,
                                                                                          String codEstado) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoAndCodDependencia(BigInteger ideDocumento,
                                                                              String codDependencia) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodDependencia", AgenteDTO.class)
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
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgente", AgenteDTO.class)
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
                String estadoDistribucionFisica = reqDistFisica.equals(correspondencia.getReqDistFisica()) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null;

                em.createNamedQuery("CorAgente.redireccionarCorrespondencia")
                        .setParameter("COD_SEDE", agente.getCodSede())
                        .setParameter("COD_DEPENDENCIA", agente.getCodDependencia())
                        .setParameter("IDE_AGENTE", agente.getIdeAgente())
                        .setParameter("COD_ESTADO", EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())
                        .setParameter("ESTADO_DISTRIBUCION", estadoDistribucionFisica)
                        .executeUpdate();

                //-----------------Asignacion--------------------------

                asignacionControl.actualizarAsignacion(agente.getIdeAgente(), correspondencia.getIdeDocumento(),
                        agente.getCodDependencia(), redireccion.getTraza().getIdeFunci());

                //-----------------------------------------------------

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
     *
     * @param devolucion
     * @throws SystemException
     */
    public void  devolverCorrespondencia(DevolucionDTO devolucion)throws SystemException{
        try {
            for (ItemDevolucionDTO itemDevolucion : devolucion.getItemsDevolucion()) {
                CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeAgente(itemDevolucion.getAgente().getIdeAgente());
                actualizarEstadoAgente(AgenteDTO.newInstance()
                        .ideAgente(itemDevolucion.getAgente().getIdeAgente())
                        .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                        .build());

                //-----------------Asignacion--------------------------

                asignacionControl.actualizarAsignacionFromDevolucion(itemDevolucion.getAgente().getIdeAgente(),
                        correspondencia.getIdeDocumento(), devolucion.getTraza().getIdeFunci(), itemDevolucion.getCausalDevolucion().toString(),
                        devolucion.getTraza().getObservacion());

                //-----------------------------------------------------
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
    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) throws SystemException {
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        listarRemitentesByIdeDocumento(idDocumento).stream().forEach(agenteDTOList::add);
        listarDestinatariosByIdeDocumento(idDocumento).stream().forEach(agenteDTOList::add);
        return agenteDTOList;
    }

    /**
     * @param agentes
     * @param datosContactoList
     * @param rDistFisica
     * @return
     */
    public List<CorAgente> conformarAgentes(List<AgenteDTO> agentes, List<DatosContactoDTO> datosContactoList, String rDistFisica) {
        List<CorAgente> corAgentes = new ArrayList<>();
        for (AgenteDTO agenteDTO : agentes) {
            CorAgente corAgente = corAgenteTransform(agenteDTO);

            if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite()) && datosContactoList != null)
                AgenteControl.asignarDatosContacto(corAgente, datosContactoList);

            if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                corAgente.setCodEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo());
                corAgente.setEstadoDistribucion(reqDistFisica.equals(rDistFisica) ? EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo() : null);
            }

            corAgentes.add(corAgente);
        }
        return corAgentes;
    }

    /**
     *
     * @param ideAgente
     * @param estadoDistribucion
     * @throws SystemException
     */
    public void actualizarEstadoDistribucion(BigInteger ideAgente, String estadoDistribucion)throws SystemException{
        try {
            em.createNamedQuery("CorAgente.updateEstadoDistribucion")
                    .setParameter("ESTADO_DISTRIBUCION", estadoDistribucion)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .executeUpdate();
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
                .tvsDatosContactoList(new ArrayList<>())
                .build();
    }
}
