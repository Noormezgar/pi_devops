import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PartnerPerformanceComponent } from './partner-performance.component';
import { PartnerPerformanceService } from '../../../core/services/partner-performance.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

describe('PartnerPerformanceComponent', () => {
  let component: PartnerPerformanceComponent;
  let fixture: ComponentFixture<PartnerPerformanceComponent>;
  let mockPerformanceService: jasmine.SpyObj<PartnerPerformanceService>;

  beforeEach(async () => {
    mockPerformanceService = jasmine.createSpyObj('PartnerPerformanceService', [
      'getPartnerKpis',
      'getLeaderboard',
      'getAlerts',
      'resolveAlert'
    ]);

    mockPerformanceService.getPartnerKpis.and.returnValue(of({
      partnerId: 1, issued: 100, redeemed: 50, expired: 10, redemptionRate: 0.5, activeDeals: 5
    }));
    mockPerformanceService.getLeaderboard.and.returnValue(of([]));
    mockPerformanceService.getAlerts.and.returnValue(of([]));
    mockPerformanceService.resolveAlert.and.returnValue(of(void 0));

    await TestBed.configureTestingModule({
      imports: [PartnerPerformanceComponent, FormsModule],
      providers: [
        { provide: PartnerPerformanceService, useValue: mockPerformanceService },
        { provide: ActivatedRoute, useValue: { params: of({}) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PartnerPerformanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load KPIs, leaderboard and alerts on init', () => {
    expect(mockPerformanceService.getPartnerKpis).toHaveBeenCalled();
    expect(mockPerformanceService.getLeaderboard).toHaveBeenCalled();
    expect(mockPerformanceService.getAlerts).toHaveBeenCalled();
  });

  it('should resolve alert', () => {
    component.resolveAlert(1);
    expect(mockPerformanceService.resolveAlert).toHaveBeenCalledWith(1);
  });

  it('should calculate alert pagination', () => {
    component.alerts = Array(12).fill({ severity: 'HIGH', type: 'TEST', message: 'msg' });
    component.alertPage = 1;
    expect(component.alertTotalPages).toBe(3); // 12 / 5 rounded up = 3
    expect(component.paginatedAlerts.length).toBe(5);
    component.nextAlertPage();
    expect(component.alertPage).toBe(2);
    component.previousAlertPage();
    expect(component.alertPage).toBe(1);
  });

  it('should handle API errors gracefully', () => {
    mockPerformanceService.getPartnerKpis.and.returnValue(throwError(() => new Error('Error')));
    component.loadKpi();
    expect(component.kpiError).toBeTruthy();
    expect(component.loadingKpi).toBeFalse();
  });
});
