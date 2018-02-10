import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosDestinatarioExternoComponent } from './datos-destinatario-externo.component';

describe('DatosDestinatarioExternoComponent', () => {
  let component: DatosDestinatarioExternoComponent;
  let fixture: ComponentFixture<DatosDestinatarioExternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosDestinatarioExternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosDestinatarioExternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
