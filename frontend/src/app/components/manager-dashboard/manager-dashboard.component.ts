import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChartConfiguration } from 'chart.js';
import { UserService } from '../../services/user.service';
import { ManagerService } from '../../services/manager.service';
import { Location, Review } from '../../models';
import { Analytics } from '../../models';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.scss']
})
export class ManagerDashboardComponent implements OnInit {
  managedLocations: Location[] = [];
  selectedLocationId: number | null = null;
  reviews: Review[] = [];
  analytics: Analytics | null = null;
  period = 'monthly';
  activeManagerTab = 0;

  eventForm: FormGroup;
  selectedEventImage: File | null = null;

  chartData: ChartConfiguration<'pie'>['data'] | null = null;
  chartOptions: ChartConfiguration<'pie'>['options'] = { responsive: true };

  constructor(private userService: UserService,
              private managerService: ManagerService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar) {
    this.eventForm = this.fb.group({
      name: ['', Validators.required],
      address: [''],
      type: ['', Validators.required],
      date: ['', Validators.required],
      price: [0],
      recurrent: [false]
    });
  }

  ngOnInit(): void {
    this.userService.getManagedLocations().subscribe(data => {
      this.managedLocations = data;
      if (data.length > 0) {
        this.selectedLocationId = data[0].id;
        this.loadData();
      }
    });
  }

  onLocationChange(): void {
    this.loadData();
  }

  loadData(): void {
    if (!this.selectedLocationId) return;
    this.managerService.getLocationReviews(this.selectedLocationId)
      .subscribe(data => this.reviews = data);
    this.loadAnalytics();
  }

  loadAnalytics(): void {
    if (!this.selectedLocationId) return;
    this.managerService.getAnalytics(this.selectedLocationId, this.period)
      .subscribe(data => {
        this.analytics = data;
        this.buildChart();
      });
  }

  buildChart(): void {
    if (!this.analytics) return;
    this.chartData = {
      labels: ['Recurrent', 'Non-Recurrent', 'Free', 'Paid'],
      datasets: [{
        data: [
          this.analytics.recurrentEvents,
          this.analytics.nonRecurrentEvents,
          this.analytics.freeEvents,
          this.analytics.paidEvents
        ],
        backgroundColor: ['#673ab7', '#ff9800', '#4caf50', '#f44336']
      }]
    };
  }

  hideReview(id: number): void {
    this.managerService.hideReview(id).subscribe({
      next: () => { this.snackBar.open('Review hidden', 'Close', { duration: 3000 }); this.loadData(); }
    });
  }

  deleteReview(id: number): void {
    if (!confirm('Delete this review? Rating will be removed.')) return;
    this.managerService.deleteReview(id).subscribe({
      next: () => { this.snackBar.open('Review deleted', 'Close', { duration: 3000 }); this.loadData(); }
    });
  }

  onEventImageSelected(event: any): void { this.selectedEventImage = event.target.files[0]; }

  createEvent(): void {
    if (this.eventForm.invalid || !this.selectedLocationId || !this.selectedEventImage) return;
    const formData = new FormData();
    const val = this.eventForm.value;
    formData.append('name', val.name);
    if (val.address) formData.append('address', val.address);
    formData.append('type', val.type);
    formData.append('date', val.date);
    formData.append('price', val.price?.toString() || '0');
    formData.append('recurrent', val.recurrent?.toString() || 'false');
    formData.append('image', this.selectedEventImage);

    this.managerService.createEvent(this.selectedLocationId, formData).subscribe({
      next: () => {
        this.snackBar.open('Event created', 'Close', { duration: 3000 });
        this.eventForm.reset();
        this.selectedEventImage = null;
        this.loadAnalytics();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error', 'Close', { duration: 3000 })
    });
  }
}
