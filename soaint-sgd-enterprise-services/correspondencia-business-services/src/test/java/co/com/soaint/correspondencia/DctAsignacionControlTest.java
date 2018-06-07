package co.com.soaint.correspondencia;


import co.com.soaint.correspondencia.business.control.DctAsignacionControl;
import org.junit.Test;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
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

public class DctAsignacionControlTest {

   DctAsignacionControl dctAsignacionControl=new DctAsignacionControl();


    @Test
    public void dctAsignacionTransform() throws Exception {

       DctAsignacion dctAsignacion= dctAsignacionControl.dctAsignacionTransform(AsignacionDTO.newInstance().ideFunci(BigInteger.valueOf(100)).build());

       assertNotNull(dctAsignacion);
       assertEquals(dctAsignacion.getIdeFunci(),BigInteger.valueOf(100));

    }

}