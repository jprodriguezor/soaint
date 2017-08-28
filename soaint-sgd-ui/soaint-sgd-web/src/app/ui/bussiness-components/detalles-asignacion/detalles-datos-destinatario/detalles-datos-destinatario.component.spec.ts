import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesDatosDestinatarioComponent } from './detalles-datos-destinatario.component';

describe('DetallesDatosDestinatarioComponent', () => {
  let component: DetallesDatosDestinatarioComponent;
  let fixture: ComponentFixture<DetallesDatosDestinatarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetallesDatosDestinatarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetallesDatosDestinatarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
