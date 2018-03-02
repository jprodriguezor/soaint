import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleUnidadConservacionComponent } from './detalle-unidad-conservacion.component';

describe('DetalleUnidadConservacionComponent', () => {
  let component: DetalleUnidadConservacionComponent;
  let fixture: ComponentFixture<DetalleUnidadConservacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetalleUnidadConservacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetalleUnidadConservacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
