package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
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
public class GestionarConstantes {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarConstantes.class.getName());

    @PersistenceContext
    private EntityManager em;


    // ----------------------

    public GestionarConstantes(){super();}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByEstado(String estado){

        List<ConstanteDTO> constantes = new ArrayList<>();
        em.createNamedQuery("TvsConstantes.findAll", ConstanteDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream()
                .forEach((constante) -> {
                    ConstanteDTO constanteDTO = ConstanteDTO.newInstance()
                            .ideConst(constante.getIdeConst())
                            .codigo(constante.getCodigo())
                            .nombre(constante.getNombre())
                            .codPadre(constante.getCodPadre())
                            .build();
                    constantes.add(constanteDTO);
                });
        return constantes;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodigoAndEstado(String codigo, String estado){
        List<ConstanteDTO> constantes = new ArrayList<>();
        em.createNamedQuery("TvsConstantes.findAllByCodigoAndEstado", ConstanteDTO.class)
                .setParameter("CODIGO", codigo)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream()
                .forEach((constante) -> {
                    ConstanteDTO constanteDTO = ConstanteDTO.newInstance()
                            .ideConst(constante.getIdeConst())
                            .codigo(constante.getCodigo())
                            .nombre(constante.getNombre())
                            .codPadre(constante.getCodPadre())
                            .build();
                    constantes.add(constanteDTO);
                });
        return constantes;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodPadreAndEstado(String codPadre, String estado){
        List<ConstanteDTO> constantes = new ArrayList<>();
        em.createNamedQuery("TvsConstantes.findAllByCodPadreAndEstado", ConstanteDTO.class)
                .setParameter("COD_PADRE", codPadre)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream()
                .forEach((constante) -> {
                    ConstanteDTO constanteDTO = ConstanteDTO.newInstance()
                            .ideConst(constante.getIdeConst())
                            .codigo(constante.getCodigo())
                            .nombre(constante.getNombre())
                            .codPadre(constante.getCodPadre())
                            .build();
                    constantes.add(constanteDTO);
                });
        return constantes;
    }
}
