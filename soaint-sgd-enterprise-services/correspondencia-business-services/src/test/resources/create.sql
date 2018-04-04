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
FEC_CREACION TIMESTAMP NULL,
ESTADO_DISTRIBUCION varchar(8),
PRIMARY KEY (IDE_AGENTE)
);
DROP TABLE IF EXISTS DCT_ASIGNACION;
CREATE TABLE DCT_ASIGNACION
(
IDE_ASIGNACION NUMBER NOT NULL ,
FEC_ASIGNACION TIMESTAMP   NULL ,
IDE_DOCUMENTO      NUMBER NOT NULL ,
IDE_AGENTE         NUMBER  NULL,
IDE_FUNCI          NUMBER  NULL,
COD_DEPENDENCIA    varchar(32)  NULL,
COD_TIP_ASIGNACION varchar(8) NOT NULL ,
OBSERVACIONES      varchar(500)  NULL,
COD_TIP_CAUSAL     varchar(12)  NULL,
IDE_USUARIO_CREO   varchar(18) NOT NULL ,
FEC_CREO TIMESTAMP NULL,
COD_TIP_PROCESO varchar(8) NULL,
PRIMARY KEY (IDE_ASIGNACION)
);
DROP TABLE IF EXISTS COR_CORRESPONDENCIA;
CREATE TABLE COR_CORRESPONDENCIA
(
IDE_DOCUMENTO       NUMBER NOT NULL,
DESCRIPCION     varchar(500)  NULL,
TIEMPO_RESPUESTA    varchar(8)  NULL,
COD_UNIDAD_TIEMPO   varchar(8)  NULL,
COD_MEDIO_RECEPCION varchar(8)  NULL,
FEC_RADICADO TIMESTAMP NULL,
NRO_RADICADO    varchar(50) NOT NULL,
COD_TIPO_CMC    varchar(8)  NULL,
IDE_INSTANCIA   varchar(50)  NULL,
REQ_DIST_FISICA varchar(2)  NULL,
COD_FUNC_RADICA varchar(8)  NULL,
COD_SEDE       varchar(32)  NULL,
COD_DEPENDENCIA varchar(32)  NULL,
REQ_DIGITA      varchar(2)  NULL,
COD_EMP_MSJ     varchar(8)  NULL,
NRO_GUIA        varchar(50)  NULL,
FEC_VEN_GESTION TIMESTAMP NULL,
COD_ESTADO varchar(8)  NULL,
COD_CLASE_ENVIO  varchar(50)  NULL,
COD_MODALIDAD_ENVIO  varchar(50)  NULL,
PRIMARY KEY (IDE_DOCUMENTO)
);
DROP TABLE IF EXISTS DCT_ASIG_ULTIMO;
CREATE TABLE DCT_ASIG_ULTIMO
(
IDE_ASIG_ULTIMO  NUMBER NOT NULL ,
IDE_DOCUMENTO    NUMBER NOT NULL ,
IDE_AGENTE      NUMBER NOT NULL ,
IDE_ASIGNACION   NUMBER NOT NULL ,
IDE_USUARIO_CREO varchar(8) NOT NULL ,
FEC_CREO TIMESTAMP NULL ,
IDE_USUARIO_CAMBIO NUMBER NOT NULL ,
FEC_CAMBIO TIMESTAMP  NULL ,
NIV_LECTURA   SMALLINT NULL,
NIV_ESCRITURA SMALLINT NULL,
FECHA_VENCIMIENTO TIMESTAMP  NULL,
ID_INSTANCIA      varchar(50)  NULL,
COD_TIP_PROCESO   varchar(8)  NULL,
NUM_REDIRECCIONES BIGINT NULL,
NUM_DEVOLUCIONES  BIGINT NULL,
PRIMARY KEY (IDE_ASIG_ULTIMO)
);
DROP TABLE IF EXISTS PPD_DOCUMENTO;
CREATE TABLE PPD_DOCUMENTO
(
IDE_PPD_DOCUMENTO NUMBER NOT NULL,
COD_TIPO_DOC      varchar(8),
FEC_DOCUMENTO TIMESTAMP NULL ,
ASUNTO      varchar(500) NULL ,
NRO_FOLIOS  BIGINT,
NRO_ANEXOS  BIGINT,
COD_EST_DOC varchar(8),
FEC_CREACION TIMESTAMP NULL ,
IDE_ECM           varchar(50),
COD_TIPO_SOPORTE  varchar(8),
COD_EST_ARCHIVADO varchar(8),
IDE_DOCUMENTO     NUMBER,
PRIMARY KEY (IDE_PPD_DOCUMENTO)
);
DROP TABLE IF EXISTS PPD_TRAZ_DOCUMENTO;
CREATE TABLE PPD_TRAZ_DOCUMENTO
(
IDE_TRAZ_DOCUMENTO NUMBER NOT NULL,
IDE_PPD_DOCUMENTO  NUMBER NOT NULL,
FEC_TRAZ_DOCUMENTO TIMESTAMP NULL,
OBSERVACION    varchar(500),
IDE_FUNCI      NUMBER,
COD_ESTADO     varchar(8),
IDE_DOCUMENTO  NUMBER,
COD_ORGA_ADMIN varchar(32),
PRIMARY KEY (IDE_TRAZ_DOCUMENTO)
);
DROP TABLE IF EXISTS COR_ANEXO;
CREATE TABLE COR_ANEXO
(
IDE_ANEXO NUMBER NOT NULL,
COD_ANEXO  varchar(20),
DESCRIPCION    varchar(500),
COD_TIPO_SOP     varchar(8),
IDE_PPD_DOCUMENTO  NUMBER,
PRIMARY KEY (IDE_ANEXO)
)