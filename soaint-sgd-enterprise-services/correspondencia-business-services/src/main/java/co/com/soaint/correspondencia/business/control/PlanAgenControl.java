package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 04-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PlanAgenControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AgenteControl agenteControl;

    @Autowired
    ConstantesControl constantesControl;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    CorrespondenciaControl correspondenciaControl;
    /**
     * @param idePlanilla
     * @return
     * @throws SystemException
     */
    public List<PlanAgenDTO> listarAgentesByIdePlanilla(BigInteger idePlanilla, String estado) throws SystemException {
        try {
            List<PlanAgenDTO> planAgenDTOList = em.createNamedQuery("CorPlanAgen.findByIdePlanillaAndEstado", PlanAgenDTO.class)
                    .setParameter("IDE_PLANILLA", idePlanilla)
                    .setParameter("ESTADO", estado)
                    .getResultList();
            for (PlanAgenDTO planAgen : planAgenDTOList) {
                List<AgenteDTO> remitentes = agenteControl.listarRemitentesByIdeDocumento(planAgen.getIdeDocumento());
                if (remitentes.get(0).getCodTipoPers() != null)
                    planAgen.setTipoPersona(constantesControl.consultarConstanteByCodigo(remitentes.get(0).getCodTipoPers()));
                planAgen.setNit(remitentes.get(0).getNit());
                planAgen.setNroDocuIdentidad(remitentes.get(0).getNroDocuIdentidad());
                planAgen.setNombre(remitentes.get(0).getNombre());
                planAgen.setRazonSocial(remitentes.get(0).getRazonSocial());
                List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(planAgen.getIdeDocumento());
                if (!ppdDocumentoDTOList.isEmpty()){
                    planAgen.setTipologiaDocumental(constantesControl.consultarConstanteByCodigo(ppdDocumentoDTOList.get(0).getCodTipoDoc()));
                    planAgen.setFolios(ppdDocumentoDTOList.get(0).getNroFolios());
                    planAgen.setAnexos(ppdDocumentoDTOList.get(0).getNroAnexos());
                }
            }

            return planAgenDTOList;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param idePlanilla
     * @return
     * @throws SystemException
     */
    public List<PlanAgenSalidaDTO> listarAgentesSalidaByIdePlanilla(BigInteger idePlanilla, String estado) throws SystemException {
        try {
            List<PlanAgenSalidaDTO> planAgenSalidaDTOS = new ArrayList<PlanAgenSalidaDTO>();
            List<PlanAgenDTO> planAgenDTOList = em.createNamedQuery("CorPlanAgen.findByIdePlanillaAndEstado", PlanAgenDTO.class)
                    .setParameter("IDE_PLANILLA", idePlanilla)
                    .setParameter("ESTADO", estado)
                    .getResultList();
            for (PlanAgenDTO planAgen : planAgenDTOList) {

                PlanAgenSalidaDTO planAgenSalidaDTO = planAgenDTOTransformToPlanAgenSaidaDTO(planAgen);

                List<AgenteDTO> remitentes = agenteControl.listarRemitentesByIdeDocumento(planAgen.getIdeDocumento());
                if (remitentes.get(0).getCodTipoPers() != null)
                    planAgenSalidaDTO.setTipoPersona(constantesControl.consultarConstanteByCodigo(remitentes.get(0).getCodTipoPers()));
                planAgenSalidaDTO.setNit(remitentes.get(0).getNit());
                planAgenSalidaDTO.setNroDocuIdentidad(remitentes.get(0).getNroDocuIdentidad());
                planAgenSalidaDTO.setNombre(remitentes.get(0).getNombre());
                planAgenSalidaDTO.setRazonSocial(remitentes.get(0).getRazonSocial());
                List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(planAgen.getIdeDocumento());
                if (!ppdDocumentoDTOList.isEmpty()){
                    planAgenSalidaDTO.setTipologiaDocumental(constantesControl.consultarConstanteByCodigo(ppdDocumentoDTOList.get(0).getCodTipoDoc()));
                    planAgenSalidaDTO.setFolios(ppdDocumentoDTOList.get(0).getNroFolios());
                    planAgenSalidaDTO.setAnexos(ppdDocumentoDTOList.get(0).getNroAnexos());
                }
                planAgenSalidaDTOS.add(planAgenSalidaDTO);
            }

            return planAgenSalidaDTOS;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param planAgen
     * @throws SystemException
     */
    public void updateEstadoDistribucion(PlanAgenDTO planAgen) throws SystemException {
        try {
            em.createNamedQuery("CorPlanAgen.updateEstadoDistribucion")
                    .setParameter("IDE_PLAN_AGEN", planAgen.getIdePlanAgen())
                    .setParameter("ESTADO", planAgen.getEstado())
                    .setParameter("VAR_PESO", planAgen.getVarPeso())
                    .setParameter("VAR_VALOR", planAgen.getVarValor())
                    .setParameter("VAR_NUMERO_GUIA", planAgen.getVarNumeroGuia())
                    .setParameter("FEC_OBSERVACION", planAgen.getFecObservacion())
                    .setParameter("COD_NUEVA_SEDE", planAgen.getCodNuevaSede())
                    .setParameter("COD_NUEVA_DEPEN", planAgen.getCodNuevaDepen())
                    .setParameter("OBSERVACIONES", planAgen.getObservaciones())
                    .setParameter("COD_CAU_DEVO", planAgen.getCodCauDevo())
                    .setParameter("FEC_CARGUE_PLA", planAgen.getFecCarguePla())
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
     * @param planAgenDTO
     * @return
     */
    public CorPlanAgen corPlanAgenTransform(PlanAgenDTO planAgenDTO) {
        return CorPlanAgen.newInstance()
                .idePlanAgen(planAgenDTO.getIdePlanAgen())
                .varPeso(planAgenDTO.getVarPeso())
                .varValor(planAgenDTO.getVarValor())
                .varNumeroGuia(planAgenDTO.getVarNumeroGuia())
                .fecObservacion(planAgenDTO.getFecObservacion())
                .codNuevaSede(planAgenDTO.getCodNuevaSede())
                .codNuevaDepen(planAgenDTO.getCodNuevaDepen())
                .observaciones(planAgenDTO.getObservaciones())
                .codCauDevo(planAgenDTO.getCodCauDevo())
                .fecCarguePla(planAgenDTO.getFecCarguePla())
                .corAgente(CorAgente.newInstance().ideAgente(planAgenDTO.getIdeAgente()).build())
                .corCorrespondencia(CorCorrespondencia.newInstance().ideDocumento(planAgenDTO.getIdeDocumento()).build())
                .build();
    }

    /**
     * @param planAgenDTO
     * @return
     */
    public PlanAgenSalidaDTO planAgenDTOTransformToPlanAgenSaidaDTO(PlanAgenDTO planAgenDTO) throws SystemException, BusinessException {

        AgenteDTO agenteDTO = agenteControl.consultarAgenteByIdeAgente(planAgenDTO.getIdeAgente());
        if (agenteDTO != null && agenteDTO.getCodTipAgent()== TipoAgenteEnum.DESTINATARIO.getCodigo()){
            List<DatosContactoDTO> datosContacto = datosContactoControl.consultarDatosContactoByIdAgente(planAgenDTO.getIdeAgente());
            agenteDTO.setDatosContactoList(datosContacto);
        }

        CorrespondenciaDTO correspondenciaDTO = (planAgenDTO.getIdeAgente() == null)? null : correspondenciaControl.consultarCorrespondenciaByIdeDocumento(planAgenDTO.getIdeDocumento());

        return PlanAgenSalidaDTO.newInstance()
                .idePlanAgen(planAgenDTO.getIdePlanAgen())
                .varPeso(planAgenDTO.getVarPeso())
                .varValor(planAgenDTO.getVarValor())
                .varNumeroGuia(planAgenDTO.getVarNumeroGuia())
                .fecObservacion(planAgenDTO.getFecObservacion())
                .codNuevaSede(planAgenDTO.getCodNuevaSede())
                .codNuevaDepen(planAgenDTO.getCodNuevaDepen())
                .observaciones(planAgenDTO.getObservaciones())
                .codCauDevo(planAgenDTO.getCodCauDevo())
                .fecCarguePla(planAgenDTO.getFecCarguePla())
                .agente(agenteControl.agenteTransformToFull(agenteDTO))
                .correspondencia(correspondenciaControl.correspondenciaTransformToFull(correspondenciaDTO))
                .build();
    }
}
