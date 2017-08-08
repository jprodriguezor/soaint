import {Component, Input, OnInit, EventEmitter, ViewChild, Inject, forwardRef, Output, OnDestroy} from '@angular/core';
import {trigger, state, style, transition, animate} from '@angular/animations';
import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {MenuItem} from 'primeng/primeng';
import {AdminLayoutComponent} from '../../container/admin-layout/admin-layout.component';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Subscribable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-menu',
  template: `
    <form [formGroup]="form" novalidate>
      <div class="ui-g ui-fluid form-group">
        <div style=" padding: 16px 16px 0 16px;
    color: blueviolet;">Grupo Funcionario Activo
        </div>
        <div class="ui-g-12 md-dropdownfield" style="padding-bottom: 0;">
          <p-dropdown [options]="dependencias | dropdownItem"
                      [ngModel]="dependenciaSelected"
                      formControlName="dependencia"
                      placeholder="Seleccione"
                      [filter]="true"
                      [autoWidth]="false"
          >
          </p-dropdown>
        </div>
      </div>
    </form>

    <ul app-submenu [item]="model" root="true" class="ultima-menu ultima-main-menu clearfix" [reset]="reset"
        visible="true"></ul>
  `
})
export class AppMenuComponent implements OnInit, OnDestroy {
  form: FormGroup;
  formSubscription: Subscription;
  @Input() reset: boolean;
  @Input() model: any[];
  @Input() dependencias: Array<any> = [];
  @Input() dependenciaSelected: any;
  @Output() onSelectDependencia: EventEmitter<any> = new EventEmitter();

  constructor(@Inject(forwardRef(() => AdminLayoutComponent)) public app: AdminLayoutComponent,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      'dependencia': [null]
    });

    this.formSubscription = this.form.get('dependencia').valueChanges.distinctUntilChanged().subscribe(value => {
      this.onSelectDependencia.emit(value);
    });
  }

  ngOnDestroy() {
    this.formSubscription.unsubscribe();
  }

  changeTheme(theme) {
    let themeLink: HTMLLinkElement = <HTMLLinkElement> document.getElementById('theme-css');
    let layoutLink: HTMLLinkElement = <HTMLLinkElement> document.getElementById('layout-css');

    themeLink.href = 'assets/theme/theme-' + theme + '.css';
    layoutLink.href = 'assets/layout/css/layout-' + theme + '.css';
  }


}

@Component({
  selector: '[app-submenu]',
  template: `
    <ng-template ngFor let-child let-i="index" [ngForOf]="(root ? item : item.items)">
      <li [ngClass]="{'active-menuitem': isActive(i)}" *ngIf="child.visible === false ? false : true">
        <a [href]="child.url||'#'" (click)="itemClick($event,child,i)" class="ripplelink" *ngIf="!child.routerLink"
           [attr.tabindex]="!visible ? '-1' : null" [attr.target]="child.target">
          <i class="material-icons">{{child.icon}}</i>
          <span>{{child.label}}</span>
          <i class="material-icons" *ngIf="child.items">keyboard_arrow_down</i>
        </a>

        <a (click)="itemClick($event,child,i)" class="ripplelink" *ngIf="child.routerLink"
           [routerLink]="child.routerLink" routerLinkActive="active-menuitem-routerlink"
           [routerLinkActiveOptions]="{exact: true}" [attr.tabindex]="!visible ? '-1' : null"
           [attr.target]="child.target">
          <i class="material-icons">{{child.icon}}</i>
          <span>{{child.label}}</span>
          <i class="material-icons" *ngIf="child.items">keyboard_arrow_down</i>
        </a>
        <ul app-submenu [item]="child" *ngIf="child.items" [@children]="isActive(i) ? 'visible' : 'hidden'"
            [visible]="isActive(i)" [reset]="reset"></ul>
      </li>
    </ng-template>
  `,
  animations: [
    trigger('children', [
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
export class AppSubMenuComponent {

  @Input() item: MenuItem;

  @Input() root: boolean;

  @Input() visible: boolean;

  _reset: boolean;

  activeIndex: number;

  constructor(@Inject(forwardRef(() => AdminLayoutComponent)) public app: AdminLayoutComponent, public router: Router, public location: Location) {
  }

  itemClick(event: Event, item: MenuItem, index: number) {
    // avoid processing disabled items
    if (item.disabled) {
      event.preventDefault();
      return true;
    }

    // activate current item and deactivate active sibling if any
    this.activeIndex = (this.activeIndex === index) ? null : index;

    // // execute command
    // if (item.command) {
    //   if (!item.eventEmitter) {
    //     item.eventEmitter = new EventEmitter();
    //     item.eventEmitter.subscribe(item.command);
    //   }
    //
    //   item.eventEmitter.emit({
    //     originalEvent: event,
    //     item: item
    //   });
    // }

    // prevent hash change
    if (item.items || (!item.url && !item.routerLink)) {
      event.preventDefault();
    }

    // hide menu
    if (!item.items) {
      if (this.app.isHorizontal())
        this.app.resetMenu = true;
      else
        this.app.resetMenu = false;

      this.app.overlayMenuActive = false;
      this.app.staticMenuMobileActive = false;
    }
  }

  isActive(index: number): boolean {
    return this.activeIndex === index;
  }

  @Input() get reset(): boolean {
    return this._reset;
  }

  set reset(val: boolean) {
    this._reset = val;

    if (this._reset && this.app.isHorizontal()) {
      this.activeIndex = null;
    }
  }
}
