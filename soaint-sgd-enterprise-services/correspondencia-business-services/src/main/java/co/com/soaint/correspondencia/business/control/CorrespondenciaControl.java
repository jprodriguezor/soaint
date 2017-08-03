package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.business.boundary.GestionarTrazaDocumento;
import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
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
public class CorrespondenciaControl {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(CorrespondenciaControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AgenteControl agenteControl;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    AnexoControl anexoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    ReferidoControl referidoControl;

    @Autowired
    GestionarTrazaDocumento gestionarTrazaDocumento;

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

    private String constCodEstado = "COD_ESTADO";
    private String constNroRadicado = "NRO_RADICADO";

    // ----------------------

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        Date fecha = new Date();
        try {
            if (comunicacionOficialDTO.getCorrespondencia().getNroRadicado() == null) {
                comunicacionOficialDTO.getCorrespondencia().setNroRadicado(generarNumeroRadicado(comunicacionOficialDTO.getCorrespondencia()));
            }

            if (comunicacionOficialDTO.getCorrespondencia().getFecRadicado() == null)
                comunicacionOficialDTO.getCorrespondencia().setFecRadicado(fecha);

            CorCorrespondencia correspondencia = corCorrespondenciaTransform(comunicacionOficialDTO.getCorrespondencia());
            correspondencia.setCodEstado(EstadoCorrespondenciaEnum.REGISTRADO.getCodigo());
            correspondencia.setFecVenGestion(calcularFechaVencimientoGestion(comunicacionOficialDTO.getCorrespondencia()));

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

           return listarCorrespondenciaByNroRadicado(correspondencia.getNroRadicado());
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
                    .setParameter(constNroRadicado, nroRadicado)
                    .getSingleResult();

            return consultarComunicacionOficialByCorrespondencia(correspondenciaDTO);
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
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateEstado")
                    .setParameter(constNroRadicado, correspondenciaDTO.getNroRadicado())
                    .setParameter(constCodEstado, correspondenciaDTO.getCodEstado())
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

    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateIdeInstancia")
                    .setParameter(constNroRadicado, correspondenciaDTO.getNroRadicado())
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
                    .setParameter(constCodEstado, EstadoCorrespondenciaEnum.ASIGNADO.getCodigo())
                    .setParameter("COD_DEPENDENCIA", codDependencia)
                    .setParameter("COD_EST_AG", codEstado)
                    .setParameter("COD_TIP_AGENT", TipoAgenteEnum.DESTINATARIO.getCodigo())
                    .setParameter(constNroRadicado, nroRadicado == null ? null : "%" + nroRadicado + "%")
                    .getResultList();

            if (correspondenciaDTOList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.not_exist_by_periodo_and_dependencia_and_estado")
                        .buildBusinessException();
            }

            List<ComunicacionOficialDTO> comunicacionOficialDTOList = new ArrayList<>();

            for (CorrespondenciaDTO correspondenciaDTO : correspondenciaDTOList) {
                List<AgenteDTO> agenteDTOList = em.createNamedQuery("CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", AgenteDTO.class)
                        .setParameter(constCodEstado, codEstado)
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

    public ComunicacionOficialDTO consultarComunicacionOficialByCorrespondencia(CorrespondenciaDTO correspondenciaDTO) {
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
                .corAgenteList(new ArrayList<>())
                .ppdDocumentoList(new ArrayList<>())
                .corReferidoList(new ArrayList<>())
                .build();
    }

    public Boolean verificarByNroRadicado(String nroRadicado) {
        long cantidad = em.createNamedQuery("CorCorrespondencia.countByNroRadicado", Long.class)
                .setParameter(constNroRadicado, nroRadicado)
                .getSingleResult();
        return cantidad > 0;
    }

    public String generarNumeroRadicado(CorrespondenciaDTO correspondencia) {

        int rangoI = Integer.parseInt(this.rangoReservado[0]);
        int rangoF = Integer.parseInt(this.rangoReservado[1]);
        String reservadoIni = this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), rangoI);
        String reservadoFin = this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), rangoF);

        String nroR = em.createNamedQuery("CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC", String.class)
                .setParameter("COD_SEDE", correspondencia.getCodSede())
                .setParameter("COD_TIPO_CMC", correspondencia.getCodTipoCmc())
                .setParameter("RESERVADO_INI", reservadoIni)
                .setParameter("RESERVADO_FIN", reservadoFin)
                .getSingleResult();

        int consecRadicado = 0;

        if (nroR != null) {
            CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter(constNroRadicado, nroR)
                    .getSingleResult();
            Calendar calendar = Calendar.getInstance();
            int anno = calendar.get(Calendar.YEAR);
            calendar.setTime(correspondenciaDTO.getFecRadicado());
            if (anno == calendar.get(Calendar.YEAR)) {
                consecRadicado = Integer.parseInt(nroR.substring(nroR.length() - 6));
            }
        }
        consecRadicado++;

        if (consecRadicado >= rangoI && consecRadicado <= rangoF) {
            consecRadicado = rangoF + 1;
        }

        return this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), consecRadicado);
    }

    public String formarNroRadicado(String codSede, String codTipoCmc, String anno, int consecutivo) {
        return codSede
                .concat(codTipoCmc)
                .concat(anno)
                .concat(String.format("%06d", consecutivo));
    }

    public Date calcularFechaVencimientoGestion(CorrespondenciaDTO correspondenciaDTO) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(correspondenciaDTO.getFecRadicado());
        if (correspondenciaDTO.getInicioConteo().equals(diaSiguienteHabil))
            calendario.setTime(calcularDiaHabilSiguiente(calendario.getTime()));

        Long tiempoDuracionTramite = Long.parseLong(correspondenciaDTO.getTiempoRespuesta());
        long cantHorasLaborales = horasHabilesDia(horarioLaboral[0], horarioLaboral[1]);
        if (correspondenciaDTO.getCodUnidadTiempo().equals(unidadTiempoDias))
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

    public long horasHabilesDia(String horaInicio, String horaFin) {
        return ChronoUnit.HOURS.between(LocalTime.parse(horaInicio), LocalTime.parse(horaFin));
    }

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

    public Boolean esDiaHabil(Date fecha) {
        return !(esDiaFestivo(fecha) || esFinSemana(fecha));
    }

    public Boolean esDiaFestivo(Date fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return Arrays.stream(diasFestivos).anyMatch(x-> x.equals(formatoFecha.format(fecha)));
    }

    public Boolean esFinSemana(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        return calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

}
