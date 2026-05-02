import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PartnerPerformanceService } from './partner-performance.service';
import { environment } from '../../../enviroments/environment';

describe('PartnerPerformanceService', () => {
  let service: PartnerPerformanceService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PartnerPerformanceService]
    });
    service = TestBed.inject(PartnerPerformanceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get partner KPIs', () => {
    const dummyKpi = { partnerId: 1, overallScore: 80 };
    service.getPartnerKpis(1).subscribe(kpi => {
      expect(kpi).toEqual(dummyKpi as any);
    });

    const req = httpMock.expectOne(`${environment.partnerPerformanceApiUrl}/v1/partners/1/kpis`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyKpi);
  });

  it('should get leaderboard', () => {
    const dummyLeaderboard = [{ partnerId: 1, score: 90 }];
    service.getLeaderboard('30d', 'redemptionRate', 5).subscribe(rows => {
      expect(rows).toEqual(dummyLeaderboard as any);
    });

    const req = httpMock.expectOne(`${environment.partnerPerformanceApiUrl}/v1/leaderboard?period=30d&metric=redemptionRate&limit=5`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyLeaderboard);
  });

  it('should get alerts', () => {
    const dummyAlerts = [{ id: 1, type: 'TYPE1' }];
    service.getAlerts().subscribe(alerts => {
      expect(alerts).toEqual(dummyAlerts as any);
    });

    const req = httpMock.expectOne(`${environment.partnerPerformanceApiUrl}/v1/alerts?open=true`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyAlerts);
  });

  it('should resolve alert', () => {
    service.resolveAlert(1).subscribe();
    const req = httpMock.expectOne(`${environment.partnerPerformanceApiUrl}/v1/alerts/1/resolve`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});
