import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesDatosGeneralesComponent } from './detalles-datos-generales.component';

describe('DetallesDatosGeneralesComponent', () => {
  let component: DetallesDatosGeneralesComponent;
  let fixture: ComponentFixture<DetallesDatosGeneralesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetallesDatosGeneralesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetallesDatosGeneralesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
