---------------------------------------------------------------------------------
  -- AJUSTES TABLE COR_CORRESPONDENCIA
  ---------------------------------------------------------------------------------

  /*ADICION DE CAMPOS */

  ALTER TABLE COR_CORRESPONDENCIA ADD (COD_CLASE_ENVIO VARCHAR2(8 BYTE) DEFAULT NULL, COD_MODALIDAD_ENVIO VARCHAR2(8 BYTE) DEFAULT NULL) ;