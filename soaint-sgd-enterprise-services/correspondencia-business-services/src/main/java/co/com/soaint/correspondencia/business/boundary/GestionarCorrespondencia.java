package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.common.MessageUtil;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    private static Logger LOGGER = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;
    // ----------------------

    public GestionarCorrespondencia() {
        super();
    }

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        try {
            String nroRadicado = generarNumeroRadicado(comunicacionOficialDTO.getCorrespondencia().getCodSede(), comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc());
            comunicacionOficialDTO.getCorrespondencia().setNroRadicado(nroRadicado);
            Date fecha = new Date();
            CorCorrespondencia correspondencia = CorCorrespondencia.newInstance()
                    .descripcion(comunicacionOficialDTO.getCorrespondencia().getDescripcion())
                    .tiempoRespuesta(comunicacionOficialDTO.getCorrespondencia().getTiempoRespuesta())
                    .codUnidadTiempo(comunicacionOficialDTO.getCorrespondencia().getCodUnidadTiempo())
                    .codMedioRecepcion(comunicacionOficialDTO.getCorrespondencia().getCodMedioRecepcion())
                    .fecRadicado(comunicacionOficialDTO.getCorrespondencia().getFecRadicado())
                    .nroRadicado(comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                    .codTipoCmc(comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc())
                    .ideInstancia(comunicacionOficialDTO.getCorrespondencia().getIdeInstancia())
                    .reqDistFisica(comunicacionOficialDTO.getCorrespondencia().getReqDistFisica())
                    .codFuncRadica(comunicacionOficialDTO.getCorrespondencia().getCodFuncRadica())
                    .codSede(comunicacionOficialDTO.getCorrespondencia().getCodSede())
                    .codDependencia(comunicacionOficialDTO.getCorrespondencia().getCodDependencia())
                    .reqDigita(comunicacionOficialDTO.getCorrespondencia().getReqDigita())
                    .codEmpMsj(comunicacionOficialDTO.getCorrespondencia().getCodEmpMsj())
                    .nroGuia(comunicacionOficialDTO.getCorrespondencia().getNroGuia())
                    .fecVenGestion(comunicacionOficialDTO.getCorrespondencia().getFecVenGestion())
                    .codEstado(comunicacionOficialDTO.getCorrespondencia().getCodEstado())
                    .corAgenteList(new ArrayList<>())
                    .ppdDocumentoList(new ArrayList<>())
                    .corReferidoList(new ArrayList<>())
                    .build();

            CorAgente corAgente = CorAgente.newInstance()
                    .codTipoRemite(comunicacionOficialDTO.getAgente().getCodTipoRemite())
                    .codTipoPers(comunicacionOficialDTO.getAgente().getCodTipoPers())
                    .nombre(comunicacionOficialDTO.getAgente().getNombre())
                    .nroDocumentoIden(comunicacionOficialDTO.getAgente().getNroDocumentoIden())
                    .razonSocial(comunicacionOficialDTO.getAgente().getRazonSocial())
                    .nit(comunicacionOficialDTO.getAgente().getNit())
                    .codCortesia(comunicacionOficialDTO.getAgente().getCodCortesia())
                    .codCargo(comunicacionOficialDTO.getAgente().getCodCargo())
                    .codEnCalidad(comunicacionOficialDTO.getAgente().getCodEnCalidad())
                    .codTipDocIdent(comunicacionOficialDTO.getAgente().getCodTipDocIdent())
                    .nroDocuIdentidad(comunicacionOficialDTO.getAgente().getNroDocuIdentidad())
                    .codSede(comunicacionOficialDTO.getAgente().getCodSede())
                    .codDependencia(comunicacionOficialDTO.getAgente().getCodDependencia())
                    .codFuncRemite(comunicacionOficialDTO.getAgente().getCodFuncRemite())
                    .fecAsignacion(comunicacionOficialDTO.getAgente().getFecAsignacion())
                    .ideContacto(comunicacionOficialDTO.getAgente().getIdeContacto())
                    .codTipAgent(comunicacionOficialDTO.getAgente().getCodTipAgent())
                    .indOriginal(comunicacionOficialDTO.getAgente().getIndOriginal())
                    .corCorrespondencia(correspondencia)
                    .build();
            correspondencia.getCorAgenteList().add(corAgente);

            PpdDocumento ppdDocumento = PpdDocumento.newInstance()
                    .codTipoDoc(comunicacionOficialDTO.getPpdDocumento().getCodTipoDoc())
                    .fecDocumento(comunicacionOficialDTO.getPpdDocumento().getFecDocumento())
                    .codAsunto(comunicacionOficialDTO.getPpdDocumento().getCodAsunto())
                    .nroFolios(comunicacionOficialDTO.getPpdDocumento().getNroFolios())
                    .nroAnexos(comunicacionOficialDTO.getPpdDocumento().getNroAnexos())
                    .codEstDoc(comunicacionOficialDTO.getPpdDocumento().getCodEstDoc())
                    .ideEcm(comunicacionOficialDTO.getPpdDocumento().getIdeEcm())
                    .codTipoSoporte(comunicacionOficialDTO.getPpdDocumento().getCodTipoSoporte())
                    .codEstArchivado(comunicacionOficialDTO.getPpdDocumento().getCodEstArchivado())
                    .fecCreacion(fecha)
                    .corCorrespondencia(correspondencia)
                    .build();
            ppdDocumento.setCorAnexoList(new ArrayList<>());
            comunicacionOficialDTO.getAnexoList().stream().forEach((anexoDTO) -> {
                CorAnexo corAnexo = CorAnexo.newInstance()
                        .codAnexo(anexoDTO.getCodAnexo())
                        .descripcion(anexoDTO.getDescripcion())
                        .ppdDocumento(ppdDocumento)
                        .build();
                ppdDocumento.getCorAnexoList().add(corAnexo);
            });
            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            comunicacionOficialDTO.getReferidoList().stream().forEach((referidoDTO) -> {
                CorReferido corReferido = CorReferido.newInstance()
                        .nroRadRef(referidoDTO.getNroRadRef())
                        .corCorrespondencia(correspondencia)
                        .build();
                correspondencia.getCorReferidoList().add(corReferido);
            });

            em.persist(correspondencia);
            em.flush();

            return listarCorrespondenciaByNroRadicado(nroRadicado);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();

            if (correspondenciaDTO == null) {
                ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }

            AgenteDTO agenteDTO = em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getSingleResult();

            PpdDocumentoDTO ppdDocumentoDTO =  em.createNamedQuery("PpdDocumento.findByIdeDocumento", PpdDocumentoDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getSingleResult();


            List<AnexoDTO> anexoList = em.createNamedQuery("CorAnexo.findByIdePpdDocumento", AnexoDTO.class)
                        .setParameter("IDE_PPD_DOCUMENTO", ppdDocumentoDTO.getIdePpdDocumento())
                        .getResultList();

            List<ReferidoDTO> referidoList = em.createNamedQuery("CorReferido.findByIdeDocumento", ReferidoDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getResultList();

            ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                    .correspondencia(correspondenciaDTO)
                    .agente(agenteDTO)
                    .ppdDocumento(ppdDocumentoDTO)
                    .anexoList(anexoList)
                    .referidoList(referidoList)
                    .build();

            return comunicacionOficialDTO;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public String generarNumeroRadicado(String sede, String tipoComunicacion) {//TODO
        String consecutivo = "00001";
        int anno = Calendar.getInstance().get(Calendar.YEAR);
        String numeroRadicado = "";
        numeroRadicado = numeroRadicado
                .concat(sede)
                .concat("-")
                .concat(tipoComunicacion)
                .concat("-")
                .concat(String.valueOf(anno))
                .concat("-")
                .concat(consecutivo);
        return numeroRadicado;
    }
}
