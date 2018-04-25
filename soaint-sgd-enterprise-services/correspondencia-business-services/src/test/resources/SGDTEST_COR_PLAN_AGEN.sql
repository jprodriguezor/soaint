CREATE TABLE SGDTEST.COR_PLAN_AGEN
(
    IDE_PLAN_AGEN NUMBER(18) PRIMARY KEY NOT NULL,
    IDE_PLANILLA NUMBER(18) NOT NULL,
    IDE_AGENTE NUMBER(18) NOT NULL,
    IDE_DOCUMENTO NUMBER(18) NOT NULL,
    ESTADO VARCHAR2(8) NOT NULL,
    VAR_PESO VARCHAR2(50 char) DEFAULT NULL
,
    VAR_VALOR VARCHAR2(250) DEFAULT NULL
,
    VAR_NUMERO_GUIA VARCHAR2(250) DEFAULT NULL
,
    FEC_OBSERVACION TIMESTAMP(6) DEFAULT NULL
,
    COD_NUEVA_SEDE VARCHAR2(32) DEFAULT NULL
,
    COD_NUEVA_DEPEN VARCHAR2(32) DEFAULT NULL
,
    OBSERVACIONES VARCHAR2(500) DEFAULT NULL
,
    COD_CAU_DEVO VARCHAR2(8) DEFAULT NULL
,
    FEC_CARGUE_PLA TIMESTAMP(6) DEFAULT NULL
,
    CONSTRAINT COR_PLAN_IDE_AGE_FK FOREIGN KEY (IDE_AGENTE) REFERENCES COR_AGENTE (IDE_AGENTE)
);