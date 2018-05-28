package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.DocumentMimeType;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public interface ContentStamper extends Serializable {

    byte[] getStampedDocument(byte[] stampImg, byte[] contentBytes, DocumentMimeType mimeType) throws SystemException;
}
