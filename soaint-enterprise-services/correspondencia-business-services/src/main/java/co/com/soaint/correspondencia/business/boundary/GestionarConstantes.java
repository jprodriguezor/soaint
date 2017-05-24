package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@BusinessBoundary
public class GestionarConstantes {
    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarConstantes.class.getName());

    @PersistenceContext
    private EntityManager em;



    // ----------------------

    public GestionarConstantes(){super();}

    public List<TvsConstantes> listarConstantesByEstado(){
        return em.createNamedQuery("TvsConstantes.findAll").getResultList();
    }

    public List<TvsConstantes> listarConstantesByCodigoAndEstado(String codigo){
        return em.createNamedQuery("TvsConstantes.findAllByCodigo").setParameter("CODIGO", codigo).getResultList();
    }
}
