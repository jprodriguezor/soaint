import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosDestinatarioComponent } from './datos-destinatario.component';

describe('DatosDestinatarioComponent', () => {
  let component: DatosDestinatarioComponent;
  let fixture: ComponentFixture<DatosDestinatarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatosDestinatarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatosDestinatarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
