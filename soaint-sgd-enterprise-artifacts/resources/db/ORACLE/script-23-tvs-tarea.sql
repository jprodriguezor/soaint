/*-------------------------------------------------------------------
  --Crear TVS_TAREA
------------------------------------------------------------------- */

CREATE TABLE TVS_TAREA
(
  IDE_TAREA NUMBER(18, 0) NOT NULL
, ID_INSTANCIA_PROCESO VARCHAR2(50 BYTE) DEFAULT NULL
, ID_TAREA_PROCESO VARCHAR2(50 BYTE) NOT NULL
, PAYLOAD BLOB DEFAULT NULL
, CONSTRAINT TVS_TAREA_PK PRIMARY KEY
  (
    IDE_TAREA
  )
  ENABLE
);