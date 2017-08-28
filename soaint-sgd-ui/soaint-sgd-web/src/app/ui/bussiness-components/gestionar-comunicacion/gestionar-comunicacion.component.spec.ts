import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarComunicacionComponent } from './gestionar-comunicacion.component';

describe('GestionarComunicacionComponent', () => {
  let component: GestionarComunicacionComponent;
  let fixture: ComponentFixture<GestionarComunicacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GestionarComunicacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GestionarComunicacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
