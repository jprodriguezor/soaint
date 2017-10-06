import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {Observable} from 'rxjs/Observable';
import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';
import {DocumentoDTO} from "../../domain/documentoDTO";

export class RadicacionEntradaDTV {

  constructor(private dataSource: ComunicacionOficialDTO) {

  }

  getDatosRemitente(): Observable<AgentDTO> {
    return Observable.of(this.dataSource.agenteList.find(value => value.codTipAgent === 'TP-AGEE'));
  }

  getDatosContactos(): Observable<ContactoDTO[]> {
    return Observable.of(this.dataSource.datosContactoList);
  }

  getDatosDocumento(): Observable<DocumentoDTO[]> {
    return Observable.of(this.dataSource.ppdDocumentoList);
  }

  getDatosDestinatarios(): Observable<AgentDTO[]> {
    return Observable.of(this.dataSource.agenteList.filter(value => value.codTipAgent === 'TP-AGEI'));
  }

  getDatosDestinatarioInmediate(): AgentDTO[] {
    return this.dataSource.agenteList.filter(value => value.codTipAgent === 'TP-AGEI');
  }

}
