export interface AccountRequest {
  id: number;
  email: string;
  address?: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  createdAt: string;
  rejectionReason?: string;
}
