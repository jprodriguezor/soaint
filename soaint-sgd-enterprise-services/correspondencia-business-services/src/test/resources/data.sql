DELETE FROM TVS_PAIS;
INSERT INTO TVS_PAIS(IDE_PAIS, NOMBRE_PAIS,COD_PAIS,ESTADO,FEC_CAMBIO,FEC_CREACION,COD_USUARIO_CREA)
       VALUES (1, 'CUBA', 'CUBA', 'ACTIVO',NULL,NULL,'USER1');
DELETE FROM TABLE_GENERATOR;
INSERT INTO TABLE_GENERATOR(SEQ_NAME, SEQ_VALUE)
       VALUES ('TVS_PAIS_SEQ', 1);

DELETE FROM TVS_MUNICIPIO;
INSERT INTO TVS_MUNICIPIO(IDE_MUNIC, NOMBRE_MUNIC, COD_MUNIC,COD_DEPAR,ESTADO,FEC_CAMBIO,FEC_CREACION,COD_USUARIO_CREA)
       VALUES ('1', 'MUNICIPIO1','CODIGO1','CODIGODEPTO1','ACTIVO',NULL, NULL ,'USER1');
INSERT INTO TVS_MUNICIPIO(IDE_MUNIC, NOMBRE_MUNIC, COD_MUNIC,COD_DEPAR,ESTADO,FEC_CAMBIO,FEC_CREACION,COD_USUARIO_CREA)
       VALUES ('2', 'MUNICIPIO2','CODIGO2','CODIGODEPTO2','ACTIVO',NULL, NULL ,'USER1');



DELETE  FROM TVS_DEPARTAMENTO;
INSERT  INTO TVS_DEPARTAMENTO (IDE_DEPAR,NOMBRE_DEPAR,COD_DEPAR, COD_PAIS, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA)
        VALUES ('1', 'DEPARTAMENTO1','CODIGODEPTO1','CODIGOPAIS1','ACTIVO',NULL, NULL ,'USER1');
INSERT  INTO TVS_DEPARTAMENTO (IDE_DEPAR,NOMBRE_DEPAR,COD_DEPAR, COD_PAIS, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA)
        VALUES ('2', 'DEPARTAMENTO2','CODIGODEPTO2','CODIGOPAIS2','ACTIVO',NULL, NULL ,'USER1');


DELETE FROM TVS_CONSTANTES;
INSERT INTO TVS_CONSTANTES(IDE_CONST, CODIGO, NOMBRE, COD_PADRE, ESTADO)
       VALUES ('1','CODIGO1','NOMBRE1','CODIGOPADRE1','ACTIVO');
INSERT INTO TVS_CONSTANTES(IDE_CONST, CODIGO, NOMBRE, COD_PADRE, ESTADO)
       VALUES ('2','CODIGO2','NOMBRE2','CODIGOPADRE1','INACTIVO');

DELETE FROM FUNCIONARIOS;
INSERT INTO FUNCIONARIOS(IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2,
                          CORR_ELECTRONICO, LOGIN_NAME, ESTADO, CREDENCIALES)
       VALUES ('1','COD_TIP_DOC_IDENT','NRO_IDENTIFICACION','NOM_FUNCIONARIO1','VAL_APELLIDO1','VAL_APELLIDO2','CORR_ELECTRONICO',
              'LOGIN_NAME','ACTIVO','CREDENCIALES 1');
INSERT INTO FUNCIONARIOS(IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2,
                          CORR_ELECTRONICO, LOGIN_NAME, ESTADO, CREDENCIALES)
       VALUES ('2','COD_TIP_DOC_IDENT','NRO_IDENTIFICACION','NOM_FUNCIONARIO2','VAL_APELLIDO1','VAL_APELLIDO2','CORR_ELECTRONICO',
              'LOGIN_NAME1','INACTIVO','CREDENCIALES 2');

DELETE FROM TVS_ORGANIGRAMA_ADMINISTRATIVO;
INSERT INTO TVS_ORGANIGRAMA_ADMINISTRATIVO (IDE_ORGA_ADMIN, COD_ORG, NOM_ORG, DESC_ORG, IND_ES_ACTIVO, COD_NIVEL, IDE_USUARIO_CAMBIO)
VALUES ('1', '1', 'DIRECCION DE IMPUESTOS Y ADUANAS NACIONALES', 'DIRECCION DE IMPUESTOS Y ADUANAS NACIONALES', '1', '0', '1');

INSERT INTO TVS_ORGANIGRAMA_ADMINISTRATIVO (IDE_ORGA_ADMIN, COD_ORG, NOM_ORG, DESC_ORG, IND_ES_ACTIVO, IDE_ORGA_ADMIN_PADRE, COD_NIVEL, IDE_USUARIO_CAMBIO)
VALUES ('2', '10120140', 'GIT GES JURIDICA', 'GRUPO INTERNO DE TRABAJO DE GESTION JURIDICA', '1', '1', '1', '1');

