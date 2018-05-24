package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

@Log4j2
@BusinessControl
public final class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    private ContentStamperImpl() {}

    @Override
    public byte[] getStampedDocument(byte[] stamperImg, byte[] htmlBytes) throws SystemException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            final Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            final Charset UTF8_CHARSET = Charset.forName("UTF-8");
            final String htmlCad = new String(htmlBytes, UTF8_CHARSET);
            final InputStream inputStream = new ByteArrayInputStream((top() + htmlCad + bottom()).getBytes(UTF8_CHARSET));

            Image image = Image.getInstance(stamperImg);
            image.setAbsolutePosition(350F, 690F);
            image.scalePercent(40);
            document.add(image);

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private String top() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE html\n" +
                "        PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div style=\"font-size: 10px\">";
    }

    private String bottom() {
        return "</div>\n" +
                "</body>\n" +
                "</html>";
    }
}
