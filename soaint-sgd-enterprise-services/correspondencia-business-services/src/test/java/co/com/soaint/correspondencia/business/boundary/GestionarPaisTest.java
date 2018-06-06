package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.JPAHibernateTest;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jorge on 15/05/2017.
 */
public class GestionarPaisTest extends JPAHibernateTest {

    @Test
    public void testTvsPais_findAll_success() {
        String estado = "ACTIVO";
        List<PaisDTO> paises = em.createNamedQuery("TvsPais.findAll").setParameter("ESTADO",estado).getResultList();
        assertEquals(2, paises.size());
        assertEquals("CUBA",paises.get(0).getNombre());
        assertEquals("EEUU",paises.get(1).getNombre());
    }
}
