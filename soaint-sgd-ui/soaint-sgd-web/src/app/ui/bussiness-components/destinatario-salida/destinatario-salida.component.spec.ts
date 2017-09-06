import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DestinatarioSalidaComponent } from './destinatario-salida.component';

describe('DestinatarioSalidaComponent', () => {
  let component: DestinatarioSalidaComponent;
  let fixture: ComponentFixture<DestinatarioSalidaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DestinatarioSalidaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DestinatarioSalidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
