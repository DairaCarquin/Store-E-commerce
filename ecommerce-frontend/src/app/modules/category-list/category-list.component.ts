import { Component, OnInit, OnDestroy } from '@angular/core';
import { CategoryService, Category } from '../../core/services/category.service';
import { ProductService, Product } from '../../core/services/product.service';
import { CategoryStateService } from '../../core/services/category-state.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
import { CartVisibilityService } from '../../core/services/cart-visibility.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit, OnDestroy {
  categories: Category[] = [];
  selectedCategory: Category | null = null;
  products: Product[] = [];
  searchQuery = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  userId = 1;
  page = 0;
  totalPages = 1;
  pageSize = 8;
  adding = false;

  private searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(
    public authService: AuthService,
    private categoryService: CategoryService,
    private productService: ProductService,
    private categoryState: CategoryStateService,
    private route: ActivatedRoute,
    private cartService: CartService,
    private cartVisibility: CartVisibilityService,
    public router: Router
  ) { }

  ngOnInit(): void {
    this.categoryState.initialize();

    this.searchSubject
      .pipe(debounceTime(400), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(query => this.watchSearchQuery(query));

    this.categoryService.getAll().subscribe({
      next: (res) => {
        this.categories = res;
        this.route.queryParams.subscribe(params => {
          const categoryId = Number(params['id']);
          const found = res.find(c => c.id === categoryId);
          const selected = found || this.categoryState.getCategory() || res[0];
          if (selected) this.selectCategory(selected);
        });
      },
      error: () => (this.categories = [])
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  selectCategory(cat: Category | { id: number; name: string }) {
    this.selectedCategory = cat as Category;
    this.page = 0;
    this.categoryState.setCategory({ id: cat.id, name: cat.name });
    this.loadProducts(cat.id);
  }

  loadProducts(categoryId: number) {
    this.productService.getByCategory(categoryId, this.page, this.pageSize).subscribe({
      next: (res) => {
        this.products = res.content;
        this.totalPages = res.totalPages;
      },
      error: () => (this.products = [])
    });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.loadProducts(this.selectedCategory!.id);
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadProducts(this.selectedCategory!.id);
    }
  }

  onPageSizeChange() {
    this.page = 0;
    if (this.selectedCategory) {
      this.loadProducts(this.selectedCategory.id);
    }
  }

  onSearchChange(query: string) {
    this.searchSubject.next(query);
  }

  watchSearchQuery(query: string) {
    if (!this.selectedCategory) return;
    query = query.trim();
    if (query === '') {
      this.loadProducts(this.selectedCategory.id);
      return;
    }

    this.productService.getBySearch(query, this.page, this.pageSize).subscribe({
      next: (res) => {
        this.products = res.content.filter(p => p.categoryId === this.selectedCategory!.id);
        this.totalPages = res.totalPages;
      },
      error: () => (this.products = [])
    });
  }

  applyPriceFilter() {
    this.searchQuery = '';
  }

  clearFilters() {
    this.searchQuery = '';
    this.minPrice = null;
    this.maxPrice = null;
    if (this.selectedCategory) this.loadProducts(this.selectedCategory.id);
  }

  get filteredProducts() {
    return this.products.filter(p => {
      const nameMatch = p.name.toLowerCase().includes(this.searchQuery.toLowerCase());
      const minMatch = this.minPrice == null || p.price >= this.minPrice;
      const maxMatch = this.maxPrice == null || p.price <= this.maxPrice;
      return nameMatch && minMatch && maxMatch;
    });
  }

  getGridPosition(index: number) {
    const col = (index % 4) + 2;
    const row = Math.floor(index / 4) + 1;
    return {
      'grid-column-start': col,
      'grid-row-start': row
    };
  }

  validateNameInput(event: any) {
    const input = event.target;
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÜüÑñ\s]*$/;
    if (!regex.test(input.value)) {
      input.value = input.value.replace(/[^A-Za-zÁÉÍÓÚáéíóúÜüÑñ\s]/g, '');
      this.searchQuery = input.value;
    }
  }

  addToCart(productId: number) {
    if (this.adding) return;
    this.adding = true;

    const product = this.products.find(p => p.id === productId);
    if (!product) return;

    const userId = this.authService.getUserId();
    if (!userId) return;

    const currentCart = this.cartService['cartSubject'].value || { items: [] };
    const existing = currentCart.items.find(i => i.productId === productId);
    const qty = existing ? existing.quantity + 1 : 1;

    this.cartService
      .addItem(
        userId,
        productId,
        qty,
        product.name,
        product.price,
        product.description || 'Sin descripción',
        product.imageBase64 || ''
      )
      .subscribe({
        next: (cart) => {
          console.info('✅ Producto agregado correctamente al carrito:', cart);
          this.cartService['cartSubject'].next(cart);
          this.cartService['saveLocal'](cart);
          this.cartVisibility.open();
        },
        error: (err) => {
          console.warn('Error al agregar producto:', err);
          Swal.fire('Error', 'No se pudo agregar el producto al carrito', 'error');
        },
        complete: () => (this.adding = false) 
      });
  }


  increaseQty(productId: number) {
    const cart = this.cartService['cartSubject'].value;
    const userId = this.authService.getUserId();
    if (!cart || !userId) return;

    const item = cart.items.find(i => i.productId === productId);
    if (!item) return;

    const newQty = item.quantity + 1;
    this.cartService.updateItem(userId, productId, newQty);
  }

  decreaseQty(productId: number) {
    const cart = this.cartService['cartSubject'].value;
    const userId = this.authService.getUserId();
    if (!cart || !userId) return;

    const item = cart.items.find(i => i.productId === productId);
    if (!item) return;

    const newQty = item.quantity - 1;
    if (newQty <= 0) {
      this.cartService.removeItem(userId, productId);
    } else {
      this.cartService.updateItem(userId, productId, newQty);
    }
  }


  removeFromCart(productId: number) {
    if (!this.authService.isAuthenticated()) {
      alert('⚠️ Debes iniciar sesión para modificar tu carrito.');
      this.router.navigate(['/login']);
      return;
    }

    const userId = this.authService.getUserId();
    if (!userId) return;

    const cart = this.cartService['cartSubject'].value;
    if (!cart) return;

    const existing = cart.items.find(i => i.productId === productId);
    if (!existing) return;

    const qty = existing.quantity - 1;

    if (qty <= 0) {
      this.cartService.removeItem(userId, productId);
    } else {
      this.cartService.updateItem(userId, productId, qty);
    }
  }


  isInCart(productId: number): boolean {
    const cart = this.cartService['cartSubject'].value;
    return !!cart?.items?.some(i => i.productId === productId);
  }

  getCartQuantity(productId: number): number {
    const cart = this.cartService['cartSubject'].value;
    const item = cart?.items?.find(i => i.productId === productId);
    return item ? item.quantity : 0;
  }

  toggleCart() {
    document.querySelector('app-cart-sidebar')?.classList.toggle('open');
  }

  handleAddClick(productId: number) {
    if (!this.authService.isAuthenticated()) {
      Swal.fire({
        title: 'Inicia sesión para continuar',
        text: 'Debes iniciar sesión para agregar productos al carrito.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Ir al login',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#009fe3'
      }).then(result => {
        if (result.isConfirmed) {
          this.router.navigate(['/login']);
        }
      });
      return;
    }

    this.addToCart(productId);
  }
}
