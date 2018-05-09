package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.correspondencia.business.control.CorrespondenciaControl;
import co.com.soaint.correspondencia.business.control.PpdTrazDocumentoControl;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by eric.rodriguez on 14/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
@Log4j2
public class GestionarAgenteTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GestionarAgente boundary;

    @Autowired
    private AgenteControl control;

    @Autowired
    private CorrespondenciaControl correspondenciaControl;

    @Autowired
    PpdTrazDocumentoControl ppdTrazDocumentoControl;

    @Test
    @Transactional
    public void test_actualizar_estado_agente_success() throws SystemException, BusinessException {
        // given
        BigInteger idAgente = new BigInteger("200");
        String estadoAgente = "DT";
        AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(idAgente);
        agenteDTO.setCodEstado(estadoAgente);

        // when
        boundary.actualizarEstadoAgente(agenteDTO);

        // then
        AgenteDTO agenteObtenido = control.consultarAgenteByIdeAgente(idAgente);
        assertEquals(estadoAgente, agenteObtenido.getCodEstado());
    }

    @Test
    @Transactional
    public void test_obtener_agente_por_id_failure() {
        BigInteger ideAgente = new BigInteger("150");
        try {
            AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(ideAgente);
        } catch (BusinessException e) {
            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
            log.error("GestionarAgenteTest - a system error has occurred", e);
        }

    }

    @Test
    @Transactional
    public void test_consultar_remitente_by_nro_radicado_success() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000001";

        // when
        AgenteDTO agenteDTO = boundary.consultarRemitenteByNroRadicado(nroRadicado);

        // then
        assertNotNull(agenteDTO);
    }

    @Test
    @Transactional
    public void test_consultar_remitente_by_nro_radicado_failure(){
        String nroRadicado = "1040TC-CMCOE2017000001";
        try {
            AgenteDTO agenteDTO = boundary.consultarRemitenteByNroRadicado(nroRadicado);
        } catch (BusinessException e) {
            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        }
    }

    @Test
    @Transactional
    public void test_redireccionar_correspondencia_failure() {
        // given
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        agenteDTOList.add(AgenteDTO.newInstance()
                .ideAgente(new BigInteger("100"))
                .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                .build());
        agenteDTOList.add(AgenteDTO.newInstance()
                .ideAgente(new BigInteger("200"))
                .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                .build());
        agenteDTOList.add(new AgenteDTO());

        PpdTrazDocumentoDTO ppdTrazaDocumento = PpdTrazDocumentoDTO.newInstance()
                .ideDocumento(new BigInteger("836"))
                .ideTrazDocumento(new BigInteger("100"))
                .ideFunci(new BigInteger("2"))
                .codEstado("")
                .observacion("Nueva observacion")
                .codOrgaAdmin("10120140")
                .build();
        // when
        try {
            boundary.redireccionarCorrespondencia(RedireccionDTO.newInstance()
                    .agentes(agenteDTOList)
                    .traza(ppdTrazaDocumento)
                    .build());
        }
        //Then
        catch (Exception ex){
            assertTrue(ex.getCause() instanceof Exception);
            log.error("GestionarAgenteTest - a system error has occurred", ex);
        }
    }

