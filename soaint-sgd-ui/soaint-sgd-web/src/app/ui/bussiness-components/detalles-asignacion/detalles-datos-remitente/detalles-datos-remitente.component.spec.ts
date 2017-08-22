import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesDatosRemitenteComponent } from './detalles-datos-remitente.component';

describe('DetallesDatosRemitenteComponent', () => {
  let component: DetallesDatosRemitenteComponent;
  let fixture: ComponentFixture<DetallesDatosRemitenteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetallesDatosRemitenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetallesDatosRemitenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
