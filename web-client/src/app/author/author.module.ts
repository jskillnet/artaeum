import { NgModule } from '@angular/core'
import { RouterModule } from '@angular/router'

import { SharedModule } from '../shared'
import { authorRoutes } from './author.route'
import { CreateUpdateArticleComponent } from './create-update-article'
import { AuthorComponent } from './author.component'
import { DashboardComponent } from './dashboard'
import { CategoriesComponent } from './categories'
import { AllArticlesComponent } from './all-articles'

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(authorRoutes)
  ],
  declarations: [
    AuthorComponent,
    CreateUpdateArticleComponent,
    DashboardComponent,
    CategoriesComponent,
    AllArticlesComponent
  ]
})
export class AuthorModule {}
