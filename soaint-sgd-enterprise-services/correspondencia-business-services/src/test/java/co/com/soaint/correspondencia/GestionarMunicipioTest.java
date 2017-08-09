package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;


import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Jorge on 07/06/2017.
 */
public class GestionarMunicipioTest extends  JPAHibernateTest{


    @Test
    public void listarMunicipiosByCodDeparAndEstado_success() {

    }


    @Test
    public void listarMunicipiosByEstado_success() {
        String estado = "ACTIVO";

        List<MunicipioDTO> listado = em.createNamedQuery("TvsMunicipio.findAll", MunicipioDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();
        Assert.assertNotNull("La lista no debe ser nula", listado);

        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(2));


    }
}
