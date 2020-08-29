import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookIndexComponent } from './components/book-index/book-index.component';
import { LoginComponent } from './components/login/login.component';
import { AuthenticationGuard } from './guards/authentication.guard';
import { BookDetailComponent } from './components/book-detail/book-detail.component';
import { BookDetailResolver } from './resolvers/book-detail-resolver.service';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'books',
  },
  {
    path: 'books',
    canActivate: [AuthenticationGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: BookIndexComponent,
      },
      {
        path: ':bookId',
        component: BookDetailComponent,
        pathMatch: 'full',
        resolve: {
          book: BookDetailResolver,
        },
      },
    ],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
