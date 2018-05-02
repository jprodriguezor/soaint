import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SeleccionarDocumentosComponent } from './seleccionar-documentos.component';

describe('SeleccionarDocumentosComponent', () => {
  let component: SeleccionarDocumentosComponent;
  let fixture: ComponentFixture<SeleccionarDocumentosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeleccionarDocumentosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeleccionarDocumentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
