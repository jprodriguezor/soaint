import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosRemitenteComponent } from './datos-remitente.component';

describe('DatosRemitenteComponent', () => {
  let component: DatosRemitenteComponent;
  let fixture: ComponentFixture<DatosRemitenteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosRemitenteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosRemitenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
