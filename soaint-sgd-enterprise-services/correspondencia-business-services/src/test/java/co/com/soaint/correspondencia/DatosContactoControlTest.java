package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import org.junit.Test;
import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
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

import static org.junit.Assert.*;

public class DatosContactoControlTest {


    @Rule
    public ExpectedException fails = ExpectedException.none();

    final BigInteger ID_AGENTE = BigInteger.valueOf(100);
    final String COD_TIP_AGENT = "TP-AGEE";
    final String COD_TIPO_REMITE = "EXT";


    DatosContactoControl datosContactoControl;

    // Dependencias
    EntityManager em;
    DepartamentoControl departamentoControl;
    MunicipioControl municipioControl;
    PaisControl paisControl;
    ConstantesControl constantesControl;


    @Before
    public void setUp() throws Exception {

        datosContactoControl = new DatosContactoControl();

        em = mock(EntityManager.class);
        departamentoControl = mock(DepartamentoControl.class);
        municipioControl = mock(MunicipioControl.class);
        paisControl = mock(PaisControl.class);
        constantesControl = mock(ConstantesControl.class);


        ReflectionTestUtils.setField(datosContactoControl, "em", em);
        ReflectionTestUtils.setField(datosContactoControl, "departamentoControl", departamentoControl);
        ReflectionTestUtils.setField(datosContactoControl, "municipioControl", municipioControl);
        ReflectionTestUtils.setField(datosContactoControl, "paisControl", paisControl);
        ReflectionTestUtils.setField(datosContactoControl, "constantesControl", constantesControl);
    }


        private <T> TypedQuery<T> getSingleResultMock (String namedQuery, Class < T > type, T dummyObject){
            TypedQuery<T> typedQuery = mock(TypedQuery.class);
            when(em.createNamedQuery(namedQuery, type)).thenReturn(typedQuery);
            when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
            when(typedQuery.getSingleResult()).thenReturn(dummyObject);
            return typedQuery;
        }
        private <T> TypedQuery<T> getResultListMock (String namedQuery, Class < T > type, List<T> list){
            TypedQuery<T> typedQuery = mock(TypedQuery.class);
            when(em.createNamedQuery(namedQuery, type)).thenReturn(typedQuery);
            when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(list);
            return typedQuery;
        }

        private <T> TypedQuery<T> getExecuteUpdateMock (String namedQuery, Class < T > type){
            TypedQuery<T> typedQuery = mock(TypedQuery.class);
            if (type == null)
                when(em.createNamedQuery(namedQuery)).thenReturn(typedQuery);
            else
                when(em.createNamedQuery(namedQuery, type)).thenReturn(typedQuery);
            when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
            return typedQuery;
        }


        @Test
        public void consultarDatosContactoByAgentes () throws Exception {

            //given
            List<DatosContactoDTO> datosContactoDTOListResult = new ArrayList<DatosContactoDTO>();
            List<DatosContactoDTO> datosContactoDTO = new ArrayList<DatosContactoDTO>();

            //asig
            List<AgenteDTO> agenteDTOList = new ArrayList<AgenteDTO>();

            AgenteDTO agenteDTO = AgenteDTO.newInstance().ideAgente(ID_AGENTE).codTipAgent(COD_TIP_AGENT).codTipoRemite(COD_TIPO_REMITE).build();
            agenteDTOList.add(agenteDTO);
            TypedQuery<DatosContactoDTO> datosContactoDTOTypedQuery = getResultListMock("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class, datosContactoDTO);

            //when
            datosContactoDTOListResult = datosContactoControl.consultarDatosContactoByAgentes(agenteDTOList);

            //then

            verify(em).createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class);//se ejecuta
            verify(datosContactoDTOTypedQuery).setParameter("IDE_AGENTE", ID_AGENTE);//con parametros

        }
        @Test
        public void consultarDatosContactoByAgentes_Fail () throws SystemException {

            List<AgenteDTO> agenteDTOList = new ArrayList<AgenteDTO>();

            AgenteDTO agenteDTO = AgenteDTO.newInstance().build();
            agenteDTOList.add(agenteDTO);

            when(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)).thenThrow(AssertionError.class);

           /* fails.expect(SystemException.class);
            fails.expectMessage("system.generic.error");*/

