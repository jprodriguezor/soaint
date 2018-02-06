import {Injectable, OnDestroy} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanDeactivate, CanLoad, Route, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/withLatestFrom';
import {Store} from '@ngrx/store';
import {State as RootStore} from '../../redux-store/redux-reducers';
import {ContinueWithNextTaskAction, UnlockActiveTaskAction} from './tareasDTO-actions';
import {ConfirmationService} from 'primeng/primeng';
import {Sandbox as NotificationsService} from '../notifications-state/notifications-sandbox';
import {go} from '@ngrx/router-store';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {Sandbox} from './tareasDTO-sandbox';


/**
 * Prevent unauthorized activating and loading of routes
 * @class AuthenticatedGuard
 */
@Injectable()
export class TareaDtoGuard implements CanDeactivate<any>, OnDestroy {

  /**
   * @constructor
   */
  constructor(private _store: Store<RootStore>, private _taskSandbox: Sandbox, private _notify: NotificationsService, private _confirm: ConfirmationService) {
  }

  /**
   * True when there is no Current Task locking the process
   * @method canDeactivate
   */
  canDeactivate(component: any, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState: RouterStateSnapshot):
    Observable<boolean>
    | boolean {

    // get observable
    const observable = Observable.combineLatest(
      this._store.select((s: RootStore) => s.tareas.activeTask),
      this._store.select((s: RootStore) => s.tareas.nextTask)
    ).switchMap(([activeTask, nextTask]) => {
        if (this._taskSandbox.isTaskRoutingStarted()) {
          this._taskSandbox.taskRoutingEnd();
          return Observable.of(true);
        }

        if (activeTask === null) {
          if (nextTask === null) {
            return Observable.of(true);
          } else {
            this._confirm.confirm({
              message: 'Esta tarea se asoció con otra para ser ejecutadas de forma secuencial e inmediata. ' +
              'Si confirma esta acción la tarea siguiente se adicionará a su lista de tareas para posterior ejecusión. ' +
              'Está seguro desea confirmar la acción ?',
              accept: () => {
                this._store.dispatch(new ContinueWithNextTaskAction());
              }
            });
            return Observable.of(false);
          }
        } else {
          if (component.type === TaskTypes.TASK_FORM) {
            const taskId = component.getTask().idTarea;
            const previousNot = this._notify.showNotification({
              severity: 'info',
              summary: 'Espere mientras se guardan los datos de la tarea',
              options: {timeOut: 0}
            });
            component.save().subscribe(() => {
              this._notify.showNotification({
                severity: 'success',
                summary: `La tarea ${taskId} se ha agendado.`
              }).onShown.subscribe(() => {
                this._notify.hideNotification(previousNot.toastId);
                this.goForward(nextState)
              });
            });
            return Observable.of(false);
          } else {
            return Observable.of(true);
          }
        }
      }
    );
    return observable;
  }

  goForward(state) {
    this._store.dispatch(new UnlockActiveTaskAction());
    this._store.dispatch(go(state.url));
  }

  ngOnDestroy() {
    // this.subscription.unsubscribe();
  }

}
