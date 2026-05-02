import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PartnerContractsComponent } from './partner-contracts.component';
import { PartnerContractService } from '../../../core/services/partner-contract.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

describe('PartnerContractsComponent', () => {
  let component: PartnerContractsComponent;
  let fixture: ComponentFixture<PartnerContractsComponent>;
  let mockContractService: jasmine.SpyObj<PartnerContractService>;

  const mockContracts = [
    { id: 1, partnerId: 10, title: 'Contract 1', status: 'ACTIVE', startDate: '2023-01-01', endDate: '2023-12-31', commissionRate: 0.1 },
    { id: 2, partnerId: 20, title: 'Contract 2', status: 'DRAFT', startDate: '2023-02-01', endDate: '2023-11-30', commissionRate: 0.15 }
  ];

  beforeEach(async () => {
    mockContractService = jasmine.createSpyObj('PartnerContractService', [
      'getAllContracts', 'createContract', 'updateStatus'
    ]);
    mockContractService.getAllContracts.and.returnValue(of(mockContracts as any[]));
    mockContractService.createContract.and.returnValue(of({} as any));
    mockContractService.updateStatus.and.returnValue(of({} as any));

    await TestBed.configureTestingModule({
      imports: [PartnerContractsComponent, FormsModule],
      providers: [
        { provide: PartnerContractService, useValue: mockContractService },
        { provide: ActivatedRoute, useValue: { params: of({}) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PartnerContractsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load contracts', () => {
    expect(component).toBeTruthy();
    expect(mockContractService.getAllContracts).toHaveBeenCalledWith(false);
    expect(component.contracts.length).toBe(2);
  });

  it('should filter contracts by search term and status', () => {
    component.searchTerm = 'Contract 1';
    expect(component.filteredContracts.length).toBe(1);
    expect(component.filteredContracts[0].title).toBe('Contract 1');

    component.searchTerm = '';
    component.statusFilter = 'DRAFT';
    expect(component.filteredContracts.length).toBe(1);
    expect(component.filteredContracts[0].status).toBe('DRAFT');
  });

  it('should calculate stats correctly', () => {
    expect(component.stats.total).toBe(2);
    expect(component.stats.active).toBe(1);
    expect(component.stats.draft).toBe(1);
  });

  it('should validate and create contract', () => {
    component.newContract = {
      partnerId: 1, title: 'Test', commissionRate: 0.1, startDate: '2023-01-01', endDate: '2023-12-31'
    };
    component.createContract();
    expect(mockContractService.createContract).toHaveBeenCalled();
    expect(component.createError).toBe('');
  });

  it('should fail creation validation on empty required fields', () => {
    component.newContract = { partnerId: 0 };
    component.createContract();
    expect(mockContractService.createContract).not.toHaveBeenCalled();
    expect(component.createError).toContain('Please fill partner');
  });

  it('should fail creation validation on invalid commission rate', () => {
    component.newContract = {
      partnerId: 1, title: 'Test', startDate: '2023', endDate: '2023', commissionRate: 1.5
    };
    component.createContract();
    expect(component.createError).toContain('Commission rate must be between 0 and 1');
  });

  it('should update contract status', () => {
    component.updateStatus(1, 'TERMINATED');
    expect(mockContractService.updateStatus).toHaveBeenCalledWith(1, 'TERMINATED');
    expect(mockContractService.getAllContracts).toHaveBeenCalled(); // Should reload
  });

  it('should handle pagination', () => {
    component.contracts = Array(15).fill({ status: 'ACTIVE', title: 'A' } as any);
    component.statusFilter = 'ALL';
    component.searchTerm = '';
    
    expect(component.totalPages).toBe(2);
    expect(component.paginatedContracts.length).toBe(8);
    component.nextPage();
    expect(component.currentPage).toBe(2);
    component.previousPage();
    expect(component.currentPage).toBe(1);
  });
});
