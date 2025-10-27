import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, LoginRequest, RegisterRequest } from '../../core/services/auth.service';
import { AuthStateService } from '../../core/services/auth-state.service';
import Swal from 'sweetalert2'; 

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  mode: 'login' | 'register' = 'login';
  email = '';
  password = '';
  fullName = '';
  phone = '';
  dni = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private authState: AuthStateService
  ) { }

  onSubmit() {
    if (this.mode === 'login') {
      const request: LoginRequest = {
        email: this.email.trim(),
        password: this.password
      };

      if (!request.email || !request.password) {
        Swal.fire({
          icon: 'warning',
          title: 'Campos incompletos',
          text: 'Por favor ingresa tu correo y contraseña.'
        });
        return;
      }

      this.authService.login(request).subscribe({
        next: (res) => {
          this.authService.saveSession(res);
          this.authState.setLoggedIn(true);

          Swal.fire({
            icon: 'success',
            title: '¡Bienvenido!',
            text: 'Inicio de sesión exitoso',
            showConfirmButton: false,
            timer: 1500
          }).then(() => {
            this.router.navigate(['/']);
          });
        },
        error: (err) => {
          console.error(err);

          let message = ' Credenciales incorrectas o usuario no existe';
          if (err.status === 0) message = 'No se pudo conectar con el servidor';
          if (err.status === 500) message = 'Error interno del servidor';

          Swal.fire({
            icon: 'error',
            title: 'Error al iniciar sesión',
            text: message
          });
        }
      });

    } else {
      const request: RegisterRequest = {
        fullName: this.fullName.trim(),
        email: this.email.trim(),
        password: this.password,
        phone: this.phone,
        dni: this.dni
      };

      if (!request.fullName || !request.email || !request.password || !request.phone || !request.dni) {
        Swal.fire({
          icon: 'warning',
          title: 'Campos incompletos',
          text: 'Por favor llena todos los campos del registro.'
        });
        return;
      }

      this.authService.register(request).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: '¡Cuenta creada!',
            text: 'Tu cuenta se ha registrado correctamente. Ahora puedes iniciar sesión.'
          }).then(() => {
            this.switchMode(null, 'login');
          });
        },
        error: (err) => {
          console.error(err);
          Swal.fire({
            icon: 'error',
            title: 'Error al registrarse',
            text: err.error?.message || 'No se pudo completar el registro'
          });
        }
      });
    }
  }

  switchMode(event: any, mode: 'login' | 'register') {
    if (event) event.preventDefault();
    this.mode = mode;
  }

  forgotPassword(e: Event) {
    e.preventDefault();
    Swal.fire({
      icon: 'info',
      title: 'Función pendiente',
      text: 'La recuperación de contraseña aún no está implementada.'
    });
  }
}
