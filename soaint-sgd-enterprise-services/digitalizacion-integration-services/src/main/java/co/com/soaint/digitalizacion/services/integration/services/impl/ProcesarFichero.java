package co.com.soaint.digitalizacion.services.integration.services.impl;

import co.com.soaint.digitalizacion.services.integration.services.IProcesarFichero;
import co.com.soaint.digitalizacion.services.util.SystemParameters;
import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by amartinez on 01/03/2018.
 */
@Service
@Log4j2
public class ProcesarFichero implements IProcesarFichero{

    public ProcesarFichero() {
    }
    @Override
    public String getImgText(String imagen){

        Tesseract instance = new Tesseract ();


        instance.setLanguage("eng");
        try {
            String imgText = instance.doOCR(new File("C:/testDoc/test-image.png"));
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
            Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
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
            }finally {
                log.info("Barcode text is " + barCode);
                  barCodeInputStream.close();
                log.info("**** Finalizar obtenerCodigoBarra ****");
            }
        return barCode;
    }
    @Override
    public void leerDirectorioEvento() throws IOException, FormatException, ChecksumException {
        log.info("Iniciar Leer directorio");
        Path directoryToWatch = Paths.get(SystemParameters.getParameter(SystemParameters.DIR_PROCESAR));
        if (directoryToWatch == null) {
            throw new UnsupportedOperationException("Directory not found");
        }
        WatchService watchService = directoryToWatch.getFileSystem().newWatchService();
        directoryToWatch.register(watchService, new WatchEvent.Kind[] {StandardWatchEventKinds.ENTRY_CREATE});
        try {
            WatchKey key = watchService.take();
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    String eventKind = event.kind().toString();
                    String file = event.context().toString();
                    log.info("Event : " + eventKind + " in File " +  file);
                                }
                key.reset();
                key = watchService.take();
            }
        } catch (InterruptedException e) {
            log.error("Error en operarcion leer fichero".concat(e.getMessage()));
            throw new RuntimeException(e);
        }finally {
            log.info("Finalizar leer directorio");
        }
    }

    @Override
    public void leerDirectorio(String filePathOrigen, String filePathDestino) throws IOException, FormatException, ChecksumException {
        log.info("Iniciar Leer directorio");
       File folder = new File(filePathOrigen);
        try {
        if(folder.listFiles().length > 0 ){
            Image image = new Image(ImageDataFactory.create(filePathOrigen.concat(folder.list()[0])));
            String barCode = obtenerCodigoBarra(folder.listFiles()[0]);
            Document doc;
            try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filePathDestino.concat(barCode).concat(".pdf")))) {
                doc = new Document(pdfDoc, new PageSize(595.0F, 842.0F));
                for (File fileEntry : folder.listFiles()) {
                    log.info(fileEntry.getName());
                    //String barCode = obtenerCodigoBarra(fileEntry);
                    image = new Image(ImageDataFactory.create(filePathOrigen.concat(fileEntry.getName())));
                    pdfDoc.addNewPage(new PageSize(595.0F, 842.0F));
                    doc.add(image);
                    Files.move(Paths.get(filePathOrigen.concat(fileEntry.getName())), Paths.get(filePathDestino.concat(fileEntry.getName())), StandardCopyOption.REPLACE_EXISTING);
                }
                pdfDoc.close();
            }
            doc.close();

        }
        }catch (Exception e){
            log.error("*** ERROR ****".concat(e.getMessage()));

        } finally {

            log.info("Finalizar leer directorio");

        }


    }



}
