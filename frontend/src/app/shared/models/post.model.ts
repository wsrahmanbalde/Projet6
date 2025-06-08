export interface PostRequest {
    title: string;
    content: string;
    subjectId: number;
  }

  export interface PostResponse {
    id: number;
    title: string;
    content: string;
    subjectName: string; // Si tu utilises un SubjectDTO, adapte ici.
    authorUsername: string;
    createdAt: string; // ou Date si tu les convertis au parsing
  }