package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class DatosContactoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     * @param agenteDTOList
     * @return
     */
    public List<DatosContactoDTO> consultarDatosContactoByAgentes(List<AgenteDTO> agenteDTOList) throws SystemException {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        try {
            agenteDTOList.stream().forEach(agenteDTO -> {
                if (TipoAgenteEnum.REMITENTE.getCodigo().equals(agenteDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipoRemite())) {
                    em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                            .getResultList()
                            .stream()
                            .forEach(datosContactoDTOList::add);
                }
            });
            return datosContactoDTOList;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param datosContactoDTO
     * @return
     */
    public TvsDatosContacto datosContactoTransform(DatosContactoDTO datosContactoDTO) {
        return TvsDatosContacto.newInstance()
                .ideContacto(datosContactoDTO.getIdeContacto())
                .nroViaGeneradora(datosContactoDTO.getNroViaGeneradora())
                .nroPlaca(datosContactoDTO.getNroPlaca())
                .codTipoVia(datosContactoDTO.getCodTipoVia())
                .codPrefijoCuadrant(datosContactoDTO.getCodPrefijoCuadrant())
                .codPostal(datosContactoDTO.getCodPostal())
                .direccion(datosContactoDTO.getDireccion())
                .celular(datosContactoDTO.getCelular())
                .telFijo(datosContactoDTO.getTelFijo())
                .extension(datosContactoDTO.getExtension())
                .corrElectronico(datosContactoDTO.getCorrElectronico())
                .codPais(datosContactoDTO.getCodPais())
                .codDepartamento(datosContactoDTO.getCodDepartamento())
                .codMunicipio(datosContactoDTO.getCodMunicipio())
                .provEstado(datosContactoDTO.getProvEstado())
                .principal(datosContactoDTO.getPrincipal())
                .ciudad(datosContactoDTO.getCiudad())
                .build();
    }
}
