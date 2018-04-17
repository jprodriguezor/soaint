import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AgentDTO} from '../../../../domain/agentDTO';
import {ContactoDTO} from '../../../../domain/contactoDTO';

@Component({
  selector: 'app-detalles-datos-remitente',
  templateUrl: './detalles-datos-remitente.component.html',
  styleUrls: ['./detalles-datos-remitente.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosRemitenteComponent {

  @Input()
  constantesList: ConstanteDTO[];

  @Input()
  municipiosList: any[] = [];

  @Input()
  remitente: AgentDTO;

  @Input()
  contactos: ContactoDTO[];


  constructor() {


  }

  getPais(codigoPais): string {
    if(this.municipiosList !== undefined){
      const pais = this.municipiosList.find((municipio) => municipio.departamento.pais.codigo === codigoPais);
      if (pais) {
        return pais.departamento.pais.nombre;
      }
    }
    return '';
  }

  getDepartamento(codigoDepartamento): string {
    return this.municipiosList !== undefined ? this.municipiosList.find((municipio) => municipio.departamento.codigo === codigoDepartamento).departamento.nombre: '';
  }

  getMunicipio(codigoMunicipio): string {
    return this.municipiosList !== undefined ? this.municipiosList.find((municipio) => municipio.codigo === codigoMunicipio).nombre: '';
  }

}
