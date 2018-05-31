package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentMimeType {

    APPLICATION_PDF("application/pdf"),
    APPLICATION_HTML("text/html"),
    APPLICATION_ICON("image/png");

    private final String type;
}
