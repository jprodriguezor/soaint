package co.com.foundation.sgd.utils;

import com.itextpdf.text.DocumentException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfConverterTest {

    private final String HTML_CONTENT = "<!DOCTYPE html><html><body><h1>Hello World</h1></body></html>";
    private final String CSS_CONTENT = "body {margin:0}";
    private final String PDF_LOCATION = System.getProperty("java.io.tmpdir")
            .concat(File.separator)
            .concat("pdfFolder");

    @Test
    public void constructWithHtml() throws IOException {

        // when
        FakePdfConverter fakePdfConverter = new FakePdfConverter(HTML_CONTENT);

        // then
        fakePdfConverter.assertThat(pdfConverter -> {
            assertThat(pdfConverter.document).isNotNull();
            assertThat(pdfConverter.inputStream).isNotNull();
        });
    }

    @Test
    public void constructWithInputStream() throws IOException {

        // given
        InputStream inputStream = new ByteArrayInputStream(HTML_CONTENT.getBytes());

        // when
        FakePdfConverter fakePdfConverter = new FakePdfConverter(inputStream);

        // then
        fakePdfConverter.assertThat(pdfConverter -> {
            assertThat(pdfConverter.document).isNotNull();
            assertThat(pdfConverter.inputStream).isNotNull();
        });
    }

    @Test
    public void constructWithInputStreamAndCssFile() throws IOException {

        // given
        InputStream htmlStream = new ByteArrayInputStream(HTML_CONTENT.getBytes());
        InputStream cssStream = new ByteArrayInputStream(CSS_CONTENT.getBytes());

        // when
        FakePdfConverter fakePdfConverter = new FakePdfConverter(htmlStream, cssStream);

        // then
        fakePdfConverter.assertThat(pdfConverter -> {
            assertThat(pdfConverter.document).isNotNull();
            assertThat(pdfConverter.inputStream).isNotNull();
            assertThat(pdfConverter.cssInputStream).isNotNull();
        });
    }



    @Test
    public void setOutputFile() throws IOException {
        // given
        FakePdfConverter fakePdfConverter = new FakePdfConverter(HTML_CONTENT);
        String PDF_FILE = "file.pdf";

        // when
        fakePdfConverter.setOutputFile(PDF_FILE);

        // then
        fakePdfConverter.assertThat(pdfConverter -> assertThat(pdfConverter.outputFile).isEqualTo(PDF_FILE));
    }

    @Test
    public void setCssFile() throws IOException {

        // given
        FakePdfConverter fakePdfConverter = new FakePdfConverter(HTML_CONTENT);
        String CSS_FILE = "style.css";

        // when
        fakePdfConverter.setCssFile(CSS_FILE);

        // then
        fakePdfConverter.assertThat(pdfConverter -> assertThat(pdfConverter.cssFile).isEqualTo(CSS_FILE));

    }

    @Test
    public void convertPdfWithoutCss() throws IOException, DocumentException {
        // given
        File pdfFolder = new File(PDF_LOCATION);
        if(!pdfFolder.exists())
            pdfFolder.mkdir();

        String PDF_NAME = "file.pdf";
        String PDF_FILE = PDF_LOCATION + File.separator + PDF_NAME;
        FakePdfConverter fakePdfConverter = new FakePdfConverter(HTML_CONTENT);
        fakePdfConverter.setOutputFile(PDF_FILE);

        // when
        fakePdfConverter.convert();

        // then
        File pdfFile = new File(PDF_FILE);
        fakePdfConverter.assertThat(pdfConverter -> assertThat(pdfFile).exists());

        pdfFile.delete();
        pdfFolder.delete();
    }

    @Test
    public void convertPdfWithCss() throws IOException, DocumentException {
        // given
        File pdfFolder = new File(PDF_LOCATION);
        if(!pdfFolder.exists())
            pdfFolder.mkdir();

        String CSS_NAME = "style.css";
        String CSS_FILE = PDF_LOCATION + File.separator + CSS_NAME;
        File cssFile = new File(CSS_FILE);
        cssFile.createNewFile();

        String PDF_NAME = "file.pdf";
        String PDF_FILE = PDF_LOCATION + File.separator + PDF_NAME;

        FakePdfConverter fakePdfConverter = new FakePdfConverter(HTML_CONTENT);
        fakePdfConverter.setOutputFile(PDF_FILE);
        fakePdfConverter.setCssFile(CSS_FILE);

        // when
        fakePdfConverter.convert();

        // then
        File pdfFile = new File(PDF_FILE);
        fakePdfConverter.assertThat(pdfConverter -> assertThat(pdfFile).exists());

        cssFile.delete();
        pdfFile.delete();
        pdfFolder.delete();
    }

    public static class FakePdfConverter extends PdfConverter {

        public FakePdfConverter(String html) throws IOException {
            super(html);
        }

        public FakePdfConverter(InputStream htmlFile) throws IOException {
            super(htmlFile);
        }

        public FakePdfConverter(InputStream htmlFile, InputStream cssFile) throws IOException {
            super(htmlFile, cssFile);
        }

        public void assertThat(Consumer<PdfConverter> assertionFn) {
            assertionFn.accept(this);
        }
    }
}