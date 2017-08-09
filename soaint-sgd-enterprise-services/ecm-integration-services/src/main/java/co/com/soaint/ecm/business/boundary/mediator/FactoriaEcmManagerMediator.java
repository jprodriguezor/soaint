package co.com.soaint.ecm.business.boundary.mediator;

/**
 * Created by Dasiel on 01/06/2017.
 */

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;

/**
 * Factoria para los diferentes ECM, hasta ahora solo Alfresco
 * */
public class FactoriaEcmManagerMediator {
static ContentManagerMediator mmediator;
    public static EcmManagerMediator getManagerMediator(String tipo) {

        if (tipo.equals("alfresco")) {

            return new EcmManagerAlfresco(mmediator);
        }
        else {
            return new EcmManagerAlfresco(mmediator);
        }

    }

}