import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UdTramitadasComponent } from './ud-tramitadas.component';

describe('UdTramitadasComponent', () => {
  let component: UdTramitadasComponent;
  let fixture: ComponentFixture<UdTramitadasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UdTramitadasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UdTramitadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
