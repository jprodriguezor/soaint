export  class ArchivarDocumentoModel{

  constructor(private _unidadDocumental:any ="",private _documentos:any[] = []){}

  get UnidadDocumental(){ return this._unidadDocumental}

  set UnidadDocumental(value: any){ this._unidadDocumental = value }

  get Documentos():any[]{ return this._documentos}

  set Documentos(value:any[]){this._documentos = value}

  Archivar(){


  }
}
