import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosRemitentesComponent } from './datos-remitentes.component';

describe('DatosRemitentesComponent', () => {
  let component: DatosRemitentesComponent;
  let fixture: ComponentFixture<DatosRemitentesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosRemitentesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosRemitentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
