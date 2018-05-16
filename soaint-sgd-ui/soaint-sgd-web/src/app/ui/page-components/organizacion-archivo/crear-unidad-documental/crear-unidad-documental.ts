import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";

export  interface  EventChangeActionArgs{
  nativeEvent?:any;
  action:CreateUDActionType;
  solicitud:SolicitudCreacionUDDto;
}

export type CreateUDActionType = "Creación UD"|"No tramitar creación UD";


