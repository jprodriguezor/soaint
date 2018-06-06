import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarPlanillaSalidaComponent } from './cargar-planilla-salida.component';

describe('CargarPlanillaSalidaComponent', () => {
  let component: CargarPlanillaSalidaComponent;
  let fixture: ComponentFixture<CargarPlanillaSalidaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CargarPlanillaSalidaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CargarPlanillaSalidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
