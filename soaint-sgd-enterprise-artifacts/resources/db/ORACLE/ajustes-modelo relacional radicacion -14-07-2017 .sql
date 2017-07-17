  ---------------------------------------------------------------------------------
  -- AJUSTES MODELO REALCIONAL DE RADICACION 
  ---------------------------------------------------------------------------------

  ---------------------------------------------------------------------------------
  -- AJUSTES TABLE TVS_DATOS_CONTACTO
  ---------------------------------------------------------------------------------
  
	/*ACTUALIZACION DE CAMPOS */

	ALTER TABLE TVS_DATOS_CONTACTO RENAME COLUMN "TEL_FIJO1" TO "TEL_FIJO";
	ALTER TABLE TVS_DATOS_CONTACTO RENAME COLUMN "EXTENSION1" TO "EXTENSION";


	/*ELIMINACION DE CAMPOS */
	ALTER TABLE TVS_DATOS_CONTACTO DROP COLUMN EXTENSION2 ; 
	ALTER TABLE TVS_DATOS_CONTACTO DROP COLUMN TEL_FIJO2 ;	
	ALTER TABLE TVS_DATOS_CONTACTO DROP COLUMN CIUDAD;	
	
	/*ADICION DE CAMPOS */
	
	ALTER TABLE TVS_DATOS_CONTACTO ADD PRINCIPAL  VARCHAR2(2) NOT NULL;
	
	/*DOCUMENTACION DE CAMPOS TABLA */
	
	COMMENT ON TABLE TVS_DATOS_CONTACTO IS  'Tabla que repesenta los datos del contacto';
	COMMENT ON COLUMN TVS_DATOS_CONTACTO.IDE_CONTACTO IS  'Identificador de la tabla';
	COMMENT ON COLUMN TVS_DATOS_CONTACTO.IDE_AGENTE IS  'Identificador de agente asociado a los datos de contacto';
	COMMENT ON COLUMN TVS_DATOS_CONTACTO.PRINCIPAL IS  'Indica si son los datos principales del contacto para enviar 
	la correspondencia posibles valores P=principal o S =segundario  ';


  ---------------------------------------------------------------------------------
  -- AJUSTES TABLE COR_AGENTE
  ---------------------------------------------------------------------------------
  	
	/*ELIMINACION DE CAMPOS */
	
	ALTER TABLE COR_AGENTE DROP COLUMN COD_CARGO;	
	ALTER TABLE COR_AGENTE DROP COLUMN NRO_DOCUMENTO_IDEN;	
	ALTER TABLE COR_AGENTE DROP COLUMN IDE_CONTACTO;
	ALTER TABLE COR_AGENTE DROP COLUMN COD_FUNC_REMITE;		
	
	/*ADICION DE CAMPOS */
	
	ALTER TABLE COR_AGENTE ADD FEC_CREACION  TIMESTAMP NOT NULL;
	
	/*ACTUALIZACION DE CAMPOS */
	
	ALTER TABLE COR_AGENTE MODIFY FEC_ASIGNACION TIMESTAMP ;
	
	
	/*DOCUMENTACION DE CAMPOS TABLA */
	
	COMMENT ON TABLE COR_AGENTE IS  'Tabla que ralciona todos los agentes de una radicacion ya sean internos o externos';
	COMMENT ON COLUMN COR_AGENTE.IND_ORIGINAL IS  'Indica si el destinatario es original o copia';
	

  ---------------------------------------------------------------------------------
  -- AJUSTES TABLE PPD_DOCUMENTO
  ---------------------------------------------------------------------------------
	
	/*ELIMINACION DE CAMPOS */
	
	ALTER TABLE PPD_DOCUMENTO DROP COLUMN COD_EST_ARCHIVADO;	
	ALTER TABLE PPD_DOCUMENTO DROP COLUMN COD_TIPO_SOPORTE;	
	
	/*ACTUALIZACION DE CAMPOS */


	ALTER TABLE PPD_DOCUMENTO RENAME COLUMN COD_ASUNTO TO ASUNTO;
	ALTER TABLE PPD_DOCUMENTO MODIFY ASUNTO VARCHAR2(500) ;
	
		
	
	