            datosContactoControl.consultarDatosContactoByAgentes(agenteDTOList);
        }

        @Test
        public void consultarDatosContactoByAgentesCorreo () throws Exception {
            //given
            AgenteDTO agenteDTO= AgenteDTO.newInstance().ideAgente(ID_AGENTE).build();
            List<DatosContactoDTO> datosContactoDTOListResult=new ArrayList<DatosContactoDTO>();
            List<DatosContactoDTO> datosContactoDTO=new ArrayList<DatosContactoDTO>();

            //asig

            TypedQuery<DatosContactoDTO> datosContactoDTOTypedQuery = getResultListMock("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class, datosContactoDTO);

            //when
            datosContactoDTOListResult = datosContactoControl.consultarDatosContactoByAgentesCorreo(agenteDTO);

            //then

            verify(em).createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class);//se ejecuta
            verify(datosContactoDTOTypedQuery).setParameter("IDE_AGENTE", ID_AGENTE);//con parametros
        }
    @Test
    public void consultarDatosContactoByAgentesCorreo_Fail () throws SystemException {

        AgenteDTO agenteDTO= AgenteDTO.newInstance().ideAgente(ID_AGENTE).build();

        when(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)).thenThrow(SystemException.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        datosContactoControl.consultarDatosContactoByAgentesCorreo(agenteDTO);
    }

        @Test
        public void datosContactoTransformToFull () throws Exception {
            //given
            DatosContactoDTO datosContactoDTO= DatosContactoDTO.newInstance().
                    ideContacto(BigInteger.valueOf(337))
                    .codPais("CO")
                    .build();
            DatosContactoFullDTO datosContactoFullDTO= DatosContactoFullDTO.newInstance().build();

            //when
            datosContactoFullDTO= datosContactoControl.datosContactoTransformToFull(datosContactoDTO);

            //then
            assertNotNull(datosContactoFullDTO);
            assertEquals(datosContactoFullDTO.getIdeContacto(),BigInteger.valueOf(337));

        }
        @Test
        public void datosContactoTransformToFull_Fail () throws SystemException, BusinessException {

            DatosContactoDTO datosContactoDTO = DatosContactoDTO.newInstance().build();
            DatosContactoFullDTO datosContactoFullDTO = DatosContactoFullDTO.newInstance().build();

            datosContactoFullDTO = datosContactoControl.datosContactoTransformToFull(datosContactoDTO);

            //then
            assertNull(datosContactoFullDTO.getIdeContacto());
        }
        @Test
        public void datosContactoListTransformToFull () throws Exception {
        List<DatosContactoDTO> datosContactoDTOList =new ArrayList<DatosContactoDTO>();
            DatosContactoDTO datosContactoDTO= DatosContactoDTO.newInstance().
                    ideContacto(BigInteger.valueOf(337))
                    .codPais("CO")
                    .codDepartamento("11")
                    .codMunicipio("11001")
                    .build();
        datosContactoDTOList.add(datosContactoDTO);
        List<DatosContactoFullDTO> datosContactoFullDTOList=datosContactoControl.datosContactoListTransformToFull(datosContactoDTOList);

        assertNotNull(datosContactoFullDTOList);
        assertEquals(datosContactoDTOList.size(),1);
        }

        @Test
        public void datosContactoListTransformToFull_Null () throws Exception {
            List<DatosContactoDTO> datosContactoDTOList =new ArrayList<DatosContactoDTO>();
            DatosContactoDTO datosContactoDTO= DatosContactoDTO.newInstance()
                    .build();
            datosContactoDTOList.add(datosContactoDTO);
            List<DatosContactoFullDTO> datosContactoFullDTOList=datosContactoControl.datosContactoListTransformToFull(datosContactoDTOList);

            assertNotNull(datosContactoFullDTOList);
            assertEquals(datosContactoDTOList.size(),1);

        }

        @Test
        public void consultarDatosContactoFullByAgentes () throws Exception {

            //given
            AgenteFullDTO agenteDTO = AgenteFullDTO.newInstance().ideAgente(ID_AGENTE).codTipAgent(COD_TIP_AGENT).codTipoRemite(COD_TIPO_REMITE).build();
            List<AgenteFullDTO> agenteDTOList=new ArrayList<AgenteFullDTO>();
            List<DatosContactoFullDTO> datosContactoDTOListResult=new ArrayList<DatosContactoFullDTO>();
            List<DatosContactoDTO> datosContactoDTOList=new ArrayList<DatosContactoDTO>();
            agenteDTOList.add(agenteDTO);

            //asig

            TypedQuery<DatosContactoDTO> datosContactoDTOTypedQuery = getResultListMock("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class, datosContactoDTOList);

            //when
            datosContactoDTOListResult = datosContactoControl.consultarDatosContactoFullByAgentes(agenteDTOList);

            //then

            verify(em).createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class);//se ejecuta
            verify(datosContactoDTOTypedQuery).setParameter("IDE_AGENTE", ID_AGENTE);//con parametros
        }
    @Test
    public void consultarDatosContactoFullByAgentes_Fail () throws SystemException, BusinessException {

        AgenteFullDTO agenteDTO = AgenteFullDTO.newInstance().ideAgente(ID_AGENTE).codTipAgent(COD_TIP_AGENT).codTipoRemite(COD_TIPO_REMITE).build();
        List<AgenteFullDTO> agenteDTOList = new ArrayList<AgenteFullDTO>();
        agenteDTOList.add(agenteDTO);

        when(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)).thenThrow(BusinessException.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        datosContactoControl.consultarDatosContactoFullByAgentes(agenteDTOList);
    }




        @Test
        public void consultarDatosContactoByIdAgente () throws Exception {
            //given

            List<DatosContactoDTO>datosContactoDTOListResult=new ArrayList<DatosContactoDTO>();
            List<DatosContactoDTO> datosContactoDTO=new ArrayList<DatosContactoDTO>();

            //asig

            TypedQuery<DatosContactoDTO> datosContactoDTOTypedQuery = getResultListMock("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class, datosContactoDTO);

            //when
            datosContactoDTOListResult = datosContactoControl.consultarDatosContactoByIdAgente(ID_AGENTE);

            //then

            verify(em).createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class);//se ejecuta
            verify(datosContactoDTOTypedQuery).setParameter("IDE_AGENTE", ID_AGENTE);//con parametros
        }
      @Test
        public void consultarDatosContactoByIdAgente_Fail () throws SystemException {


          when(em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)).thenThrow(SystemException.class);

          fails.expect(SystemException.class);
          fails.expectMessage("system.generic.error");

          datosContactoControl.consultarDatosContactoByIdAgente(ID_AGENTE);
      }



        @Test
        public void datosContactoTransform () throws Exception {
            DatosContactoDTO datosContactoDTO= DatosContactoDTO.newInstance().
                    ideContacto(BigInteger.valueOf(337))
                    .codPais("CO")
                    .build();
            TvsDatosContacto tvsDatosContacto=datosContactoControl.datosContactoTransform(datosContactoDTO);

            assertNotNull(tvsDatosContacto);
        }

    }

