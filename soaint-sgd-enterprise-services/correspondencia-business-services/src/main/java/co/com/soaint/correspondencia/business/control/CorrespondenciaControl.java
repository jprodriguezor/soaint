package co.com.soaint.correspondencia.business.control;

import co.com.foundation.cartridge.email.model.Attachment;
import co.com.foundation.cartridge.email.model.MailRequestDTO;
import co.com.foundation.cartridge.email.proxy.MailServiceProxy;
import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoDistribucionFisicaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
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
    private AgenteControl agenteControl;

    @Autowired
    private ConstantesControl constanteControl;

    @Autowired
    private FuncionariosControl funcionarioControl;

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Autowired
    private PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    private AnexoControl anexoControl;

    @Autowired
    private DatosContactoControl datosContactoControl;

    @Autowired
    private ReferidoControl referidoControl;

    @Autowired
    private DserialControl dserialControl;

    @Autowired
    private AsignacionControl asignacionControl;

    @Autowired
    private SolicitudUnidadDocumentalControl solicitudUnidadDocumentalControl;

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

    /**
     * @param codigo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    private String consultarNombreEnumCorrespondencia(String codigo) throws BusinessException, SystemException {

            if (codigo.equals(EstadoCorrespondenciaEnum.ASIGNACION.getCodigo()))
                return EstadoCorrespondenciaEnum.ASIGNACION.getNombre();
            else if (codigo.equals(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo()))
                return EstadoCorrespondenciaEnum.REGISTRADO.getNombre();
            else if (codigo.equals(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo()))
                return EstadoCorrespondenciaEnum.SIN_ASIGNAR.getNombre();
            else if (codigo.equals(EstadoCorrespondenciaEnum.RADICADO.getCodigo()))
                return EstadoCorrespondenciaEnum.RADICADO.getNombre();
            else return null;
    }

    /**
     * @param nroRadicado
     * @param codSede
     * @param codTipoCmc
     * @param anno
     * @param consecutivo
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
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
            String funcionario = "No existe.";
            if (funcionarioControl.existFuncionarioByIdeFunci(new BigInteger(correspondenciaDTO.getCodFuncRadica()))){
                FuncionarioDTO funcionarioDTO = funcionarioControl.consultarFuncionarioByIdeFunci(new BigInteger(correspondenciaDTO.getCodFuncRadica()));
                funcionario = funcionarioDTO.getNomFuncionario().concat(" ").concat(funcionarioDTO.getValApellido1().concat(" ").concat(funcionarioDTO.getValApellido2()));
            }

            return CorrespondenciaFullDTO.newInstance()
                    .codClaseEnvio(correspondenciaDTO.getCodClaseEnvio())
                    .descClaseEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodClaseEnvio()))
                    .codDependencia(correspondenciaDTO.getCodDependencia())
                    .descDependencia(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodDependencia()))
                    .codEmpMsj(correspondenciaDTO.getCodEmpMsj())
                    .descEmpMsj(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodEmpMsj()))
                    .codEstado(correspondenciaDTO.getCodEstado())
                    .descEstado(this.consultarNombreEnumCorrespondencia(correspondenciaDTO.getCodEstado()))
                    .codFuncRadica(correspondenciaDTO.getCodFuncRadica())
                    .descFuncRadica(funcionario)
                    .codMedioRecepcion(correspondenciaDTO.getCodMedioRecepcion())
                    .descMedioRecepcion(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodMedioRecepcion()))
                    .codModalidadEnvio(correspondenciaDTO.getCodModalidadEnvio())
                    .descModalidadEnvio(constanteControl.consultarNombreConstanteByCodigo(correspondenciaDTO.getCodModalidadEnvio()))
                    .codSede(correspondenciaDTO.getCodSede())
                    .descSede(organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodSede()))
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

        List<AgenteFullDTO> agenteFullDTOList = agenteControl.consultarAgentesFullByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());

        List<DatosContactoFullDTO> datosContactoDTOList = datosContactoControl.consultarDatosContactoFullByAgentes(agenteFullDTOList);

        List<PpdDocumentoDTO> ppdDocumentoDTOList = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());

        List<AnexoDTO> anexoList = anexoControl.consultarAnexosByPpdDocumentos(ppdDocumentoDTOList);

        List<ReferidoDTO> referidoList = referidoControl.consultarReferidosByCorrespondencia(correspondenciaFullDTO.getIdeDocumento());
//        log.info("processing rest request - referidoControl.consultarReferidosByCorrespondencia OK");

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
                comunicacionOficialDTO.getAnexoList().forEach(anexoDTO -> {
                    CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                    corAnexo.setPpdDocumento(ppdDocumento);
                    ppdDocumento.getCorAnexoList().add(corAnexo);
                });

            }

            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            if (comunicacionOficialDTO.getReferidoList() != null)
                comunicacionOficialDTO.getReferidoList().forEach(referidoDTO -> {
                    CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                    corReferido.setCorCorrespondencia(correspondencia);
                    correspondencia.getCorReferidoList().add(corReferido);
                });

            String consecutivo = dserialControl.consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(comunicacionOficialDTO.getCorrespondencia().getCodSede(),
                    comunicacionOficialDTO.getCorrespondencia().getCodTipoCmc(), String.valueOf(anno));

            String tipoRadicacion = "";
            for (CorAgente corAgente : correspondencia.getCorAgenteList()) {
                if (corAgente.getCodTipAgent().equals(TipoAgenteEnum.DESTINATARIO.getCodigo()))
                    if (corAgente.getIndOriginal().equals("TP-DESP"))
                        tipoRadicacion = (corAgente.getCodTipoRemite().equals("EXT")) ? "SE" : "SI";
            }

            correspondencia.setNroRadicado(procesarNroRadicado(correspondencia.getNroRadicado(),
                    correspondencia.getCodSede(),
                    tipoRadicacion,
                    String.valueOf(anno), consecutivo));

            String endpoint = System.getProperty("ecm-api-endpoint");
            WebTarget wt = ClientBuilder.newClient().target(endpoint);

            correspondencia.getPpdDocumentoList().forEach(ppdDoc -> {
                log.info("Se modificara el documento con NroRadicado = " + correspondencia.getNroRadicado() + " y con ID " + ppdDoc.getIdeEcm());
                co.com.soaint.foundation.canonical.ecm.DocumentoDTO dto = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance()
                        .nroRadicado(correspondencia.getNroRadicado())
                        .idDocumento(ppdDoc.getIdeEcm())
                        .build();
                Response response = wt.path("/modificarMetadatosDocumentoECM/")
                        .request()
                        .put(Entity.json(dto));
                log.info("Response del cambio de radicado " + response.toString());
            });

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

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public Boolean sendMail(String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - dentro enviar correo radicar correspondencia salida");

        HashMap<String,String> parameters = new HashMap<String, String>();
        MailRequestDTO request = new MailRequestDTO( "PA001" );
        log.info("processing rest request - template: "+request.getTemplate());

        CorrespondenciaDTO correspondenciaDTO = this.consultarCorrespondenciaByNroRadicado(nroRadicado);
        log.info("processing rest request - ideDocumento correspondencia: "+correspondenciaDTO.getIdeDocumento().toString());

        String asunto = "Respuesta "+nroRadicado+" "+correspondenciaDTO.getFecRadicado()+".";
        request.setSubject(asunto);
        log.info("processing rest request - asunto: "+request.getSubject());

        //------------------- Inicio Attachments ------------------------------------------------------//
        String endpoint = System.getProperty("ecm-api-endpoint");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        //Estos son para los documentos de la correspondencia con ese número de radicado
        co.com.soaint.foundation.canonical.ecm.DocumentoDTO dto = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance().nroRadicado(nroRadicado).build();
        Response response = wt.path("/obtenerDocumentosAdjuntosECM/")
                .request()
                .post(Entity.json(dto));

        //Estos son para los documentos de la correspondencia con ese número de radicado referido
        // TODO Traer el nroRadicado de la correspondencia por referido - nroRadRef =
//        co.com.soaint.foundation.canonical.ecm.DocumentoDTO dtoRef = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance().nroRadicado(nroRadRef).build();
//        Response responseReferido = wt.path("/obtenerDocumentosAdjuntosECM/")
//                .request()
//                .post(Entity.json(dto));
//
        ArrayList<Attachment> attachmentsList = new ArrayList<Attachment>();
        if (response.getStatus() == HttpStatus.OK.value()) {
            MensajeRespuesta mensajeRespuesta = response.readEntity(MensajeRespuesta.class);
            if (mensajeRespuesta.getCodMensaje().equals("0000")) {
                final List<co.com.soaint.foundation.canonical.ecm.DocumentoDTO> documentoDTOList = mensajeRespuesta.getDocumentoDTOList();
                log.info("processing rest request - documentoDTOList.size(): "+documentoDTOList.size());

                if (mensajeRespuesta.getDocumentoDTOList() != null || !mensajeRespuesta.getDocumentoDTOList().isEmpty()) {
                    documentoDTOList.forEach(documento -> {
                        Attachment doc =  new Attachment();
                        doc.setAttachments(documento.getDocumento());
                        doc.setContentTypeattachment(documento.getTipoDocumento());
                        doc.setNameAttachments(documento.getNombreDocumento());
                        attachmentsList.add(doc);
                        log.info("processing rest request - documento.getTipoDocumento(): "+documento.getTipoDocumento().toString());
                        log.info("processing rest request - documento.getNombreDocumento(): "+documento.getNombreDocumento().toString());
                    });
                }
            } else{
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de negocio obtenerDocumentosAdjuntosECM")
                        .buildSystemException();
            }
        }
        //------------------- Fin Attachments ------------------------------------------------------//
        request.setAttachmentsList(attachmentsList);

        final List<AgenteDTO> destinatariosList= this.agenteControl.listarDestinatariosByIdeDocumento(correspondenciaDTO.getIdeDocumento());
        log.info("Destinatarios list" + destinatariosList.toString());

        if (destinatariosList == null || destinatariosList.isEmpty()) throw ExceptionBuilder.newBuilder()
                .withMessage("No existen destinatarios para enviar correo.")
                .buildSystemException();

        List<DatosContactoDTO> datosContactoDTOS = new ArrayList<>();
        final List<String> destinatarios = new ArrayList<String>();

        destinatariosList.forEach((AgenteDTO agenteDTO) -> {

            if (agenteDTO.getCodTipoRemite().equals("INT")){
                try {
                    FuncionarioDTO funcionario = funcionarioControl.consultarFuncionarioByIdeFunci(agenteDTO.getIdeFunci());
                    log.info("Funcionario correspondencia" + funcionario.getCorrElectronico()+ " " + funcionario.getNomFuncionario());
                        if (agenteDTO.getIndOriginal()!=null){
                            if (agenteDTO.getIndOriginal().equals("TP-DESP")){
                                if (agenteDTO.getCodTipoPers().equals("TP-PERA")) parameters.put("#USER#", "");
                                else parameters.put("#USER#", funcionario.getNomFuncionario());
                            }
                        }

                    log.info("processing rest request - funcionarioDTO.getNomFuncionario(): "+funcionario.getNomFuncionario().toString());
                    destinatarios.add(funcionario.getCorrElectronico());
                 } catch (Exception ex) {
                    log.error("Business Control - a system error has occurred", ex);
                }
            } else{
                try{
                    if (agenteDTO.getIndOriginal()!=null){
                        if (agenteDTO.getIndOriginal().equals("TP-DESP"))
                            if (agenteDTO.getCodTipoPers().equals("TP-PERA")) parameters.put("#USER#", "");
                            else parameters.put("#USER#", organigramaAdministrativoControl.consultarNombreFuncionarioByCodOrg(agenteDTO.getCodDependencia()).get(0));
                        log.info("processing rest request - agenteDTO.getNombre(): "+organigramaAdministrativoControl.consultarNombreFuncionarioByCodOrg(agenteDTO.getCodDependencia()).get(0));
                    }

                    List<DatosContactoDTO> datosContacto = datosContactoControl.consultarDatosContactoByAgentesCorreo(agenteDTO);
                    for (DatosContactoDTO contactoDTO : datosContacto) {
                        datosContactoDTOS.add(contactoDTO);
                    }
                }catch (Exception ex) {
                    log.error("Business Control - a system error has occurred", ex);
                }
            }
        });

        if (!parameters.containsKey("#USER#"))
            throw ExceptionBuilder.newBuilder().withMessage("No existe un destinatario principal.").buildBusinessException();

        if (datosContactoDTOS == null || datosContactoDTOS.isEmpty())
        datosContactoDTOS.forEach(datosContactoDTO -> {
            destinatarios.add(datosContactoDTO.getCorrElectronico());
        });

        log.info("processing rest request - agenteDTO.getNombre(): "+destinatarios.toString());

        if (destinatarios == null || destinatarios.isEmpty()) throw ExceptionBuilder.newBuilder()
                .withMessage("No existen destinatarios para enviar correo.")
                .buildSystemException();

        request.setTo(destinatarios.toArray(new String[destinatarios.size()]));
        log.info("processing rest request - enviar correo radicar correspondencia"+request.getTo());

        parameters.put("#ORG#",this.organigramaAdministrativoControl.consultarNombreElementoByCodOrg(correspondenciaDTO.getCodDependencia()));

        request.setParameters( parameters );

        log.info("processing rest request - enviar correo radicar correspondencia"+request.getParameters());
        try {
            log.info("processing rest request - enviar correo radicar correspondencia- preparando enviar...");
            return MailServiceProxy.getInstance().sendEmail2(request);
        }catch (Exception e){
            log.info("processing rest request - error enviar correo radicar correspondencia"+e.getMessage());
            throw new BusinessException("system.error.correo.enviado");
        }

    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String consultarNroRadicadoCorrespondenciaReferida(String nroRadicado) throws BusinessException, SystemException {
        return referidoControl.consultarNroRadicadoCorrespondenciaReferida(nroRadicado);
    }
}
