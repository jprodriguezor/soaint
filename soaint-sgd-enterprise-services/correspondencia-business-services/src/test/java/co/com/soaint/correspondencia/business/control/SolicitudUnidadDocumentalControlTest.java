package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsSolicitudUnidadDocumental;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class SolicitudUnidadDocumentalControlTest {

    @Autowired
    private SolicitudUnidadDocumentalControl solicitudUnidadDocumentalControl;

    final String ID_CONST="505050", ID_SOL="2", MOTIVO = "TP-MNTN", COD_SEDE= "1040", COD_DEP= "10401040", COD_SERIE= "0231", COD_SUBSERIE= "02311"
            , COD_DESC1= "Descriptor 1", COD_DESC2= "Descriptor 2", NRO= "5d7aedfa-3711-4396-879c-414def97335z", OBSERV= "solicitud crear UD expiediente"
            , NOM_UD= "Unidad 50", ID= "50";
    final String ID_CONST1="505051", ID_SOL1="3", MOTIVO1 = "TP-MNTN", COD_SEDE1= "1000", COD_DEP1= "10001040", COD_SERIE1= "0232", COD_SUBSERIE1= "02312"
            , COD_DESC11= "Descriptor 11", COD_DESC21= "Descriptor 21", NRO1= "5d7aedfa-3711-4396-879c-414def97335a", OBSERV1= "solicitud crear UD expiediente"
            , NOM_UD1= "Unidad 51", ID1= "51";
    final BigInteger ID_MOD=  new BigInteger("1");

    String sede = "1040";
    String sede2 = "1000";
    String dependencia = "10401040";
    String dependencia2 = "10001040";
    String fechaI = "2018-01-01";
    String fechaF = "2018-06-31";
    String fechaFNoExist = "2017-06-31";
    String fechaFfallo = "2017-12-31";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    SolicitudUnidadDocumentalDTO solicitud = SolicitudUnidadDocumentalDTO.newInstance()
//            .idSolicitud(new BigInteger("1"))
            .idConstante(ID_CONST)
            .id(ID)
            .idSolicitante(ID_SOL)
            .motivo(MOTIVO)
            .nombreUnidadDocumental(NOM_UD)
            .codigoSede(COD_SEDE)
            .codigoDependencia(COD_DEP)
            .codigoSerie(COD_SERIE)
            .codigoSubSerie(COD_SUBSERIE)
            .descriptor1(COD_DESC1)
            .descriptor2(COD_DESC2)
            .nro(NRO)
            .estado("insertado en bd")
            .observaciones(OBSERV)
            .build();

    SolicitudUnidadDocumentalDTO solicitud2 = SolicitudUnidadDocumentalDTO.newInstance()
//            .idSolicitud(new BigInteger("2"))
            .idConstante(ID_CONST1)
            .id(ID1)
            .idSolicitante(ID_SOL1)
            .motivo(MOTIVO1)
            .nombreUnidadDocumental(NOM_UD1)
            .codigoSede(COD_SEDE1)
            .codigoDependencia(COD_DEP1)
            .codigoSerie(COD_SERIE1)
            .codigoSubSerie(COD_SUBSERIE1)
            .descriptor1(COD_DESC11)
            .descriptor2(COD_DESC21)
            .nro(NRO1)
            .estado("Insertado en bd")
            .observaciones(OBSERV1)
            .build();

    List<SolicitudUnidadDocumentalDTO> solicitudesUnidadDocumental = new ArrayList<SolicitudUnidadDocumentalDTO>();

    @Test
    @Transactional
    public void crearSolicitudUnidadDocumental() throws SystemException, BusinessException {
        //given
        solicitudesUnidadDocumental.add(solicitud);
        solicitudesUnidadDocumental.add(solicitud2);

        //when
        solicitudUnidadDocumentalControl.crearSolicitudUnidadDocumental(SolicitudesUnidadDocumentalDTO.newInstance().
                solicitudesUnidadDocumentalDTOS(solicitudesUnidadDocumental)
                .build());
        Long cantidad = solicitudUnidadDocumentalControl.obtenerCantidadSolicitudes();
        List<SolicitudUnidadDocumentalDTO> solicitudes = solicitudUnidadDocumentalControl.listarSolicitudes();
        assertThat(6L, is(cantidad));
        solicitud.setIdSolicitud(new BigInteger("1"));
        solicitud2.setIdSolicitud(new BigInteger("2"));
        assertThat(solicitud, isIn(solicitudes));
        assertThat(solicitud2, isIn(solicitudes));
    }

    @Test
    @Transactional
    public void insertarSolicitudUnidadDocumental() throws SystemException, BusinessException {
            solicitudUnidadDocumentalControl.insertarSolicitudUnidadDocumental(solicitud);
            solicitudesUnidadDocumental = solicitudUnidadDocumentalControl.listarSolicitudes();
            assertTrue(solicitudesUnidadDocumental.size()>0);
            assertThat(solicitudesUnidadDocumental.size(), is(5));
    }

    @Test
    public void tvsSolicitudUnidadDocumentalTransform() throws BusinessException, SystemException {
        TvsSolicitudUnidadDocumental unidadDocumental = solicitudUnidadDocumentalControl.tvsSolicitudUnidadDocumentalTransform(solicitud);
        assertThat(solicitud.getId(), is(unidadDocumental.getId()));
        assertThat(solicitud.getIdConstante(), is(unidadDocumental.getIdConstante()));
        assertThat(solicitud.getIdSolicitante(), is(unidadDocumental.getIdSolicitante()));
        assertThat(solicitud.getCodigoDependencia(), is(unidadDocumental.getCodDependencia()));
        assertThat(solicitud.getCodigoSede(), is(unidadDocumental.getCodSede()));
        assertThat(solicitud.getCodigoSerie(), is(unidadDocumental.getCodSerie()));
        assertThat(solicitud.getCodigoSubSerie(), is(unidadDocumental.getCodSubserie()));
        assertThat(solicitud.getDescriptor1(), is(unidadDocumental.getDescriptor1()));
        assertThat(solicitud.getDescriptor2(), is(unidadDocumental.getDescriptor2()));
        assertThat(solicitud.getAccion(), is(unidadDocumental.getAccion()));
        assertThat(solicitud.getEstado(), is(unidadDocumental.getEstado()));
        assertThat(solicitud.getMotivo(), is(unidadDocumental.getMotivo()));
        assertThat(solicitud.getNombreUnidadDocumental(), is(unidadDocumental.getNombreUD()));
        assertThat(solicitud.getNro(), is(unidadDocumental.getNro()));
        assertThat(solicitud.getObservaciones(), is(unidadDocumental.getObservaciones()));
        assertThat(solicitud.getFechaHora(), is(unidadDocumental.getFecHora()));
    }

    @Test
    public void obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo() throws ParseException, SystemException, BusinessException {
        //given
        Date fechaInicial = dateFormat.parse(fechaI);
        Date fechaFinal = dateFormat.parse(fechaF);
        Date fechaFinalNoExist = dateFormat.parse(fechaFNoExist);
        Date fechaFinfallo = dateFormat.parse(fechaFfallo);

        //when
        try {
            SolicitudesUnidadDocumentalDTO resultado = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaInicial,fechaFinfallo,sede,dependencia);
        } catch (BusinessException e) {
            assertThat("La fecha final no puede ser igual o menor que la fecha inicial.", is(e.getMessage()));
        }
            SolicitudesUnidadDocumentalDTO resultado = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(null,null,sede,dependencia);
            assertNotNull(resultado);
            assertTrue(resultado.getSolicitudesUnidadDocumentalDTOS().size()>0);
            assertThat(resultado.getSolicitudesUnidadDocumentalDTOS().size(), is(3));

        try {
            SolicitudesUnidadDocumentalDTO resultado1 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(null,fechaFinalNoExist,sede,dependencia);
            assertNotNull(resultado);
            assertThat(resultado1.getSolicitudesUnidadDocumentalDTOS().size(), is(0));
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado1 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(null,fechaFinal,sede,dependencia);
            assertNotNull(resultado);
            assertThat(resultado1.getSolicitudesUnidadDocumentalDTOS().size(), is(3));
        } catch (BusinessException e) {
            e.printStackTrace();
            assertThat("La fecha final no puede ser igual o menor que la fecha inicial.", is(e.getMessage()));
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado2 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaInicial,null,sede,dependencia);
            assertNotNull(resultado);
            assertThat(resultado2.getSolicitudesUnidadDocumentalDTOS().size(), is(3));
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado3 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(null,null,null,null);
            assertNotNull(resultado3);
            assertThat(resultado3.getSolicitudesUnidadDocumentalDTOS().size(), is(4));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante() throws SystemException, ParseException {
        //given
        Date fechaInicial = dateFormat.parse(fechaI);
        Date fechaFinal = dateFormat.parse(fechaF);
        Date fechaFinalNoExist = dateFormat.parse(fechaFNoExist);
        Date fechaFinfallo = dateFormat.parse(fechaFfallo);
        String idSolicitante = "2";

        try {
            SolicitudesUnidadDocumentalDTO resultado = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(null,null,null,null);
            assertNotNull(resultado);
            assertThat(resultado.getSolicitudesUnidadDocumentalDTOS().size(), is(4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(null,idSolicitante,null,null);
            assertNotNull(resultado);
            assertThat(resultado.getSolicitudesUnidadDocumentalDTOS().size(), is(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado1 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(null,null,sede2,null);
            assertNotNull(resultado1);
            assertThat(resultado1.getSolicitudesUnidadDocumentalDTOS().size(), is(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SolicitudesUnidadDocumentalDTO resultado2 = solicitudUnidadDocumentalControl.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaInicial,null,null,dependencia2);
            assertNotNull(resultado2);
            assertThat(resultado2.getSolicitudesUnidadDocumentalDTOS().size(), is(1));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar() {
    }

    @Test
    public void verificarByIdeSolicitud() {
    }

    @Test
    public void verificarByIdNombreUMSolicitud() {
    }

    @Test
    public void actualizarSolicitudUnidadDocumental() {
    }
}