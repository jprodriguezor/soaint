package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.CorrespondenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Created by gyanet on 28/03/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
@Log4j2
public class GestionarCorrespondenciaTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GestionarCorrespondencia boundary;

    @Autowired
    private CorrespondenciaControl control;

    @Test
    @Transactional
    public void test_actualizar_estado_correspondencia_success() throws SystemException, BusinessException {
        // given
        String estado = "SA";
        BigInteger ideAgente = new BigInteger("100");

        // when
        CorrespondenciaDTO correspondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);

        System.out.println(correspondenciaDTO.getCodEstado());

        correspondenciaDTO.setCodEstado(estado);
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
        CorrespondenciaDTO afterCorrespondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);

        // then
        assertEquals(estado,afterCorrespondenciaDTO.getCodEstado());
    }

    @Test
    @Transactional
    public void test_actualizar_estado_correspondencia_failure() throws SystemException, BusinessException {
        // given
        String estado = "SA";
        BigInteger ideAgente = new BigInteger("150");

        // when
        try{
            CorrespondenciaDTO correspondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);
            correspondenciaDTO.setCodEstado(estado);
            boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
        } catch (Exception e){
            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarCorrespondenciaTest - a business error has occurred", e);
        }
        // then
    }

    @Test
    @Transactional
    public void test_actualizar_ideInstancia_success() throws SystemException, BusinessException {
        // given
        String ideInstancia = "33146";
        BigInteger ideAgente = new BigInteger("100");

        // when
        CorrespondenciaDTO correspondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);

        System.out.println(correspondenciaDTO.getIdeInstancia());

        correspondenciaDTO.setIdeInstancia(ideInstancia);
        boundary.actualizarIdeInstancia(correspondenciaDTO);
        CorrespondenciaDTO afterCorrespondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);

        // then
        assertEquals(ideInstancia,afterCorrespondenciaDTO.getIdeInstancia());
    }

    @Test
    @Transactional
    public void test_actualizar_ideInstancia_failure() throws SystemException, BusinessException {
        // given
        String ideInstancia = "33146";
        BigInteger ideAgente = new BigInteger("150");

        // when
        try{
            CorrespondenciaDTO correspondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);

            System.out.println(correspondenciaDTO.getIdeInstancia());

            correspondenciaDTO.setIdeInstancia(ideInstancia);
            boundary.actualizarIdeInstancia(correspondenciaDTO);
            CorrespondenciaDTO afterCorrespondenciaDTO = control.consultarCorrespondenciaByIdeAgente(ideAgente);
        } catch (Exception e){
            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarCorrespondenciaTest - a business error has occurred", e);
        }
        // then
    }

    @Test
    @Transactional
    public void test_verificar_by_nro_radicado_success() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000001";

        // when
        boolean verificacion = control.verificarByNroRadicado(nroRadicado);

        // then
        assertTrue(verificacion);
    }

    @Test
    @Transactional
    public void test_verificar_by_nro_radicado_failure() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000003";

        // when
        boolean verificacion = control.verificarByNroRadicado(nroRadicado);

        // then
        assertFalse(verificacion);
    }

    @Test
    @Transactional
    public void test_listar_correspondencia_sin_distribuir_success() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000003";
        String reqDistrFis = "SD";

        // when

        // then
    }

    @Test
    @Transactional
    public void test_listar_correspondencia_sin_distribuir_failure() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000003";

        // when

        // then
    }

}
