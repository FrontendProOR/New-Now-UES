import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EventService } from '../../services/event.service';
import { ReviewService } from '../../services/review.service';
import { Event } from '../../models';

@Component({
  selector: 'app-create-review',
  templateUrl: './create-review.component.html',
  styleUrls: ['./create-review.component.scss']
})
export class CreateReviewComponent implements OnInit {
  reviewForm: FormGroup;
  events: Event[] = [];
  locationId!: number;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService,
              private reviewService: ReviewService,
              private snackBar: MatSnackBar) {
    this.reviewForm = this.fb.group({
      eventId: ['', Validators.required],
      performance: [null],
      soundAndLighting: [null],
      venue: [null],
      overallImpression: [null],
      comment: ['']
    });
  }

  ngOnInit(): void {
    this.locationId = Number(this.route.snapshot.paramMap.get('locationId'));
    this.eventService.search({ locationId: this.locationId })
      .subscribe(data => this.events = data.filter(e => e.recurrent));
  }

  onSubmit(): void {
    if (this.reviewForm.invalid) return;
    const val = this.reviewForm.value;
    const request = {
      eventId: val.eventId,
      rate: {
        performance: val.performance,
        soundAndLighting: val.soundAndLighting,
        venue: val.venue,
        overallImpression: val.overallImpression
      },
      comment: val.comment || undefined
    };
    this.reviewService.create(this.locationId, request).subscribe({
      next: () => {
        this.snackBar.open('Review submitted!', 'Close', { duration: 3000 });
        this.router.navigate(['/locations', this.locationId]);
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Failed to submit review', 'Close', { duration: 3000 })
    });
  }
}
