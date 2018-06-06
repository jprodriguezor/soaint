import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {isNullOrUndefined} from "util";

export class SolicitudCreacioUdModel{

  private _solicitudEnviada:boolean = false;

  constructor(private _solicitudService:SolicitudCreacionUdService,private _solicitudes:SolicitudCreacionUDDto[] = [],private _selectedIndex:number = -1 ){}

  get Solicitudes():SolicitudCreacionUDDto[] { return this._solicitudes}

  set Solicitudes(value:SolicitudCreacionUDDto[]) { this._solicitudes = value}

  get SelectedIndex():number { return this._selectedIndex}

  set SelectedIndex(value:number){ this._selectedIndex = value;}

  get SolicitudEnvida():boolean { return this._solicitudEnviada;}

  Solicitar(){
    return  this._solicitudService
      .solicitarUnidadDocumental({solicitudesUnidadDocumentalDTOS:this.Solicitudes})
      .do(() => { this._solicitudEnviada = true;});
  }

  get SolicitudSelected():SolicitudCreacionUDDto{ return  this.SelectedIndex > -1 ? this.Solicitudes[this.SelectedIndex] : null;}

  selectSolicitud(solicitud:SolicitudCreacionUDDto){



    const index = this.Solicitudes.findIndex( sol => {

      return sol.nro == solicitud.nro;
    });

    this.SelectedIndex = isNullOrUndefined(index) ? -1 : index;
  }

  removeAtIndex(index?:number){

    const  idx =  isNullOrUndefined(index) ? this.SelectedIndex : index;

    if(idx < 0 || index >= this.Solicitudes.length)
      return;

    this.SelectedIndex = -1;

    this.Solicitudes.splice(idx,1);

    this.Solicitudes = [... this.Solicitudes];

    }

}
