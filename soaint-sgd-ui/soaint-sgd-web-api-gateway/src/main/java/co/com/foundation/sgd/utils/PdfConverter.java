package co.com.foundation.sgd.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PdfConverter {
    protected Document document;
    protected String outputFile;
    protected String cssFile;
    protected InputStream inputStream;
    protected InputStream cssInputStream;
    protected OutputStream outputStream;

    public PdfConverter(String html) throws IOException
    {
        this.init();
        this.inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8.name()));
    }

    public PdfConverter(InputStream htmlFile) throws IOException
    {
        this.init();
        this.inputStream = htmlFile;
    }

    public PdfConverter(InputStream htmlFile, InputStream cssFile) throws IOException
    {
        this.init();
        this.inputStream = htmlFile;
        this.cssInputStream = cssFile;
    }

    public void convert() throws DocumentException, IOException {
        File cssFile = new File(this.cssFile);
        if (cssFile.exists()) {
            this.cssInputStream = new FileInputStream(this.cssFile);
        }

        this.outputStream = new FileOutputStream(this.outputFile);
        PdfWriter writer = PdfWriter.getInstance(document, this.outputStream);
        this.document.open();
        if (null != this.cssInputStream) {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, this.inputStream, this.cssInputStream);
        } else {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, this.inputStream);
        }
        document.close();
        System.out.println( "PDF Created!" );
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    private void init() throws IOException
    {
        this.document = new Document();
        this.outputFile = "generatedPdf.pdf";
        this.cssFile = "";
    }
}
