import { SubjectResponse } from "./subject.model";

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}
  
export interface AuthResponse {
  token: string;
}

export interface UserResponse {
  id: number;
  email: string;
  username: string;
  role: string;
  subscriptions: SubjectResponse[];
}

export interface UserRequest {
  email: string;
  username: string;
  password?: string;
}