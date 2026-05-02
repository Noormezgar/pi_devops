import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VoucherFraudComponent } from './voucher-fraud.component';
import { VoucherFraudService } from '../../../core/services/voucher-fraud.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

describe('VoucherFraudComponent', () => {
  let component: VoucherFraudComponent;
  let fixture: ComponentFixture<VoucherFraudComponent>;
  let mockFraudService: jasmine.SpyObj<VoucherFraudService>;

  const mockAlerts = [
    { id: 1, voucherCode: 'V1', partnerId: 1, alertType: 'TYPE1', detectionDetails: 'det', severityLevel: 5, status: 'INVESTIGATION_PENDING' },
    { id: 2, voucherCode: 'V2', partnerId: 2, alertType: 'TYPE2', detectionDetails: 'det', severityLevel: 2, status: 'CONFIRMED' }
  ];

  beforeEach(async () => {
    mockFraudService = jasmine.createSpyObj('VoucherFraudService', ['getAllAlerts', 'updateStatus']);
    mockFraudService.getAllAlerts.and.returnValue(of(mockAlerts as any[]));
    mockFraudService.updateStatus.and.returnValue(of({} as any));

    await TestBed.configureTestingModule({
      imports: [VoucherFraudComponent, FormsModule],
      providers: [
        { provide: VoucherFraudService, useValue: mockFraudService },
        { provide: ActivatedRoute, useValue: { params: of({}) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VoucherFraudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load alerts', () => {
    expect(component).toBeTruthy();
    expect(mockFraudService.getAllAlerts).toHaveBeenCalledWith(false);
    expect(component.alerts.length).toBe(2);
  });

  it('should filter pending and resolved alerts', () => {
    expect(component.filteredPendingAlerts.length).toBe(1);
    expect(component.filteredResolvedAlerts.length).toBe(1);
  });

  it('should filter by search term, status and severity', () => {
    component.searchTerm = 'V1';
    expect(component.filteredAlerts.length).toBe(1);

    component.searchTerm = '';
    component.statusFilter = 'CONFIRMED';
    expect(component.filteredAlerts.length).toBe(1);

    component.statusFilter = 'ALL';
    component.minSeverity = 4;
    expect(component.filteredAlerts.length).toBe(1);
  });

  it('should calculate stats', () => {
    expect(component.stats.total).toBe(2);
    expect(component.stats.pending).toBe(1);
    expect(component.stats.confirmed).toBe(1);
    expect(component.stats.highSeverity).toBe(1);
  });

  it('should update status', () => {
    component.updateStatus(1, 'CONFIRMED');
    expect(mockFraudService.updateStatus).toHaveBeenCalledWith(1, 'CONFIRMED');
    expect(mockFraudService.getAllAlerts).toHaveBeenCalled();
  });

  it('should handle pagination for pending', () => {
    component.alerts = Array(15).fill({ status: 'INVESTIGATION_PENDING', severityLevel: 1 });
    component.statusFilter = 'ALL';
    expect(component.totalPendingPages).toBe(2);
    expect(component.paginatedPendingAlerts.length).toBe(8);
    component.nextPendingPage();
    expect(component.currentPagePending).toBe(2);
    component.previousPendingPage();
    expect(component.currentPagePending).toBe(1);
  });

  it('should handle pagination for resolved', () => {
    component.alerts = Array(10).fill({ status: 'CONFIRMED', severityLevel: 1 });
    component.statusFilter = 'ALL';
    expect(component.totalResolvedPages).toBe(2);
    expect(component.paginatedResolvedAlerts.length).toBe(8);
    component.nextResolvedPage();
    expect(component.currentPageResolved).toBe(2);
    component.previousResolvedPage();
    expect(component.currentPageResolved).toBe(1);
  });
});
