import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistribucionFisicaComponent } from './distribucion-fisica.component';

describe('DistribucionFisicaComponent', () => {
  let component: DistribucionFisicaComponent;
  let fixture: ComponentFixture<DistribucionFisicaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistribucionFisicaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistribucionFisicaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
