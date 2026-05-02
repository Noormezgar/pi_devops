import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PartnerIntelligenceService } from './partner-intelligence.service';
import { environment } from '../../../enviroments/environment';

describe('PartnerIntelligenceService', () => {
  let service: PartnerIntelligenceService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PartnerIntelligenceService]
    });
    service = TestBed.inject(PartnerIntelligenceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should run inference', () => {
    const dummyOverview = { avgHealthScore: 90, openAnomalies: 0, pendingRecommendations: 0, forecast30d: 100 };
    service.runInference(1).subscribe(overview => {
      expect(overview).toEqual(dummyOverview as any);
    });

    const req = httpMock.expectOne(`${environment.partnerIntelligenceApiUrl}/v1/partners/1/run`);
    expect(req.request.method).toBe('POST');
    req.flush(dummyOverview);
  });

  it('should get recommendations', () => {
    const dummyRecs = [{ id: 1, text: 'Rec 1' }];
    service.getRecommendations(1).subscribe(recs => {
      expect(recs).toEqual(dummyRecs as any);
    });

    const req = httpMock.expectOne(`${environment.partnerIntelligenceApiUrl}/v1/partners/1/recommendations`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyRecs);
  });

  it('should get anomalies', () => {
    const dummyAnomalies = [{ id: 1 }];
    service.getAnomalies().subscribe(anoms => {
      expect(anoms).toEqual(dummyAnomalies as any);
    });

    const req = httpMock.expectOne(`${environment.partnerIntelligenceApiUrl}/v1/anomalies`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyAnomalies);
  });

  it('should decide on recommendation', () => {
    service.decide(1, 'APPROVED', 'comment').subscribe();
    const req = httpMock.expectOne(`${environment.partnerIntelligenceApiUrl}/v1/recommendations/1/decision`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ decision: 'APPROVED', comment: 'comment', reviewer: 'admin' });
    req.flush({});
  });
});