//    @Test
//    @Transactional
//    public void test_redireccionar_correspondencia_success() throws SystemException, BusinessException {
//        // Given
//        BigInteger idAgente = new BigInteger("100");
//        BigInteger idTrazaDocumento = new BigInteger("100");
//        BigInteger idDocumento = new BigInteger("836");
//        BigInteger ideFunc = new BigInteger("2"); // se produce un failure con id 1, no existe - NullPointerException
//        String codEstado= "AS";
//        String estadoCambiado = "SA";
//        String coDependencia = "CD";
//        String codSede = "CS";
//
//        AgenteDTO agenteDTO = new AgenteDTO();
//        agenteDTO.setIdeAgente(idAgente);
//        agenteDTO.setCodDependencia(coDependencia);
//        agenteDTO.setCodSede(codSede);
//        agenteDTO.setCodEstado(codEstado);
//
//        List<AgenteDTO> agenteDTOList = new ArrayList<>();
//        agenteDTOList.add(agenteDTO);
//
//        PpdTrazDocumentoDTO ppdTrazaDocumento = new PpdTrazDocumentoDTO();
//        ppdTrazaDocumento.setIdeDocumento(idDocumento);
//        ppdTrazaDocumento.setIdeTrazDocumento(idTrazaDocumento);
//        ppdTrazaDocumento.setIdeFunci(ideFunc);
//
//        RedireccionDTO  redireccionDTO = RedireccionDTO.newInstance()
//                .agentes(agenteDTOList)
//                .traza(ppdTrazaDocumento)
//                .build();
//        // when
//        boundary.redireccionarCorrespondencia(redireccionDTO);
//        //Then
//        AgenteDTO afterAgente = control.consultarAgenteByIdeAgente(idAgente);
//
//        assertEquals(estadoCambiado, afterAgente.getCodEstado());
//
//    }

//    @Test
//    @Transactional
//    public void test_devolver_correspondencia_success() throws SystemException, BusinessException {
//        // given
//        BigInteger idAgente = new BigInteger("100");
//        BigInteger idTrazaDocumento = new BigInteger("100");
//        BigInteger idDocumento = new BigInteger("836");
//        BigInteger ideFunc = new BigInteger("2");
//
//        PpdTrazDocumentoDTO ppdTrazaDocumento = new PpdTrazDocumentoDTO();
//        ppdTrazaDocumento.setIdeDocumento(idDocumento);
//        ppdTrazaDocumento.setIdeTrazDocumento(idTrazaDocumento);
//        ppdTrazaDocumento.setIdeFunci(ideFunc);
//
//        ItemDevolucionDTO itemDevolucionDTO = ItemDevolucionDTO.newInstance()
//                .agente(control.consultarAgenteByIdeAgente(idAgente))
//                .correspondencia(correspondenciaControl.consultarCorrespondenciaByIdeAgente(idAgente))
//                .build();
//
//        List<ItemDevolucionDTO> itemsDevolucionDTOS= new ArrayList<>();
//        itemsDevolucionDTOS.add(itemDevolucionDTO);
//
//        DevolucionDTO devolucion = DevolucionDTO.newInstance()
//                .itemsDevolucion(itemsDevolucionDTOS)
//                .traza(ppdTrazaDocumento)
//                .build();
//
//        // when
//        boundary.devolverCorrespondencia(devolucion);
//
//        AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(idAgente);
//
//        // then
//        assertEquals(EstadoAgenteEnum.DEVUELTO.getCodigo(), agenteDTO.getCodEstado());
//    }

    @Test
    @Transactional
    public void test_actualizar_remitente_success() throws SystemException, BusinessException {

        BigInteger idAgente = new BigInteger("100");
        BigInteger ideFunc = new BigInteger("2");
        String nroRadicado = "1040TC-CMCOE2017000001";

        try {
            RemitenteDTO remitenteDTO = RemitenteDTO.newInstance()
                    .agenteRemitente(control.consultarAgenteByIdeAgente(idAgente))
                    .ideFuncionarioCreaModifica(ideFunc)
                    .build();
            String result = boundary.actualizarRemitente(remitenteDTO);

            assertEquals("1", result);
        } catch (SystemException e) {
            log.error("GestionarAgenteTest - a business error has occurred", e);
        }
    }

    @Test
    @Transactional
    public void test_actualizar_remitente_failure() throws SystemException, BusinessException{

        BigInteger idAgente = new BigInteger("150");
        BigInteger ideFunc = new BigInteger("2");

        try {
            AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(idAgente);
            RemitenteDTO remitenteDTO = RemitenteDTO.newInstance()
                    .agenteRemitente(agenteDTO)
                    .ideFuncionarioCreaModifica(ideFunc)
                    .build();
            String result = boundary.actualizarRemitente(remitenteDTO);
            assertEquals("0",result);
        } catch (Exception e) {
//            assertTrue(e.getCause() instanceof SystemException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        }
    }

}
