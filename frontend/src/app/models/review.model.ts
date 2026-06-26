export interface Review {
  id: number;
  createdAt: string;
  eventCount?: number;
  hidden: boolean;
  authorId: number;
  authorName: string;
  locationId: number;
  locationName: string;
  eventId: number;
  eventName: string;
  rate: Rate;
  rootComment?: string;
}

export interface Rate {
  performance?: number;
  soundAndLighting?: number;
  venue?: number;
  overallImpression?: number;
}

export interface CreateReviewRequest {
  eventId: number;
  rate: Rate;
  comment?: string;
}
