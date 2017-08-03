package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
                .setParameter("NRO_RADICADO", nroRadicado)
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
                    .setParameter("NRO_RADICADO", nroR)
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
