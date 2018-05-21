package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsDserial;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 17-Oct-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class DserialControl {
    @PersistenceContext
    private EntityManager em;

    @Value("${radicado.rango.reservado}")
    private String[] rangoReservado;

    /**
     * @param codSede
     * @param codCmc
     * @param anno
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String consultarConsecutivoRadicadoByCodSedeAndCodCmcAndAnno(String codSede, String codCmc, String anno) throws SystemException {
        try {
            int consecutivoRadicadoReservadoI = Integer.parseInt(this.rangoReservado[0]);
            int consecutivoRadicadoReservadoF = Integer.parseInt(this.rangoReservado[1]);

            List<String> consecutivos = em.createNamedQuery("TvsDserial.consultarConsecutivoRadicado", String.class)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_CMC", codCmc)
                    .setParameter("ANNO", anno)
                    .setParameter("RESERVADO_I", String.valueOf(consecutivoRadicadoReservadoI))
                    .setParameter("RESERVADO_F", String.valueOf(consecutivoRadicadoReservadoF))
                    .getResultList();

            String consecutivoRadicado = null;
            if (consecutivos != null && !consecutivos.isEmpty())
                consecutivoRadicado = consecutivos.get(0);

            if (consecutivoRadicado == null)
                consecutivoRadicado = String.format("%06d", 1);
            else {
                int ultimoConsecutivoGenerado = Integer.parseInt(consecutivoRadicado);
                consecutivoRadicado = String.format("%06d", (ultimoConsecutivoGenerado == consecutivoRadicadoReservadoI - 1 ? consecutivoRadicadoReservadoF : ultimoConsecutivoGenerado) + 1);
            }
            return consecutivoRadicado;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codSede
     * @param codDependencia
     * @param codTipoCmc
     * @param anno
     * @param consecutivoRadicado
     * @param codFuncionarioRadica
     * @throws SystemException
     */
    public void updateConsecutivo(String codSede, String codDependencia, String codTipoCmc, String anno,
                                  String consecutivoRadicado, String codFuncionarioRadica) throws SystemException {
        try {
            em.persist(TvsDserial.newInstance()
                    .codSede(codSede)
                    .codDependencia(codDependencia)
                    .codFuncRadica(codFuncionarioRadica)
                    .codCmc(codTipoCmc)
                    .valConsecutivoRad(consecutivoRadicado)
                    .valAno(anno)
                    .build());
            em.flush();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codSede
     * @param codTipoCmc
     * @param anno
     * @param consecutivoRadicado
     * @throws SystemException
     */
    public Boolean verificarConsecutivo(String codSede, String codTipoCmc, String anno,
                                  String consecutivoRadicado) throws SystemException {
        try {
            return em.createNamedQuery("TvsDserial.consultarConsecutivoExiste", Long.class)
                    .setParameter("COD_SEDE", codSede)
                    .setParameter("COD_CMC", codTipoCmc)
                    .setParameter("ANNO", anno)
                    .setParameter("RESERVADO", consecutivoRadicado)
                    .getSingleResult()>0;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
