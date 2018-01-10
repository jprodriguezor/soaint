import {Component, OnInit, Optional} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {Store} from '@ngrx/store';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {ApiBase} from '../../../../../infrastructure/api/api-base';
import {environment} from '../../../../../../environments/environment';
import {RolDTO} from '../../../../../domain/rolesDTO';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'funcionario-role-list',
  templateUrl: './funcionario-list.component.html',
  styleUrls: ['./funcionario-list.component.css']
})
export class FuncionarioListComponent implements OnInit {

  form: FormGroup;


  funcionarios: Observable<FuncionarioDTO[]> = new Observable<FuncionarioDTO[]>();
  funcionarioEdit: FuncionarioDTO;
  funcionarioSelected: FuncionarioDTO;
  funcionarioEditDialog: Boolean = false;
  funcionarioBusqueda = { nombre : '', valApellido1: '', valApellido2: '' };
  funcionarioPassword: '';

  selectedFuncionarios: string[];

  roles: Observable<RolDTO[]> = new Observable<RolDTO[]>();
  roleSelected: any;

  constructor(private _store: Store<RootState>, private _api: ApiBase, private formBuilder: FormBuilder) {
    this.initForm();
  }

  ngOnInit() {
    /*const payload = {cod_dependencia: '10401040'};
    const endpoint = `${environment.listarFuncionarios_endpoint}` + '/' + payload.cod_dependencia;
    this.funcionarios = this._api.list(endpoint).map(data => data.funcionarios);*/
    this.searchFuncionarios();
    this.loadAllRoles();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nombre': [null],
      'primerApellido': [null],
      'segundoApellido': [null],
    });
  }

  loadAllRoles() {
    const endpoint = `${environment.obtenerFuncionarios_roles_endpoint}`;
    this.roles = this._api.list(endpoint);
  }

  searchFuncionarios() {
    const endpoint = `${environment.buscarFuncionarios_endpoint}`;
    this.funcionarioBusqueda.nombre = this.form.get('nombre').value;
    this.funcionarioBusqueda.valApellido1 = this.form.get('primerApellido').value;
    this.funcionarioBusqueda.valApellido2 = this.form.get('segundoApellido').value;

    this.funcionarios = this._api.post(endpoint, this.funcionarioBusqueda).map( _data => _data.funcionarios);
  }

  editFuncionario(func: FuncionarioDTO): void {
    this.funcionarioEdit = func;
    this.funcionarioPassword = '';
    this.selectedFuncionarios = (null == func.roles) ? [] :
      func.roles.map(role => role.rol).concat();
    this.funcionarioEditDialog = true;
  }

  updateFuncionarioRol(): void {
    this.funcionarioEdit.roles = this.selectedFuncionarios.map(role => {
      return {rol: role};
    }).concat();
    this.funcionarioEdit.password = this.funcionarioPassword;
    console.log(this.funcionarioEdit);
    const endpoint = `${environment.updateFuncionarios_roles_endpoint}`;
    this._api.put(endpoint, this.funcionarioEdit).subscribe(state => {
      console.log(state);
    });
    this.hideEditFuncionario();
  }

  hideEditFuncionario() {
    this.funcionarioEditDialog = false;
  }

}
