package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;

@Service
public interface ContentStamper extends Serializable {

    byte[] getStampedDocument(byte[] stampImg, byte[] htmlBytes) throws DocumentException, IOException, SystemException;
}
