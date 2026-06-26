export interface User {
  id: number;
  email: string;
  name: string;
  phoneNumber?: string;
  birthday?: string;
  address?: string;
  city?: string;
  role: string;
  createdAt: string;
  imageUrl?: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}
