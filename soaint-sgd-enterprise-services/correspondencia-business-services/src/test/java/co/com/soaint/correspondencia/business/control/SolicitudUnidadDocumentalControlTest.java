package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudesUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
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
    final BigInteger ID_MOD=  new BigInteger("2");

    SolicitudUnidadDocumentalDTO solicitud = SolicitudUnidadDocumentalDTO.newInstance()
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
        assertThat(2L, is(cantidad));
    }

    @Test
    @Transactional
    public void insertarSolicitudUnidadDocumental() throws SystemException, BusinessException {
            solicitudUnidadDocumentalControl.insertarSolicitudUnidadDocumental(solicitud);
            solicitudesUnidadDocumental = solicitudUnidadDocumentalControl.listarSolicitudes();
            assertTrue(solicitudesUnidadDocumental.size()>0);
            assertThat(solicitudesUnidadDocumental.size(), is(1));
    }

    @Test
    public void tvsSolicitudUnidadDocumentalTransform() {
    }

    @Test
    public void obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo() {
    }

    @Test
    public void obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante() {
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