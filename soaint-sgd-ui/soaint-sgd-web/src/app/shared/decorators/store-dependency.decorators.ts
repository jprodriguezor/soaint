import { ReflectiveInjector } from '@angular/core';
import { provideStore, Store, StoreModule } from '@ngrx/store';
import { State } from '../../infrastructure/redux-store/redux-reducers';
import { ReduxStore } from '../../infrastructure/redux-store/__redux-config';

export function StoreDependency() {
  return function (target: Function) {

    const injector = ReflectiveInjector.resolveAndCreate( [
      StoreModule.provideStore(() => {} , {}).providers
    ] );

    Object.defineProperty(target.prototype, '_store', {
      value: injector.get( Store )
    });
  };

}



