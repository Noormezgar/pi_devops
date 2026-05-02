import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { VoucherFraudService } from './voucher-fraud.service';
import { environment } from '../../../enviroments/environment';

describe('VoucherFraudService', () => {
  let service: VoucherFraudService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VoucherFraudService]
    });
    service = TestBed.inject(VoucherFraudService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all alerts', () => {
    const dummyAlerts = [{ id: 1, voucherCode: 'V1' }];
    service.getAllAlerts().subscribe(alerts => {
      expect(alerts).toEqual(dummyAlerts as any);
    });

    const req = httpMock.expectOne(`${environment.voucherFraudApiUrl}/alerts`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyAlerts);
  });

  it('should get alerts by partner', () => {
    const dummyAlerts = [{ id: 1, voucherCode: 'V1' }];
    service.getAlertsByPartner(1).subscribe(alerts => {
      expect(alerts).toEqual(dummyAlerts as any);
    });

    const req = httpMock.expectOne(`${environment.voucherFraudApiUrl}/alerts/partner/1`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyAlerts);
  });

  it('should report fraud', () => {
    const newAlert = { voucherCode: 'V2' };
    service.reportFraud(newAlert).subscribe(alert => {
      expect(alert).toEqual(newAlert as any);
    });

    const req = httpMock.expectOne(`${environment.voucherFraudApiUrl}/detect`);
    expect(req.request.method).toBe('POST');
    req.flush(newAlert);
  });

  it('should update status', () => {
    service.updateStatus(1, 'CONFIRMED').subscribe();
    const req = httpMock.expectOne(`${environment.voucherFraudApiUrl}/alerts/1/status?status=CONFIRMED`);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });
});
