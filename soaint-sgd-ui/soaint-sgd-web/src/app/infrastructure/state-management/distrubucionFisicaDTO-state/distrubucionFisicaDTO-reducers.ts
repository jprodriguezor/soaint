import {Actions, ActionTypes as Autocomplete} from './distrubucionFisicaDTO-actions';
import {tassign} from 'tassign';
import {CorrespondenciaDTO} from 'app/domain/correspondenciaDTO';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';


export interface State {
  ids: number[];
  entities: { [ideDocumento: number]: CorrespondenciaDTO };
  selectedId: number;
  filters: {
    fecha_ini: string;
    fecha_fin: string;
    cod_dependencia: string;
    cod_tipologia_documental: string;
    nro_radicado: string;
  }
}

const initialState: State = {
  ids: [],
  entities: {},
  selectedId: null,
  filters: {
    fecha_ini: null,
    fecha_fin: null,
    cod_dependencia: null,
    cod_tipologia_documental: null,
    nro_radicado: null
  }
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case Autocomplete.FILTER_COMPLETE:
    case Autocomplete.LOAD_SUCCESS: {
      const values = action.payload.comunicacionesOficiales;
      if (!values) {
        return tassign(state, {
          ids: [],
          entities: {},
          selectedId: null
        });
      }
      // const newValues = values.filter(data => !state.entities[data.ideDocumento]);
      const ids = values.map(data => data.correspondencia.ideDocumento);
      const entities = values.reduce((entities: { [ideDocumento: number]: ComunicacionOficialDTO }, value: ComunicacionOficialDTO) => {
        return Object.assign(entities, {
          [value.correspondencia.ideDocumento]: value
        });
      }, {});
      return tassign(state, {
        ids: [...ids],
        entities: entities,
        selectedId: state.selectedId
      });
    }
    case Autocomplete.LOAD: {
      const filters = action.payload;
      return tassign(state, {
        filters: {
          fecha_ini: filters.fecha_ini || null,
          fecha_fin: filters.fecha_fin || null,
          cod_dependencia: filters.cod_dependencia || null,
          nro_radicado: filters.nro_radicado || null,
          cod_tipologia_documental: filters.cod_tipologia_documental || null
        }
      });
    }

    default:
      return state;
  }
}


