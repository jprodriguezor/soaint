import co.com.soaint.ecm.business.boundary.documentmanager.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.FactoriaContentControl;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;

/**
 * Created by Dasiel on 30/05/2017.
 */
public class Main {

    public static void main(String[] args) {
        ContentControl a = FactoriaContentControl.getContentControl("alfresco");
        try {
          MensajeRespuesta b= a.establecerConexiones();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }
}
