import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferenciasDocumentalesComponent } from './transferencias-documentales.component';

describe('TransferenciasDocumentalesComponent', () => {
  let component: TransferenciasDocumentalesComponent;
  let fixture: ComponentFixture<TransferenciasDocumentalesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransferenciasDocumentalesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransferenciasDocumentalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
