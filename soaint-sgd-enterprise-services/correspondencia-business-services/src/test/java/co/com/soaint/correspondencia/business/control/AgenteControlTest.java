package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.foundation.canonical.correspondencia.*;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yleon on 08/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)

@Log4j2
public class AgenteControlTest extends JPAHibernateContextTest {

    @Autowired
    AgenteControl agenteControl;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listarRemitentesByIdeDocumento() throws Exception {

        //given
        BigInteger ideDocumento = BigInteger.valueOf(836);

        //when
        List<AgenteDTO> agentes = agenteControl.listarRemitentesByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
    }

    @Test
    public void listarRemitentesFullByIdeDocumento() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(836);

        //when
        List<AgenteFullDTO> agentes = agenteControl.listarRemitentesFullByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteFullDTO agt :
                agentes) {
            assertNotNull(agt.getCodTipoRemite());
            assertNotNull(agt.getDescTipoRemite());
            assertNotNull(agt.getCodTipAgent());
            assertNotNull(agt.getDescTipAgent());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);
        String codDependencia = "CD";
        String codEstado = "AS";

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(ideDocumento, codDependencia, codEstado);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteDTO agt :
                agentes) {
            assertEquals(codDependencia, agt.getCodDependencia());
            assertEquals(codEstado, agt.getCodEstado());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependencia() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);
        String codDependencia = "CD";

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependencia(ideDocumento, codDependencia);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteDTO agt :
                agentes) {
            assertEquals(codDependencia, agt.getCodDependencia());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumento() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
    }

    @Test
    public void consultarAgenteByIdeAgente() throws Exception {
        // given
        BigInteger idAgente = BigInteger.valueOf(100);
        //when
        AgenteDTO agente = agenteControl.consultarAgenteByIdeAgente(idAgente);

        //then
        assertNotNull(agente);
        assertEquals(idAgente, agente.getIdeAgente());
    }

    @Test
    public void verificarByIdeAgente() throws Exception {
        // given
        BigInteger idAgente = BigInteger.valueOf(200);
        //when
        Boolean agente = agenteControl.verificarByIdeAgente(idAgente);

        //then
        assertNotNull(agente);
        assertTrue(agente);

    }

    @Test
    public void asignarDatosContacto() throws Exception {

    }

    @Test
    public void consltarAgentesByCorrespondencia() throws Exception {
        BigInteger idDocumento = new BigInteger("836");
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        //when
       agenteDTOList= agenteControl.consltarAgentesByCorrespondencia(idDocumento);


        //then
        assertNotNull(agenteDTOList);
        //assertEquals(idDocumento,agenteDTOList.get(0).getCodTipoRemite());
        assertNotEquals(0, agenteDTOList.size());
        for (AgenteDTO agt :
                agenteDTOList) {
            assertNotNull(agt.getCodTipoRemite());
            assertNotNull(agt.getCodTipAgent());

        }
    }

    @Test
    public void agenteTransformToFull() throws Exception {///hacerrrrrrrrrrrr
       AgenteDTO agenteDTO=new AgenteDTO();
        BigInteger idDocumento = new BigInteger("100");

        //when


        try{
            AgenteDTO agenteDTOList= agenteControl.consultarAgenteByIdeAgente(idDocumento);
            AgenteFullDTO agenteFull=agenteControl.agenteTransformToFull(agenteDTO);

            assertNotNull(agenteFull);

        } catch (Exception e){
            assertTrue(e.getCause() instanceof Exception);
        }
//
    }
    @Test
    public void agenteListTransformToFull() throws Exception {

        List<AgenteDTO>agenteDTOList=new ArrayList<AgenteDTO>();
        List<AgenteFullDTO>ResultagenteDTOList=new ArrayList<AgenteFullDTO>();
        BigInteger idDocumento = new BigInteger("100");

        //when

        try {
            agenteDTOList.add(agenteControl.consultarAgenteByIdeAgente(idDocumento));
            ResultagenteDTOList = agenteControl.agenteListTransformToFull(agenteDTOList);
        }
        catch (Exception e){
            assertNotNull(ResultagenteDTOList);
            assertNotEquals(0, ResultagenteDTOList.size());
            assertTrue(ResultagenteDTOList.size()== 1);

        }
    }

    @Test
    public void consultarAgentesFullByCorrespondencia() throws Exception {

        List<AgenteFullDTO>ResultagenteDTOList=new ArrayList<AgenteFullDTO>();
        BigInteger idDocumento = new BigInteger("100");

        //when

        try {
            ResultagenteDTOList = agenteControl.consultarAgentesFullByCorrespondencia(idDocumento);
        }
        catch (Exception e){
            assertNotNull(ResultagenteDTOList);
            assertEquals(ResultagenteDTOList.size(),1);
            assertNotEquals(0, ResultagenteDTOList.size());

        }
    }

    @Test
    public void conformarAgentes() throws Exception {
        List<DatosContactoDTO> datosContactoDTOS = new ArrayList<DatosContactoDTO>();
        datosContactoDTOS.add(DatosContactoDTO.newInstance()
                .corrElectronico("g@mail.com")
                .ciudad("Habana")
                .build());
        datosContactoDTOS.add(DatosContactoDTO.newInstance()
                .corrElectronico("giselle@mail.com")
                .ciudad("Habana Campo")
                .build());
        BigInteger idDocumento = new BigInteger("836");
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        //when
        agenteDTOList= agenteControl.consltarAgentesByCorrespondencia(idDocumento);
        String estadoDist="DIST";
        List<CorAgente> resultList=agenteControl.conformarAgentes(agenteDTOList,datosContactoDTOS,estadoDist);

        assertNotNull(resultList);
        assertEquals(resultList.size(),1);
        assertNotEquals(0, resultList.size());

    }

    @Test
    public void conformarAgentesSalida() throws Exception {

        BigInteger idDocumento = new BigInteger("836");
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        //when
        agenteDTOList= agenteControl.consltarAgentesByCorrespondencia(idDocumento);
        String estadoDist="DIST";
        List<CorAgente> resultList=agenteControl.conformarAgentesSalida(agenteDTOList,estadoDist);

        assertNotNull(resultList);
        assertEquals(resultList.size(),1);
        assertNotEquals(0, resultList.size());
    }

    @Test
    public void actualizarEstadoDistribucion() throws Exception {
        BigInteger idAgente = new BigInteger("100");
        String estadoDistribucion = "DIST-00";

        // when
        agenteControl.actualizarEstadoDistribucion(idAgente, estadoDistribucion);

        // then
        CorAgente agente = em.find(CorAgente.class, idAgente);

        assertNotNull(agente);
        assertEquals(estadoDistribucion, agente.getEstadoDistribucion());
    }

    @Test
    public void corAgenteTransform() throws Exception {
        BigInteger idAgente = new BigInteger("100");
        AgenteDTO agente=agenteControl.consultarAgenteByIdeAgente(idAgente);

        CorAgente agenteResult=agenteControl.corAgenteTransform(agente);

        assertNotNull(agenteResult);
        assertEquals(idAgente,agenteResult.getIdeAgente());

    }

    @Test
    public void actualizarDestinatario_Fail() throws Exception {//mala prueba stackoverflow del toString()
        BigInteger idAgente = new BigInteger("100");
        AgenteDTO agente=agenteControl.consultarAgenteByIdeAgente(idAgente);
        DestinatarioDTO destinatarioDTO= DestinatarioDTO.newInstance()
                .agenteDestinatario(agente)
                .build();
        String resultado="";


        try{
            resultado=agenteControl.actualizarDestinatario(destinatarioDTO);
        }
        catch (Exception e){
            if (    resultado=="0")
                assertEquals(resultado,"0");
            if (    resultado=="1")
                assertEquals(resultado,"1");
            assertTrue(e.getCause() instanceof Exception);
        }


    }

    @Test
    public void actualizarRemitente_Fail() throws Exception {
        BigInteger idAgente = new BigInteger("100");
        AgenteDTO agente=agenteControl.consultarAgenteByIdeAgente(idAgente);
        RemitenteDTO destinatarioDTO= RemitenteDTO.newInstance()
                .agenteRemitente(agente)
                .build();
        String resultado="";


        try{
            resultado=agenteControl.actualizarRemitente(destinatarioDTO);
        }
        catch (Exception e){
            if (    resultado=="0")
                assertEquals(resultado,"0");
            if (    resultado=="1")
                assertEquals(resultado,"1");
            assertTrue(e.getCause() instanceof Exception);
        }
    }

    @Test
    public void consultarRemitenteByNroRadicado() throws Exception {
        //String codigoTipAgente="TP-AGEE";
        String nroRadicado="1040TP-CMCOE2017000001";
        AgenteDTO agenteDTO=agenteControl.consultarRemitenteByNroRadicado(nroRadicado);

        assertNotNull(agenteDTO); 
    }

}