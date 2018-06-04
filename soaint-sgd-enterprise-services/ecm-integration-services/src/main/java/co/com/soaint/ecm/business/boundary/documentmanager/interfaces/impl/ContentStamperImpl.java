package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.ecm.domain.entity.DocumentMimeType;
import co.com.soaint.ecm.domain.entity.ImagePositionType;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

@Log4j2
@Getter
@Service("contentStamper")
public final class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    private final ImagePositionType positionType;

    public ContentStamperImpl(@Value("${pdf.image.location}") String imagePosition) {
        this.positionType = ImagePositionType.valueOf(imagePosition.toUpperCase());
    }

    @Override
    public byte[] getStampedDocument(final byte[] stamperImg, byte[] contentBytes, DocumentMimeType mimeType) throws SystemException {
        log.info("Ejecutando el metodo que estampa una imagen en un documento HTML y luego lo convierte a PDF");
        Document document = new Document(PageSize.A4);
        try(final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if (mimeType == DocumentMimeType.APPLICATION_HTML) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                document.open();
                final Charset UTF8_CHARSET = Charset.forName("UTF-8");
                final String htmlCad = new String(contentBytes, UTF8_CHARSET);
                contentBytes = (top() + htmlCad + bottom()).getBytes(UTF8_CHARSET);
                XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
                InputStream is = new ByteArrayInputStream(contentBytes);
                worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
                document.close();
                contentBytes = outputStream.toByteArray();
                System.out.println(Arrays.toString(contentBytes));
                outputStream.flush();
                outputStream.close();
                is.close();
            }

            PdfReader reader = new PdfReader(contentBytes);
            PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);
            Image image = getImage(stamperImg);
            PdfImage stream = new PdfImage(image, "", null);
            PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
            image.setDirectReference(ref.getIndirectReference());
            PdfContentByte over = stamper.getOverContent(1);
            over.addImage(image);
            stamper.flush();
            stamper.close();
            reader.close();
            document.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            log.error("Ocurrio un error al poner la etiqueta en el PDF");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private Image getImage(byte[] imageBytes) throws IOException, BadElementException {
        Image image = Image.getInstance(imageBytes);
        image.setBackgroundColor(BaseColor.ORANGE);
        image.setAbsolutePosition(positionType.getAbsoluteX(), positionType.getAbsoluteY());
        image.scalePercent(40);
        return image;
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