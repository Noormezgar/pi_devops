import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PartnersComponent } from './partners.component';
import { BusinessService } from '../../../core/services/business.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { Partner } from '../../../core/models/business.models';

describe('PartnersComponent', () => {
  let component: PartnersComponent;
  let fixture: ComponentFixture<PartnersComponent>;
  let mockBusinessService: jasmine.SpyObj<BusinessService>;

  const mockPartners: Partner[] = [
    { id: 1, name: 'Partner 1', contactEmail: 'p1@test.com', contactPhone: '123' },
    { id: 2, name: 'Partner 2', contactEmail: 'p2@test.com', contactPhone: '456' }
  ];

  beforeEach(async () => {
    mockBusinessService = jasmine.createSpyObj('BusinessService', ['getPartners', 'createPartner', 'updatePartner', 'deletePartner']);
    mockBusinessService.getPartners.and.returnValue(of(mockPartners));
    mockBusinessService.createPartner.and.returnValue(of({} as Partner));
    mockBusinessService.updatePartner.and.returnValue(of({} as Partner));
    mockBusinessService.deletePartner.and.returnValue(of(void 0));

    await TestBed.configureTestingModule({
      imports: [PartnersComponent, FormsModule],
      providers: [
        { provide: BusinessService, useValue: mockBusinessService }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PartnersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load partners on init', () => {
    expect(mockBusinessService.getPartners).toHaveBeenCalledWith(false);
    expect(component.partners.length).toBe(2);
    expect(component.filteredPartners.length).toBe(2);
  });

  it('should filter partners', () => {
    component.searchTerm = 'Partner 1';
    component.applyFilter();
    expect(component.filteredPartners.length).toBe(1);
    expect(component.filteredPartners[0].name).toBe('Partner 1');
  });

  it('should open create modal', () => {
    component.openCreate();
    expect(component.isEdit).toBeFalse();
    expect(component.showModal).toBeTrue();
    expect(component.form.name).toBe('');
  });

  it('should open edit modal', () => {
    component.openEdit(mockPartners[0]);
    expect(component.isEdit).toBeTrue();
    expect(component.showModal).toBeTrue();
    expect(component.form.name).toBe('Partner 1');
  });

  it('should save new partner', () => {
    component.isEdit = false;
    component.form = { name: 'New P', contactEmail: 'a@a.com', contactPhone: '123' };
    component.save();
    expect(mockBusinessService.createPartner).toHaveBeenCalledWith(component.form);
    expect(component.showModal).toBeFalse();
    expect(mockBusinessService.getPartners).toHaveBeenCalled();
  });

  it('should save edited partner', () => {
    component.isEdit = true;
    component.form = { id: 1, name: 'P', contactEmail: 'a', contactPhone: '1' };
    component.save();
    expect(mockBusinessService.updatePartner).toHaveBeenCalledWith(1, component.form);
    expect(component.showModal).toBeFalse();
  });

  it('should confirm delete', () => {
    component.confirmDelete(1);
    expect(component.deleteId).toBe(1);
    expect(component.showDeleteModal).toBeTrue();
  });

  it('should do delete', () => {
    component.deleteId = 1;
    component.doDelete();
    expect(mockBusinessService.deletePartner).toHaveBeenCalledWith(1);
    expect(component.showDeleteModal).toBeFalse();
    expect(component.deleteId).toBeNull();
  });
});
