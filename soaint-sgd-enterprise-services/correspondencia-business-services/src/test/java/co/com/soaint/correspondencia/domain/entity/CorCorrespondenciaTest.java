package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class CorCorrespondenciaTest extends JPAHibernateContextTest {

    @Test
    public void test_CorCorrespondenciafindAll_success() {
        //given
        String namedQuery = "CorCorrespondencia.findAll";

        //when
        List<CorCorrespondencia> coresp = createNamedQuery(namedQuery, CorCorrespondencia.class)
                .getResultList();

        //then
        assertNotNull(coresp);
        assertEquals(2, coresp.size());
    }

    @Test
    public void test_CorCorrespondenciafindByNroRadicado_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.findByNroRadicado";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";


        //when
        CorrespondenciaDTO coresp = createNamedQuery(namedQuery, CorrespondenciaDTO.class)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .getSingleResult();

        //then
        assertNotNull(coresp);
        assertEquals(coresp.getNroRadicado(), NRO_RADICADO);

    }

    @Test
    public void test_CorCorrespondenciafindByIdeDocumento_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.findByIdeDocumento";
        BigInteger IDE_DOCUMENTO = new BigInteger("837");
        //when
        CorrespondenciaDTO coresp = createNamedQuery(namedQuery, CorrespondenciaDTO.class)
                .setParameter("IDE_DOCUMENTO", IDE_DOCUMENTO)
                .getSingleResult();
        //then
        assertNotNull(coresp);
        assertEquals(coresp.getIdeDocumento(), IDE_DOCUMENTO);

    }

    @Test
    public void test_CorCorrespondenciafindIdeDocumentoByNroRadicado_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.findIdeDocumentoByNroRadicado";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";
        BigInteger IDE_DOCUMENTO = new BigInteger("837");
        //when
        BigInteger coresp = createNamedQuery(namedQuery, BigInteger.class)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .getSingleResult();

        //then
        assertNotNull(coresp);
        assertEquals(coresp, IDE_DOCUMENTO);

    }

    @Test
    public void test_CorCorrespondenciacountByNroRadicado_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.countByNroRadicado";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";
        Long cant = new Long("1");
        //when
        Long coresp = createNamedQuery(namedQuery, Long.class)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .getSingleResult();

        //then
        assertNotNull(coresp);
        assertEquals(coresp, cant);

    }

    @Test
    public void test_CorCorrespondenciamaxNroRadicadoByCodSedeAndCodTipoCMC_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC";
        String COD_SEDE = "1040";
        String COD_TIPO_CMC = "TP-CMCOE";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";

        //when
        String coresp = createNamedQuery(namedQuery, String.class)
                .setParameter("COD_SEDE", COD_SEDE)
                .setParameter("COD_TIPO_CMC", COD_TIPO_CMC)
                .setParameter("RESERVADO_INI", "80")
                .setParameter("RESERVADO_FIN", "100")
                .getSingleResult();

        //then
        assertNotNull(coresp);
        assertEquals(coresp, NRO_RADICADO);

    }

    @Test
    public void test_CorCorrespondenciafindFechaVenGestionByIdeDocumento_success() throws SystemException {

        //given
        String namedQuery = "CorCorrespondencia.findFechaVenGestionByIdeDocumento";
        BigInteger IDE_DOCUMENTO = new BigInteger("837");

        //when
        Date coresp = createNamedQuery(namedQuery, Date.class)
                .setParameter("IDE_DOCUMENTO", IDE_DOCUMENTO)
                .getSingleResult();

        //then
        assertNotNull(coresp);
    }

    @Test
    public void test_CorCorrespondenciaupdateEstado_success() {
        //given
        String namedQueryUpdate = "CorCorrespondencia.updateEstado";
        String namedQuery = "CorCorrespondencia.findByNroRadicado";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";
        String COD_ESTADO = "AS";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .setParameter("COD_ESTADO", COD_ESTADO)
                .executeUpdate();

        List<CorrespondenciaDTO> coresp = createNamedQuery(namedQuery, CorrespondenciaDTO.class)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .getResultList();

        //then
        assertNotNull(coresp);
        for (CorrespondenciaDTO cor :
                coresp) {
            if (cor.getNroRadicado().equals(NRO_RADICADO))
                assertEquals(COD_ESTADO, cor.getCodEstado());

        }

    }

    @Test
    public void test_CorCorrespondenciaupdateIdeInstancia_success() {
        //given
        String namedQueryUpdate = "CorCorrespondencia.updateIdeInstancia";
        String namedQuery = "CorCorrespondencia.findByNroRadicado";
        String NRO_RADICADO = "1040TP-CMCOE2017000002";
        String IDE_INSTANCIA = "33144";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .setParameter("IDE_INSTANCIA", IDE_INSTANCIA)
                .executeUpdate();

        List<CorrespondenciaDTO> coresp = createNamedQuery(namedQuery, CorrespondenciaDTO.class)
                .setParameter("NRO_RADICADO", NRO_RADICADO)
                .getResultList();

        //then
        assertNotNull(coresp);
        for (CorrespondenciaDTO cor :
                coresp) {
            if (cor.getNroRadicado().equals(NRO_RADICADO))
                assertEquals(IDE_INSTANCIA, cor.getIdeInstancia());

        }
    }
}
