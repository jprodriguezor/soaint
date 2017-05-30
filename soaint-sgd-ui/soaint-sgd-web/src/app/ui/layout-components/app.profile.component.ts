import {Component,Input,OnInit,EventEmitter,ViewChild,trigger,state,transition,style,animate,Inject,forwardRef} from '@angular/core';
import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {MenuItem} from 'primeng/primeng';
import { SessionService } from "app/infrastructure/web/session.service";
import { MessageBridgeService, MessageType } from "app/infrastructure/web/message-bridge.service";

@Component({
    selector: 'inline-profile',
    template: `
        <div class="profile" [ngClass]="{'profile-expanded':active}">
            <div class="profile-image"></div>
            <a href="#" (click)="onClick($event)">
                <span class="profile-name">Jane Williams</span>
                <i class="material-icons">keyboard_arrow_down</i>
            </a>
        </div>

        <ul class="ultima-menu profile-menu" [@menu]="active ? 'visible' : 'hidden'">
            <li role="menuitem">
                <a href="#" class="ripplelink" [attr.tabindex]="!active ? '-1' : null">
                    <i class="material-icons">person</i>
                    <span>Profile</span>
                </a>
            </li>
            <li role="menuitem">
                <a href="#" class="ripplelink" [attr.tabindex]="!active ? '-1' : null">
                    <i class="material-icons">security</i>
                    <span>Privacy</span>
                </a>
            </li>
            <li role="menuitem">
                <a href="#" class="ripplelink" [attr.tabindex]="!active ? '-1' : null">
                    <i class="material-icons">settings_application</i>
                    <span>Settings</span>
                </a>
            </li>
            <li role="menuitem">
                <a (click)="logout()" class="ripplelink" [attr.tabindex]="!active ? '-1' : null">
                    <i class="material-icons">power_settings_new</i>
                    <span>Logout</span>
                </a>
            </li>
        </ul>
    `,
    animations: [
        trigger('menu', [
            state('hidden', style({
                height: '0px'
            })),
            state('visible', style({
                height: '*'
            })),
            transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
            transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class InlineProfileComponent {

    active: boolean;

    constructor(private session: SessionService, private _bridge: MessageBridgeService, private _router: Router){}

    public logout(): void{
        this.session.logout();
        this._bridge.sendMessage({ type: MessageType.LOGOUT_DONE });
        this._router.navigate(['/login']);
    }

    public onClick(event): void {
        this.active = !this.active;
        event.preventDefault();
    }
}