import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistribucionFisicaSalidaComponent } from './distribucion-fisica-salida.component';

describe('DistribucionFisicaSalidaComponent', () => {
  let component: DistribucionFisicaSalidaComponent;
  let fixture: ComponentFixture<DistribucionFisicaSalidaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistribucionFisicaSalidaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistribucionFisicaSalidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
