package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.TvsMunicipio;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@BusinessBoundary
public class GestionarMunicipio {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarMunicipio.class.getName());

    @PersistenceContext
    private EntityManager em;



    // ----------------------

    public GestionarMunicipio(){super();}

    public List<TvsMunicipio> listarMunicipiosByCodDepar(String codDepar) throws BusinessException, SystemException{
        return em.createNamedQuery("TvsMunicipio.findAllByCodDepar").setParameter("COD_DEPAR", codDepar).getResultList();
    }
}
