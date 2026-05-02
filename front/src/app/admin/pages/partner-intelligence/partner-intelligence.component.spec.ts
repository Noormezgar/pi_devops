import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PartnerIntelligenceComponent } from './partner-intelligence.component';
import { PartnerIntelligenceService } from '../../../core/services/partner-intelligence.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';

describe('PartnerIntelligenceComponent', () => {
  let component: PartnerIntelligenceComponent;
  let fixture: ComponentFixture<PartnerIntelligenceComponent>;
  let mockIntelligenceService: jasmine.SpyObj<PartnerIntelligenceService>;

  beforeEach(async () => {
    mockIntelligenceService = jasmine.createSpyObj('PartnerIntelligenceService', [
      'runInference', 'getRecommendations', 'getAnomalies', 'getForecasts', 'getSummaries', 'decide'
    ]);

    mockIntelligenceService.runInference.and.returnValue(of({
      avgHealthScore: 90, openAnomalies: 0, pendingRecommendations: 0, forecast30d: 100
    }));
    mockIntelligenceService.getRecommendations.and.returnValue(of([]));
    mockIntelligenceService.getAnomalies.and.returnValue(of([]));
    mockIntelligenceService.getForecasts.and.returnValue(of([]));
    mockIntelligenceService.getSummaries.and.returnValue(of([]));
    mockIntelligenceService.decide.and.returnValue(of({} as any));

    await TestBed.configureTestingModule({
      imports: [PartnerIntelligenceComponent, FormsModule],
      providers: [
        { provide: PartnerIntelligenceService, useValue: mockIntelligenceService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PartnerIntelligenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load data on init', () => {
    expect(component).toBeTruthy();
    expect(mockIntelligenceService.runInference).toHaveBeenCalledWith(1, false);
    expect(mockIntelligenceService.getRecommendations).toHaveBeenCalledWith(1, false);
    expect(mockIntelligenceService.getAnomalies).toHaveBeenCalledWith(false);
    expect(mockIntelligenceService.getForecasts).toHaveBeenCalled();
    expect(mockIntelligenceService.getSummaries).toHaveBeenCalledWith(false);
  });

  it('should handle recommendation decision', () => {
    component.onDecision({ id: 1, decision: 'APPROVED', comment: 'ok' });
    expect(mockIntelligenceService.decide).toHaveBeenCalledWith(1, 'APPROVED', 'ok');
    expect(component.selectedRecommendation).toBeNull();
    expect(mockIntelligenceService.getRecommendations).toHaveBeenCalledWith(1, true); // reloaded
  });
});
