import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesAsignacionComponent } from './detalles-asignacion.component';

describe('DetallesAsignacionComponent', () => {
  let component: DetallesAsignacionComponent;
  let fixture: ComponentFixture<DetallesAsignacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetallesAsignacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetallesAsignacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
