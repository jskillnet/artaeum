import { AllArticlesComponent } from './all-articles.component'
import { ResolvePagingParamsService } from '../../../shared'

export const allArticlesRoute = {
  path: '',
  component: AllArticlesComponent,
  resolve: { 'pagingParams': ResolvePagingParamsService }
}
