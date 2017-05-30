/*-------------------------------------------------------------------
  --Crear COR_CORRESPONDENCIA
------------------------------------------------------------------- */
CREATE TABLE COR_CORRESPONDENCIA 
(
  IDE_DOCUMENTO NUMBER(18, 0) NOT NULL 
, DESCRIPCION VARCHAR2(500 BYTE) DEFAULT NULL 
, TIEMPO_RESPUESTA VARCHAR2(8 BYTE) DEFAULT NULL 
, COD_UNIDAD_TIEMPO VARCHAR2(8 BYTE) DEFAULT NULL 
, COD_MEDIO_RECEPCION VARCHAR2(8 BYTE) DEFAULT NULL 
, FEC_RADICADO TIMESTAMP(6) NOT NULL 
, NRO_RADICADO VARCHAR2(50 BYTE) NOT NULL 
, COD_TIPO_CMC VARCHAR2(8 BYTE) DEFAULT NULL 
, IDE_INSTANCIA VARCHAR2(50 BYTE) DEFAULT NULL 
, REQ_DIST_FISICA VARCHAR2(2 BYTE) DEFAULT NULL 
, COD_FUNC_RADICA VARCHAR2(8 BYTE) DEFAULT NULL 
, COD_SEDE VARCHAR2(32 BYTE) DEFAULT NULL 
, COD_DEPENDENCIA VARCHAR2(32 CHAR) DEFAULT NULL 
, REQ_DIGITA VARCHAR2(2 BYTE) DEFAULT NULL 
, COD_EMP_MSJ VARCHAR2(8 BYTE) DEFAULT NULL 
, NRO_GUIA VARCHAR2(50 BYTE) DEFAULT NULL 
, FEC_VEN_GESTION TIMESTAMP(6) DEFAULT NULL 
, COD_ESTADO VARCHAR2(8 BYTE) DEFAULT NULL 
, CONSTRAINT PK_COR_CORRESPONDENCIA PRIMARY KEY 
  (
    IDE_DOCUMENTO 
  )
  ENABLE 
) ;