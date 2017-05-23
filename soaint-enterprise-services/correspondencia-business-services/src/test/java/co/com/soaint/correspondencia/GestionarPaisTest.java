package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.domain.entity.TvsPais;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jorge on 15/05/2017.
 */
public class GestionarPaisTest extends  JPAHibernateTest {

    @Test
    public void testTvsPais_findAll_success() {
        String estado = "ACTIVO";
        List<TvsPais> paises = em.createNamedQuery("TvsPais.findAll").getResultList();
        assertEquals(1, paises.size());
        assertEquals("CUBA",paises.get(0).getNombrePais());
    }
}
