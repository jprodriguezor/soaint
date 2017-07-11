package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DctAsigUltimoControl;
import co.com.soaint.correspondencia.business.control.DctAsignacionControl;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarAsignacion {
    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarAsignacion.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DctAsignacionControl dctAsignacionControl;

    @Autowired
    DctAsigUltimoControl dctAsigUltimoControl;
    // ----------------------

    public void asignarCorrespondencia(AsignacionDTO asignacionDTO) throws BusinessException, SystemException {
        try {
            CorAgente corAgente = CorAgente.newInstance()
                    .ideAgente(asignacionDTO.getIdeAgente())
                    .build();
            CorCorrespondencia corCorrespondencia = CorCorrespondencia.newInstance()
                    .ideDocumento(asignacionDTO.getIdeDocumento())
                    .build();

            Date fecha = new Date();
            String usuarioCreo = "0"; //TODO
            Long usuarioCambio = Long.parseLong("0"); //TODO

            DctAsignacion dctAsignacion = dctAsignacionControl.dctAsignacionTransform(asignacionDTO.getDctAsignacion());
            DctAsigUltimo dctAsigUltimo;

            List<DctAsigUltimo> dctAsigUltimoList = em.createNamedQuery("DctAsigUltimo.findByIdeAgente", DctAsigUltimo.class)
                    .setParameter("IDE_AGENTE", asignacionDTO.getIdeAgente())
                    .getResultList();

            if (dctAsigUltimoList.size() > 0){
                dctAsigUltimo = dctAsigUltimoList.get(0);
            }
            else{
                dctAsigUltimo = DctAsigUltimo.newInstance()
                        .ideUsuarioCreo(usuarioCreo)
                        .fecCreo(fecha)
                        .build();
            }

            dctAsigUltimo.setDctAsignacion(dctAsignacion);
            dctAsigUltimo.setCorAgente(corAgente);
            dctAsigUltimo.setCorCorrespondencia(corCorrespondencia);
            dctAsigUltimo.setIdeUsuarioCambio(usuarioCambio);
            dctAsigUltimo.setFecCambio(fecha);

            dctAsignacion.setCorAgente(corAgente);
            dctAsignacion.setCorCorrespondencia(corCorrespondencia);
            dctAsignacion.setIdeUsuarioCreo(usuarioCreo);
            dctAsignacion.setFecCreo(fecha);//TODO

            em.persist(dctAsignacion);
            em.merge(dctAsigUltimo);
            em.flush();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
