package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


import org.junit.Test;

import static org.junit.Assert.*;

public class DepartamentoControlTest {

    @Rule
    public ExpectedException fails = ExpectedException.none();
    EntityManager em;
    DepartamentoControl departamentoControl;
    PaisControl paisControl;

    final String ESTADO="ACTIVO";
    final String COD_PAIS="CUBA";
    final String COD_MUNIC="CODIGO1";
    final String COD_DEPART="CODIGODEPTO1";


    @Before
    public void setUp() throws Exception {

        departamentoControl = new DepartamentoControl();

        em = mock(EntityManager.class);
        paisControl = mock(PaisControl.class);


        ReflectionTestUtils.setField(departamentoControl, "em", em);
        ReflectionTestUtils.setField(departamentoControl, "paisControl", paisControl);


    }
    private <T> TypedQuery<T> getSingleResultMock(String namedQuery, Class<T> type, T dummyObject) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(dummyObject);
        return typedQuery;
    }
    private <T> TypedQuery<T> getResultListMock(String namedQuery, Class<T> type, List<T>list) {
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




    @Test
    public void listarDepartamentosByEstado() throws Exception {
        //given
        List<DepartamentoDTO> departamentoDTOList=new ArrayList<DepartamentoDTO>();
        List<DepartamentoDTO> departamentoDTOListStub = new ArrayList<DepartamentoDTO>();

        TypedQuery<DepartamentoDTO> departamentoDTOTypedQuery = getResultListMock("TvsDepartamento.findAll", DepartamentoDTO.class,departamentoDTOListStub);

        //when
       departamentoDTOList=departamentoControl.listarDepartamentosByEstado(ESTADO);
        //then
        verify(em).createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class);
        verify(departamentoDTOTypedQuery).setParameter("ESTADO", ESTADO);//con parametros
    }

    @Test
    public void listarDepartamentosByEstado_Fail() throws SystemException, BusinessException {

        when(em.createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        departamentoControl.listarDepartamentosByEstado(ESTADO);
    }

    @Test
    public void listarDepartamentosByCodPaisAndEstado() throws Exception {
        List<DepartamentoDTO> departamentoDTOList=new ArrayList<DepartamentoDTO>();
        List<DepartamentoDTO> departamentoDTOListStub = new ArrayList<DepartamentoDTO>();

        TypedQuery<DepartamentoDTO> departamentoDTOTypedQuery = getResultListMock("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class,departamentoDTOListStub);

        //when
        departamentoDTOList=departamentoControl.listarDepartamentosByCodPaisAndEstado(COD_PAIS,ESTADO);
        //then
        verify(em).createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class);
        verify(departamentoDTOTypedQuery).setParameter("COD_PAIS", COD_PAIS);//con parametros
        verify(departamentoDTOTypedQuery).setParameter("ESTADO", ESTADO);//con parametros

    }
    @Test
    public void listarDepartamentosByCodPaisAndEstado_Fail() throws SystemException, BusinessException {

        when(em.createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        departamentoControl.listarDepartamentosByCodPaisAndEstado(ESTADO,COD_PAIS);
    }

    @Test
    public void consultarDepartamentoByCodMunic() throws Exception {

        DepartamentoDTO departamentoDTO= DepartamentoDTO.newInstance().build();
        DepartamentoDTO departamentoDTOStub= DepartamentoDTO.newInstance().build();

        TypedQuery<DepartamentoDTO> departamentoDTOTypedQuery = getSingleResultMock("TvsDepartamento.findByCodMunic", DepartamentoDTO.class,departamentoDTOStub);

        //when
        departamentoDTO=departamentoControl.consultarDepartamentoByCodMunic(COD_MUNIC);
        //then
        verify(em).createNamedQuery("TvsDepartamento.findByCodMunic", DepartamentoDTO.class);
        verify(departamentoDTOTypedQuery).setParameter("COD_MUNIC", COD_MUNIC);//con parametros
    }
    @Test
    public void consultarDepartamentoByCodMunic_Fail() throws SystemException, BusinessException {

        when(em.createNamedQuery("TvsDepartamento.findByCodMunic", DepartamentoDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        departamentoControl.consultarDepartamentoByCodMunic(COD_MUNIC);
    }

    @Test
    public void consultarDepartamentoByCod() throws Exception {
        DepartamentoDTO departamentoDTO= DepartamentoDTO.newInstance().build();
        DepartamentoDTO departamentoDTOStub= DepartamentoDTO.newInstance().build();

        TypedQuery<DepartamentoDTO> departamentoDTOTypedQuery = getSingleResultMock("TvsDepartamento.findByCodDep", DepartamentoDTO.class,departamentoDTOStub);

        //when
        departamentoDTO=departamentoControl.consultarDepartamentoByCod(COD_DEPART);
        //then
        verify(em).createNamedQuery("TvsDepartamento.findByCodDep", DepartamentoDTO.class);
        verify(departamentoDTOTypedQuery).setParameter("COD_DEP", COD_DEPART);//con parametros
    }
    @Test
    public void consultarDepartamentoByCod_Fail() throws SystemException, BusinessException {

        when(em.createNamedQuery("TvsDepartamento.findByCodDep", DepartamentoDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        departamentoControl.consultarDepartamentoByCod(COD_DEPART);
    }

    @Test
    public void existeDepartamentoByCod() throws Exception {
        DepartamentoDTO departamentoDTO= DepartamentoDTO.newInstance().build();
        DepartamentoDTO departamentoDTOStub= DepartamentoDTO.newInstance().build();

        TypedQuery<DepartamentoDTO> departamentoDTOTypedQuery = getSingleResultMock("TvsDepartamento.existeDepartamentoByCodDep", DepartamentoDTO.class,departamentoDTOStub);

        //when
        departamentoDTO=departamentoControl.existeDepartamentoByCod(COD_DEPART);
        //then
        verify(em).createNamedQuery("TvsDepartamento.existeDepartamentoByCodDep", DepartamentoDTO.class);
        verify(departamentoDTOTypedQuery).setParameter("COD_DEP", COD_DEPART);//con parametros
    }
    public void existeDepartamentoByCod_Fail() throws SystemException, BusinessException {

        when(em.createNamedQuery("TvsDepartamento.existeDepartamentoByCodDep", DepartamentoDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        departamentoControl.existeDepartamentoByCod(COD_DEPART);
    }

}