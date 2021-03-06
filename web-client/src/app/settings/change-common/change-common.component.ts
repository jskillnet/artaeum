import { Component, OnInit } from '@angular/core'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { Title } from '@angular/platform-browser'

import { Principal, AccountService } from '../../shared'

@Component({
  selector: 'ae-change-common',
  templateUrl: './change-common.component.html'
})
export class ChangeCommonComponent implements OnInit {

  form: FormGroup

  constructor(
    private principal: Principal,
    private accountService: AccountService,
    private title: Title
  ) {}

  ngOnInit(): void {
    this.title.setTitle('Common settings - Artaeum')
    this.form = new FormGroup({
      'login': new FormControl(null, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern('^[_\'.@A-Za-z0-9-]*$')
      ]),
      'email': new FormControl(null, [
        Validators.required,
        Validators.email,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]),
      'firstName': new FormControl(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50)
      ]),
      'lastName': new FormControl(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50)
      ]),
      'langKey': new FormControl(null, [
        Validators.required
      ])
    })
    this.setFormValues()
  }

  onSubmit(): void {
    this.accountService.save(this.form.value).subscribe()
  }

  private setFormValues(): void {
    this.principal.identity().then((user) => {
      this.form.setValue({
        'login': user.login,
        'email': user.email,
        'firstName': user.firstName,
        'lastName': user.lastName,
        'langKey': user.langKey
      })
    })
  }
}
