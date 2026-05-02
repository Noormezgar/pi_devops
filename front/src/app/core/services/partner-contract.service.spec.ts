import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PartnerContractService } from './partner-contract.service';
import { environment } from '../../../enviroments/environment';

describe('PartnerContractService', () => {
  let service: PartnerContractService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PartnerContractService]
    });
    service = TestBed.inject(PartnerContractService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all contracts', () => {
    const dummyContracts = [{ id: 1, title: 'Contract 1' }];
    service.getAllContracts().subscribe(contracts => {
      expect(contracts.length).toBe(1);
      expect(contracts).toEqual(dummyContracts as any);
    });

    const req = httpMock.expectOne(`${environment.partnerContractApiUrl}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyContracts);
  });

  it('should get contract by id', () => {
    const dummyContract = { id: 1, title: 'Contract 1' };
    service.getContract(1).subscribe(contract => {
      expect(contract).toEqual(dummyContract as any);
    });

    const req = httpMock.expectOne(`${environment.partnerContractApiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyContract);
  });

  it('should create contract', () => {
    const newContract = { title: 'New Contract' };
    service.createContract(newContract).subscribe(contract => {
      expect(contract).toEqual(newContract as any);
    });

    const req = httpMock.expectOne(`${environment.partnerContractApiUrl}`);
    expect(req.request.method).toBe('POST');
    req.flush(newContract);
  });

  it('should update status', () => {
    service.updateStatus(1, 'ACTIVE').subscribe();
    const req = httpMock.expectOne(`${environment.partnerContractApiUrl}/1/status?status=ACTIVE`);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });

  it('should delete contract', () => {
    service.deleteContract(1).subscribe();
    const req = httpMock.expectOne(`${environment.partnerContractApiUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
