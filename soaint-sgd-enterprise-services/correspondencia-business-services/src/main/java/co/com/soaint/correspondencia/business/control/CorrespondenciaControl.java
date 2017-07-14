package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


    @Value( "${radicado.rango.reservado}" )
    private String rangoReservado = "";


    public ComunicacionOficialDTO consultarComunicacionOficialByCorrespondencia(CorrespondenciaDTO correspondenciaDTO){
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

    public String generarNumeroRadicado(CorrespondenciaDTO correspondencia){//TODO

        String[] rango = this.rangoReservado.split(",");
        int rangoI = Integer.parseInt(rango[0]);
        int rangoF = Integer.parseInt(rango[1]);
        String reservadoIni = this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), rangoI);
        String reservadoFin = this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), rangoF);

        List<String> nroRList = em.createNamedQuery("CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC", String.class)
                .setParameter("COD_SEDE", correspondencia.getCodSede())
                .setParameter("COD_TIPO_CMC", correspondencia.getCodTipoCmc())
                .setParameter("RESERVADO_INI", reservadoIni)
                .setParameter("RESERVADO_FIN", reservadoFin)
                .getResultList();

        int consecRadicado = 0;

        if (!nroRList.isEmpty()) {
            String nroR = nroRList.get(0);
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

        if (consecRadicado >= rangoI && consecRadicado <= rangoF){
            consecRadicado = rangoF + 1;
        }

        return this.formarNroRadicado(correspondencia.getCodSede(), correspondencia.getCodTipoCmc(),
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), consecRadicado);
    }

    public String formarNroRadicado(String codSede, String codTipoCmc, String anno, int consecutivo){
        return codSede
                .concat(codTipoCmc)
                .concat(anno)
                .concat(String.format("%06d", consecutivo));
    }

    public Date calcularFechaVencimientoGestion(CorrespondenciaDTO correspondenciaDTO){//TODO
        return new Date();
    }

}
