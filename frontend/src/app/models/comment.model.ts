export interface Comment {
  id: number;
  text: string;
  createdAt: string;
  authorId: number;
  authorName: string;
  authorRole: string;
  reviewId: number;
  parentId?: number;
  replies: Comment[];
}
