/*-------------------------------------------------------------------
  --Crear TVS_PLANTILLA
------------------------------------------------------------------- */
CREATE TABLE TVS_PLANTILLA
(
  IDE_PLANTILLA NUMBER(18, 0) NOT NULL
, REFERENCIA VARCHAR2(50 BYTE) NOT NULL
, COD_CLASIFICACION VARCHAR2(8 BYTE)
, COD_TIPO_UBICACION VARCHAR2(8 BYTE)
, UBICACION VARCHAR2(50 BYTE)
, ESTADO VARCHAR2(8 BYTE)
, CONSTRAINT TVS_PLANTILLA_PK PRIMARY KEY
  (
    IDE_PLANTILLA
  )
  ENABLE
);

/*-------------------------------------------------------------------
  --Crear TVS_PLANTILLA_METADATO
------------------------------------------------------------------- */
CREATE TABLE TVS_PLANTILLA_METADATO
(
  IDE_PLANTILLA_METADATO NUMBER(18, 0) NOT NULL
, COD_METADATO VARCHAR2(8 BYTE)
, NOMB_METADATO VARCHAR2(50 BYTE) NOT NULL
, IDE_PLANTILLA NUMBER(18, 0) NOT NULL
, CONSTRAINT TVS_PLANTILLA_METADATO_PK PRIMARY KEY
  (
    IDE_PLANTILLA_METADATO
  )
  ENABLE
);

/*-------------------------------------------------------------------
  --Modificar TVS_PLANTILLA_METADATO
------------------------------------------------------------------- */
ALTER TABLE TVS_PLANTILLA_METADATO
ADD CONSTRAINT TVS_PLANT_MET_IDE_PLANT_FK FOREIGN KEY
(
  IDE_PLANTILLA
)
REFERENCES TVS_PLANTILLA
(
  IDE_PLANTILLA
)
ENABLE;

/*-------------------------------------------------------------------
  --Insertar Sequencias
------------------------------------------------------------------- */

INSERT INTO TABLE_GENERATOR (SEQ_NAME, SEQ_VALUE) VALUES ('TVS_PLANTILLA_SEQ', 0);
INSERT INTO TABLE_GENERATOR (SEQ_NAME, SEQ_VALUE) VALUES ('TVS_PLANTILLA_METADATO_SEQ', 0);