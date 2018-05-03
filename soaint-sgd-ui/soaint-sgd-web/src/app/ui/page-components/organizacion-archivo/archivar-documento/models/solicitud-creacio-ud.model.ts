import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";

export class SolicitudCreacioUdModel{

  constructor(private _solicitudService:SolicitudCreacionUdService,private _solicitudes:SolicitudCreacionUDDto[] = []){}

  get Solicitudes():SolicitudCreacionUDDto[] { return this._solicitudes}

  set Solicitudes(value:SolicitudCreacionUDDto[]) { this._solicitudes = value}

  Solicitar(){

    this._solicitudService.solicitarUnidadDocumental( this.Solicitudes);
  }


}
