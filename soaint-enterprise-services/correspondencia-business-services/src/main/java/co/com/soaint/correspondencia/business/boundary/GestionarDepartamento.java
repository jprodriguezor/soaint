package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
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
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Generic Artifact
 * Created: 25-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarDepartamento {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarDepartamento.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    public GestionarDepartamento(){super();}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByEstado(String estado) throws BusinessException, SystemException{
        List<DepartamentoDTO> departamentos = new ArrayList<>();
        em.createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream().forEach((departamento)->{
            DepartamentoDTO departamentoDTO = DepartamentoDTO.newInstance()
                    .ideDepar(departamento.getIdeDepar())
                    .nombreDepar(departamento.getNombreDepar())
                    .codDepar(departamento.getCodDepar())
                    .codPais(departamento.getCodPais())
                    .build();
            departamentos.add(departamentoDTO);
        });
        return departamentos;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByCodPaisAndEstado(String codPais, String estado) throws BusinessException, SystemException{
        List<DepartamentoDTO> departamentos = new ArrayList<>();
        em.createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class)
                .setParameter("COD_PAIS", codPais)
                .setParameter("ESTADO", estado)
                .getResultList()
                .stream().forEach((departamento)->{
            DepartamentoDTO departamentoDTO = DepartamentoDTO.newInstance()
                    .ideDepar(departamento.getIdeDepar())
                    .nombreDepar(departamento.getNombreDepar())
                    .codDepar(departamento.getCodDepar())
                    .codPais(departamento.getCodPais())
                    .build();
            departamentos.add(departamentoDTO);
        });
        return departamentos;
    }
}
