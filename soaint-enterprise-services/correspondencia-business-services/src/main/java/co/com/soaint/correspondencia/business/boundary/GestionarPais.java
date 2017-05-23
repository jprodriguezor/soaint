package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.TvsPais;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jrodriguez on 12/05/2017.
 */
@BusinessBoundary
public class GestionarPais {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarPais.class.getName());

    @PersistenceContext
    private EntityManager em;



    // ----------------------

    public GestionarPais() {
        super();
    }

    public List<TvsPais> listarPaisesByEstado() {
        return em.createNamedQuery("TvsPais.findAll").getResultList();
    }

}
