/**
 * This function coerces a string into a string literal type.
 * Using tagged union types in TypeScript 2.0, this enables
 * powerful typechecking of our reducers.
 *
 * Since every action label passes through this function it
 * is a good place to ensure all of our action labels
 * are unique.
 */

import {tassign} from 'tassign';

const typeCache: { [label: string]: boolean } = {};
export function type<T>(label: T | ''): T {
  if (typeCache[<string>label]) {
    throw new Error(`Action type "${label}" is not unique"`);
  }

  typeCache[<string>label] = true;

  return <T>label;
}

interface State {
  ids: string[] | number[];
  entities: any;
};

interface Action {
  payload: any
}

export function loadDataReducer(action, state, values, idKey) {

  const valuesIds = values.map(value => value[idKey]);
  const valuesEntities = values.reduce((entities: { [key: number]: any }, value: any) => {
    return Object.assign(entities, {
      [value[idKey]]: value
    });
  }, {});

  return tassign(state, {
    ids: [...valuesIds],
    entities: valuesEntities
  });
}

