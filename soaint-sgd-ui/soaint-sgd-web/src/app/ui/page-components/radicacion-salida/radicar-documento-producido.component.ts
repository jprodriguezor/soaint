import {RadicarSalidaComponent} from "./radicar-salida.component";
import {ChangeDetectorRef, Component} from "@angular/core";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {Sandbox as TaskSandBox} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {Subject} from "rxjs/Subject";
import {isNullOrUndefined} from "util";
import * as domtoimage from 'dom-to-image';
import {LoadNextTaskPayload} from "../../../shared/interfaces/start-process-payload,interface";
import {ScheduleNextTaskAction} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";

@Component({
  selector: 'app-radicar-documento-producido',
  templateUrl: './radicar-documento-producido.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarDocumentoProducidoComponent extends  RadicarSalidaComponent {


  datosGenerales$: Subject<any> = new Subject;
  datosContacto$: Subject<any> = new Subject;

  ideEcm;

  constructor(
    protected _store: Store<RootState>
    , protected _changeDetectorRef: ChangeDetectorRef
    , protected _sandbox: RadicacionSalidaService
    , protected _taskSandbox: TaskSandBox
  ) {

    super(_store, _changeDetectorRef, _sandbox, _taskSandbox);
  }

  ngOnInit() {

    super.ngOnInit();

    ViewFilterHook.addFilter("app-datos-direccion-show-block-dist-dig", () => false);
  }

  restore() {
    console.log('RESTORE...');
    if (this.task) {
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        const results = response.payload;
        if (!results) {

          this._taskSandbox.getTareaPersisted(this.task.variables.idInstancia, '0000').map(r => r.payload)
            .subscribe(resp => {

              resp.datosGenerales.reqDigit = 2;
              if(!isNullOrUndefined(this.task.variables.numeroRadicado))
              resp.datosGenerales.radicadosReferidos = [{nombre: this.task.variables.numeroRadicado}];
              resp.datosGenerales.reqDistFisica = resp.datosContacto.distribucion === "física" ? 1:2;

              if(resp.datosGenerales.reqDistFisica == 1){
                const payload: LoadNextTaskPayload = {
                  idProceso: this.task.idProceso,
                  idInstanciaProceso: this.task.idInstanciaProceso,
                  idDespliegue: this.task.idDespliegue
                };
                this._store.dispatch(new ScheduleNextTaskAction(payload));
              }
              else
                this._store.dispatch(new ScheduleNextTaskAction(null));

              this.datosGenerales$.next(resp.datosGenerales);
              this.ideEcm = resp.datosGenerales.listaVersionesDocumento[0].id;

              this.datosContacto$.next(resp.datosContacto);
            });

          return;
        }

       this.restoreByPayload(results);

        });
    }
  }

  ngAfterViewInit(){}


  ngOnDestroy() {

    super.ngOnDestroy();

    ViewFilterHook.removeFilter("app-datos-direccion-show-block-dist-dig");

  }

  protected buildTaskCompleteParameters(generales:any,noRadicado:any):any{

    return {
      codDependencia:this.dependencySelected.codigo,
      numeroRadicado:noRadicado,
      requiereDistribucion:generales.reqDistFisica ? "1" : "2"
    }
  }

  protected buildPayload():any{

    let payload = super.buildPayload();

    payload.generales.ideEcm = this.ideEcm;

    return payload;
  }

  radicacionButtonIsShown():boolean {

    const conditions: boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0
    ];

    return conditions.every(condition => condition);
  }

  protected uploadTemplate(codDependencia,nroRadicado,ideEcm){

    const node = document.getElementById("ticket-rad");

     if(!isNullOrUndefined(node)){

      domtoimage.toBlob(node).then((blob) => {

        let formData = new FormData();

        formData.append("documento",blob,"etiqueta.png");
        formData.append("idDocumento",ideEcm)
        formData.append("nroRadicado",nroRadicado);
        formData.append("codigoDependencia",codDependencia);


        this._sandbox.uploadTemplate(formData).subscribe();

      });

     /*  domtoimage.toPng(node)
         .then(function (dataUrl) {
           let img = new Image();
           img.src = dataUrl;
           document.getElementById('ticket-image').appendChild(img);
         })
         .catch(function (error) {
           console.error('oops, something went wrong!', error);
         });*/
    }
  }

}
