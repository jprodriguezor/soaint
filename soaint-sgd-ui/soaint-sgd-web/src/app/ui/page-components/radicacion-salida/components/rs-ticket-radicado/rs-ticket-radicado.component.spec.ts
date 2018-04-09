import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RsTicketRadicadoComponent } from './rs-ticket-radicado.component';

describe('RsTicketRadicadoComponent', () => {
  let component: RsTicketRadicadoComponent;
  let fixture: ComponentFixture<RsTicketRadicadoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RsTicketRadicadoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RsTicketRadicadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
