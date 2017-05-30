package co.com.soaint.foundation.framework.components.adapters.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import co.com.soaint.foundation.framework.components.adapters.LegacyWrapper;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.constants.DataBaseResource;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;


/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 31-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: LEGACY WRAPPER - DB FUNCTIONS
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public class DBFunctionLegacyWrapper implements LegacyWrapper<Map<String, Object>, List<Map<String, Object>>, JdbcTemplate> {

    //[fields] ---------------------------

    private JdbcTemplate template;
    private String functionName;
    private String bundle;

    private static Logger LOGGER = LogManager.getLogger(DBFunctionLegacyWrapper.class.getName());

    //[constructor] ---------------------------

    private DBFunctionLegacyWrapper(final String functionName) {
        this.functionName = functionName;
    }

    public static DBFunctionLegacyWrapper newInstance(final DataBaseResource sp) {
        return new DBFunctionLegacyWrapper(sp.name());
    }

    //[method] ---------------------------

    @Override
    public void initResources(JdbcTemplate resource, String bundle) throws SystemException {
        template = resource;
        this.bundle = bundle;
    }

    //[method] ---------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> callTarget(Map<String, Object> input) throws SystemException, BusinessException {

        MapSqlParameterSource parameters = new MapSqlParameterSource();

        input.entrySet().stream().forEach((Entry<String, Object> entry) -> {
            parameters.addValue(entry.getKey(), entry.getValue());
        });

        SimpleJdbcCall caller = new SimpleJdbcCall(template);
        caller.withFunctionName(functionName);

        try {
            return (List<Map<String, Object>>) caller.executeFunction(Object.class, parameters);
        } catch (Exception e) {

            LOGGER.error(e.getMessage());

            if (e.getCause() instanceof SQLException) {
                int errorCode = ((SQLException) e.getCause()).getErrorCode();
                throw ExceptionBuilder.newBuilder()
                        .withRootException(e)
                        .withMessage(bundle, "system.database.stored_procedure.error." + errorCode)
                        .buildBusinessException();
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withRootException(e)
                        .withMessage(bundle, "system.database.error")
                        .buildSystemException();
            }
        }
    }

}
