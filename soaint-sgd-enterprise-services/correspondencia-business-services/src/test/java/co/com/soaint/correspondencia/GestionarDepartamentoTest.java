package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by Jorge on 07/06/2017.
 */
public class GestionarDepartamentoTest extends  JPAHibernateTest {

    @Test
    public void listarDepartamentosByEstado_success() {
        String estado = "ACTIVO";
        List<DepartamentoDTO> listado = em.createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();
        Assert.assertNotNull("La lista no debe ser nula", listado);

        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(2));

    }

    @Test
    public void listarMunicipiosByCodDeparAndEstado_success() {
        String estado = "ACTIVO";
        String codPais = "CODIGOPAIS1";
        List<DepartamentoDTO> listado = em.createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class)
                .setParameter("COD_PAIS", codPais)
                .setParameter("ESTADO", estado)
                .getResultList();
        Assert.assertNotNull("La lista no debe ser nula", listado);

        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(1));


    }
}
