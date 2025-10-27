import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path: '',
        loadChildren: () =>
            import('./modules/home/home.module').then((m) => m.HomeModule)
    },
    {
        path: 'categories',
        loadChildren: () =>
            import('./modules/category-list/category-list.module').then((m) => m.CategoryListModule)
    },
    {
        path: 'login',
        loadChildren: () =>
            import('./modules/auth/auth.module').then((m) => m.AuthModule)
    },
    {
        path: 'account',
        loadChildren: () =>
            import('./modules/account/account.module').then(m => m.AccountModule)
    },
    {
        path: 'checkout',
        loadChildren: () =>
            import('./modules/checkout/checkout.module').then(m => m.CheckoutModule)
    },

    {
        path: 'products',
        loadChildren: () =>
            import('./modules/products/products.module').then((m) => m.ProductsModule)
    },
    {
        path: 'admin',
        loadChildren: () =>
            import('./modules/admin/admin.module').then((m) => m.AdminModule)
    },
    { path: '**', redirectTo: '' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
