import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanillaGeneradaComponent } from './planilla-generada.component';

describe('PlanillaGeneradaComponent', () => {
  let component: PlanillaGeneradaComponent;
  let fixture: ComponentFixture<PlanillaGeneradaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlanillaGeneradaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlanillaGeneradaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
