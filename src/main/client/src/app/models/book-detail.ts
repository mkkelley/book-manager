import { Book } from './book';
import { BookNote } from './book-note';

export class BookDetail extends Book {
  notes: BookNote[];
}
