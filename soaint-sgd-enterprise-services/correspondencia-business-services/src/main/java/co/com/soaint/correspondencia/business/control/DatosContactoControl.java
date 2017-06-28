package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.correspondencia.domain.entity.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

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
public class DatosContactoControl {

    @PersistenceContext
    private EntityManager em;

    public List<DatosContactoDTO> consultarDatosContactoByAgentes(List<AgenteDTO> agenteDTOList) {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        agenteDTOList.stream().forEach((agenteDTO) -> {
            if (TipoAgenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipAgent())) {
                em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                        .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                        .getResultList()
                        .stream()
                        .forEach((datosContactoDTO) -> {
                            datosContactoDTOList.add(datosContactoDTO);
                        });
            }
        });
        return datosContactoDTOList;
    }

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
                .telFijo1(datosContactoDTO.getTelFijo1())
                .telFijo2(datosContactoDTO.getTelFijo2())
                .extension1(datosContactoDTO.getExtension1())
                .extension2(datosContactoDTO.getExtension2())
                .corrElectronico(datosContactoDTO.getCorrElectronico())
                .codPais(datosContactoDTO.getCodPais())
                .codDepartamento(datosContactoDTO.getCodDepartamento())
                .codMunicipio(datosContactoDTO.getCodMunicipio())
                .provEstado(datosContactoDTO.getProvEstado())
                .ciudad(datosContactoDTO.getCiudad())
                .build();
    }
}
