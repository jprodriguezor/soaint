package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsTarea;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class TareaControl {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param tarea
     * @throws SystemException
     */
    public void guardarEstadoTarea(TareaDTO tarea)throws SystemException{
        try{
            if(existTareaByIdInstanciaProcesoAndIdTareaProceso(tarea.getIdInstanciaProceso(), tarea.getIdTareaProceso())){
                em.createNamedQuery("TvsTarea.updatePayloadByIdInstanciaProcesoAndIdTareaProceso")
                        .setParameter("ID_INSTANCIA_PROCESO", tarea.getIdInstanciaProceso())
                        .setParameter("ID_TAREA_PROCESO", tarea.getIdTareaProceso())
                        .setParameter("PAYLOAD", serializeObject(tarea.getPayload()))
                        .executeUpdate();
            }
            else {
                em.persist(trasformTvsTarea(tarea));
                em.flush();
            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param idInstanciaProceso
     * @param idTareaProceso
     * @return
     * @throws SystemException
     */
    public TareaDTO listarEstadoTarea(String idInstanciaProceso, String idTareaProceso)throws SystemException{
        try{
            TvsTarea tarea = em.createNamedQuery("TvsTarea.findByIdInstanciaProcesoAndIdTareaProceso", TvsTarea.class)
                    .setParameter("ID_INSTANCIA_PROCESO", idInstanciaProceso)
                    .setParameter("ID_TAREA_PROCESO", idTareaProceso)
                    .getSingleResult();
            return trasformToDto(tarea);
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            return null;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private boolean existTareaByIdInstanciaProcesoAndIdTareaProceso(String idInstanciaProceso, String idTareaProceso)throws SystemException{
        try{
            return em.createNamedQuery("TvsTarea.existByIdInstanciaProcesoAndIdTareaProceso", Long.class)
                    .setParameter("ID_INSTANCIA_PROCESO", idInstanciaProceso)
                    .setParameter("ID_TAREA_PROCESO", idTareaProceso)
                    .getSingleResult() > 0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param tareaDTO
     * @return
     * @throws IOException
     */
    public TvsTarea trasformTvsTarea(TareaDTO tareaDTO)throws IOException{
        return TvsTarea.newInstance()
                .ideTarea(tareaDTO.getIdeTarea())
                .idInstanciaProceso(tareaDTO.getIdInstanciaProceso())
                .idTareaProceso(tareaDTO.getIdTareaProceso())
                .payload(serializeObject(tareaDTO.getPayload()))
                .build();
    }

    /**
     *
     * @param tarea
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public TareaDTO trasformToDto(TvsTarea tarea)throws IOException, ClassNotFoundException{
        return TareaDTO.newInstance()
                .ideTarea(tarea.getIdeTarea())
                .idInstanciaProceso(tarea.getIdInstanciaProceso())
                .idTareaProceso(tarea.getIdTareaProceso())
                .payload(deSerializeObject(tarea.getPayload()))
                .build();
    }

    private byte[] serializeObject(Object object)throws IOException{
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(object);
        os.close();
        return bs.toByteArray();
    }

    private Object deSerializeObject(Serializable data)throws IOException, ClassNotFoundException{
        ByteArrayInputStream in = new ByteArrayInputStream(serializeObject(data));
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
