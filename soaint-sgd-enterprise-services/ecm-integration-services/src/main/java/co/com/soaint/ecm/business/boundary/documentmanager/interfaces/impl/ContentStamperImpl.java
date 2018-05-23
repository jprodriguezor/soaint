package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.Charset;

@Log4j2
@BusinessControl
public final class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    private ContentStamperImpl() {}

    @Override
    public byte[] getStampedDocument(byte[] stamperImg, byte[] htmlBytes) throws SystemException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            final Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            final Charset UTF8_CHARSET = Charset.forName("UTF-8");
            final String htmlCad = new String(htmlBytes, UTF8_CHARSET);
            final InputStream inputStream = new ByteArrayInputStream((top() + htmlCad + bottom()).getBytes(UTF8_CHARSET));
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);

            Image image = Image.getInstance(stamperImg);
            image.setAbsolutePosition(470F, 770F);
            document.add(image);
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
                "<div style=\"font-size: 9px\">";
    }

    private String bottom() {
        return "</div>\n" +
                "</body>\n" +
                "</html>";
    }
}
