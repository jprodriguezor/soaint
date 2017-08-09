package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AsignacionControl;
import co.com.soaint.correspondencia.business.control.DctAsigUltimoControl;
import co.com.soaint.correspondencia.business.control.DctAsignacionControl;
import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarAsignacion {
    // [fields] -----------------------------------

    @Autowired
    AsignacionControl control;

    // ----------------------

    public AsignacionesDTO asignarCorrespondencia(AsignacionesDTO asignacionesDTO) throws SystemException {
        return control.asignarCorrespondencia(asignacionesDTO);
    }

    public void actualizarIdInstancia(AsignacionDTO asignacion) throws BusinessException, SystemException {
        control.actualizarIdInstancia(asignacion);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(BigInteger ideFunci, String nroRadicado) throws BusinessException, SystemException {
       return control.listarAsignacionesByFuncionarioAndNroRadicado(ideFunci, nroRadicado);
    }
}
