import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

import DefaultLayout from '@/layouts/DefaultLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'

const routes = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      { path: '', name: 'Home', component: () => import('@/pages/Home.vue') },
      { path: 'products', name: 'ProductList', component: () => import('@/pages/ProductList.vue') },
      { path: 'products/:slug', name: 'ProductDetail', component: () => import('@/pages/ProductDetail.vue') },
      { path: 'category/:slug', name: 'CategoryProducts', component: () => import('@/pages/ProductList.vue') },
      { path: 'cart', name: 'Cart', component: () => import('@/pages/Cart.vue') },
      { path: 'checkout', name: 'Checkout', component: () => import('@/pages/Checkout.vue'), meta: { requiresAuth: true } },
      { path: 'order-confirmation/:id', name: 'OrderConfirmation', component: () => import('@/pages/OrderConfirmation.vue'), meta: { requiresAuth: true } },
      { path: 'login', name: 'Login', component: () => import('@/pages/Login.vue') },
      { path: 'register', name: 'Register', component: () => import('@/pages/Register.vue') },
      { path: 'forgot-password', name: 'ForgotPassword', component: () => import('@/pages/ForgotPassword.vue') },
      { path: 'account', name: 'Account', component: () => import('@/pages/Account.vue'), meta: { requiresAuth: true } },
      { path: 'orders', name: 'OrderHistory', component: () => import('@/pages/OrderHistory.vue'), meta: { requiresAuth: true } },
      { path: 'wishlist', name: 'Wishlist', component: () => import('@/pages/Wishlist.vue') },
      { path: 'about', name: 'About', component: () => import('@/pages/About.vue') },
      { path: 'contact', name: 'Contact', component: () => import('@/pages/Contact.vue') },
      { path: 'privacy-policy', name: 'PrivacyPolicy', component: () => import('@/pages/PrivacyPolicy.vue') },
      { path: 'terms', name: 'TermsConditions', component: () => import('@/pages/TermsConditions.vue') },
      { path: 'search', name: 'Search', component: () => import('@/pages/Search.vue') },
    ],
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('@/pages/admin/Dashboard.vue') },
      { path: 'products', name: 'AdminProducts', component: () => import('@/pages/admin/ProductManage.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('@/pages/admin/OrderManage.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('@/pages/admin/UserManage.vue') },
      { path: 'categories', name: 'AdminCategories', component: () => import('@/pages/admin/CategoryManage.vue') },
      { path: 'coupons', name: 'AdminCoupons', component: () => import('@/pages/admin/CouponManage.vue') },
      { path: 'banners', name: 'AdminBanners', component: () => import('@/pages/admin/BannerManage.vue') },
      { path: 'reviews', name: 'AdminReviews', component: () => import('@/pages/admin/ReviewManage.vue') },
      { path: 'settings', name: 'AdminSettings', component: () => import('@/pages/admin/Settings.vue') },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/pages/NotFound.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }

  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return { name: 'Home' }
  }

  if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    return { name: 'Home' }
  }
})

export default router
