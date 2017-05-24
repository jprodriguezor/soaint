package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Generic Artifact
 * Created: 23-May-2017
 * Author: jprodriguezor
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByEstado(String estado) {

        List<PaisDTO> paises = new ArrayList<>();
        em.createNamedQuery("TvsPais.findAll", PaisDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream().forEach((pais) -> {
            PaisDTO paisDTO = PaisDTO.newInstance()
                    .id(pais.getId())
                    .nombre(pais.getNombre())
                    .codigo(pais.getCodigo())
                    .build();

            paises.add(paisDTO);
        });
        return paises;
    }

}
