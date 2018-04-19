package co.com.soaint.digitalizacion.services.integration.services.impl;

import co.com.soaint.digitalizacion.services.apis.delegator.MensajeriaIntegrationClient;
import co.com.soaint.digitalizacion.services.apis.delegator.RadicacionFacturaElectronicaClient;
import co.com.soaint.digitalizacion.services.integration.services.IProcesarFichero;
import co.com.soaint.digitalizacion.services.util.SystemParameters;
import co.com.soaint.foundation.canonical.digitalizar.MensajeGenericoDigitalizarDTO;
import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amartinez on 01/03/2018.
 */
@Component("procesarFichero")
@Log4j2
@NoArgsConstructor
public class ProcesarFichero implements IProcesarFichero {
    @Autowired
    private MensajeriaIntegrationClient apiClienteMensajeria;
    @Autowired
    private RadicacionFacturaElectronicaClient apiClienteFactura;

    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Override
    public String getImgText(String imagen) {
        Tesseract instance = new Tesseract();
        instance.setLanguage("eng");
        try {
            String imgText = instance.doOCR(new File(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR)));
            return imgText;
        } catch (TesseractException e) {
            e.getMessage();
            return "Error en leer texto";
        }
    }

    @Override
    public String obtenerCodigoBarra(File fileEntry) throws IOException, FormatException, ChecksumException {
        Result result = null;
        log.info("**** Iniciar obtenerCodigoBarra ****");
        String barCode = "";
        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.ALLOWED_LENGTHS, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        InputStream barCodeInputStream = new FileInputStream(fileEntry);
        BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        try {
            result = reader.decode(bitmap, tmpHintsMap);
            barCode = result.getText();
        } catch (NotFoundException e) {
            log.error("No se encontro codigo de barra".concat(e.getMessage()));
            barCode = "No trae barCode";
        } finally {
            log.info("Barcode text is " + barCode);
            barCodeInputStream.close();
            log.info("**** Finalizar obtenerCodigoBarra ****");
        }
        return barCode;
    }

    @Override
    public void leerDirectorioEvento(String origen, String destino) throws IOException, FormatException, ChecksumException, SystemException {
        log.info("Iniciar Leer directorio");
        Map<String, Object> data = new HashMap<>();
        File folder = new File(origen);
        if (folder.listFiles().length > 0) {
            for (File fileEntry : folder.listFiles()) {
                FileInputStream xml = new FileInputStream(fileEntry);
                byte[] bytes = IOUtils.toByteArray(xml);
                String encodedFile = Base64.getEncoder().encodeToString(bytes);
                data.put("base64", encodedFile);
                Files.move(Paths.get(origen.concat(fileEntry.getName())), Paths.get(destino.concat(fileEntry.getName())), StandardCopyOption.REPLACE_EXISTING);
            }
            apiClienteFactura.radicarFactura(data);
        }


    }

    @Override
    public MensajeGenericoDigitalizarDTO leerDirectorio(MensajeGenericoDigitalizarDTO entradaDigitalizar) throws SystemException {
        log.info("Iniciar Leer directorio");
        File folder = new File(entradaDigitalizar.getDirProcesar());
        Map<String, Object> data = new HashMap<>();
        try {
            if (folder.listFiles().length > 0) {
                String barCode = obtenerCodigoBarra(folder.listFiles()[0]);
              //  String encodedFile = generarPDFBase64(entradaDigitalizar, folder);
                String encodedFile = generarPDFFileSystem(entradaDigitalizar,barCode, folder);
                data.put("nroRadicado", barCode);
                data.put("fileName", barCode + ".pdf");
                data.put("fileType", "application/pdf");
                data.put("encodedFile", encodedFile);
                apiClienteMensajeria.digitalizarDocumento(MensajeGenericoQueueDTO.newInstance()
                        .colaEntrada("simple.queue.name")
                        .operacion("digitalizar-documento")
                        .mensajeData(data)
                        .build());
            }
            return MensajeGenericoDigitalizarDTO.newInstance()
                    .mensajeData(data)
                    .build();
        } catch (Exception e) {

            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("Finalizar leer directorio");

        }
    }

    public String generarPDFBase64(MensajeGenericoDigitalizarDTO entradaDigitalizar, File carpeta) throws SystemException {
        Document doc = null;
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baosPDF)))
        //PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filePathDestino.concat(barCode).concat(".pdf")))
        {
            doc = new Document(pdfDoc, new PageSize(595.0F, 842.0F));
            for (File fileEntry : carpeta.listFiles()) {
                log.info(fileEntry.getName());
                Image image = new Image(ImageDataFactory.create(entradaDigitalizar.getDirProcesar().concat(fileEntry.getName())));
                pdfDoc.addNewPage(new PageSize(595.0F, 842.0F));
                doc.add(image);
                Files.move(Paths.get(entradaDigitalizar.getDirProcesar().concat(fileEntry.getName())), Paths.get(entradaDigitalizar.getDirProcesados().concat(fileEntry.getName())), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        }
        byte[] bytes = baosPDF.toByteArray();
        String encodedFile = Base64.getEncoder().encodeToString(bytes);
        doc.close();
        return encodedFile;

    }

    public String generarPDFFileSystem(MensajeGenericoDigitalizarDTO entradaDigitalizar,String barCode, File carpeta) throws SystemException {
        Document doc = null;
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter("/home/sgd/procesados/".concat(barCode).concat(".pdf"))))
        {
            doc = new Document(pdfDoc, new PageSize(595.0F, 842.0F));
            for (File fileEntry : carpeta.listFiles()) {
                log.info(fileEntry.getName());
                Image image = new Image(ImageDataFactory.create(entradaDigitalizar.getDirProcesar().concat(fileEntry.getName())));
                pdfDoc.addNewPage(new PageSize(595.0F, 842.0F));
                doc.add(image);
                Files.move(Paths.get(entradaDigitalizar.getDirProcesar().concat(fileEntry.getName())), Paths.get(entradaDigitalizar.getDirProcesados().concat(fileEntry.getName())), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        }
        byte[] bytes = baosPDF.toByteArray();
        String encodedFile = Base64.getEncoder().encodeToString(bytes);
        doc.close();
        return encodedFile;

    }



    public void leerDirectorioEventoEscucha() throws IOException, FormatException, ChecksumException, SystemException {
        log.info("Iniciar Leer directorio");
        Path directoryToWatch = Paths.get(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR));
        if (directoryToWatch == null) {
            throw new UnsupportedOperationException("Directory not found");
        }
        WatchService watchService = directoryToWatch.getFileSystem().newWatchService();
        directoryToWatch.register(watchService, (WatchEvent.Kind<?>[]) new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE});
        try {
            WatchKey key = watchService.take();
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    String eventKind = event.kind().toString();
                    String file = event.context().toString();
                    log.info("Event : " + eventKind + " in File " + file);
                }
                key.reset();
                key = watchService.take();
            }
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("Finalizar leer directorio");
        }
    }

}
