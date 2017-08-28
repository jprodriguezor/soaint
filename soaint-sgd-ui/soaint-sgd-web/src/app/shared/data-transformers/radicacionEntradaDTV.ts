import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {Observable} from 'rxjs/Observable';
import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';

export class RadicacionEntradaDTV {

  constructor(private dataSource: ComunicacionOficialDTO) {

  }

  getDatosRemitente(): Observable<AgentDTO> {
    return Observable.of(this.dataSource.agenteList.find(value => value.codTipoRemite !== null));
  }

  getDatosContactos(): Observable<ContactoDTO[]> {
    return Observable.of(this.dataSource.datosContactoList);
  }

  getDatosDestinatarios(): Observable<AgentDTO[]> {
    return Observable.of(this.dataSource.agenteList.filter(value => value.codTipoRemite === null));
  }

}
