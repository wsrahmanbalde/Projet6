export interface CommentRequest {
    content: string;
    postId: number;
  }
  
  export interface CommentResponse {
    id: number;
    content: string;
    authorUsername: string;
    postId: number;
    createdAt: string;
  }