/*
Navicat Oracle Data Transfer
Oracle Client Version : 10.2.0.5.0

Source Server         : 192.168.3.244
Source Server Version : 120100
Source Host           : 192.168.3.244:1521
Source Schema         : SGDTEST

Target Server Type    : ORACLE
Target Server Version : 120100
File Encoding         : 65001

Date: 2017-06-07 11:27:23
*/


-- ----------------------------
-- Table structure for "SGDTEST"."TVS_CONSTANTES"
-- ----------------------------
DROP TABLE "SGDTEST"."TVS_CONSTANTES";
CREATE TABLE "SGDTEST"."TVS_CONSTANTES" (
  "IDE_CONST" NUMBER(18) NOT NULL ,
  "CODIGO" VARCHAR2(8 BYTE) DEFAULT NULL  NULL ,
  "NOMBRE" VARCHAR2(200 BYTE) DEFAULT NULL  NULL ,
  "COD_PADRE" VARCHAR2(8 BYTE) NULL ,
  "ESTADO" CHAR(2 CHAR) DEFAULT NULL  NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of TVS_CONSTANTES
-- ----------------------------
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('7', 'FAX-SERV', 'Fax Server', 'MED-RECE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('8', 'RADIC-CO', 'Radicación Contingencia', 'MED-RECE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('9', 'SEDE-ELE', 'Sede Electrónica', 'MED-RECE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('10', 'VENTANIL', 'Ventanilla', 'MED-RECE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('11', 'TIPOL-DO', 'Tipología documental', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('12', 'OFICIO', 'Oficio', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('13', 'TUTELA', 'Tutela', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('14', 'DEREC-PE', 'Derecho de petición', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('15', 'INVITACI', 'Invitación', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('16', 'PQSR', 'PQSR', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('17', 'DEMANDA', 'Demanda', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('18', 'FACTURA', 'Factura', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('19', 'INVESTIG', 'Investigación', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('20', 'DENUNCIA', 'Denuncia', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('21', 'RECURSO', 'Recurso', 'TIPOL-DO', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('22', 'DIAS', 'Días', 'UNID-TIE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('23', 'HORAS', 'Horas', 'UNID-TIE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('24', 'TIPO-ANE', 'Tipo anexo', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('25', 'CD', 'CD', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('26', 'CAJA', 'Caja', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('27', 'EXPEDIEN', 'Expediente', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('28', 'FOLLETO', 'Folleto', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('29', 'FOTOGRA', 'Fotografías', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('30', 'HOJ-VIDA', 'Hojas de vida', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('31', 'INVITACS', 'Invitaciones', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('32', 'LIBRO', 'Libro', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('33', 'OFICIOS', 'Oficios', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('34', 'OTRO', 'Otro', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('35', 'PERIODIC', 'Periódicos', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('36', 'PRUEBAS', 'Pruebas', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('37', 'REVISTA', 'Revista', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('38', 'SOB-SELL', 'Sobre sellado', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('39', 'USB', 'USB', 'TIPO-ANE', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('40', 'TIP-PERS', 'Tipo de persona', null, null);
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('41', 'ANONIM', 'Anónimo', 'TIP-PERS', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('42', 'PERS-JUR', 'Persona Jurídica', 'TIP-PERS', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('43', 'PERS-NAT', 'Persona Natural', 'TIP-PERS', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('44', 'TIPO-DOC', 'Tipo documento', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('45', 'NU-ID-TR', 'Numero de Identificación Tributario', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('46', 'CED-CIUD', 'Cedula de ciudadanía', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('47', 'CED-EXTR', 'Cedula de extranjería', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('48', 'PASAPORT', 'Pasaporte', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('49', 'REG-CIVI', 'Registro civil', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('50', 'TARJ-IDE', 'Tarjeta de identidad', 'TIPO-DOC', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('51', 'TIPO-DES', 'Tipo destinatario', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('52', 'PRINCIPA', 'Principal', 'TIPO-DES', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('53', 'COPIA', 'Copia', 'TIPO-DES', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('1', 'TIP-COMU', 'Tipo de Comunicacion', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('2', 'EE', 'Comunicación oficial externa recibida', 'TIP-COMU', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('3', 'EI', 'Comunicación oficial interna recibida', 'TIP-COMU', 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('4', 'UNID-TIE', 'Unidad de tiempo', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('5', 'MED-RECE', 'Medio de recepción', null, 'A ');
INSERT INTO "SGDTEST"."TVS_CONSTANTES" VALUES ('6', 'CORR-ELE', 'Correo Electrónico', 'MED-RECE', 'A ');

-- ----------------------------
-- Indexes structure for table TVS_CONSTANTES
-- ----------------------------

-- ----------------------------
-- Checks structure for table "SGDTEST"."TVS_CONSTANTES"
-- ----------------------------
ALTER TABLE "SGDTEST"."TVS_CONSTANTES" ADD CHECK ("IDE_CONST" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "SGDTEST"."TVS_CONSTANTES"
-- ----------------------------
ALTER TABLE "SGDTEST"."TVS_CONSTANTES" ADD PRIMARY KEY ("IDE_CONST");











Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (55,'DT-MAG','D.T MAGADALENA','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (56,'DT-MET','D.T META','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (57,'DT-MAR','D.T NARIÑO','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (58,'DT-NOR','D.T NORTE DE SANTANDER','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (59,'DT-PUT','D.T PUTUMAYO','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (60,'DT-CEN','CENTRALES D.T','SEDE-ADM','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (61,'DDT','DESPACHO DIRECCION TERRITORIAL','DT-MAG','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (62,'GAC','GRUPO DE ATENCION AL CIUDADANO','DT-MAG','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (63,'GPIVC','GRUPO DE PREVENCION, INSPECCION, VIGILANCIA, CONTROL','DT-MAG','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (64,'GRCC','GRUPO DE RESOLUCION DE CONFLICTOS-CONCILIACION','DT-MAG','A ');

Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (65,'TRAT-COR','Tratamiento de cortesia',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (66,'SENOR','SEÑOR','TRAT-COR','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (67,'SENORA','SEÑORA','TRAT-COR','A ');


Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (68,'TIPO-TEL','Tipo de télefono',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (69,'TEL-MOV','Móvil','TIPO-TEL','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (70,'TEL-FI','Fijo','TIPO-TEL','A ');

INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('COR_CORRESPONDENCIA_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('COR_AGENTE_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('COR_REFERIDO_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('PPD_DOCUMENTO_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('COR_ANEXO_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('TVS_DATOS_CONTACTO_SEQ', '1');
INSERT INTO "SGDTEST"."TABLE_GENERATOR" VALUES ('PPD_TRAZ_DOCUMENTO_SEQ', '1');



Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (71,'TIPO-VIA','Tipo de via',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (72,'VIA1','Via 1','TIPO-VIA','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (73,'VIA2','Via 2','TIPO-VIA','A ');

Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (74,'BIS','Bis',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (75,'BIS1','Bis 1','BIS','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (76,'BIS2','Bis 2','BIS','A ');

Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (77,'ORIENT','Orientacion',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (78,'ORIEN1','Orientacion 1','ORIENT','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (79,'ORIEN2','Orientacion 2','ORIENT','A ');

Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (80,'PREFI-CU','Orientacion',null,'A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (81,'PREF1','Prefijo 1','PREFI-CU','A ');
Insert into "SGDTEST"."TVS_CONSTANTES" (IDE_CONST,CODIGO,NOMBRE,COD_PADRE,ESTADO) values (82,'PREF2','Prefijo 2','PREFI-CU','A ');