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

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    ConstantesControl constanteControl;

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Value("${radicado.max.num.redirecciones}")
    private int numMaxRedirecciones;

    @Value("${radicado.req.dist.fisica}")
    private String reqDistFisica;

    /**
     * @param codigo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String consultarNombreEnumEstadoAgente(String codigo) throws BusinessException, SystemException {

        if (codigo.equals(EstadoAgenteEnum.ASIGNADO.getCodigo()))
            return EstadoAgenteEnum.ASIGNADO.getNombre();
        else if (codigo.equals(EstadoAgenteEnum.DEVUELTO.getCodigo()))
            return EstadoAgenteEnum.DEVUELTO.getNombre();
        else if (codigo.equals(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo()))
            return EstadoAgenteEnum.SIN_ASIGNAR.getNombre();
        else if (codigo.equals(EstadoAgenteEnum.TRAMITADO.getCodigo()))
            return EstadoAgenteEnum.TRAMITADO.getNombre();
        else return null;
    }

    /**
     * @param codigo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String consultarNombreEnumTipoAgente(String codigo) throws BusinessException, SystemException {

        if (codigo.equals(TipoAgenteEnum.DESTINATARIO.getCodigo()))
            return TipoAgenteEnum.DESTINATARIO.getNombre();
        else if (codigo.equals(TipoAgenteEnum.REMITENTE.getCodigo()))
            return TipoAgenteEnum.REMITENTE.getNombre();
        else return null;
    }

    /**
     * @param codigo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String consultarNombreEnumTipoRemitente(String codigo) throws BusinessException, SystemException {

        if (codigo.equals(TipoRemitenteEnum.EXTERNO.getCodigo()))
            return TipoRemitenteEnum.EXTERNO.getNombre();
        else if (codigo.equals(TipoRemitenteEnum.INTERNO.getCodigo()))
            return TipoRemitenteEnum.INTERNO.getNombre();
        else return null;
    }

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
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteFullDTO> listarRemitentesFullByIdeDocumento(BigInteger ideDocumento) throws SystemException {
        try {
            List<AgenteDTO> agenteDTOS = listarRemitentesByIdeDocumento(ideDocumento);
            return  agenteListTransformToFull(agenteDTOS);

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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteDTO> listarDestinatariosByIdeDocumentoMail(BigInteger ideDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorAgente.findDestinatariosByIdeDocumentoAndCodTipoAgenteMail", AgenteDTO.class)
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
     * @param agenteDTO
     * @return
     */
    public AgenteFullDTO agenteTransformToFull(AgenteDTO agenteDTO) throws SystemException, BusinessException{
        try{
            return AgenteFullDTO.newInstance()
                    .codCortesia(agenteDTO.getCodCortesia())
                    .descCortesia(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodCortesia()))
                    .codDependencia(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(agenteDTO.getCodDependencia()))
                    .descDependencia(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodDependencia()))
                    .codEnCalidad(agenteDTO.getCodEnCalidad())
                    .descEnCalidad(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodEnCalidad()))
                    .codEstado(agenteDTO.getCodEstado())
                    .descEstado(this.consultarNombreEnumEstadoAgente(agenteDTO.getCodEstado()))
                    .codSede(agenteDTO.getCodSede())
                    .descSede(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(agenteDTO.getCodSede()))
                    .codTipAgent(agenteDTO.getCodTipAgent())
                    .descTipAgent(this.consultarNombreEnumTipoAgente(agenteDTO.getCodTipAgent()))
                    .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                    .descTipDocIdent(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodTipDocIdent()))
                    .codTipoPers(agenteDTO.getCodTipoPers())
                    .descTipoPers(constanteControl.consultarNombreConstanteByCodigo(agenteDTO.getCodTipoPers()))
                    .codTipoRemite(agenteDTO.getCodTipoRemite())
                    .descTipoRemite(this.consultarNombreEnumTipoRemitente(agenteDTO.getCodTipoRemite()))
                    .fecAsignacion(agenteDTO.getFecAsignacion())
                    .ideAgente(agenteDTO.getIdeAgente())
                    .indOriginal(agenteDTO.getIndOriginal())
                    .nit(agenteDTO.getNit())
                    .nombre(agenteDTO.getNombre())
                    .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                    .numDevoluciones(agenteDTO.getNumDevoluciones())
                    .numRedirecciones(agenteDTO.getNumRedirecciones())
                    .razonSocial(agenteDTO.getRazonSocial())
                    .datosContactoList(datosContactoControl.datosContactoListTransformToFull(agenteDTO.getDatosContactoList()))
                    .build();
            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param agenteList
     * @return
     */
    public List<AgenteFullDTO> agenteListTransformToFull(List<AgenteDTO> agenteList) throws SystemException, BusinessException{
        try{
            List<AgenteFullDTO> agenteFullDTOList = new ArrayList<>();
            for (AgenteDTO agenteDTO:agenteList){
                agenteFullDTOList.add(agenteTransformToFull(agenteDTO));
            }

            return agenteFullDTOList;

            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param idDocumento
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AgenteFullDTO> consultarAgentesFullByCorrespondencia(BigInteger idDocumento) throws SystemException, BusinessException {

        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        listarRemitentesByIdeDocumento(idDocumento).stream().forEach(agenteDTOList::add);
        listarDestinatariosByIdeDocumento(idDocumento).stream().forEach(agenteDTOList::add);

        return agenteListTransformToFull(agenteDTOList);
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
     * @param agentes
     * @param rDistFisica
     * @return
     */
    public List<CorAgente> conformarAgentesSalida(List<AgenteDTO> agentes, String rDistFisica) {
        List<CorAgente> corAgentes = new ArrayList<>();
        for (AgenteDTO agenteDTO : agentes) {
            CorAgente corAgente = corAgenteTransform(agenteDTO);

            if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite()))
                AgenteControl.asignarDatosContacto(corAgente, agenteDTO.getDatosContactoList());

            if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent())) {
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

    /**
     * @param destinatarioDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarDestinatario(DestinatarioDTO destinatarioDTO) throws SystemException {
        try {
            if (!verificarByIdeAgente(destinatarioDTO.getAgenteDestinatario().getIdeAgente()))
                return "0";
            CorAgente destinatario = em.getReference(CorAgente.class, destinatarioDTO.getAgenteDestinatario().getIdeAgente());
            destinatario.setCodDependencia(destinatarioDTO.getAgenteDestinatario().getCodDependencia());
            destinatario.setCodSede(destinatarioDTO.getAgenteDestinatario().getCodSede());
            asignacionControl.actualizarAsignacion(destinatario.getIdeAgente(), destinatario.getCorCorrespondencia().getIdeDocumento(),
                    destinatario.getCodDependencia(), destinatarioDTO.getIdeFuncionarioCreaModifica());

            em.flush();

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
     * @param remitenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarRemitente(RemitenteDTO remitenteDTO) throws SystemException {
        try {
            if (!verificarByIdeAgente(remitenteDTO.getAgenteRemitente().getIdeAgente()))
                return "0";

            CorAgente remitente = em.getReference(CorAgente.class, remitenteDTO.getAgenteRemitente().getIdeAgente());
            remitente.setCodDependencia(remitenteDTO.getAgenteRemitente().getCodDependencia());
            remitente.setCodSede(remitenteDTO.getAgenteRemitente().getCodSede());
            remitente.setCodTipoPers(remitenteDTO.getAgenteRemitente().getCodTipoPers());
            remitente.setCodTipDocIdent(remitenteDTO.getAgenteRemitente().getCodTipDocIdent());
            remitente.setNroDocuIdentidad(remitenteDTO.getAgenteRemitente().getNroDocuIdentidad());
            remitente.setCodCortesia(remitenteDTO.getAgenteRemitente().getCodCortesia());
            remitente.setNombre(remitenteDTO.getAgenteRemitente().getNombre());
            remitente.setCodTipoRemite(remitenteDTO.getAgenteRemitente().getCodTipoRemite());
            remitente.setRazonSocial(remitenteDTO.getAgenteRemitente().getRazonSocial());
            remitente.setNit(remitenteDTO.getAgenteRemitente().getNit());
            remitente.setCodEnCalidad(remitenteDTO.getAgenteRemitente().getCodEnCalidad());
            remitente.setCodEstado(remitenteDTO.getAgenteRemitente().getCodEstado());
            remitente.setCodTipAgent(remitenteDTO.getAgenteRemitente().getCodTipAgent());
            remitente.setIndOriginal(remitenteDTO.getAgenteRemitente().getIndOriginal());

            CorAgente agente = CorAgente.newInstance()
                    .ideAgente(remitenteDTO.getAgenteRemitente().getIdeAgente())
                    .build();

            DatosContactoControl datosContactoControl = new DatosContactoControl();
            for (DatosContactoDTO datosContactoDTO : remitenteDTO.getDatosContactoList()) {
                TvsDatosContacto datosCont = em.find(TvsDatosContacto.class, datosContactoDTO.getIdeContacto());
                if (datosCont == null) {
                    TvsDatosContacto datosContacto = TvsDatosContacto.newInstance()
                            .ideContacto(datosContactoDTO.getIdeContacto())
                            .nroViaGeneradora(datosContactoDTO.getNroViaGeneradora())
                            .nroPlaca(datosContactoDTO.getNroPlaca())
                            .codTipoVia(datosContactoDTO.getCodTipoVia())
                            .codPrefijoCuadrant(datosContactoDTO.getCodPrefijoCuadrant())
                            .codPostal(datosContactoDTO.getCodPostal())
                            .direccion(datosContactoDTO.getDireccion())
                            .celular(datosContactoDTO.getCelular())
                            .telFijo(datosContactoDTO.getTelFijo())
                            .extension(datosContactoDTO.getExtension())
                            .corrElectronico(datosContactoDTO.getCorrElectronico())
                            .codPais(datosContactoDTO.getCodPais())
                            .codDepartamento(datosContactoDTO.getCodDepartamento())
                            .codMunicipio(datosContactoDTO.getCodMunicipio())
                            .provEstado(datosContactoDTO.getProvEstado())
                            .principal(datosContactoDTO.getPrincipal())
                            .ciudad(datosContactoDTO.getCiudad())
                            .corAgente(remitente)
                            .build();
                    remitente.getTvsDatosContactoList().add(datosContacto);
                }
            }
            em.merge(remitente);
            em.flush();

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
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AgenteDTO consultarRemitenteByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            AgenteDTO agenteDTO = em.createNamedQuery("CorAgente.findByNroRadicado", AgenteDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();
            agenteDTO.setDatosContactoList(datosContactoControl.consultarDatosContactoByIdAgente(agenteDTO.getIdeAgente()));
            return agenteDTO;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("agente.agente_not_exist_by_nro_radicado")
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
