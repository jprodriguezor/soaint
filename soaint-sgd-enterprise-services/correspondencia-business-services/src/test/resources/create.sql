DROP TABLE IF EXISTS TVS_PAIS;
CREATE TABLE TVS_PAIS (
  IDE_PAIS NUMBER NOT NULL,
  NOMBRE_PAIS varchar(255) DEFAULT NULL,
  COD_PAIS varchar(255) DEFAULT NULL,
  ESTADO varchar(255) DEFAULT NULL,
  FEC_CAMBIO TIMESTAMP  DEFAULT NULL,
  FEC_CREACION TIMESTAMP  DEFAULT NULL,
  COD_USUARIO_CREA varchar(255) DEFAULT NULL,
  PRIMARY KEY (IDE_PAIS)
);
DROP TABLE IF EXISTS TABLE_GENERATOR;
CREATE TABLE TABLE_GENERATOR (
  SEQ_NAME varchar(255) NOT NULL,
  SEQ_VALUE BIGINT  NOT NULL,
  PRIMARY KEY (SEQ_NAME)
);
DROP TABLE  IF EXISTS TVS_MUNICIPIO;
CREATE TABLE TVS_MUNICIPIO(
IDE_MUNIC NUMBER NOT NULL,
NOMBRE_MUNIC varchar(255) DEFAULT NULL,
COD_MUNIC varchar(255) DEFAULT NULL,
COD_DEPAR varchar(255) DEFAULT NULL,
ESTADO varchar(255) DEFAULT NULL,
FEC_CAMBIO TIMESTAMP DEFAULT NULL ,
FEC_CREACION TIMESTAMP DEFAULT NULL ,
COD_USUARIO_CREA varchar(255) DEFAULT NULL,
PRIMARY  KEY (IDE_MUNIC)
);
DROP TABLE IF EXISTS TVS_DEPARTAMENTO;
CREATE TABLE TVS_DEPARTAMENTO(
IDE_DEPAR NUMBER NOT NULL ,
NOMBRE_DEPAR varchar(255) DEFAULT NULL,
COD_DEPAR varchar(255) DEFAULT NULL,
COD_PAIS varchar(255) DEFAULT NULL,
ESTADO varchar(255) DEFAULT NULL,
FEC_CAMBIO TIMESTAMP DEFAULT NULL ,
FEC_CREACION TIMESTAMP DEFAULT NULL ,
COD_USUARIO_CREA varchar(255) DEFAULT NULL,
PRIMARY  KEY (IDE_DEPAR)
);
DROP TABLE  IF EXISTS TVS_CONSTANTES;
CREATE TABLE TVS_CONSTANTES(
IDE_CONST NUMBER NOT NULL,
CODIGO varchar(255) DEFAULT NULL,
NOMBRE varchar(255) DEFAULT NULL,
COD_PADRE varchar(255) DEFAULT NULL,
ESTADO varchar(255) DEFAULT NULL,
PRIMARY  KEY (IDE_CONST)
);
DROP TABLE IF EXISTS FUNCIONARIOS;
CREATE TABLE FUNCIONARIOS(
IDE_FUNCI NUMBER NOT NULL,
COD_TIP_DOC_IDENT varchar(255) DEFAULT NULL,
NRO_IDENTIFICACION varchar(255) DEFAULT NULL,
NOM_FUNCIONARIO varchar(255) DEFAULT NULL,
VAL_APELLIDO1 varchar(255) DEFAULT NULL,
VAL_APELLIDO2 varchar(255) DEFAULT NULL,
CORR_ELECTRONICO varchar(255) DEFAULT NULL,
LOGIN_NAME varchar(255) DEFAULT NULL,
ESTADO varchar(255) DEFAULT NULL,
FEC_CAMBIO TIMESTAMP NULL,
FEC_CREACION TIMESTAMP NULL,
COD_USUARIO_CREA varchar(255) NULL,
CREDENCIALES varchar(255) NULL,
PRIMARY  KEY (IDE_FUNCI)
);
DROP TABLE IF EXISTS TVS_ORGA_ADMIN_X_FUNCI_PK;
CREATE TABLE TVS_ORGA_ADMIN_X_FUNCI_PK(
COD_ORGA_ADMI varchar(255) NOT NULL,
IDE_FUNCI NUMBER NOT NULL,
ESTADO varchar(255) DEFAULT NULL,
FEC_CAMBIO TIMESTAMP NULL,
FEC_CREACION TIMESTAMP NULL,
COD_USUARIO_CREA varchar(255) NULL,
PRIMARY KEY (COD_ORGA_ADMI, IDE_FUNCI)
);
DROP TABLE IF EXISTS TVS_ORGANIGRAMA_ADMINISTRATIVO;
CREATE TABLE TVS_ORGANIGRAMA_ADMINISTRATIVO (
IDE_ORGA_ADMIN NUMBER NOT NULL ,
COD_ORG varchar(255) NOT NULL ,
NOM_ORG varchar(255) NOT NULL ,
DESC_ORG varchar(255) NOT NULL ,
IND_ES_ACTIVO varchar(255) NOT NULL ,
IDE_DIRECCION varchar(255) NULL ,
IDE_PLAN_RESPONSABLE NUMBER NULL ,
IDE_ORGA_ADMIN_PADRE varchar(255) NULL ,
COD_NIVEL varchar(255) NULL ,
FEC_CREACION TIMESTAMP NULL ,
IDE_USUARIO_CREO NUMBER NULL ,
IDE_USUARIO_CAMBIO NUMBER NOT NULL ,
FEC_CAMBIO TIMESTAMP NULL ,
NIV_LECTURA NUMBER NULL ,
NIV_ESCRITURA NUMBER NULL ,
IDE_UUID varchar(255) NULL ,
VAL_SISTEMA varchar(255) NULL ,
VAL_VERSION varchar(255) NULL ,
PRIMARY  KEY (IDE_ORGA_ADMIN)
);
DROP TABLE IF EXISTS COR_PLANILLAS;
CREATE TABLE COR_PLANILLAS
(
IDE_PLANILLA NUMBER NOT NULL,
NRO_PLANILLA varchar(50) NULL,
FEC_GENERACION TIMESTAMP (6)  NULL,
COD_TIPO_PLANILLA       varchar(8)  NULL,
COD_FUNC_GENERA         varchar(8)  NULL,
COD_SEDE_ORIGEN        varchar(8)  NULL,
COD_DEPENDENCIA_ORIGEN  varchar(32)  NULL,
COD_SEDE_DESTINO       varchar(8)  NULL,
COD_DEPENDENCIA_DESTINO varchar(8)  NULL,
COD_CLASE_ENVIO         varchar(8)  NULL,
COD_MODALIDAD_ENVIO     varchar(8)  NULL,
FECHA_CREA TIMESTAMP NULL,
COD_USUARIO_CREA varchar(20) NULL,
FECHA_MODIF TIMESTAMP (6) NULL,
COD_USUARIO_MODIF varchar(20) NULL,
IDE_ECM           varchar(50),
PRIMARY KEY (IDE_PLANILLA)
);
DROP TABLE IF EXISTS COR_AGENTE;
CREATE TABLE COR_AGENTE
(
IDE_AGENTE NUMBER NOT NULL,
IDE_DOCUMENTO NUMBER,
COD_TIPO_REMITE varchar(8) NULL,
COD_TIPO_PERS varchar(8) NULL,
NOMBRE varchar(100) NULL,
RAZON_SOCIAL  varchar(150) NULL,
NIT varchar(16) NULL,
COD_CORTESIA varchar(8) NULL,
COD_EN_CALIDAD varchar(8) NULL,
COD_TIP_DOC_IDENT varchar(8) NULL,
NRO_DOCU_IDENTIDAD varchar(250) NULL,
COD_SEDE varchar(8) NULL,
COD_DEPENDENCIA varchar(8) NULL,
COD_ESTADO varchar(8 ) NULL,
FEC_ASIGNACION TIMESTAMP NULL,
COD_TIP_AGENT varchar(8) NULL,
IND_ORIGINAL  varchar(20),
FEC_CREACION TIMESTAMP NOT NULL,
ESTADO_DISTRIBUCION varchar(8),
PRIMARY KEY (IDE_AGENTE)
)