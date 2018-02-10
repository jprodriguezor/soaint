import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosDestinatarioInternoComponent } from './datos-destinatario-interno.component';

describe('DatosDestinatarioInternoComponent', () => {
  let component: DatosDestinatarioInternoComponent;
  let fixture: ComponentFixture<DatosDestinatarioInternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosDestinatarioInternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosDestinatarioInternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
