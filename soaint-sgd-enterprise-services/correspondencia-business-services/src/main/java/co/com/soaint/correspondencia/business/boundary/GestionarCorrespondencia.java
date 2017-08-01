package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
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
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 31-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarCorrespondencia {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    AgenteControl agenteControl;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    AnexoControl anexoControl;

    @Autowired
    ReferidoControl referidoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    GestionarTrazaDocumento gestionarTrazaDocumento;

    // ----------------------

    public GestionarCorrespondencia() {
        super();
    }

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        Date fecha = new Date();
        try {
            if (comunicacionOficialDTO.getCorrespondencia().getNroRadicado() == null) {
                comunicacionOficialDTO.getCorrespondencia().setNroRadicado(correspondenciaControl.generarNumeroRadicado(comunicacionOficialDTO.getCorrespondencia()));
            }

            CorCorrespondencia correspondencia = correspondenciaControl.corCorrespondenciaTransform(comunicacionOficialDTO.getCorrespondencia());
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(correspondenciaControl.calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));

            for (AgenteDTO agenteDTO : comunicacionOficialDTO.getAgenteList()) {
                CorAgente corAgente = agenteControl.corAgenteTransform(agenteDTO);
                corAgente.setFecCreacion(fecha);
                corAgente.setCorCorrespondencia(correspondencia);

                if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite())) {
                    AgenteControl.asignarDatosContacto(corAgente, comunicacionOficialDTO.getDatosContactoList());
                }

                if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(agenteDTO.getCodTipAgent())){
                    corAgente.setCodEstado(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo());
                }

                correspondencia.getCorAgenteList().add(corAgente);
            }

            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(comunicacionOficialDTO.getPpdDocumentoList().get(0));
            ppdDocumento.setCorCorrespondencia(correspondencia);
            ppdDocumento.setCorAnexoList(new ArrayList<>());

            comunicacionOficialDTO.getAnexoList().stream().forEach(anexoDTO -> {
                CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                corAnexo.setPpdDocumento(ppdDocumento);
                ppdDocumento.getCorAnexoList().add(corAnexo);
            });
            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            comunicacionOficialDTO.getReferidoList().stream().forEach(referidoDTO -> {
                CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                corReferido.setCorCorrespondencia(correspondencia);
                correspondencia.getCorReferidoList().add(corReferido);
            });

            em.persist(correspondencia);
            em.flush();

            ComunicacionOficialDTO comunicacionOficial = listarCorrespondenciaByNroRadicado(correspondencia.getNroRadicado());

            new Thread(() -> {
                try {
                    gestionarTrazaDocumento.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                            .ideDocumento(comunicacionOficial.getCorrespondencia().getIdeDocumento())
                            .observacion("Radicado")
                            .ideFunci(null)
                            .codEstado(comunicacionOficial.getCorrespondencia().getCodEstado())
                            .codOrgaAdmin(null)
                            .build());
                } catch (Exception e) {
                    logger.error("Business Boundary - a system error has occurred", e);
                }

            }).start();

            return comunicacionOficial;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();

            return correspondenciaControl.consultarComunicacionOficialByCorrespondencia(correspondenciaDTO);
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!correspondenciaControl.verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateEstado")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("COD_ESTADO", correspondenciaDTO.getCodEstado())
                    .executeUpdate();

            new Thread(() -> {
                try {
                    BigInteger ideDocumento = em.createNamedQuery("CorCorrespondencia.findIdeDocumentoByNroRadicado", BigInteger.class)
                            .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                            .getSingleResult();
                    gestionarTrazaDocumento.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                            .ideDocumento(ideDocumento)
                            .observacion("Cambio de estado de documento")
                            .ideFunci(null)
                            .codEstado(correspondenciaDTO.getCodEstado())
                            .codOrgaAdmin(null)
                            .build());
                } catch (Exception e) {
                    logger.error("Business Boundary - a system error has occurred", e);
                }

            }).start();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!correspondenciaControl.verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateIdeInstancia")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("IDE_INSTANCIA", correspondenciaDTO.getIdeInstancia())
                    .executeUpdate();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void registrarObservacionCorrespondencia(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws SystemException{
        try{
            gestionarTrazaDocumento.generarTrazaDocumento(ppdTrazDocumentoDTO);
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, String nroRadicado) throws BusinessException, SystemException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFin);
            cal.add(Calendar.DATE, 1);
            List<CorrespondenciaDTO> correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", cal.getTime(), TemporalType.DATE)
                    .setParameter("COD_ESTADO", EstadoCorrespondenciaEnum.ASIGNADO.getCodigo())
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_EST_AG", codEstado)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();

            if (correspondenciaDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.not_exist_by_periodo_and_dependencia_and_estado")
                        .buildBusinessException();
            }

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();

            for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                List<AgenteDTO> agenteDTOList = em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
                        .setParameter("COD_ESTADO", codEstado)
                        .setParameter("COD_DEPENDENCIA", codDependencia)
                        .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                        .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                        .getResultList();
                ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaDTO)
                        .agenteList(agenteDTOList)
                        .build();
                comunicacionOficialDTOList.add(comunicacionOficialDTO);
            }

            return ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(comunicacionOficialDTOList).build();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
