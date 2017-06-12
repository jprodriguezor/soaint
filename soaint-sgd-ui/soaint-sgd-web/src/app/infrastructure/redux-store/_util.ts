
/**
 * This function coerces a string into a string literal type.
 * Using tagged union types in TypeScript 2.0, this enables
 * powerful typechecking of our reducers.
 *
 * Since every action label passes through this function it
 * is a good place to ensure all of our action labels
 * are unique.
 */

const typeCache: { [label: string]: boolean } = {};
export function type<T>(label: T | ''): T {
  if (typeCache[<string>label]) {
    throw new Error(`Action type "${label}" is not unique"`);
  }

  typeCache[<string>label] = true;

  return <T>label;
}

import { Store } from '@ngrx/store';

// import 'rxjs/add/operator/take';
// import * as fromRoot from './reducers';
//
// export function getState(store: Store<fromRoot.State>): fromRoot.State {
//     let state: fromRoot.State;
//
//     store.take(1).subscribe(s => state = s);
//
//     return state;
// }
