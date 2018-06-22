package co.com.soaint.correspondencia;


import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class DependenciaControlTest {
    @Rule
    public ExpectedException fails = ExpectedException.none();
    final BigInteger ID_FUNCIONARIO =BigInteger.valueOf(2);

    DependenciaControl dependenciaControl;

    // Dependencias
    EntityManager em;
    OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Before
    public void setUp() throws Exception {

        dependenciaControl = new DependenciaControl();

        em = mock(EntityManager.class);
        organigramaAdministrativoControl = mock(OrganigramaAdministrativoControl.class);


        ReflectionTestUtils.setField(dependenciaControl, "em", em);
        ReflectionTestUtils.setField(dependenciaControl, "organigramaAdministrativoControl", organigramaAdministrativoControl);


    }
    private <T> TypedQuery<T> getSingleResultMock(String namedQuery, Class<T> type, T dummyObject) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        mockCreateNamedQuery(typedQuery, namedQuery, type);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(dummyObject);
        return typedQuery;
    }
    private <T> TypedQuery<T> getResultListMock(String namedQuery, Class<T> type, List<T>list) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        mockCreateNamedQuery(typedQuery, namedQuery, type);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);
        return typedQuery;
    }

    private <T> TypedQuery<T> getExecuteUpdateMock(String namedQuery, Class<T> type) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        mockCreateNamedQuery(typedQuery, namedQuery, type);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        return typedQuery;
    }

    private <T> void mockCreateNamedQuery(TypedQuery<T> typedQuery, String namedQuery, Class<T> type) {
        if(type == null)
            when(em.createNamedQuery(namedQuery)).thenReturn(typedQuery);
        else
            when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
    }


    @Test
    public void dependenciaDTOTransform() throws Exception {
        OrganigramaItemDTO dependencia= OrganigramaItemDTO.newInstance().build();
        OrganigramaItemDTO sede= OrganigramaItemDTO.newInstance().build();
        DependenciaDTO dependenciaDTO= DependenciaDTO.newInstance().build();

        dependenciaDTO=dependenciaControl.dependenciaDTOTransform(dependencia,sede);
        assertNotNull(dependencia);

    }

    @Test
    public void obtenerDependenciasByFuncionario() throws Exception {
        List<String> dependenciaDTOList=new ArrayList<String>();
        OrganigramaItemDTO dependenciaDTOList1= OrganigramaItemDTO.newInstance().build();
        dependenciaDTOList.add("1");
        List<DependenciaDTO> dependenciaDTOListResult=new ArrayList<DependenciaDTO>();
        // asignacion

        TypedQuery<String> dependenciaDTOTypedQuery = getResultListMock("TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci", null, dependenciaDTOList);
        TypedQuery<OrganigramaItemDTO> dependenciaDTOTypedQuery1 = getSingleResultMock("TvsOrganigramaAdministrativo.consultarElementosByCodOrgList",  OrganigramaItemDTO.class, dependenciaDTOList1);
        // when
        dependenciaDTOListResult=dependenciaControl.obtenerDependenciasByFuncionario(ID_FUNCIONARIO);

        verify(em).createNamedQuery("TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci");
        verify(dependenciaDTOTypedQuery).setParameter("IDE_FUNCI", ID_FUNCIONARIO);

        verify(em).createNamedQuery("TvsOrganigramaAdministrativo.consultarElementosByCodOrgList",OrganigramaItemDTO.class);
        verify(dependenciaDTOTypedQuery1).setParameter("COD_ORG_LIST",dependenciaDTOList);
    }

    @Test
    public void listarDependenciaByCodigo() throws Exception {
    }
    @Test
    public void listarDependenciaByCodigo_Fail() throws SystemException, BusinessException {
        String codOrg="100";

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        dependenciaControl.listarDependenciaByCodigo(codOrg);
    }

    @Test
    public void listarDependenciasByCodigo_Fail() throws SystemException, BusinessException {
        String cod ="001";

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        dependenciaControl.listarDependenciaByCodigo(cod);
    }

    /*@Test
    public void listarDependencias_Fail() throws SystemException, BusinessException,AssertionError {

        fails.expect(BusinessException.class);
        fails.expectMessage("system.generic.error");

        dependenciaControl.listarDependencias();

    }*/

}