package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.correspondencia.domain.entity.CorPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoPlanillaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.FormatoDocEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 04-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PlanillasControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlanAgenControl planAgenControl;

    @Autowired
    private CorrespondenciaControl correspondenciaControl;

    @Autowired
    private PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    private AgenteControl agenteControl;

    @Autowired
    private ConstantesControl constantesControl;

    @Autowired
    private DependenciaControl dependenciaControl;

    @Autowired
    private FuncionariosControl funcionariosControl;

    @Value("${radicado.reports.path}")
    private String reportsPath;

    @Value("${radicado.reports.logo}")
    private String reportsLogo;

    @Value("${radicado.planilla.distribucion.report}")
    private String planillaDistribucionReport;

    /**
     * @param planilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        try {
            CorPlanillas corPlanillas = corPlanillasTransform(planilla);
            corPlanillas.setFecGeneracion(new Date());
            corPlanillas.setNroPlanilla(generarNumeroPlanilla(corPlanillas.getCodSedeOrigen()));
            corPlanillas.setCorPlanAgenList(new ArrayList<>());
            planilla.getAgentes().getAgente().stream().forEach(planAgenDTO -> {
                CorPlanAgen corPlanAgen = planAgenControl.corPlanAgenTransform(planAgenDTO);
                corPlanAgen.setEstado(EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
                corPlanAgen.setCorPlanillas(corPlanillas);
                corPlanillas.getCorPlanAgenList().add(corPlanAgen);
            });
            em.persist(corPlanillas);
            em.flush();
            return listarPlanillasByNroPlanilla(corPlanillas.getNroPlanilla());
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param planilla
     * @throws SystemException
     */
    public void cargarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        try {
            for (PlanAgenDTO planAgenDTO : planilla.getAgentes().getAgente()) {
                planAgenControl.updateEstadoDistribucion(planAgenDTO);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO listarPlanillasByNroPlanilla(String nroPlanilla) throws BusinessException, SystemException {
        try {
            PlanillaDTO planilla = em.createNamedQuery("CorPlanillas.findByNroPlanilla", PlanillaDTO.class)
                    .setParameter("NRO_PLANILLA", nroPlanilla)
                    .getSingleResult();
            planilla.setAgentes(PlanAgentesDTO.newInstance()
                    .agente(planAgenControl.listarAgentesByIdePlanilla(planilla.getIdePlanilla()))
                    .build());
            return planilla;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("planillas.planilla_not_exist_by_nroPlanilla")
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
     *
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    public ReportDTO exportarPlanilla(String nroPlanilla, String formato) throws SystemException {
        try {
            PlanillaDTO planilla = listarPlanillasByNroPlanilla(nroPlanilla);
            JasperReport report = JasperCompileManager.compileReport(reportsPath + planillaDistribucionReport);
            byte[] bytes;
            if (FormatoDocEnum.PDF.getCodigo().equals(formato)){
                bytes = getPdfReport(report, planilla);
            }
            else
                bytes = getXlsReport(report, planilla);
            return ReportDTO.newInstance()
                    .base64EncodedFile(Base64.getEncoder().encodeToString(bytes))
                            .formato(formato)
                            .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private byte[] getPdfReport(JasperReport report, PlanillaDTO planilla) throws BusinessException, SystemException, IOException {
        try {
            return JasperRunManager.runReportToPdf(report, getReportParameters(planilla), getReportDataSource(planilla));
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private byte[] getXlsReport(JasperReport report, PlanillaDTO planilla) throws BusinessException, SystemException, IOException {
        FileInputStream fileInputStream = null;
        try {
            File fileTmp = File.createTempFile(planilla.getNroPlanilla() + ".xslx", "");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, getReportParameters(planilla), getReportDataSource(planilla));
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileTmp.getPath()));
            SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
            xlsxReportConfiguration.setOnePagePerSheet(false);
            xlsxReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsxReportConfiguration.setDetectCellType(false);
            xlsxReportConfiguration.setWhitePageBackground(false);
            xlsxExporter.setConfiguration(xlsxReportConfiguration);
            xlsxExporter.exportReport();

            fileInputStream = new FileInputStream(fileTmp);

            fileTmp.deleteOnExit();
            return IOUtils.toByteArray(fileInputStream);
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }

    private Map<String, Object> getReportParameters(PlanillaDTO planilla) throws BusinessException, SystemException, IOException {
        DependenciaDTO dependenciaOrigen = dependenciaControl.listarDependenciaByCodigo(planilla.getCodDependenciaOrigen());
        BufferedImage image = ImageIO.read(new FileImageInputStream(new File(reportsPath + reportsLogo)));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lugarAdministrativo", dependenciaOrigen.getNomSede());
        parameters.put("dependenciaDestino", dependenciaControl.listarDependenciaByCodigo(planilla.getCodDependenciaDestino()).getNomDependencia());
        parameters.put("responsable", dependenciaOrigen.getNomDependencia());
        parameters.put("nroPlanilla", planilla.getNroPlanilla());
        parameters.put("fecGeneracion", planilla.getFecGeneracion());
        parameters.put("funcGenera", funcionariosControl.consultarFuncionarioByIdeFunci(BigInteger.valueOf(Long.parseLong(planilla.getCodFuncGenera()))).getNombreCompleto());
        parameters.put("logo", image );
        return parameters;
    }

    private JRBeanCollectionDataSource getReportDataSource(PlanillaDTO planilla) throws BusinessException, SystemException {
        ArrayList<ItemReportPlanillaDTO> itemPlanillaList = new ArrayList<>();
        for (PlanAgenDTO planAgen : planilla.getAgentes().getAgente())
            itemPlanillaList.add(transformToItemReport(planAgen));
        ArrayList<ItemsReportPlanillaDTO> dataSource = new ArrayList();
        dataSource.add(ItemsReportPlanillaDTO.newInstance().itemsPlanilla(itemPlanillaList).build());
        return new JRBeanCollectionDataSource(dataSource, false);
    }

    private ItemReportPlanillaDTO transformToItemReport(PlanAgenDTO planAgen) throws BusinessException, SystemException {
        CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeDocumento(planAgen.getIdeDocumento());
        PpdDocumentoDTO documento = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondencia.getIdeDocumento()).get(0);
        AgenteDTO remitente = agenteControl.listarRemitentesByIdeDocumento(correspondencia.getIdeDocumento()).get(0);
        AgenteDTO destinatario = agenteControl.consultarAgenteByIdeAgente(planAgen.getIdeAgente());
        log.info("Codigo de IndOriginal ------------------- >" + destinatario.getIndOriginal());
        return ItemReportPlanillaDTO.newInstance()
                .nroRadicado(correspondencia.getNroRadicado())
                .fecRadicado(correspondencia.getFecRadicado())
                .indOriginal(constantesControl.consultarConstanteByCodigo(destinatario.getIndOriginal()).getNombre())
                .nroDocumento(remitente.getNroDocuIdentidad())
                .nombreRemitente(remitente.getNombre())
                .dependenciaOrigen(TipoRemitenteEnum.INTERNO.getCodigo().equals(remitente.getCodTipoRemite()) ? dependenciaControl.listarDependenciaByCodigo(remitente.getCodDependencia()).getNomDependencia() : null)
                .asunto(documento.getAsunto())
                .nroFolios(String.valueOf(documento.getNroFolios()))
                .nroAnexos(String.valueOf(documento.getNroAnexos()))
                .build();
    }

    /**
     * @param planilla
     * @return
     */
    public CorPlanillas corPlanillasTransform(PlanillaDTO planilla) {
        return CorPlanillas.newInstance()
                .idePlanilla(planilla.getIdePlanilla())
                .nroPlanilla(planilla.getNroPlanilla())
                .fecGeneracion(planilla.getFecGeneracion())
                .codTipoPlanilla(planilla.getCodTipoPlanilla())
                .codFuncGenera(planilla.getCodFuncGenera())
                .codSedeOrigen(planilla.getCodSedeOrigen())
                .codDependenciaOrigen(planilla.getCodDependenciaOrigen())
                .codSedeDestino(planilla.getCodSedeDestino())
                .codDependenciaDestino(planilla.getCodDependenciaDestino())
                .codClaseEnvio(planilla.getCodClaseEnvio())
                .codModalidadEnvio(planilla.getCodModalidadEnvio())
                .build();
    }

    private String generarNumeroPlanilla(String codSede) {
        String nroPlanilla = em.createNamedQuery("CorPlanillas.findMaxNroPlanillaByCodSede", String.class)
                .setParameter("COD_SEDE", codSede)
                .getSingleResult();
        int consecutivo = 0;
        if (nroPlanilla != null)
            consecutivo = Integer.parseInt(nroPlanilla.substring(nroPlanilla.length() - codSede.length()));
        consecutivo++;
        return conformarNroPlanilla(codSede, consecutivo);
    }

    private String conformarNroPlanilla(String codSede, int consecutivo) {
        String nro = codSede;
        int relleno = 16 - (codSede.length() + String.valueOf(consecutivo).length());
        String formato = "%0" + relleno + "d";
        return nro.concat(String.format(formato, consecutivo));
    }
}
