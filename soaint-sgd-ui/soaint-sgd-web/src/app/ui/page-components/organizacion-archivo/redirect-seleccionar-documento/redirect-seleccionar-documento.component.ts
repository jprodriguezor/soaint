import {Component, OnDestroy} from '@angular/core';
import {ProcesoService} from "../../../../infrastructure/api/proceso.service";
import  {Sandbox as ProcesoSandbox} from "../../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox";

@Component({
  selector: 'app-redirect-seleccionar-documento',
  template:""
})
export class RedirectSeleccionarDocumentoComponent implements OnDestroy {

  constructor( private  _procesoService:ProcesoService,private _procesoSandbox : ProcesoSandbox) {

    this._procesoService.getProcess("process.archivar-documento").subscribe(procesos => {

         if(procesos.length > 0)
          this._procesoSandbox.initProcessDispatch(procesos[0]);
    })
  }

  ngOnDestroy(){

  }


}
