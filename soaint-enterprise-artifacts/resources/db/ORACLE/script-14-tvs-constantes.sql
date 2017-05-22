/*-------------------------------------------------------------------
  --Crear TVS_CONSTANTES
------------------------------------------------------------------- */

CREATE TABLE TVS_CONSTANTES 
(
  IDE_CONST NUMBER(18, 0) NOT NULL 
, CODIGO VARCHAR2(8 BYTE) DEFAULT NULL 
, NOMBRE VARCHAR2(200 BYTE) DEFAULT NULL 
, COD_PADRE VARCHAR2(8 BYTE) 
, ESTADO CHAR(2 CHAR) DEFAULT NULL 
, CONSTRAINT TVS_CONS_PK PRIMARY KEY 
  (
    IDE_CONST 
  )
  ENABLE 
);
