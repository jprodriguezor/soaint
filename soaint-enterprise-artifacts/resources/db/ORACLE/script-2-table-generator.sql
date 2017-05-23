/*-------------------------------------------------------------------
  --Crear TABLE_GENERATOR
------------------------------------------------------------------- */
CREATE TABLE TABLE_GENERATOR 
(
  SEQ_NAME VARCHAR2(40 BYTE) NOT NULL 
, SEQ_VALUE NUMBER(15, 0) 
, CONSTRAINT TABLE_GENERATOR_PK PRIMARY KEY 
  (
    SEQ_NAME 
  )
  ENABLE 
) ;