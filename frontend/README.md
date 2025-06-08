# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.1.6.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.

src/
│
├── app/
│   ├── main.ts                     # Point d’entrée de l'application
│   ├── app.config.ts               # Configuration root avec `provideRouter`, `provideHttpClient`, etc.
│   ├── app.routes.ts               # Toutes les routes de l'application
│
│   ├── core/                       # Services globaux (auth, interceptors, guards)
│   │   ├── auth/
│   │   │   ├── auth.service.ts
│   │   │   ├── auth.guard.ts
│   │   │   ├── jwt.interceptor.ts
│   │   │   └── auth.util.ts        # (facultatif)
│   │   ├── config/                 # Constantes, tokens, environnement
│   │   └── http/                   # Global interceptors, API config
│
│   ├── shared/                     # UI réutilisable
│   │   ├── components/
│   │   │   ├── home/
│   │   │   │   └── home.component.ts
│   │   │   └── ...
│   │   ├── models/                 # Interfaces et DTO partagés
│   │   ├── pipes/                  # Standalone pipes
│   │   └── directives/             # Standalone directives
│
│   ├── features/                   # Fonctionnalités métier
│   │   ├── auth/
│   │   │   ├── login/
│   │   │   │   └── login.component.ts
│   │   │   ├── register/
│   │   │   │   └── register.component.ts
│   │   │   └── auth.routes.ts
│   │   ├── user/
│   │   └── posts/
│
│   └── layout/                     # (facultatif) Composants de structure : header/footer
│       ├── header.component.ts
│       ├── footer.component.ts
│       └── layout.component.ts
│
├── assets/
├── environments/
└── index.html