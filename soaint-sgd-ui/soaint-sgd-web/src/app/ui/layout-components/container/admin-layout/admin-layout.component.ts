import {
  Component,
  AfterViewInit,
  ElementRef,
  Renderer,
  ViewChild,
  OnInit,
  OnDestroy,
  HostListener,
  ChangeDetectionStrategy
} from '@angular/core';
import {MessageBridgeService, MessageType} from 'app/infrastructure/web/message-bridge.service';
import {Subscription} from 'rxjs/Subscription';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';

import {MenuOrientation} from './models/admin-layout.model';
import {Observable} from 'rxjs/Observable';
import {AdminLayoutSandbox} from './redux-state/admin-layout-sandbox';
import {MENU_OPTIONS} from './menu-options';
import {ConstanteDTO} from '../../../../domain/constanteDTO';

declare var jQuery: any;

enum LayoutResponsive {
  MOBILE,
  TABLET,
  DESKTOP
}

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminLayoutComponent implements AfterViewInit, OnInit, OnDestroy {

  processOptions: Observable<any[]>;

  layoutWidth$: Observable<number>;

  menuOptions: any;

  layoutCompact = false;

  layoutMode: MenuOrientation = MenuOrientation.STATIC;

  darkMenu = false;

  profileMode = 'inline';

  rotateMenuButton: boolean;

  topbarMenuActive: boolean;

  overlayMenuActive: boolean;

  staticMenuDesktopInactive: boolean;

  staticMenuMobileActive: boolean;

  layoutContainer: HTMLDivElement;

  layoutMenuScroller: HTMLDivElement;

  menuClick: boolean;

  topbarItemClick: boolean;

  activeTopbarItem: any;

  documentClickListener: Function;

  resetMenu: boolean;

  @ViewChild('layoutContainer') layourContainerViewChild: ElementRef;

  @ViewChild('layoutMenuScroller') layoutMenuScrollerViewChild: ElementRef;

  isAuthenticated$: Observable<boolean>;

  layoutResponsive: LayoutResponsive;

  funcionarioDependenciaSuggestions$: Observable<ConstanteDTO[]>;
  funcionarioDependenciaSelected$: Observable<ConstanteDTO>;

  constructor(private _sandbox: AdminLayoutSandbox, public renderer: Renderer) {
  }

  ngOnInit(): void {

    this.menuOptions = MENU_OPTIONS;

    this.processOptions = this._sandbox.selectorDeployedProcess();

    this.isAuthenticated$ = this._sandbox.selectorIsAutenticated();
    this.layoutWidth$ = this._sandbox.selectorWindowWidth();
    this.funcionarioDependenciaSuggestions$ = this._sandbox.selectorFuncionarioAuthDependenciasSuggestions();
    this.funcionarioDependenciaSelected$ = this._sandbox.selectorFuncionarioAuthDependenciaSelected();

    this.layoutWidth$.subscribe(width => {
      if (width <= 640) {
        return this.layoutResponsive = LayoutResponsive.MOBILE
      }

      if (width <= 1024 && width > 640) {
        return this.layoutResponsive = LayoutResponsive.TABLET
      }

      if (width >= 1024) {
        return this.layoutResponsive = LayoutResponsive.DESKTOP
      }
    });

    this.hideMenu();

    this.isAuthenticated$.subscribe(isAuthenticated => {
      // console.info(isAuthenticated);
      if (isAuthenticated) {
        this._sandbox.dispatchMenuOptionsLoad();
        this.displayMenu();
      } else {
        this.hideMenu();
      }
    });
  }

  ngAfterViewInit() {
    this.layoutContainer = <HTMLDivElement>this.layourContainerViewChild.nativeElement;
    this.layoutMenuScroller = <HTMLDivElement>this.layoutMenuScrollerViewChild.nativeElement;

    // hides the horizontal submenus or top menu if outside is clicked
    this.documentClickListener = this.renderer.listenGlobal('body', 'click', (event) => {
      if (!this.topbarItemClick) {
        this.activeTopbarItem = null;
        this.topbarMenuActive = false;
      }

      if (!this.menuClick && this.isHorizontal()) {
        this.resetMenu = true;
      }

      this.topbarItemClick = false;
      this.menuClick = false;
    });

    setTimeout(() => {
      jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
    }, 10);
  }

  onMenuButtonClick(event) {
    this.rotateMenuButton = !this.rotateMenuButton;
    this.topbarMenuActive = false;

    if (this.layoutMode === MenuOrientation.OVERLAY) {
      this.overlayMenuActive = !this.overlayMenuActive;
    } else {
      if (this.isDesktop()) {

        this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
      } else {
        this.staticMenuMobileActive = !this.staticMenuMobileActive;
      }
    }

    event.preventDefault();
  }

  onMenuClick($event) {
    this.menuClick = true;
    this.resetMenu = false;

    if (!this.isHorizontal()) {
      setTimeout(() => {
        jQuery(this.layoutMenuScroller).nanoScroller();
      }, 500);
    }
  }

  onTopbarMenuButtonClick(event) {
    this.topbarItemClick = true;
    this.topbarMenuActive = !this.topbarMenuActive;

    if (this.overlayMenuActive || this.staticMenuMobileActive) {
      this.rotateMenuButton = false;
      this.overlayMenuActive = false;
      this.staticMenuMobileActive = false;
    }

    event.preventDefault();
  }

  onTopbarItemClick(event, item) {
    this.topbarItemClick = true;

    if (this.activeTopbarItem === item) {
      this.activeTopbarItem = null;
    } else {
      this.activeTopbarItem = item;
    }
    event.preventDefault();
  }

  onFuncionarioDependenciaChange(dependenciaGrupo) {
    this._sandbox.dispatchFuncionarioAuthDependenciaSelected(dependenciaGrupo);
  }

  signOff(): void {
    this._sandbox.dispatchLogoutUser();
  }

  public hideMenu(): void {
    this.staticMenuDesktopInactive = true;
    this.staticMenuMobileActive = false;
  }

  public displayMenu(): void {
    this.staticMenuDesktopInactive = false;
    this.staticMenuMobileActive = true;
  }


  isTablet() {
    return this.layoutResponsive === LayoutResponsive.TABLET;
  }

  isDesktop() {
    return this.layoutResponsive === LayoutResponsive.DESKTOP;
  }

  isMobile() {
    return this.layoutResponsive === LayoutResponsive.MOBILE;
  }

  isOverlay() {
    return this.layoutMode === MenuOrientation.OVERLAY;
  }

  isHorizontal() {
    return this.layoutMode === MenuOrientation.HORIZONTAL;
  }

  changeToStaticMenu() {
    this.layoutMode = MenuOrientation.STATIC;
  }

  changeToOverlayMenu() {
    this.layoutMode = MenuOrientation.OVERLAY;
  }

  changeToHorizontalMenu() {
    this.layoutMode = MenuOrientation.HORIZONTAL;
  }

  triggerProccess(param1, param2) {
    console.log(param1, param2, this);
  }

  ngOnDestroy() {
    if (this.documentClickListener) {
      this.documentClickListener();
    }

    jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
  }

  @HostListener('window:resize', ['$event']) onResize($event) {
    this._sandbox.dispatchWindowResize({width: $event.target.innerWidth, height: $event.target.innerHeight});
  }

}
