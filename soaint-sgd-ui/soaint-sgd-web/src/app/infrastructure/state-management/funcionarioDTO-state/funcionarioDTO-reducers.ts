import {ActionTypes, Actions} from './funcionarioDTO-actions';
import {tassign} from 'tassign';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {OrganigramaDTO} from 'app/domain/organigramaDTO';


export interface State  {
  ideFunci: number;
  codTipDocIdent: string;
  nroIdentificacion: string;
  nomFuncionario: string;
  valApellido1: string;
  valApellido2: string;
  codCargo: string;
  corrElectronico: string;
  codOrgaAdmi: string;
  loginName: string;
  estado: string;
  sede: OrganigramaDTO;
  dependencia: OrganigramaDTO;
}

const initialState: State = {
  ideFunci: null,
  codTipDocIdent: null,
  nroIdentificacion: null,
  nomFuncionario: null,
  valApellido1: null,
  valApellido2: null,
  codCargo: null,
  corrElectronico: null,
  codOrgaAdmi: null,
  loginName: null,
  estado: null,
  sede: null,
  dependencia: null
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.LOAD_SUCCESS: {
      console.log(action.payload);
      const funcionario = action.payload;
      return tassign(state, funcionario);
    }

    default:
      return state;
  }
}


