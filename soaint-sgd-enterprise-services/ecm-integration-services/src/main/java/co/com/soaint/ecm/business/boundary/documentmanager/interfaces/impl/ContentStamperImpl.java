package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.ecm.domain.entity.DocumentMimeType;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Log4j2
@Service
public final class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    @Override
    public byte[] getStampedDocument(final byte[] stamperImg, byte[] contentBytes, DocumentMimeType mimeType) throws SystemException {
        log.info("Ejecutando el metodo que estampa una imagen en un documento HTML y luego lo convierte a PDF");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            if (mimeType == DocumentMimeType.APPLICATION_HTML) {
                final Charset UTF8_CHARSET = Charset.forName("UTF-8");
                final String htmlCad = new String(contentBytes, UTF8_CHARSET);
                contentBytes = (top() + htmlCad + bottom()).getBytes(UTF8_CHARSET);
                contentBytes = convertHtmlToPdf(contentBytes);
            }
            PdfReader reader = new PdfReader(contentBytes);
            PdfStamper stamper = new PdfStamper(reader, out);
            Image image = Image.getInstance(stamperImg);
            PdfImage stream = new PdfImage(image, "", null);
            PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
            image.setDirectReference(ref.getIndirectReference());
            image.setAbsolutePosition(380F, 650F);
            image.scalePercent(40);
            PdfContentByte over = stamper.getOverContent(1);
            over.addImage(image);
            stamper.flush();
            stamper.close();
            reader.close();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private byte[] convertHtmlToPdf(byte[] htmlBytes) throws ParserConfigurationException, IOException, SAXException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        final InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        renderer.setScaleToFit(true);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        InputSource is = new InputSource(inputStream);
        org.w3c.dom.Document doc = builder.parse(is);

        renderer.setDocument(doc, null);
        renderer.layout();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            renderer.createPDF(out);
            inputStream.close();
            return out.toByteArray();
        }
    }

    private String top() {
        return "<!DOCTYPE html\n" +
                "        PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>" +
                "<div style=\"font-size: 11px\">";
    }

    private String bottom() {
        return "</div>\n" +
                "</body>\n" +
                "</html>";
    }
}