package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Log4j2
@BusinessControl
public class ContentStamperImpl implements ContentStamper {

    private static final Long serialVersionUID = 155L;

    private final Rectangle RECTANGLE = new Rectangle(120, 120);


    @Override
    public byte[] getStampedDocument(byte[] stamperImg, byte[] htmlBytes) throws SystemException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            final Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlBytes));

            Image image = Image.getInstance(stamperImg);
            image.scaleToFit(RECTANGLE);
            //image.scalePercent(20);
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
}
