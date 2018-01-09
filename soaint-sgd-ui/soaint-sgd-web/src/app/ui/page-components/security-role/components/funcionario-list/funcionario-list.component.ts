import {Component, OnInit, Optional} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {Store} from '@ngrx/store';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {ApiBase} from '../../../../../infrastructure/api/api-base';
import {environment} from '../../../../../../environments/environment';
import {RolDTO} from '../../../../../domain/rolesDTO';


@Component({
  selector: 'funcionario-role-list',
  templateUrl: './funcionario-list.component.html',
  styleUrls: ['./funcionario-list.component.css']
})
export class FuncionarioListComponent implements OnInit {

  funcionarios: Observable<FuncionarioDTO[]> = new Observable<FuncionarioDTO[]>();
  funcionarioEdit: FuncionarioDTO;
  funcionarioSelected: FuncionarioDTO;
  funcionarioEditDialog: Boolean = false;
  funcionarioBusqueda = { nombre : '', valApellido1: '', valApellido2: '' };

  selectedFuncionarios: string[];

  roles: Observable<RolDTO[]> = new Observable<RolDTO[]>();
  roleSelected: any;

  constructor(private _store: Store<RootState>, private _api: ApiBase) {
  }

  ngOnInit() {
    /*const payload = {cod_dependencia: '10401040'};
    const endpoint = `${environment.listarFuncionarios_endpoint}` + '/' + payload.cod_dependencia;
    this.funcionarios = this._api.list(endpoint).map(data => data.funcionarios);*/
    this.searchFuncionarios(); this.loadAllRoles();
  }

  loadAllRoles() {
    const endpoint = `${environment.obtenerFuncionarios_roles_endpoint}`;
    this.roles = this._api.list(endpoint);
  }

  searchFuncionarios() {
    const endpoint = `${environment.buscarFuncionarios_endpoint}`;
    this.funcionarios = this._api.post(endpoint, this.funcionarioBusqueda);
  }

  editFuncionario(func: FuncionarioDTO): void {
    this.funcionarioEdit = func;
    this.selectedFuncionarios = (null == func.roles) ? [] :
      func.roles.map(role => role.rol).concat();
    this.funcionarioEditDialog = true;
  }

  updateFuncionarioRol(): void {
    this.funcionarioEdit.roles = this.selectedFuncionarios.map(role => {
      return {rol: role};
    }).concat();
    const endpoint = `${environment.updateFuncionarios_roles_endpoint}`;
    this._api.put(endpoint, this.funcionarioEdit).subscribe(state => {
      console.log(state);
    });
  }

  hideEditFuncionario() {
    this.funcionarioEditDialog = false;
  }

}
