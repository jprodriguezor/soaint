package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentMimeType {

    APPLICATION_PDF("application/pdf"),
    APPLICATION_HTML("text/html");

    private final String type;

    public static boolean isValidAppMimeType(String type) {
        return DocumentMimeType.APPLICATION_PDF.getType().equals(type)
                || DocumentMimeType.APPLICATION_HTML.getType().equals(type);
    }
}
