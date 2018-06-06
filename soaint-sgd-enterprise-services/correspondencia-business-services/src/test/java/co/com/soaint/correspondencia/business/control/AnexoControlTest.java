package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.correspondencia.domain.entity.CorAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexosFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)

@Log4j2
public class AnexoControlTest  extends JPAHibernateContextTest {
    @Autowired
    private ConstantesControl constantesControl;
    
    @Autowired
    private AnexoControl anexoDTOCOntrol;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void anexoTransformToFull() throws Exception {
        AnexoDTO anexoDTO= AnexoDTO.newInstance()
                .ideAnexo(new BigInteger("100"))
                .codAnexo("CA-TID")
                .descripcion("ME-RECVN")
                .codTipoSoporte("CODIGO1")
                .build();
        AnexoFullDTO anexoFullDTO= anexoDTOCOntrol.anexoTransformToFull(anexoDTO);

        assertNotNull(anexoFullDTO);
        assertEquals(anexoFullDTO.getIdeAnexo(),new BigInteger("100"));
    }

    @Test
    public void anexoListTransformToFull() throws Exception {
        List<AnexoDTO> listaAnexo=new ArrayList<AnexoDTO>();
        listaAnexo.add(AnexoDTO.newInstance()
                .ideAnexo(new BigInteger("100"))
                .codAnexo("CA-TID")
                .descripcion("ME-RECVN")
                .codTipoSoporte("CODIGO1")

                .build());
        listaAnexo.add(AnexoDTO.newInstance()
                .ideAnexo(new BigInteger("200"))
                .codAnexo("CA-TID")
                .descripcion("ME-RECVN")
                .codTipoSoporte("CODIGO2")

                .build());
        List<AnexoFullDTO> anexoFullDTOList=anexoDTOCOntrol.anexoListTransformToFull(listaAnexo);

        assertNotNull(anexoFullDTOList);
        assertNotEquals(anexoFullDTOList.size(),0);

    }

    @Test
    public void consultarAnexosByPpdDocumentos() throws Exception {
        List<PpdDocumentoDTO>ppdDocumentoList=new ArrayList<PpdDocumentoDTO>();
        PpdDocumentoDTO ppdDocumento= PpdDocumentoDTO.newInstance()
                .idePpdDocumento(new BigInteger("100"))
                .build();
        ppdDocumentoList.add(ppdDocumento);
        List<AnexoDTO>anexoDTOList=anexoDTOCOntrol.consultarAnexosByPpdDocumentos(ppdDocumentoList);

        assertNotNull(anexoDTOList);
        assertEquals(anexoDTOList.size(),1);

    }

    @Test
    public void listarAnexosByNroRadicado() throws Exception {

        String nroRadicado="1040TP-CMCOE2017000001";

        AnexosFullDTO anexoFullDTO=anexoDTOCOntrol.listarAnexosByNroRadicado(nroRadicado);

        assertNotNull(anexoFullDTO);
    }

    @Test
    public void corAnexoTransform() throws Exception {
        AnexoDTO anexoDTO= AnexoDTO.newInstance()
                .ideAnexo(new BigInteger("100"))
                .codAnexo("CA-TID")
                .descripcion("ME-RECVN")
                .codTipoSoporte("CODIGO1")
                .build();
        CorAnexo corAnexo=anexoDTOCOntrol.corAnexoTransform(anexoDTO);

        assertNotNull(corAnexo);
        assertEquals(corAnexo.getCodTipoSoporte(),"CODIGO1");
    }

}