DELETE FROM COR_PLANILLAS;
INSERT INTO COR_PLANILLAS (IDE_PLANILLA,NRO_PLANILLA,FEC_GENERACION,COD_TIPO_PLANILLA,COD_FUNC_GENERA,COD_SEDE_ORIGEN,COD_DEPENDENCIA_ORIGEN,COD_SEDE_DESTINO,COD_DEPENDENCIA_DESTINO,COD_CLASE_ENVIO,COD_MODALIDAD_ENVIO,FECHA_CREA,COD_USUARIO_CREA,FECHA_MODIF,COD_USUARIO_MODIF,IDE_ECM)
VALUES ('100','104000000000001',NULL,NULL,'1','1040','10401040','1040','10401040',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO COR_PLANILLAS (IDE_PLANILLA,NRO_PLANILLA,FEC_GENERACION,COD_TIPO_PLANILLA,COD_FUNC_GENERA,COD_SEDE_ORIGEN,COD_DEPENDENCIA_ORIGEN,COD_SEDE_DESTINO,COD_DEPENDENCIA_DESTINO,COD_CLASE_ENVIO,COD_MODALIDAD_ENVIO,FECHA_CREA,COD_USUARIO_CREA,FECHA_MODIF,COD_USUARIO_MODIF,IDE_ECM)
VALUES ('200','104000000000002',NULL,NULL,'1','1040','10401040','1040','10401040',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

DELETE FROM COR_CORRESPONDENCIA;
INSERT INTO COR_CORRESPONDENCIA (IDE_DOCUMENTO,DESCRIPCION,TIEMPO_RESPUESTA,COD_UNIDAD_TIEMPO,COD_MEDIO_RECEPCION,FEC_RADICADO,NRO_RADICADO,COD_TIPO_CMC,IDE_INSTANCIA,REQ_DIST_FISICA,COD_FUNC_RADICA,COD_SEDE,COD_DEPENDENCIA,REQ_DIGITA,COD_EMP_MSJ,NRO_GUIA,FEC_VEN_GESTION,COD_ESTADO,COD_CLASE_ENVIO,COD_MODALIDAD_ENVIO)
VALUES ('836','Correspondencia 836','10','UNID-TID','ME-RECVN',NULL,'1040TP-CMCOE2017000001','TP-CMCOE','33144','SD','1','1040','10401040','0',NULL,NULL,CURRENT_DATE,'AS',NULL,NULL);

INSERT INTO COR_CORRESPONDENCIA (IDE_DOCUMENTO,DESCRIPCION,TIEMPO_RESPUESTA,COD_UNIDAD_TIEMPO,COD_MEDIO_RECEPCION,FEC_RADICADO,NRO_RADICADO,COD_TIPO_CMC,IDE_INSTANCIA,REQ_DIST_FISICA,COD_FUNC_RADICA,COD_SEDE,COD_DEPENDENCIA,REQ_DIGITA,COD_EMP_MSJ,NRO_GUIA,FEC_VEN_GESTION,COD_ESTADO,COD_CLASE_ENVIO,COD_MODALIDAD_ENVIO)
VALUES ('837','Correspondencia 837','10','UNID-TID','ME-RECVN',NULL,'1040TP-CMCOE2017000002','TP-CMCOE','33144','EM','1','1040','10401040','0',NULL,NULL,NULL,'AS',NULL,NULL);

DELETE FROM COR_AGENTE;
INSERT INTO COR_AGENTE (IDE_AGENTE, IDE_DOCUMENTO, COD_TIPO_REMITE, COD_TIPO_PERS, NOMBRE, RAZON_SOCIAL, NIT, COD_CORTESIA, COD_EN_CALIDAD, COD_TIP_DOC_IDENT, NRO_DOCU_IDENTIDAD, COD_SEDE, COD_DEPENDENCIA, COD_ESTADO, FEC_ASIGNACION, COD_TIP_AGENT, IND_ORIGINAL, FEC_CREACION, ESTADO_DISTRIBUCION)
VALUES ('100','836','EXT','TP-PERPN','NOMBRE EXT','RS','NIT','CC','CEC','CTDI','NDI','CS','CD','AS',NULL,'TP-AGEE',NULL,NULL,NULL);

INSERT INTO COR_AGENTE (IDE_AGENTE, IDE_DOCUMENTO, COD_TIPO_REMITE, COD_TIPO_PERS, NOMBRE, RAZON_SOCIAL, NIT, COD_CORTESIA, COD_EN_CALIDAD, COD_TIP_DOC_IDENT, NRO_DOCU_IDENTIDAD, COD_SEDE, COD_DEPENDENCIA, COD_ESTADO, FEC_ASIGNACION, COD_TIP_AGENT, IND_ORIGINAL, FEC_CREACION, ESTADO_DISTRIBUCION)
VALUES ('200','837','INT','TP-PERPN','NOMBRE INT','RS','NIT','CC','CEC','CTDI','NDI','CS','CD','AS',NULL,'TP-AGEI',NULL,NULL,NULL);

DELETE FROM DCT_ASIGNACION;
INSERT INTO DCT_ASIGNACION (IDE_ASIGNACION,FEC_ASIGNACION,IDE_DOCUMENTO,IDE_AGENTE,IDE_FUNCI,COD_DEPENDENCIA,COD_TIP_ASIGNACION,OBSERVACIONES,COD_TIP_CAUSAL,IDE_USUARIO_CREO,FEC_CREO,COD_TIP_PROCESO)
VALUES ('100',NULL,'836','100','1','10401040','CTA','Observaciones 100','CI','1',NULL,NULL);

INSERT INTO DCT_ASIGNACION (IDE_ASIGNACION,FEC_ASIGNACION,IDE_DOCUMENTO,IDE_AGENTE,IDE_FUNCI,COD_DEPENDENCIA,COD_TIP_ASIGNACION,OBSERVACIONES,COD_TIP_CAUSAL,IDE_USUARIO_CREO,FEC_CREO,COD_TIP_PROCESO)
VALUES ('200',NULL,'837','200','2','10401041','CTA','Observaciones 200','CI','2',NULL,NULL);

DELETE FROM DCT_ASIG_ULTIMO;
INSERT INTO DCT_ASIG_ULTIMO (IDE_ASIG_ULTIMO,IDE_DOCUMENTO,IDE_AGENTE,IDE_ASIGNACION,IDE_USUARIO_CREO,FEC_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,FECHA_VENCIMIENTO,ID_INSTANCIA,COD_TIP_PROCESO,NUM_REDIRECCIONES,NUM_DEVOLUCIONES)
VALUES ('100','836','100','100','1',NULL,'1',NULL,NULL,NULL,NULL,'49701',NULL,'0','0');

INSERT INTO DCT_ASIG_ULTIMO (IDE_ASIG_ULTIMO,IDE_DOCUMENTO,IDE_AGENTE,IDE_ASIGNACION,IDE_USUARIO_CREO,FEC_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,FECHA_VENCIMIENTO,ID_INSTANCIA,COD_TIP_PROCESO,NUM_REDIRECCIONES,NUM_DEVOLUCIONES)
VALUES ('200','837','200','200','2',NULL,'2',NULL,NULL,NULL,NULL,NULL,NULL,'2','1');

DELETE FROM PPD_DOCUMENTO;
INSERT INTO PPD_DOCUMENTO (IDE_PPD_DOCUMENTO,COD_TIPO_DOC,FEC_DOCUMENTO,ASUNTO,NRO_FOLIOS,NRO_ANEXOS,COD_EST_DOC,FEC_CREACION,IDE_ECM,COD_TIPO_SOPORTE,COD_EST_ARCHIVADO,IDE_DOCUMENTO)
VALUES ('100','TL-DOCOF',NULL,'Test de flujo','1','1',NULL,NULL,NULL,'CODIGO1',NULL,'836');

INSERT INTO PPD_DOCUMENTO (IDE_PPD_DOCUMENTO,COD_TIPO_DOC,FEC_DOCUMENTO,ASUNTO,NRO_FOLIOS,NRO_ANEXOS,COD_EST_DOC,FEC_CREACION,IDE_ECM,COD_TIPO_SOPORTE,COD_EST_ARCHIVADO,IDE_DOCUMENTO)
VALUES ('200','TL-DOCOF',NULL,'Tutela ','1','0',NULL,NULL,NULL,'CODIGO2',NULL,'837');

DELETE FROM PPD_TRAZ_DOCUMENTO;
INSERT INTO PPD_TRAZ_DOCUMENTO (IDE_TRAZ_DOCUMENTO,IDE_PPD_DOCUMENTO,FEC_TRAZ_DOCUMENTO,OBSERVACION,IDE_FUNCI,COD_ESTADO,IDE_DOCUMENTO,COD_ORGA_ADMIN)
VALUES ('100','100',NULL,'Just','2','AS','836','10120140');

INSERT INTO PPD_TRAZ_DOCUMENTO (IDE_TRAZ_DOCUMENTO,IDE_PPD_DOCUMENTO,FEC_TRAZ_DOCUMENTO,OBSERVACION,IDE_FUNCI,COD_ESTADO,IDE_DOCUMENTO,COD_ORGA_ADMIN)
VALUES ('200','200',NULL,'dsd','2','AS','837','10120140');

INSERT INTO COR_ANEXO (IDE_ANEXO,COD_ANEXO,DESCRIPCION,COD_TIPO_SOP,IDE_PPD_DOCUMENTO)
VALUES ('100','CA-TID','ME-RECVN','CODIGO1','100');

INSERT INTO COR_ANEXO (IDE_ANEXO,COD_ANEXO,DESCRIPCION,COD_TIPO_SOP,IDE_PPD_DOCUMENTO)
VALUES ('200','CA-TID',NULL ,'CODIGO2','200');
