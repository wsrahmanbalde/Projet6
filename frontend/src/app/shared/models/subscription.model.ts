export interface SubscriptionRequest {
    subjectId: number;
  }
  
  export interface SubscriptionResponse {
    id: number;
    username: string;
    subjectName: string;
    subjectDescription: string;
    subscriptionDate: string;
  }