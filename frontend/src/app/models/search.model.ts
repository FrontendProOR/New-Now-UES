export interface LocationSearchResult {
  id: number;
  name: string;
  description?: string;
  reviewCount?: number;
  avgPerformanceGrade?: number;
  avgSoundGrade?: number;
  avgLightingGrade?: number;
  avgSpaceGrade?: number;
  avgExperienceGrade?: number;
  highlights?: { [key: string]: string };
}

export interface Analytics {
  totalEvents: number;
  recurrentEvents: number;
  nonRecurrentEvents: number;
  freeEvents: number;
  paidEvents: number;
  topEventsByRating: EventRating[];
  bottomEventsByRating: EventRating[];
  latestReviewsFromTopLocation: any[];
}

export interface EventRating {
  eventId: number;
  eventName: string;
  locationName: string;
  averageRating: number;
}
