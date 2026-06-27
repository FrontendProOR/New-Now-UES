import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChartConfiguration } from 'chart.js';
import { UserService } from '../../services/user.service';
import { ManagerService } from '../../services/manager.service';
import { CommentService } from '../../services/comment.service';
import { Location, Review, Event, Comment } from '../../models';
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

  // --- M1: Location Edit ---
  locationEditForm: FormGroup;
  locationName = '';
  locationTypes = ['CLUB', 'THEATRE', 'CINEMA', 'GALLERY', 'RESTAURANT', 'CULTURAL_CENTER', 'OPEN_AIR', 'OTHER'];

  // --- M2: Event Edit/Delete ---
  events: Event[] = [];
  editingEvent: Event | null = null;
  editEventForm: FormGroup;
  selectedEditEventImage: File | null = null;

  // --- M3: Comments ---
  commentTrees: Map<number, Comment[]> = new Map();
  replyingTo: { reviewId?: number; commentId?: number } | null = null;
  replyText = '';

  constructor(private userService: UserService,
              private managerService: ManagerService,
              private commentService: CommentService,
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

    this.locationEditForm = this.fb.group({
      address: [''],
      type: [''],
      description: ['']
    });

    this.editEventForm = this.fb.group({
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
      .subscribe(data => {
        this.reviews = data;
        this.commentTrees = new Map();
        data.forEach(r => this.loadComments(r.id));
      });
    this.loadEvents();
    this.loadLocationDetails();
    this.loadAnalytics();
  }

  // --- M1: Location Edit ---
  loadLocationDetails(): void {
    if (!this.selectedLocationId) return;
    this.managerService.getLocationDetails(this.selectedLocationId).subscribe(loc => {
      this.locationName = loc.name;
      this.locationEditForm.patchValue({
        address: loc.address || '',
        type: loc.type || '',
        description: loc.description || ''
      });
    });
  }

  saveLocationEdit(): void {
    if (!this.selectedLocationId) return;
    const val = this.locationEditForm.value;
    this.managerService.updateLocation(this.selectedLocationId, {
      address: val.address,
      type: val.type,
      description: val.description
    }).subscribe({
      next: () => {
        this.snackBar.open('Location updated', 'Close', { duration: 3000 });
        this.loadLocationDetails();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error updating location', 'Close', { duration: 3000 })
    });
  }

  // --- M2: Event Edit/Delete ---
  loadEvents(): void {
    if (!this.selectedLocationId) return;
    this.managerService.getLocationEvents(this.selectedLocationId).subscribe(data => {
      this.events = data;
    });
  }

  deleteEvent(id: number): void {
    if (!confirm('Are you sure you want to delete this event?')) return;
    this.managerService.deleteEvent(id).subscribe({
      next: () => {
        this.snackBar.open('Event deleted', 'Close', { duration: 3000 });
        this.loadEvents();
        this.loadAnalytics();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error deleting event', 'Close', { duration: 3000 })
    });
  }

  editEvent(event: Event): void {
    this.editingEvent = event;
    this.selectedEditEventImage = null;
    this.editEventForm.patchValue({
      name: event.name,
      address: event.address || '',
      type: event.type,
      date: event.date ? event.date.substring(0, 10) : '',
      price: event.price,
      recurrent: event.recurrent
    });
  }

  cancelEditEvent(): void {
    this.editingEvent = null;
    this.editEventForm.reset();
    this.selectedEditEventImage = null;
  }

  onEditEventImageSelected(ev: any): void {
    this.selectedEditEventImage = ev.target.files[0];
  }

  saveEventEdit(): void {
    if (!this.editingEvent || this.editEventForm.invalid) return;
    const formData = new FormData();
    const val = this.editEventForm.value;
    formData.append('name', val.name);
    if (val.address) formData.append('address', val.address);
    formData.append('type', val.type);
    formData.append('date', val.date);
    formData.append('price', val.price?.toString() || '0');
    formData.append('recurrent', val.recurrent?.toString() || 'false');
    if (this.selectedEditEventImage) {
      formData.append('image', this.selectedEditEventImage);
    }

    this.managerService.updateEvent(this.editingEvent.id, formData).subscribe({
      next: () => {
        this.snackBar.open('Event updated', 'Close', { duration: 3000 });
        this.editingEvent = null;
        this.editEventForm.reset();
        this.selectedEditEventImage = null;
        this.loadEvents();
        this.loadAnalytics();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error updating event', 'Close', { duration: 3000 })
    });
  }

  // --- Analytics ---
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

  // --- Reviews ---
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

  // --- M3: Comments ---
  loadComments(reviewId: number): void {
    this.commentService.getCommentTree(reviewId).subscribe(data => {
      this.commentTrees.set(reviewId, data);
    });
  }

  toggleReply(reviewId?: number, commentId?: number): void {
    if (this.replyingTo &&
        this.replyingTo.reviewId === reviewId &&
        this.replyingTo.commentId === commentId) {
      this.replyingTo = null;
      this.replyText = '';
    } else {
      this.replyingTo = { reviewId, commentId };
      this.replyText = '';
    }
  }

  isReplying(reviewId?: number, commentId?: number): boolean {
    if (!this.replyingTo) return false;
    return this.replyingTo.reviewId === reviewId && this.replyingTo.commentId === commentId;
  }

  submitReply(): void {
    if (!this.replyingTo || !this.replyText.trim()) return;

    if (this.replyingTo.commentId) {
      // Reply to a comment
      this.commentService.createReply(this.replyingTo.commentId, this.replyText.trim()).subscribe({
        next: () => {
          this.snackBar.open('Reply posted', 'Close', { duration: 3000 });
          if (this.replyingTo?.reviewId) {
            this.loadComments(this.replyingTo.reviewId);
          }
          this.replyingTo = null;
          this.replyText = '';
        },
        error: (err) => this.snackBar.open(err.error?.error || 'Error posting reply', 'Close', { duration: 3000 })
      });
    } else if (this.replyingTo.reviewId) {
      // Root comment on review
      this.commentService.createComment(this.replyingTo.reviewId, this.replyText.trim()).subscribe({
        next: () => {
          this.snackBar.open('Comment posted', 'Close', { duration: 3000 });
          if (this.replyingTo?.reviewId) {
            this.loadComments(this.replyingTo.reviewId);
          }
          this.replyingTo = null;
          this.replyText = '';
        },
        error: (err) => this.snackBar.open(err.error?.error || 'Error posting comment', 'Close', { duration: 3000 })
      });
    }
  }

  // --- Create Event ---
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
        this.loadEvents();
        this.loadAnalytics();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error', 'Close', { duration: 3000 })
    });
  }
}
