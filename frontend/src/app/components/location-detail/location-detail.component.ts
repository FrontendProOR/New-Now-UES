import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LocationService } from '../../services/location.service';
import { EventService } from '../../services/event.service';
import { ReviewService } from '../../services/review.service';
import { CommentService } from '../../services/comment.service';
import { Location, Event, Review, Comment } from '../../models';

@Component({
  selector: 'app-location-detail',
  templateUrl: './location-detail.component.html',
  styleUrls: ['./location-detail.component.scss']
})
export class LocationDetailComponent implements OnInit {
  location: Location | null = null;
  events: Event[] = [];
  reviews: Review[] = [];
  sortBy = 'date';
  sortDirection = 'desc';

  commentTrees: { [reviewId: number]: Comment[] } = {};
  expandedReviews: { [reviewId: number]: boolean } = {};
  replyingTo: { reviewId?: number; commentId?: number } | null = null;
  replyText = '';

  constructor(private route: ActivatedRoute,
              private locationService: LocationService,
              private eventService: EventService,
              private reviewService: ReviewService,
              private commentService: CommentService,
              private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.locationService.getById(id).subscribe(data => this.location = data);
    this.eventService.search({ locationId: id }).subscribe(data => this.events = data);
    this.loadReviews(id);
  }

  loadReviews(locationId?: number): void {
    const id = locationId || this.location?.id;
    if (!id) return;
    this.reviewService.getByLocation(id, this.sortBy, this.sortDirection)
      .subscribe(data => this.reviews = data);
  }

  onSortChange(sortBy: string): void {
    this.sortBy = sortBy;
    this.loadReviews();
  }

  toggleDirection(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.loadReviews();
  }

  toggleComments(reviewId: number): void {
    if (this.expandedReviews[reviewId]) {
      this.expandedReviews[reviewId] = false;
      return;
    }
    this.expandedReviews[reviewId] = true;
    this.loadComments(reviewId);
  }

  loadComments(reviewId: number): void {
    this.commentService.getCommentTree(reviewId).subscribe(data => {
      this.commentTrees[reviewId] = data;
    });
  }

  startReplyToReview(reviewId: number): void {
    this.replyingTo = { reviewId };
    this.replyText = '';
  }

  startReplyToComment(commentId: number): void {
    this.replyingTo = { commentId };
    this.replyText = '';
  }

  cancelReply(): void {
    this.replyingTo = null;
    this.replyText = '';
  }

  submitReply(): void {
    if (!this.replyText.trim() || !this.replyingTo) return;

    if (this.replyingTo.reviewId) {
      this.commentService.createComment(this.replyingTo.reviewId, this.replyText.trim()).subscribe({
        next: () => {
          this.loadComments(this.replyingTo!.reviewId!);
          this.expandedReviews[this.replyingTo!.reviewId!] = true;
          this.cancelReply();
        },
        error: (err) => this.snackBar.open(err.error?.error || 'Error posting comment', 'Close', { duration: 3000 })
      });
    } else if (this.replyingTo.commentId) {
      this.commentService.createReply(this.replyingTo.commentId, this.replyText.trim()).subscribe({
        next: (comment) => {
          this.loadComments(comment.reviewId);
          this.cancelReply();
        },
        error: (err) => this.snackBar.open(err.error?.error || 'Error posting reply', 'Close', { duration: 3000 })
      });
    }
  }
}
