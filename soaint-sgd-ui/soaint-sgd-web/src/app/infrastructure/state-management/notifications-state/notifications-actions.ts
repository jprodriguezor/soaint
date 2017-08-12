import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  PUSH_NOTIFICATION: type('[NOTIFICATION] PushNotificationAction Dispatch'),
  DELETE_NOTIFICATION: type('[NOTIFICATION] DeleteNotificationAction Dispatch'),
  DELETE_ALL_NOTIFICATIONS: type('[NOTIFICATION] DeleteAllNotificationsAction Dispatch'),

  NOTIFICATION_SUCCESS: type('[NOTIFICATION] NotificationSuccessAction Dispatch'),
  NOTIFICATION_FAIL: type('[NOTIFICATION] NotificationFailAction Dispatch'),


};

type messageType = 'info' | 'error' | 'success' | 'warning';

export class PushNotificationAction implements Action {
  type = ActionTypes.PUSH_NOTIFICATION;
  constructor(public payload?: any, public severity: messageType = 'success') { }
}

export class DeleteNotificationAction implements Action {
  type = ActionTypes.DELETE_NOTIFICATION;

  constructor(public payload?: any) { }
}

export class DeleteAllNotificationsAction implements Action {
  type = ActionTypes.DELETE_ALL_NOTIFICATIONS;

  constructor(public payload?: any) { }
}


export type Actions =
  PushNotificationAction |
  DeleteNotificationAction |
  DeleteAllNotificationsAction
  ;


