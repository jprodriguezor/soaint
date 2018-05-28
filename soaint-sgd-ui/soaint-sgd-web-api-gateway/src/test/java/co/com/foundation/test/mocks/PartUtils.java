package co.com.foundation.test.mocks;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInputImpl;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PartUtils {

    public static MultiPartBuilder newMultiPart() {
        return new MultiPartBuilder();
    }

    public static class MultiPartBuilder {

        private final String boundary = "LIMITE-B5AUFnm2FnDRCgHPDE3";

        private String body = "--" + boundary;


        public MultiPartBuilder addPart(String name, String value) {
            body += "\r\nContent-Disposition: form-data; name=\"" + name + "\"\r\n"
                    + "Content-Type: text/plain; charset=US-ASCII\r\n"
                    + "Content-Transfer-Encoding: 8bit\r\n"
                    + "\r\n"
                    + value + "\r\n"
                    + "--" + boundary;

            return this;
        }

        public MultiPartBuilder addBinaryPart(String filename, String data) {
            body += "\r\nContent-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"\r\n"
                    + "Content-Type: application/octet-stream; charset=ISO-8859-1\r\n"
                    + "Content-Transfer-Encoding: binary\r\n"
                    + "\r\n"
                    + data + "\r\n"
                    + "--" + boundary;
            return this;
        }


        public MultipartFormDataInput build() {
            body += "--";

            Map<String, String> parameters = new HashMap<>();
            parameters.put("boundary", boundary);
            MediaType contentType = new MediaType("multipart", "form-data", parameters);
            MultipartFormDataInputImpl multipart = new MultipartFormDataInputImpl(contentType, ResteasyProviderFactory.getInstance());

            ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
            try {
                multipart.parse(bais);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return multipart;
        }
    }
}
