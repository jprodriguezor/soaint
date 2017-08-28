import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentosTramiteComponent } from './documentos-tramite.component';

describe('DocumentosTramiteComponent', () => {
  let component: DocumentosTramiteComponent;
  let fixture: ComponentFixture<DocumentosTramiteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentosTramiteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentosTramiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
