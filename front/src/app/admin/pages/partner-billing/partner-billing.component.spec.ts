import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PartnerBillingComponent } from './partner-billing.component';
import { PartnerBillingService } from '../../../core/services/partner-billing.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

describe('PartnerBillingComponent', () => {
  let component: PartnerBillingComponent;
  let fixture: ComponentFixture<PartnerBillingComponent>;
  let mockBillingService: jasmine.SpyObj<PartnerBillingService>;

  const mockInvoices = [
    { id: 1, partnerId: 10, amount: 100, status: 'PENDING', periodStart: '2023-01-01', periodEnd: '2023-01-31', invoiceNumber: 'INV-001' },
    { id: 2, partnerId: 20, amount: 200, status: 'PAID', periodStart: '2023-02-01', periodEnd: '2023-02-28', invoiceNumber: 'INV-002' }
  ];

  beforeEach(async () => {
    mockBillingService = jasmine.createSpyObj('PartnerBillingService', ['getAllInvoices', 'generateInvoice', 'markAsPaid']);
    mockBillingService.getAllInvoices.and.returnValue(of(mockInvoices));
    mockBillingService.generateInvoice.and.returnValue(of({ id: 3, partnerId: 1, amount: 50, status: 'PENDING', periodStart: '2023', periodEnd: '2023' }));
    mockBillingService.markAsPaid.and.returnValue(of({ ...mockInvoices[0], status: 'PAID' }));

    await TestBed.configureTestingModule({
      imports: [PartnerBillingComponent, FormsModule, DatePipe],
      providers: [
        { provide: PartnerBillingService, useValue: mockBillingService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PartnerBillingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load invoices', () => {
    expect(component).toBeTruthy();
    expect(mockBillingService.getAllInvoices).toHaveBeenCalledWith(false);
    expect(component.invoices.length).toBe(2);
  });

  it('should filter invoices', () => {
    component.searchTerm = 'INV-001';
    expect(component.filteredInvoices.length).toBe(1);
    expect(component.filteredInvoices[0].invoiceNumber).toBe('INV-001');

    component.searchTerm = '';
    component.statusFilter = 'PAID';
    expect(component.filteredInvoices.length).toBe(1);
    expect(component.filteredInvoices[0].status).toBe('PAID');
  });

  it('should calculate stats', () => {
    expect(component.stats.total).toBe(2);
    expect(component.stats.pending).toBe(1);
    expect(component.stats.paid).toBe(1);
    expect(component.stats.amountTotal).toBe(300);
  });

  it('should generate invoice', () => {
    component.newInvoice = { partnerId: 1, amount: 50, periodStart: '2023', periodEnd: '2023' };
    component.generateInvoice();
    expect(mockBillingService.generateInvoice).toHaveBeenCalled();
    expect(component.invoices.length).toBe(3);
    expect(component.newInvoice.amount).toBe(0); // reset
  });

  it('should mark as paid', () => {
    component.markAsPaid(1);
    expect(mockBillingService.markAsPaid).toHaveBeenCalledWith(1);
    expect(component.invoices[0].status).toBe('PAID');
  });

  it('should handle pagination', () => {
    component.invoices = Array(10).fill({ status: 'PAID' });
    component.statusFilter = 'ALL';
    expect(component.totalPages).toBe(2);
    expect(component.paginatedInvoices.length).toBe(8);
    component.nextPage();
    expect(component.currentPage).toBe(2);
    component.previousPage();
    expect(component.currentPage).toBe(1);
  });
});
