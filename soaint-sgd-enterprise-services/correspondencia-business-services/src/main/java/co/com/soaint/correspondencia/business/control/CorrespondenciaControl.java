package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoDistribucionFisicaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaFullDTO;

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
import javax.persistence.TemporalType;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
public class CorrespondenciaControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AgenteControl agenteControl;

    @Autowired
    ConstantesControl constanteControl;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    AnexoControl anexoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    ReferidoControl referidoControl;

    @Autowired
    DserialControl dserialControl;

    @Autowired
    AsignacionControl asignacionControl;

    @Value("${radicado.rango.reservado}")
    private String[] rangoReservado;

    @Value("${radicado.horario.laboral}")
    private String[] horarioLaboral;

    @Value("${radicado.unidad.tiempo.horas}")
    private String unidadTiempoHoras;

    @Value("${radicado.unidad.tiempo.dias}")
    private String unidadTiempoDias;

    @Value("${radicado.dia.siguiente.habil}")
    private String diaSiguienteHabil;

    @Value("${radicado.dias.festivos}")
    private String[] diasFestivos;

    @Value("${radicado.req.dist.fisica}")
    private String reqDistFisica;

    // ----------------------

    private String procesarNroRadicado(String nroRadicado, String codSede, String codTipoCmc, String anno, String consecutivo) throws BusinessException, SystemException {
        String nRadicado = nroRadicado;
        try {
            if (nroRadicado == null)
                nRadicado = codSede
                        .concat(codTipoCmc)
                        .concat(anno)
                        .concat(consecutivo);
            else if (verificarByNroRadicado(nRadicado))
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_duplicated_nroRadicado")
                        .buildBusinessException();
            return nRadicado;
        } catch (BusinessException e) {
            log.error("Business Control - a business error has occurred", e);
            throw e;
        }
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        Date fecha = new Date();
        try {
            if (comunicacionOficialDTO.getCorrespondencia().getFecRadicado() == null)
                comunicacionOficialDTO.getCorrespondencia().setFecRadicado(fecha);

            Calendar cal = Calendar.getInstance();
            cal.setTime(comunicacionOficialDTO.getCorrespondencia().getFecRadicado());
            int anno = cal.get(Calendar.YEAR);

            CorCorrespondencia correspondencia = corCorrespondenciaTransform(comunicacionOficialDTO.getCorrespondencia());
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));

            agenteControl.conformarAgentes(comunicacionOficialDTO.getAgenteList(), comunicacionOficialDTO.getDatosContactoList(), correspondencia.getReqDistFisica())
                    .stream().forEach(corAgente -> {
                corAgente.setCorCorrespondencia(correspondencia);

                //----------------------asignacion--------------
                if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(corAgente.getCodTipAgent())) {
                    DctAsignacion dctAsignacion = DctAsignacion.newInstance()
                            .corCorrespondencia(correspondencia)
                            .ideUsuarioCreo(correspondencia.getCodFuncRadica())
                            .codDependencia(corAgente.getCodDependencia())
                            .codTipAsignacion("CTA")
                            .fecAsignacion(new Date())
                            .corAgente(corAgente)
                            .build();

                    correspondencia.getDctAsignacionList().add(dctAsignacion);
                }
                //------------------------------------

                correspondencia.getCorAgenteList().add(corAgente);
            });

            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(comunicacionOficialDTO.getPpdDocumentoList().get(0));
            ppdDocumento.setCorCorrespondencia(correspondencia);

            if (comunicacionOficialDTO.getAnexoList() != null) {
                ppdDocumento.setCorAnexoList(new ArrayList<>());
                comunicacionOficialDTO.getAnexoList().stream().forEach(anexoDTO -> {
                    CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                    corAnexo.setPpdDocumento(ppdDocumento);
                    ppdDocumento.getCorAnexoList().add(corAnexo);
                });

            }

            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            if (comunicacionOficialDTO.getReferidoList() != null)
                comunicacionOficialDTO.getReferidoList().stream().forEach(referidoDTO -> {
                    CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                    corReferido.setCorCorrespondencia(correspondencia);
                    correspondencia.getCorReferidoList().add(corReferido);
                });

            String consecutivo = dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(comunicacionOficialDTO.getCorrespondencia().getCodSede(),
                    comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc(), String.valueOf(anno));

            correspondencia.setNroRadicado(procesarNroRadicado(correspondencia.getNroRadicado(),
                    correspondencia.getCodSede(),
                    correspondencia.getCodTipoCmc(),
                    String.valueOf(anno), consecutivo));

            dserialControl.updateConsecutivo(correspondencia.getCodSede(), correspondencia.getCodDependencia(),
                    correspondencia.getCodTipoCmc(), String.valueOf(anno), consecutivo, correspondencia.getCodFuncRadica());
            em.persist(correspondencia);

            //---------Asignacion-------------

            correspondencia.getDctAsignacionList().stream().forEach(dctAsignacion -> {
                DctAsigUltimo dctAsigUltimo = DctAsigUltimo.newInstance()
                        .corAgente(dctAsignacion.getCorAgente())
                        .corCorrespondencia(dctAsignacion.getCorCorrespondencia())
                        .ideUsuarioCreo(dctAsignacion.getCorCorrespondencia().getCodFuncRadica())
                        .ideUsuarioCambio(new BigInteger(dctAsignacion.getCorCorrespondencia().getCodFuncRadica()))
                        .dctAsignacion(dctAsignacion)
                        .build();
                em.persist(dctAsigUltimo);
            });

            //----------------------------------

            em.flush();

            log.info("Correspondencia - radicacion exitosa nro-radicado -> " + correspondencia.getNroRadicado());
            return ComunicacionOficialDTO.newInstance()
                    .correspondencia(consultarCorrespondenciaByNroRadicado(correspondencia.getNroRadicado()))
                    .build();
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
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            CorrespondenciaDTO correspondenciaDTO = consultarCorrespondenciaByNroRadicado(nroRadicado);

            return consultarComunicacionOficialByCorrespondencia(correspondenciaDTO);
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
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
     * @param correspondenciaDTO
     * @return
     */
    public CorrespondenciaFullDTO correspondenciaTransformToFull(CorrespondenciaDTO correspondenciaDTO) throws SystemException, BusinessException{
        log.info("processing rest request - CorrespondenciaControl-correspondenciaTransformToFull");
        try{
            return CorrespondenciaFullDTO.newInstance()
                    .codClaseEnvio(correspondenciaDTO.getCodClaseEnvio())
                    .descClaseEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodClaseEnvio()))
                    .codDependencia(correspondenciaDTO.getCodDependencia())
                    .descDependencia(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodDependencia()))
                    .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                    .descEmpMsj(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodEmpMsj()))
                    .codEstado(correspondenciaDTO.getCodEstado())
                    .descEstado(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodEstado()))
                    .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                    .descFuncRadica(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodFuncRadica()))
                    .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                    .descMedioRecepcion(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodMedioRecepcion()))
                    .codModalidadEnvio(correspondenciaDTO.getCodModalidadEnvio())
                    .descModalidadEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodModalidadEnvio()))
                    .codSede(correspondenciaDTO.getCodSede())
                    .descSede(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodSede()))
                    .codTipoCmc(correspondenciaDTO.getCodTipoCmc())
                    .descTipoCmc(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodTipoCmc()))
                    .codTipoDoc(correspondenciaDTO.getCodTipoDoc())
                    .descTipoDoc(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodTipoDoc()))
                    .codUnidadTiempo(correspondenciaDTO.getCodUnidadTiempo())
                    .descUnidadTiempo(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodUnidadTiempo()))
                    .fecDocumento(correspondenciaDTO.getFecDocumento())
                    .fecRadicado(correspondenciaDTO.getFecRadicado())
                    .fecVenGestion(correspondenciaDTO.getFecVenGestion())
                    .ideDocumento(correspondenciaDTO.getIdeDocumento())
                    .ideInstancia(correspondenciaDTO.getIdeInstancia())
                    .inicioConteo(correspondenciaDTO.getInicioConteo())
                    .nroGuia(correspondenciaDTO.getNroGuia())
                    .nroRadicado(correspondenciaDTO.getNroRadicado())
                    .reqDigita(correspondenciaDTO.getReqDigita())
                    .reqDistFisica(correspondenciaDTO.getReqDistFisica())
                    .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta())
                    .build();
            //pendiente construir transform de listaa de agenteFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialFullDTO listarFullCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {

            CorrespondenciaDTO correspondenciaDTO = consultarCorrespondenciaByNroRadicado(nroRadicado);

            CorrespondenciaFullDTO correspondenciaFullDTO = correspondenciaTransformToFull(correspondenciaDTO);

            return consultarComunicacionOficialFullByCorrespondencia(correspondenciaFullDTO);

