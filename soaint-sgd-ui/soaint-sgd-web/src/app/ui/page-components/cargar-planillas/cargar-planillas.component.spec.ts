import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarPlanillasComponent } from './cargar-planillas.component';

describe('CargarPlanillasComponent', () => {
  let component: CargarPlanillasComponent;
  let fixture: ComponentFixture<CargarPlanillasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CargarPlanillasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CargarPlanillasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
