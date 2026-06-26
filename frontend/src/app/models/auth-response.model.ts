export interface AuthResponse {
  token: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  address?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}
