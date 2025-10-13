import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    { path: 'auth', 
        loadChildren: () => 
            import('./modules/auth/auth.module')
        .then(m => m.AuthModule) },
    { path: 'products', 
        loadChildren: () => 
            import('./modules/products/products.module')
        .then(m => m.ProductsModule) },
    { path: 'cart', 
        loadChildren: () => 
            import('./modules/cart/cart.module')
        .then(m => m.CartModule) },
    { path: 'orders', 
        loadChildren: () => 
            import('./modules/orders/orders.module')
        .then(m => m.OrdersModule) },
    { path: 'admin', 
        loadChildren: () =>
             import('./modules/admin/admin.module')
        .then(m => m.AdminModule) },
    { path: 'home', 
        loadChildren: () => 
            import('./modules/home/home.module')
        .then(m => m.HomeModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
