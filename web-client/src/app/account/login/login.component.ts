import { Component, OnInit } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { Router } from '@angular/router'
import { FormGroup, FormControl, Validators } from '@angular/forms'

import { LoginService } from '../../shared'

@Component({
  selector: 'ae-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup

  constructor(
    private title: Title,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.title.setTitle('Login - Artaeum')
    this.form = new FormGroup({
      'username': new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern('^[_\'.@A-Za-z0-9-]*$')
      ]),
      'password': new FormControl('', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(50)
      ])
    })
  }

  onSubmit(): void {
    const formData = this.form.value
    this.loginService.login({
      username: formData.username,
      password: formData.password
    }).then(() => {
      this.router.navigate(['/']).then(() => location.reload())
    })
  }
}
