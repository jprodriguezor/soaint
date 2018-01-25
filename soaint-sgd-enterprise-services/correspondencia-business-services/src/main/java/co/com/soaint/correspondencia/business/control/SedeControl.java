package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.canonical.correspondencia.SedeAdministrativaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 09-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class SedeControl {

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    /**
     * @param codSede
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public SedeAdministrativaDTO consultarSedeByCodSede(String codSede) throws BusinessException, SystemException {
        try {
            return transformToDTO(organigramaAdministrativoControl.consultarElementoByCodOrg(codSede));
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("sede.sede_not_exist_by_codSede")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param organigramaItemDTO
     * @return
     */
    public SedeAdministrativaDTO transformToDTO(OrganigramaItemDTO organigramaItemDTO) {
        return SedeAdministrativaDTO.newInstance()
                .idSedeAdministrativa(organigramaItemDTO.getIdeOrgaAdmin())
                .codSede(organigramaItemDTO.getCodOrg())
                .nomSede(organigramaItemDTO.getNomOrg())
                .build();
    }
}
