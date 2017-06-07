import {Component, AfterViewInit, ElementRef, Renderer, ViewChild, OnInit, OnDestroy} from '@angular/core';
import {MessageBridgeService, MessageType} from 'app/infrastructure/web/message-bridge.service';
import {Subscription} from 'rxjs/Subscription';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';

import {MenuOrientation} from './models/admin-layout.model';
import {Observable} from 'rxjs/Observable';
import {AdminLayoutSandbox} from './redux-state/admin-layout-sandbox';

declare var jQuery: any;

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent implements AfterViewInit, OnInit, OnDestroy {

  layoutCompact: boolean = false;

  layoutMode: MenuOrientation = MenuOrientation.STATIC;

  darkMenu: boolean = false;

  profileMode: string = 'inline';

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

  constructor(private _sandbox: AdminLayoutSandbox, public renderer: Renderer) {
  }

  ngOnInit(): void {

    this.isAuthenticated$ = this._sandbox.selectorIsAutenticated();
    this.hideMenu();

    this.isAuthenticated$.subscribe(isAuthenticated => {
      // console.info(isAuthenticated);
      if (isAuthenticated) {
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
    }
    else {
      if (this.isDesktop())
        this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
      else
        this.staticMenuMobileActive = !this.staticMenuMobileActive;
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

    if (this.activeTopbarItem === item)
      this.activeTopbarItem = null;
    else
      this.activeTopbarItem = item;

    event.preventDefault();
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
    let width = window.innerWidth;
    return width <= 1024 && width > 640;
  }

  isDesktop() {
    return window.innerWidth > 1024;
  }

  isMobile() {
    return window.innerWidth <= 640;
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

  ngOnDestroy() {
    if (this.documentClickListener) {
      this.documentClickListener();
    }

    jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
  }

}
