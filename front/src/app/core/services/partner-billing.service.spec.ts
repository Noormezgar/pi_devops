import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PartnerBillingService } from './partner-billing.service';
import { environment } from '../../../enviroments/environment';

describe('PartnerBillingService', () => {
  let service: PartnerBillingService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PartnerBillingService]
    });
    service = TestBed.inject(PartnerBillingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all invoices', () => {
    const dummyInvoices = [{ id: 1, partnerId: 1, amount: 100 }];
    service.getAllInvoices().subscribe(invoices => {
      expect(invoices.length).toBe(1);
      expect(invoices).toEqual(dummyInvoices as any);
    });

    const req = httpMock.expectOne(`${environment.partnerBillingApiUrl}/invoices`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyInvoices);
  });

  it('should get invoices by partner', () => {
    const dummyInvoices = [{ id: 1, partnerId: 1, amount: 100 }];
    service.getInvoicesByPartner(1).subscribe(invoices => {
      expect(invoices.length).toBe(1);
      expect(invoices).toEqual(dummyInvoices as any);
    });

    const req = httpMock.expectOne(`${environment.partnerBillingApiUrl}/invoices/partner/1`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyInvoices);
  });

  it('should generate invoice', () => {
    const dummyInvoice = { partnerId: 1, amount: 100 };
    service.generateInvoice(dummyInvoice as any).subscribe(invoice => {
      expect(invoice).toEqual(dummyInvoice as any);
    });

    const req = httpMock.expectOne(`${environment.partnerBillingApiUrl}/invoices/generate`);
    expect(req.request.method).toBe('POST');
    req.flush(dummyInvoice);
  });

  it('should mark as paid', () => {
    service.markAsPaid(1).subscribe();
    const req = httpMock.expectOne(`${environment.partnerBillingApiUrl}/invoices/1/pay`);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });

  it('should update status', () => {
    service.updateStatus(1, 'CANCELLED').subscribe();
    const req = httpMock.expectOne(`${environment.partnerBillingApiUrl}/invoices/1/status?status=CANCELLED`);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });
});
