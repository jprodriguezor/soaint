package co.com.soaint.ecm.business.boundary.documentmanager;

/**
 * Created by Dasiel
 */

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.ContentControlAlfresco;

/**
 * Factoria para los diferentes ECM, hasta ahora solo Alfresco
 */
public class FactoriaContent {
    private static ContentControlAlfresco controlA;

    public static ContentControl getContentControl(String tipo) {

        if (tipo.equals ("alfresco")) {

            return new ContentControlAlfresco ( );
        } else {
            return new ContentControlAlfresco ( );
        }

    }

    public static ContentManagerMediator getContentManager(String tipo) {

        if (tipo.equals ("alfresco")) {

            return new ContentManagerAlfresco (controlA);
        } else {
            return new ContentManagerAlfresco (controlA);
        }

    }

}
