import index from "@angular/cli/lib/cli";

export  class ArchivarDocumentoModel{

  constructor(private _unidadDocumental:any ="",private _documentos:any[] = []){}

  get UnidadDocumental(){ return this._unidadDocumental}

  set UnidadDocumental(value: any){ this._unidadDocumental = value }

  get Documentos():any[]{ return this._documentos}

  set Documentos(value:any[]){this._documentos = value}

  Archivar(){


  }
  GroupDocumentByDependencia():any[]{

    if(this.Documentos.length === 0)
      return [];

    const documentos = [...this.Documentos].sort( (a,b) => {

      if(a.codigoDependencia == b.codigoDependencia)
        return 0;

      return  a.codigoDependencia >= b.codigoDependencia ? 1 : -1;
    });

    let documentDependency = [];


    let currentDependency = documentos[0].codigoDependencia;

    documentos.forEach( (doc,index) => {

      if(index === 0 || doc.codigoDependencia != currentDependency){

        documentDependency.push([doc]);

        if(index === 0)
        return;

        currentDependency = doc.codigoDependencia;
        return;
      }

      documentDependency[documentDependency.length - 1].push(doc);

    });

    return documentDependency;
  }
}
