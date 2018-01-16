import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnidadesDocumentalesComponent } from './unidades-documentales.component';

describe('UnidadesDocumentalesComponent', () => {
  let component: UnidadesDocumentalesComponent;
  let fixture: ComponentFixture<UnidadesDocumentalesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnidadesDocumentalesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnidadesDocumentalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