//            List<AgenteFullDTO> agentes = new ArrayList<>();
//            CorrespondenciaFullDTO correspondenciaFullDTO = CorrespondenciaFullDTO.newInstance().build();
//            List<AnexoFullDTO> anexos = new ArrayList<>();
//            List<PpdDocumentoFullDTO> documentos = new ArrayList<>();
//            List<DatosContactoFullDTO> datosContacto = new ArrayList<>();
//            List<ReferidoDTO> referidos = new ArrayList<>();

//            ComunicacionOficialFullDTO comunicacionOficialFullDTO = ComunicacionOficialFullDTO.newInstance()
//                    .agentes(agentes)
//                    .anexos(anexos)
//                    .ppdDocumentos(documentos)
//                    .datosContactos(datosContacto)
//                    .referidos(referidos)
//                    .correspondencia(correspondenciaFullDTO)
//                    .build();
//
//            return comunicacionOficialFullDTO;

        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
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
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateEstado")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("COD_ESTADO", correspondenciaDTO.getCodEstado())
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
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateIdeInstancia")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("IDE_INSTANCIA", correspondenciaDTO.getIdeInstancia())
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
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codEstado
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, String nroRadicado) throws BusinessException, SystemException {
        log.info("FechaInicio: " + fechaIni.toString());
        log.info("FechaFin: " + fechaFin.toString());
        log.info("codDependencia: " + codDependencia);
        log.info("codEstado: " + codEstado);
        log.info("nroRadicado: " + nroRadicado);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFin);
            cal.add(Calendar.DATE, 1);
            List<CorrespondenciaDTO> correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", cal.getTime(), TemporalType.DATE)
                    .setParameter("COD_ESTADO", EstadoCorrespondenciaEnum.ASIGNACION.getCodigo())
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
                List<AgenteDTO> agenteDTOList = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(correspondenciaDTO.getIdeDocumento(),
                        codDependencia,
                        codEstado);
                ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaDTO)
                        .agenteList(agenteDTOList)
                        .build();
                comunicacionOficialDTOList.add(comunicacionOficialDTO);
            }

            return ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(comunicacionOficialDTOList).build();
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
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codTipoDoc
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaSinDistribuir(Date fechaIni, Date fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) throws BusinessException, SystemException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFin);
            cal.add(Calendar.DATE, 1);
            List<CorrespondenciaDTO> correspondenciaDTOList = em.createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodTipoDocAndNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("FECHA_INI", fechaIni, TemporalType.DATE)
                    .setParameter("FECHA_FIN", cal.getTime(), TemporalType.DATE)
                    .setParameter("REQ_DIST_FISICA", reqDistFisica)
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter("ESTADO_DISTRIBUCION", EstadoDistribucionFisicaEnum.SIN_DISTRIBUIR.getCodigo())
                    .setParameter("COD_TIPO_DOC", codTipoDoc)
                    .setParameter("NRO_RADICADO", nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();

            if (correspondenciaDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.not_exist_by_periodo_and_dependencia_and_codTipoDoc_and_nroRadicado")
                        .buildBusinessException();
            }

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();

            for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                List<AgenteDTO> agenteDTOList = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependencia(correspondenciaDTO.getIdeDocumento(),
                        codDependencia);
                agenteControl.listarRemitentesByIdeDocumento(correspondenciaDTO.getIdeDocumento()).stream().forEach(agenteDTOList::add);

                ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                        .correspondencia(correspondenciaDTO)
                        .ppdDocumentoList(ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaDTO.getIdeDocumento()))
                        .agenteList(agenteDTOList)
                        .build();
                comunicacionOficialDTOList.add(comunicacionOficialDTO);
            }

            return ComunicacionesOficialesDTO.newInstance().comunicacionesOficiales(comunicacionOficialDTOList).build();
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
     * @param correspondenciaDTO
     * @return
     */
    public ComunicacionOficialDTO consultarComunicacionOficialByCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws SystemException {

        List<AgenteDTO> agenteDTOList = agenteControl.consltarAgentesByCorrespondencia(correspondenciaDTO.getIdeDocumento());

        List<DatosContactoDTO> datosContactoDTOList = datosContactoControl.consultarDatosContactoByAgentes(agenteDTOList);

        List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaDTO.getIdeDocumento());

        List<AnexoDTO> anexoList = anexoControl.consultarAnexosByPpdDocumentos(ppdDocumentoDTOList);

        List<ReferidoDTO> referidoList = referidoControl.consultarReferidosByCorrespondencia(correspondenciaDTO.getIdeDocumento());

        return ComunicacionOficialDTO.newInstance()
                .correspondencia(correspondenciaDTO)
                .agenteList(agenteDTOList)
                .ppdDocumentoList(ppdDocumentoDTOList)
                .anexoList(anexoList)
                .referidoList(referidoList)
                .datosContactoList(datosContactoDTOList)
                .build();
    }


    /**
     * @param correspondenciaFullDTO
     * @return
     */
    public ComunicacionOficialFullDTO consultarComunicacionOficialFullByCorrespondencia(CorrespondenciaFullDTO correspondenciaFullDTO) throws SystemException, BusinessException {
        log.info("processing rest request - CorrespondenciaControl-consultarComunicacionOficialFullByCorrespondencia");
        List<AgenteFullDTO> agenteFullDTOList = agenteControl.consultarAgentesFullByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());
            log.info("processing rest request - CorrespondenciaControl-agenteControl.consultarAgentesFullByCorrespondencia OK");
        List<DatosContactoFullDTO> datosContactoDTOList = datosContactoControl.consultarDatosContactoFullByAgentes(agenteFullDTOList);
            log.info("processing rest request - datosContactoControl.consultarDatosContactoFullByAgentes OK");
        List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());
            log.info("processing rest request - ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia OK");
        List<AnexoDTO> anexoList = anexoControl.consultarAnexosByPpdDocumentos(ppdDocumentoDTOList);
            log.info("processing rest request - anexoControl.consultarAnexosByPpdDocumentos OK");
        List<ReferidoDTO> referidoList = referidoControl.consultarReferidosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());
        log.info("processing rest request - referidoControl.consultarReferidosByCorrespondencia OK");

        return ComunicacionOficialFullDTO.newInstance()
                .correspondencia(correspondenciaFullDTO)
                .agentes(agenteFullDTOList)
                .ppdDocumentos(ppdDocumentoControl.ppdDocumentoListTransformToFull(ppdDocumentoDTOList))
                .anexos(anexoControl.anexoListTransformToFull(anexoList))
                .referidos(referidoList)
                .datosContactos(datosContactoDTOList)
                .build();
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
    public CorCorrespondencia corCorrespondenciaTransform(CorrespondenciaDTO correspondenciaDTO) {
        return CorCorrespondencia.newInstance()
                .descripcion(correspondenciaDTO.getDescripcion())
                .tiempoRespuesta(correspondenciaDTO.getTiempoRespuesta())
                .codUnidadTiempo(correspondenciaDTO.getCodUnidadTiempo())
                .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                .fecRadicado(correspondenciaDTO.getFecRadicado())
                .nroRadicado(correspondenciaDTO.getNroRadicado())
                .codTipoCmc(correspondenciaDTO.getCodTipoCmc())
                .ideInstancia(correspondenciaDTO.getIdeInstancia())
                .reqDistFisica(correspondenciaDTO.getReqDistFisica())
                .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                .codSede(correspondenciaDTO.getCodSede())
                .codDependencia(correspondenciaDTO.getCodDependencia())
                .reqDigita(correspondenciaDTO.getReqDigita())
                .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                .nroGuia(correspondenciaDTO.getNroGuia())
                .fecVenGestion(correspondenciaDTO.getFecVenGestion())
                .codEstado(correspondenciaDTO.getCodEstado())
                .codClaseEnvio(correspondenciaDTO.getCodClaseEnvio())
                .codModalidadEnvio(correspondenciaDTO.getCodModalidadEnvio())
                .corAgenteList(new ArrayList<>())
                .dctAsignacionList(new ArrayList<>())
                .dctAsigUltimoList(new ArrayList<>())
                .ppdDocumentoList(new ArrayList<>())
                .corReferidoList(new ArrayList<>())
                .build();
    }

    /**
     * @param nroRadicado
     * @return
     */
    public Boolean verificarByNroRadicado(String nroRadicado) throws SystemException {
        try {
            long cantidad = em.createNamedQuery("CorCorrespondencia.countByNroRadicado", Long.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();
            return cantidad > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param correspondenciaDTO
     * @return
     */
    public Date calcularFechaVencimientoGestion(CorrespondenciaDTO correspondenciaDTO) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(correspondenciaDTO.getFecRadicado());
        if (diaSiguienteHabil.equals(correspondenciaDTO.getInicioConteo()))
            calendario.setTime(calcularDiaHabilSiguiente(calendario.getTime()));

        Long tiempoDuracionTramite = Long.parseLong(correspondenciaDTO.getTiempoRespuesta());
        long cantHorasLaborales = horasHabilesDia(horarioLaboral[0], horarioLaboral[1]);
        if (unidadTiempoDias.equals(correspondenciaDTO.getCodUnidadTiempo()))
            tiempoDuracionTramite *= cantHorasLaborales;

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        while (tiempoDuracionTramite > 0) {
            long horasHabilesDia = horasHabilesDia(formatoHora.format(calendario.getTime()), horarioLaboral[1]);

            if (horasHabilesDia >= 0) {
                if (horasHabilesDia >= tiempoDuracionTramite)
                    calendario.add(Calendar.HOUR, tiempoDuracionTramite.intValue());
                tiempoDuracionTramite -= horasHabilesDia;
            }

            if (tiempoDuracionTramite > 0)
                calendario.setTime(calcularDiaHabilSiguiente(calendario.getTime()));
        }

        return calendario.getTime();
    }

    /**
     * @param horaInicio
     * @param horaFin
     * @return
     */
    public long horasHabilesDia(String horaInicio, String horaFin) {
        return ChronoUnit.HOURS.between(LocalTime.parse(horaInicio), LocalTime.parse(horaFin));
    }

    /**
     * @param fecha
     * @return
     */
    public Date calcularDiaHabilSiguiente(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        do {
            calendario.add(Calendar.DATE, 1);
            if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                calendario.add(Calendar.DATE, 2);
        }
        while (!esDiaHabil(calendario.getTime()));

        String[] tiempo = horarioLaboral[0].split(":");
        calendario.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempo[0]));
        calendario.set(Calendar.MINUTE, Integer.parseInt(tiempo[1]));
        return calendario.getTime();
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esDiaHabil(Date fecha) {
        return !(esDiaFestivo(fecha) || esFinSemana(fecha));
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esDiaFestivo(Date fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return Arrays.stream(diasFestivos).anyMatch(x -> x.equals(formatoFecha.format(fecha)));
    }

    /**
     * @param fecha
     * @return
     */
    public Boolean esFinSemana(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        return calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * @param nroRadicado
     * @return
     */
    public CorrespondenciaDTO consultarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - CorrespondenciaControl-consultarCorrespondenciaByNroRadicado");
        try {
            return em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
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
     * @param ideAgente
     * @return
     */
    public CorrespondenciaDTO consultarCorrespondenciaByIdeAgente(BigInteger ideAgente) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorCorrespondencia.findByIdeAgente", CorrespondenciaDTO.class)
                    .setParameter("IDE_AGENTE", ideAgente)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideAgente")
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
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    public CorrespondenciaDTO consultarCorrespondenciaByIdeDocumento(BigInteger ideDocumento) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorCorrespondencia.findByIdeDocumento", CorrespondenciaDTO.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideDocumento")
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
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public Date consultarFechaVencimientoByIdeDocumento(BigInteger ideDocumento) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorCorrespondencia.findFechaVenGestionByIdeDocumento", Date.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_ideDocumento")
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
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */

    public String actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        try {
            CorCorrespondencia correspondencia = em.find(CorCorrespondencia.class ,comunicacionOficialDTO.getCorrespondencia().getIdeDocumento());

            for (AgenteDTO corAgente: comunicacionOficialDTO.getAgenteList()){
                if (TipoAgenteEnum.DESTINATARIO.getCodigo().equals(corAgente.getCodTipAgent())) {
                    log.error("Destinatario");
                    DestinatarioDTO destinatario = DestinatarioDTO.newInstance()
                            .agenteDestinatario(corAgente)
                            .ideFuncionarioCreaModifica(new BigInteger(comunicacionOficialDTO.getCorrespondencia().getCodFuncRadica()))
                            .build();
                        agenteControl.actualizarDestinatario(destinatario);
                }
                else{
                    log.error("Remitente");
                    RemitenteDTO remitente = RemitenteDTO.newInstance()
                            .ideFuncionarioCreaModifica(new BigInteger(comunicacionOficialDTO.getCorrespondencia().getCodFuncRadica()))
                            .agenteRemitente(corAgente)
                            .datosContactoList(comunicacionOficialDTO.getDatosContactoList())
                            .build();
                    agenteControl.actualizarRemitente(remitente);
                }
            }

            PpdDocumento ppdDocumento = em.find(PpdDocumento.class, comunicacionOficialDTO.getPpdDocumentoList().get(0).getIdePpdDocumento());
            if (!comunicacionOficialDTO.getPpdDocumentoList().get(0).getCodTipoDoc().equals(ppdDocumento.getCodTipoDoc())){
                correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));
                ppdDocumento.setCodTipoDoc(comunicacionOficialDTO.getPpdDocumentoList().get(0).getCodTipoDoc());
            }
            ppdDocumento.setAsunto(comunicacionOficialDTO.getPpdDocumentoList().get(0).getAsunto());

            if (comunicacionOficialDTO.getAnexoList() != null) {
                comunicacionOficialDTO.getAnexoList().stream().forEach(anexoDTO -> {
                    if (em.find(CorAnexo.class, anexoDTO.getIdeAnexo()) == null){
                        CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                        corAnexo.setPpdDocumento(ppdDocumento);
                        ppdDocumento.getCorAnexoList().add(corAnexo);
                    }
                });
            }

            if (comunicacionOficialDTO.getReferidoList() != null)
                comunicacionOficialDTO.getReferidoList().stream().forEach(referidoDTO -> {
                    if (em.find(CorReferido.class, referidoDTO.getIdeReferido()) == null){
                        CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                        corReferido.setCorCorrespondencia(correspondencia);
                        correspondencia.getCorReferidoList().add(corReferido);
                    }
                });

            em.merge(correspondencia);
            em.flush();

            log.info("Actualizacion exitosa de la comunicacion");
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
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */

    public ComunicacionOficialDTO radicarCorrespondenciaSalida(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        Date fecha = new Date();
        try {
            if (comunicacionOficialDTO.getCorrespondencia().getFecRadicado() == null)
                comunicacionOficialDTO.getCorrespondencia().setFecRadicado(fecha);

            Calendar cal = Calendar.getInstance();
            cal.setTime(comunicacionOficialDTO.getCorrespondencia().getFecRadicado());
            int anno = cal.get(Calendar.YEAR);

            CorCorrespondencia correspondencia = corCorrespondenciaTransform(comunicacionOficialDTO.getCorrespondencia());
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));
            correspondencia.setCorAgenteList(new ArrayList<>());

            log.info("Antes de Agente");
            agenteControl.conformarAgentesSalida(comunicacionOficialDTO.getAgenteList(), correspondencia.getReqDistFisica())
                    .stream().forEach(corAgente -> {
                corAgente.setCorCorrespondencia(correspondencia);
                log.info(corAgente.getTvsDatosContactoList().size());
                correspondencia.getCorAgenteList().add(corAgente);
            });
            log.info("Despues de Agente");

            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(comunicacionOficialDTO.getPpdDocumentoList().get(0));
            ppdDocumento.setCorCorrespondencia(correspondencia);

            if (comunicacionOficialDTO.getAnexoList() != null) {
                ppdDocumento.setCorAnexoList(new ArrayList<>());
                comunicacionOficialDTO.getAnexoList().stream().forEach(anexoDTO -> {
                    CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                    corAnexo.setPpdDocumento(ppdDocumento);
                    ppdDocumento.getCorAnexoList().add(corAnexo);
                });

            }

            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            if (comunicacionOficialDTO.getReferidoList() != null)
                comunicacionOficialDTO.getReferidoList().stream().forEach(referidoDTO -> {
                    CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                    corReferido.setCorCorrespondencia(correspondencia);
                    correspondencia.getCorReferidoList().add(corReferido);
                });

            String consecutivo = dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(comunicacionOficialDTO.getCorrespondencia().getCodSede(),
                    comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc(), String.valueOf(anno));

            correspondencia.setNroRadicado(procesarNroRadicado(correspondencia.getNroRadicado(),
                    correspondencia.getCodSede(),
                    correspondencia.getCodTipoCmc(),
                    String.valueOf(anno), consecutivo));

            dserialControl.updateConsecutivo(correspondencia.getCodSede(), correspondencia.getCodDependencia(),
                    correspondencia.getCodTipoCmc(), String.valueOf(anno), consecutivo, correspondencia.getCodFuncRadica());
            em.persist(correspondencia);

            em.flush();

            log.info("Correspondencia - radicacion salida exitosa nro-radicado -> " + correspondencia.getNroRadicado());
            return ComunicacionOficialDTO.newInstance()
                    .correspondencia(consultarCorrespondenciaByNroRadicado(correspondencia.getNroRadicado()))
                    .build();
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
}
