package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.TvsDepartamento;
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
public class GestionarDepartamento {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarDepartamento.class.getName());

    @PersistenceContext
    private EntityManager em;



    // ----------------------

    public GestionarDepartamento(){super();}

    public List<TvsDepartamento> listarDepartamentosByCodPais(String codPais) throws BusinessException, SystemException{
        return em.createNamedQuery("TvsDepartamento.findAllByCodPais").setParameter("COD_PAIS", codPais).getResultList();
    }
}
