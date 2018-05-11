
export  const oa_dataSource = {
  archivar_documento_endpoint: {success: true},
  crear_solicitud_ud: {success: true},
  actualizar_solicitud_ud:{success : true},
  listar_solicitud_ud:{
    solicitudesUnidadDocumentalDTOS:
    [
      {
        "codigoSede":"1040",
        "codigoDependencia":"10401000",
        "codigoSerie": "11111111",
        "codigoSubSerie" : "1111.11",
        "id" : "1",
        "nombreUnidadDocumental" : "Unidad 1",
        "descriptor1" : "Descriptor 1",
        "descriptor2": "Descriptor 2",
        "observaciones": "Prueba 1",
        "estado": "",
        "fechaHora":"15123698251",
        "nro":"sdve99ef9ewf9we9f9",
        "idSolicitante":"1"
      },
      {
        "codigoSede":"1040",
        "codigoDependencia":"10401040",
        "codigoSerie": "222221",
        "codigoSubSerie": "22221.11",
        "id": "2",
        "nombreUnidadDocumental": "Unidad 2",
        "descriptor1": "Descriptor 1",
        "descriptor2": "Descriptor 2",
        "observaciones": "Prueba 2",
        "estado": "",
        "fechaHora":"15123698251",
        "nro":"w3er535r32532r2r2",
        "idSolicitante":"1"
      }
     ]
  },
  listar_documentos_archivar:{
    documentList:[
      {
       idProcesoSolicitud:"4339794",
       nroRadicado:"54564565",
       nombreDocumento:"Documento 1",
       fechaCreacion:"1626649494994",
       tipologia:""
      }
    ]
  },
  listar_documentos_archivados:{
    documentList:[
      {
      nro:"refw4ref5re4r",
      idUnidadDocumental:"58694494994",
      nombreUnidadDocumental:"Unidad 1",
      codigoSerie:"498449949",
      codigoSubSerie:"499494949",
      fechaHora:"12574999994",
      nroRadicado:"4949494949",
      tipologia:"",
      nombreDocumento:"Documento 1"
    },
      {
        nro:"4wefe4wf9we",
        idUnidadDocumental:"44999499",
        nombreUnidadDocumental:"Unidad 1",
        codigoSerie:"498449949",
        codigoSubSerie:"499494949",
        fechaHora:"12459499949",
        nroRadicado:"84494949",
        tipologia:"",
        nombreDocumento:"Documento 2"
      },
      {
        nro:"fwee4w9ew94",
        idUnidadDocumental:"49899999",
        nombreUnidadDocumental:"Unidad 1",
        codigoSerie:"4984999",
        codigoSubSerie:"6464494",
        fechaHora:"14494999",
        nroRadicado:"419499499",
        tipologia:"",
        nombreDocumento:"Documento 3"
      },
    ]
  },
  tipologiasDocumentales:[
    {
      id: 1,
      nombre: "Tipologia 1"
    },
    {
      id: 2,
      nombre: "Tipologia 2"
    },
    {
      id: 3,
      nombre: "Tipologia 3"
    },
  ],
};
