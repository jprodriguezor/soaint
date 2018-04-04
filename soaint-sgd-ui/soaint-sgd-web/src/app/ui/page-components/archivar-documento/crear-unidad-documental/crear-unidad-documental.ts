import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";

export  interface  EventChangeActionArgs{
  nativeEvent?:any;
  action:CreateUDActionType;
  solicitud:SolicitudCreacionUDDto;
}

export  enum CreateUDActionType {   createUD ,  noTramiteUD }
