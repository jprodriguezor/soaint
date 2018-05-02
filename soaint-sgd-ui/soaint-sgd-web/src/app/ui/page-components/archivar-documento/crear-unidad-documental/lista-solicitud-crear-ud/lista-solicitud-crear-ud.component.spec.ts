import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaSolicitudCrearUdComponent } from './lista-solicitud-crear-ud.component';

describe('ListaSolicitudCrearUdComponent', () => {
  let component: ListaSolicitudCrearUdComponent;
  let fixture: ComponentFixture<ListaSolicitudCrearUdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaSolicitudCrearUdComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaSolicitudCrearUdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
