package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsDatosContacto;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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

    @Autowired
    private DepartamentoControl departamentoControl;

    @Autowired
    private MunicipioControl municipioControl;

    @Autowired
    private PaisControl paisControl;

    @Autowired
    private ConstantesControl constantesControl;

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
     * @param agenteDTOList
     * @return
     */
    public List<DatosContactoDTO> consultarDatosContactoByAgentesCorreo(List<AgenteDTO> agenteDTOList) throws SystemException {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        try {
            agenteDTOList.stream().forEach(agenteDTO -> {
                    em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                            .getResultList()
                            .stream()
                            .forEach(datosContactoDTOList::add);

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
     * @param contactoDTO
     * @return
     */
    public DatosContactoFullDTO datosContactoTransformToFull(DatosContactoDTO contactoDTO) throws SystemException, BusinessException{
        try{
            return DatosContactoFullDTO.newInstance()
                    .celular(contactoDTO.getCelular())
                    .ciudad(contactoDTO.getCiudad())
                    .codDepartamento(contactoDTO.getCodDepartamento())
                    .departamento(departamentoControl.consultarDepartamentoByCod(contactoDTO.getCodDepartamento()))
                    .codMunicipio(contactoDTO.getCodMunicipio())
                    .municipio(municipioControl.consultarMunicipioByCodMunic(contactoDTO.getCodMunicipio()))
                    .codPais(contactoDTO.getCodPais())
                    .pais(paisControl.consultarPaisByCod(contactoDTO.getCodPais()))
                    .codPostal(contactoDTO.getCodPostal())
                    .codPrefijoCuadrant(contactoDTO.getCodPrefijoCuadrant())
                    .codTipoVia(contactoDTO.getCodTipoVia())
                    .corrElectronico(contactoDTO.getCorrElectronico())
                    .descPrefijoCuadrant(contactoDTO.getCodPrefijoCuadrant())
                    .descTipoVia(constantesControl.consultarNombreConstanteByCodigo(contactoDTO.getCodTipoVia()))
                    .descPrefijoCuadrant(constantesControl.consultarNombreConstanteByCodigo(contactoDTO.getCodPrefijoCuadrant()))
                    .direccion(contactoDTO.getDireccion())
                    .extension(contactoDTO.getExtension())
                    .ideContacto(contactoDTO.getIdeContacto())
                    .nroPlaca(contactoDTO.getNroPlaca())
                    .nroViaGeneradora(contactoDTO.getNroViaGeneradora())
                    .principal(contactoDTO.getPrincipal())
                    .provEstado(contactoDTO.getProvEstado())
                    .telFijo(contactoDTO.getTelFijo())
                    .build();
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param datosContactoDTOList
     * @return
     */
    public List<DatosContactoFullDTO> datosContactoListTransformToFull(List<DatosContactoDTO> datosContactoDTOList) throws SystemException, BusinessException {
        List<DatosContactoFullDTO> datosContactoFullDTOList = new ArrayList<>();
        try{
            for (DatosContactoDTO contactoDTO:datosContactoDTOList){
                datosContactoFullDTOList.add(datosContactoTransformToFull(contactoDTO));
            }

            return datosContactoFullDTOList;

            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param agenteDTOList
     * @return
     */
    public List<DatosContactoFullDTO> consultarDatosContactoFullByAgentes(List<AgenteFullDTO> agenteDTOList) throws SystemException {
        List<DatosContactoDTO> datosContactoDTOList = new ArrayList<>();
        try {
            agenteDTOList.stream().forEach(AgenteFullDTO -> {
                if (TipoAgenteEnum.REMITENTE.getCodigo().equals(AgenteFullDTO.getCodTipAgent()) && TipoRemitenteEnum.EXTERNO.getCodigo().equals(AgenteFullDTO.getCodTipoRemite())) {
                    em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", AgenteFullDTO.getIdeAgente())
                            .getResultList()
                            .stream()
                            .forEach(datosContactoDTOList::add);
                }
            });
            return datosContactoListTransformToFull(datosContactoDTOList);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param idAgente
     * @return
     */
    public List<DatosContactoDTO> consultarDatosContactoByIdAgente(BigInteger idAgente) throws SystemException {
        try {
            return em.createNamedQuery("TvsDatosContacto.findByIdeAgente", DatosContactoDTO.class)
                            .setParameter("IDE_AGENTE", idAgente)
                            .getResultList();
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
