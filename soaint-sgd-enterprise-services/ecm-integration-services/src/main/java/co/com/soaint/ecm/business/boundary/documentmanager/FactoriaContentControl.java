package co.com.soaint.ecm.business.boundary.documentmanager;

/**
 * Created by Dasiel on 29/05/2017.
 */

/**
 * Factoria para los diferentes ECM, hasta ahora solo Alfresco
 * */
public class FactoriaContentControl {

    public static ContentControl getContentControl(String tipo) {

        if (tipo.equals("alfresco")) {

            return new ContentControlAlfresco();
        }
        else {
            return new ContentControlAlfresco();
        }

    }

}
