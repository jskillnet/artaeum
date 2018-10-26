import { NgModule } from '@angular/core'
import { RouterModule } from '@angular/router'

import { SharedModule } from '../shared'
import { profileRoutes } from './profile.route'
import { ProfileComponent } from './profile.component'
import { WallComponent, ModalPostComponent, PostComponent, CreatePostComponent } from './wall'

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(profileRoutes)
  ],
  declarations: [
    ProfileComponent,
    WallComponent,
    ModalPostComponent,
    PostComponent,
    CreatePostComponent
  ]
})
export class ProfileModule {}
