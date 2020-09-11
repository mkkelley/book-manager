import { Author } from './author';
import { BookRead } from './book-read';

export class Book {
  id: number;
  title: string;
  author: Author;
  createdAt: number;
  published: number;
  bookReads: BookRead[];
  tags: string[];
}
