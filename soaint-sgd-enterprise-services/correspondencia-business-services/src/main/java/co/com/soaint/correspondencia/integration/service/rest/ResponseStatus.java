package co.com.soaint.correspondencia.integration.service.rest;

import lombok.Value;

@Value(staticConstructor = "of")
public class ResponseStatus {

    boolean success;
}
