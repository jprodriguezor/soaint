import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadicarSalidaComponent } from './radicar-salida.component';

describe('RadicarSalidaComponent', () => {
  let component: RadicarSalidaComponent;
  let fixture: ComponentFixture<RadicarSalidaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadicarSalidaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadicarSalidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
