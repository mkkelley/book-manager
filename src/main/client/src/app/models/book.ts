import { BookRead } from './book-read';

export class Book {
  id: number;
  title: string;
  author: string;
  createdAt: number;
  published: number;
  bookReads: BookRead[];
  tags: string[];
}
