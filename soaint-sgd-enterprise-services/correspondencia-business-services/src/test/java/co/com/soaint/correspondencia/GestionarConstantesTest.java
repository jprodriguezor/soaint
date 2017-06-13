package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by Jorge on 08/06/2017.
 */
public class GestionarConstantesTest extends  JPAHibernateTest  {

    @Test
    public void listarConstantesByEstado_success() {

        String estado = "ACTIVO";
        List<ConstanteDTO> listado = em.createNamedQuery("TvsConstantes.findAll", ConstanteDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();
        Assert.assertNotNull("La lista no debe ser nula", listado);

        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(1));


    }


    @Test
    public void listarConstantesByCodigoAndEstado_success() {

        String codigo = "CODIGO1";
        String estado = "ACTIVO";
        List<ConstanteDTO> listado = em.createNamedQuery("TvsConstantes.findAllByCodigoAndEstado", ConstanteDTO.class)
                .setParameter("CODIGO", codigo)
                .setParameter("ESTADO", estado)
                .getResultList();
        Assert.assertNotNull("La lista no debe ser nula", listado);

        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(1));


    }


    @Test
    public void listarConstantesByCodPadreAndEstado_success() {

        String codPadre = "CODIGOPADRE1";
        String estado = "INACTIVO";
        List<ConstanteDTO> listado = em.createNamedQuery("TvsConstantes.findAllByCodPadreAndEstado", ConstanteDTO.class)
                .setParameter("COD_PADRE", codPadre)
                .setParameter("ESTADO", estado)
                .getResultList();
        assertThat(listado, not(IsEmptyCollection.empty()));
        assertThat(new ArrayList<>(), IsEmptyCollection.empty());
        assertThat(listado, hasSize(1));

    }
}
