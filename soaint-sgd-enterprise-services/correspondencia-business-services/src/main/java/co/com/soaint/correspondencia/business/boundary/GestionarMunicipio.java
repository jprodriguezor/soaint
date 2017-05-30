package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByCodDeparAndEstado(String codDepar, String estado) throws BusinessException, SystemException{
        List<MunicipioDTO> municipios = new ArrayList<>();
        em.createNamedQuery("TvsMunicipio.findAllByCodDeparAndEstado", MunicipioDTO.class)
                .setParameter("COD_DEPAR", codDepar)
                .setParameter("ESTADO", estado)
                .getResultList()
        .stream().forEach((municipio)-> {
        MunicipioDTO municipioDTO = MunicipioDTO.newInstance()
                .ideMunic(municipio.getIdeMunic())
                .nombreMunic(municipio.getNombreMunic())
                .codMunic(municipio.getCodMunic())
                .codDepar(municipio.getCodDepar())
                .build();
            municipios.add(municipioDTO);
        });
        return municipios;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByEstado(String estado) throws BusinessException, SystemException{
        List<MunicipioDTO> municipios = new ArrayList<>();
        em.createNamedQuery("TvsMunicipio.findAll", MunicipioDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream().forEach((municipio)-> {
            MunicipioDTO municipioDTO = MunicipioDTO.newInstance()
                    .ideMunic(municipio.getIdeMunic())
                    .nombreMunic(municipio.getNombreMunic())
                    .codMunic(municipio.getCodMunic())
                    .codDepar(municipio.getCodDepar())
                    .build();
            municipios.add(municipioDTO);
        });
        return municipios;
    }
}
