---------------------------------------------------------------------------------
  -- AJUSTES TABLE COR_AGENTE
  ---------------------------------------------------------------------------------

/*ADICION DE CAMPOS */

ALTER TABLE COR_AGENTE ADD (NUM_REDIRECCIONES NUMBER(18,0), NUM_DEVOLUCIONES NUMBER(18,0)) ;

---------------------------------------------------------------------------------
  -- AJUSTES TABLE DCT_ASIG_ULTIMO
  ---------------------------------------------------------------------------------

/*ELIMINACION DE CAMPOS */

ALTER TABLE DCT_ASIG_ULTIMO DROP COLUMN NUM_REDIRECCIONES;