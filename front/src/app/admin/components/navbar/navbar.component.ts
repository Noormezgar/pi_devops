import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DashboardService } from '../../../core/services/dashboard.service';
import { OnboardingService } from '../../../core/services/onboarding.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly onboarding = inject(OnboardingService);
  private readonly router = inject(Router);
  pendingRescheduleCount = 0;

  ngOnInit(): void {
    this.dashboardService.getAdminStats().subscribe({
      next: (stats) => { this.pendingRescheduleCount = stats.pendingReschedules ?? 0; },
      error: () => {}
    });
  }

  canShowTourEntry(): boolean {
    return this.onboarding.canShowHeaderEntry() && this.isBusinessSection();
  }

  private isBusinessSection(): boolean {
    const url = this.router.url.toLowerCase();
    return [
      '/admin/partners',
      '/admin/deals',
      '/admin/packs',
      '/admin/access-codes',
      '/admin/partner-performance',
      '/admin/partner-contracts',
      '/admin/partner-billing',
      '/admin/partner-intelligence',
      '/admin/voucher-fraud'
    ].some(path => url.startsWith(path));
  }

  startTour(): void {
    this.onboarding.startTour();
  }
}