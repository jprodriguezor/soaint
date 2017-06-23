package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.correspondencia.domain.entity.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 31-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarCorrespondencia {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarCorrespondencia.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    CorrespondenciaControl correspondenciaControl;

    @Autowired
    AgenteControl agenteControl;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    AnexoControl anexoControl;

    @Autowired
    ReferidoControl referidoControl;

    @Autowired
    DatosContactoControl datosContactoControl;

    @Autowired
    GestionarTrazaDocumento gestionarTrazaDocumento;
    // ----------------------

    public GestionarCorrespondencia() {
        super();
    }

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        try {
            comunicacionOficialDTO.getCorrespondencia().setNroRadicado(correspondenciaControl.generarNumeroRadicado(comunicacionOficialDTO.getCorrespondencia()));

            CorCorrespondencia correspondencia = correspondenciaControl.corCorrespondenciaTransform(comunicacionOficialDTO.getCorrespondencia());

            for (AgenteDTO agenteDTO : comunicacionOficialDTO.getAgenteList()) {
                CorAgente corAgente = agenteControl.corAgenteTransform(agenteDTO);
                corAgente.setCorCorrespondencia(correspondencia);

                if (TipoAgenteEnum.EXTERNO.getCodigo().equals(agenteDTO.getCodTipAgent())) {

                    for (DatosContactoDTO datosContactoDTO : comunicacionOficialDTO.getDatosContactoList()) {
                        TvsDatosContacto datosContacto = datosContactoControl.datosContactoTransform(datosContactoDTO);
                        datosContacto.setCorAgente(corAgente);
                        corAgente.getTvsDatosContactoList().add(datosContacto);
                    }
                }

                correspondencia.getCorAgenteList().add(corAgente);
            }


            PpdDocumento ppdDocumento = ppdDocumentoControl.ppdDocumentoTransform(comunicacionOficialDTO.getPpdDocumento());
            ppdDocumento.setCorCorrespondencia(correspondencia);
            ppdDocumento.setCorAnexoList(new ArrayList<>());
            comunicacionOficialDTO.getAnexoList().stream().forEach((anexoDTO) -> {
                CorAnexo corAnexo = anexoControl.corAnexoTransform(anexoDTO);
                corAnexo.setPpdDocumento(ppdDocumento);
                ppdDocumento.getCorAnexoList().add(corAnexo);
            });
            correspondencia.getPpdDocumentoList().add(ppdDocumento);

            comunicacionOficialDTO.getReferidoList().stream().forEach((referidoDTO) -> {
                CorReferido corReferido = referidoControl.corReferidoTransform(referidoDTO);
                corReferido.setCorCorrespondencia(correspondencia);
                correspondencia.getCorReferidoList().add(corReferido);
            });

            em.persist(correspondencia);
            em.flush();

            ComunicacionOficialDTO comunicacionOficial = listarCorrespondenciaByNroRadicado(correspondencia.getNroRadicado());

            new Thread(() -> {
                Date fecha = new Date();
                try {
                    gestionarTrazaDocumento.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                            .fecTrazDocumento(fecha)
                            .ideDocumento(comunicacionOficial.getCorrespondencia().getIdeDocumento())
                            .observacion("")
                            .ideFunci(null)
                            .codEstado(comunicacionOficial.getCorrespondencia().getCodEstado())
                            .codOrgaAdmin(null)
                            .build());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }).start();

            return comunicacionOficial;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            CorrespondenciaDTO correspondenciaDTO = em.createNamedQuery("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getSingleResult();

            List<AgenteDTO> agenteDTOList = em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getResultList();

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

            PpdDocumentoDTO ppdDocumentoDTO = em.createNamedQuery("PpdDocumento.findByIdeDocumento", PpdDocumentoDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getSingleResult();


            List<AnexoDTO> anexoList = em.createNamedQuery("CorAnexo.findByIdePpdDocumento", AnexoDTO.class)
                    .setParameter("IDE_PPD_DOCUMENTO", ppdDocumentoDTO.getIdePpdDocumento())
                    .getResultList();

            List<ReferidoDTO> referidoList = em.createNamedQuery("CorReferido.findByIdeDocumento", ReferidoDTO.class)
                    .setParameter("IDE_DOCUMENTO", correspondenciaDTO.getIdeDocumento())
                    .getResultList();

            return ComunicacionOficialDTO.newInstance()
                    .correspondencia(correspondenciaDTO)
                    .agenteList(agenteDTOList)
                    .ppdDocumento(ppdDocumentoDTO)
                    .anexoList(anexoList)
                    .referidoList(referidoList)
                    .datosContactoList(datosContactoDTOList)
                    .build();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        try {
            if (!correspondenciaControl.verificarByNroRadicado(correspondenciaDTO.getNroRadicado())) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.correspondencia_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorCorrespondencia.updateEstado")
                    .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                    .setParameter("COD_ESTADO", correspondenciaDTO.getCodEstado())
                    .executeUpdate();

            new Thread(() -> {
                Date fecha = new Date();
                try {
                    BigInteger ideDocumento = em.createNamedQuery("CorCorrespondencia.findIdeDocumentoByNroRadicado", BigInteger.class)
                            .setParameter("NRO_RADICADO", correspondenciaDTO.getNroRadicado())
                            .getSingleResult();
                    gestionarTrazaDocumento.generarTrazaDocumento(PpdTrazDocumentoDTO.newInstance()
                            .fecTrazDocumento(fecha)
                            .ideDocumento(ideDocumento)
                            .observacion("Cambio de estado de documento")
                            .ideFunci(null)
                            .codEstado(correspondenciaDTO.getCodEstado())
                            .codOrgaAdmin(null)
                            .build());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }).start();

        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
