package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import static org.junit.Assert.*;


public class DocumentoControlTest {

    @Rule
    public ExpectedException fails = ExpectedException.none();
    DocumentoControl documentoControl;

    // Dependencias
    EntityManager em;
    PpdTrazDocumentoControl ppdTrazDocumentoControl;
    PpdDocumentoControl ppdDocumentoControl;
    String IDE_PPDDOCUMENTO="100";
    BigInteger IDE_DOCUMENTO=BigInteger.valueOf(836);
    String IDE_ECM="666";
    String OBSERVACION = "Just";
    String COD_ESTADO = "AS";
    String COD_ORGA_ADMIN = "10120140";
    BigInteger IDE_TRAZA_DOCUMENTO = BigInteger.valueOf(100);
    BigInteger IDE_FUNCIONARIO = BigInteger.valueOf(2);

    private <T> TypedQuery<T> getSingleResultMock(String namedQuery, Class<T> type, T dummyObject) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(dummyObject);
        return typedQuery;
    }
    private <T> TypedQuery<T> getResultListMock(String namedQuery, Class<T> type, List<T> list) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);
        return typedQuery;
    }

    private <T> TypedQuery<T> getExecuteUpdateMock(String namedQuery, Class<T> type) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        if(type == null)
            when(em.createNamedQuery(namedQuery)).thenReturn(typedQuery);
        else
            when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        return typedQuery;
    }

    @Before
    public void setUp() throws Exception {

        documentoControl = new DocumentoControl();

        em = mock(EntityManager.class);
        ppdTrazDocumentoControl = mock(PpdTrazDocumentoControl.class);
        ppdDocumentoControl = mock(PpdDocumentoControl.class);

        ReflectionTestUtils.setField(documentoControl, "em", em);
        ReflectionTestUtils.setField(documentoControl, "ppdTrazDocumentoControl", ppdTrazDocumentoControl);
        ReflectionTestUtils.setField(documentoControl, "ppdDocumentoControl", ppdDocumentoControl);

    }

    @Test
    public void actualizarReferenciaECM() throws Exception {
        DocumentoDTO documentoDTO= DocumentoDTO.newInstance()
                .nroRadicado(IDE_PPDDOCUMENTO)
                .ideEcm(IDE_ECM)
                .build();

        TypedQuery<DocumentoDTO> documentoDTOTypedQuery = getExecuteUpdateMock("PpdDocumento.updateIdEcm", null);

        List<BigInteger> LIST_IDE_PPD_DOCUMENTO = new ArrayList<BigInteger>();
        LIST_IDE_PPD_DOCUMENTO.add(IDE_DOCUMENTO);
        when(ppdDocumentoControl.consultarPpdDocumentosByNroRadicado(anyString())).thenReturn(LIST_IDE_PPD_DOCUMENTO);

        documentoControl.actualizarReferenciaECM(documentoDTO);

        verify(em).createNamedQuery("PpdDocumento.updateIdEcm");
        verify(documentoDTOTypedQuery).setParameter("IDE_PPDDOCUMENTO", LIST_IDE_PPD_DOCUMENTO.get(0));
        verify(documentoDTOTypedQuery).setParameter("IDE_ECM", IDE_ECM);
    }

    @Test
    public void generarTrazaDocumento() throws Exception {

        try {PpdTrazDocumentoDTO ppdTrazDocumentoDTO= PpdTrazDocumentoDTO.newInstance()
                .codEstado("AS")
                .codOrgaAdmin("10120140")
                .build();
            documentoControl.generarTrazaDocumento(ppdTrazDocumentoDTO);
        }
        catch (Exception e){
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    @Test
    public void listarObservacionesDocumento() throws Exception {

        List<PpdTrazDocumentoDTO> LIST_OBSERVACIONES = new ArrayList<PpdTrazDocumentoDTO>();

        PpdTrazDocumentoDTO OBSERVACION_DTO = PpdTrazDocumentoDTO.newInstance()
                .observacion(OBSERVACION)
                .ideTrazDocumento(IDE_TRAZA_DOCUMENTO)
                .ideDocumento(IDE_DOCUMENTO)
                .ideFunci(IDE_FUNCIONARIO)
                .codEstado(COD_ESTADO)
                .codOrgaAdmin(COD_ORGA_ADMIN)
                .build();

        LIST_OBSERVACIONES.add(OBSERVACION_DTO);

        ObservacionesDocumentoDTO OBSERVACIONES = ObservacionesDocumentoDTO.newInstance()
                .observaciones(LIST_OBSERVACIONES)
                .build();

        when(ppdTrazDocumentoControl.listarTrazasDocumento(any(BigInteger.class))).thenReturn(OBSERVACIONES);

        ObservacionesDocumentoDTO observacionesDocumentoDTO=documentoControl.listarObservacionesDocumento(IDE_DOCUMENTO);

        assertNotNull(observacionesDocumentoDTO);
        assertThat(OBSERVACION_DTO, is(Matchers.isIn(observacionesDocumentoDTO.getObservaciones())));
    }

    @Test
    public void consultarDocumentoPorIdeDocumento() throws Exception {
    }

}