package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;

/**
 * Factoria para los diferentes ECM, hasta ahora solo Alfresco
 */
class FactoriaEcmManagerMediator {
    private static ContentManagerMediator mmediator;

    /**
     * Constructor de la clase que crea objeto del tipo de ECM utilizado
     * @param tipo Tipo de ECM
     * @return Instancia del tipo de ECM a utilizar
     */
    public static EcmManagerMediator getManagerMediator(String tipo) {

        if (tipo.equals ("alfresco")) {

            return new EcmManagerAlfresco (mmediator);
        } else {
            return new EcmManagerAlfresco (mmediator);
        }

    }

}