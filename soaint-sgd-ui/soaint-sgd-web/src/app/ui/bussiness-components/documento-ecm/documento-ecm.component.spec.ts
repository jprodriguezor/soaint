import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentoEcmComponent } from './documento-ecm.component';

describe('DocumentoEcmComponent', () => {
  let component: DocumentoEcmComponent;
  let fixture: ComponentFixture<DocumentoEcmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentoEcmComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentoEcmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
