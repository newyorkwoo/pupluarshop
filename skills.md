# PopularShop — 精品電商平台開發技能指南 (skills.md)

> 風格參考：[Mytheresa](https://www.mytheresa.com/) — 極簡奢華、黑白為主調、大量留白、高質感圖片展示  
> 目標：打造一個符合安全規範、保護個資、具備管理後台的全端精品電商網站

---

## 一、專案技術棧總覽

| 層級     | 技術                        | 版本建議 |
| -------- | --------------------------- | -------- |
| 前端框架 | Vue 3 (Composition API)     | ^3.4+    |
| 建置工具 | Vite                        | ^5.x     |
| 狀態管理 | Pinia                       | ^2.x     |
| CSS 框架 | Tailwind CSS                | ^3.4+    |
| 語言     | JavaScript (ES2022+)        | —        |
| 後端框架 | Spring Boot                 | ^3.2+    |
| 安全框架 | Spring Security 6           | ^6.2+    |
| 資料庫   | PostgreSQL                  | ^16.x    |
| ORM      | Spring Data JPA / Hibernate | —        |
| API 風格 | RESTful + JWT 認證          | —        |
| 容器化   | Docker + Docker Compose     | —        |

---

## 二、設計風格與色調系統 (參考 Mytheresa)

### 2.1 色調定義 (Tailwind 自訂色票)

```js
// tailwind.config.js 色彩擴展
colors: {
  brand: {
    black:    '#1A1A1A',   // 主色 — 導航列、標題、按鈕
    charcoal: '#333333',   // 次要文字
    gray:     '#757575',   // 輔助文字、icon
    light:    '#F5F5F5',   // 背景底色
    white:    '#FFFFFF',   // 主背景
    cream:    '#FAF9F7',   // 卡片/區塊背景
    gold:     '#C8A96E',   // 重點強調 (CTA hover、VIP標籤)
    error:    '#D32F2F',   // 錯誤提示
    success:  '#2E7D32',   // 成功提示
  }
}
```

### 2.2 設計原則

- **極簡留白**：大量 white space，讓商品圖片成為視覺焦點
- **字體**：無襯線體為主 (`Inter`, `Helvetica Neue`, 系統字體)；中文使用 `Noto Sans TC`
- **排版層級**：標題大寫字母間距加寬 (`tracking-widest`)，正文 `text-sm` 至 `text-base`
- **按鈕風格**：黑底白字為主 CTA，hover 時微動畫；次要按鈕為白底黑框
- **商品卡片**：無邊框，hover 時輕微放大 (`scale-[1.02]`) + 陰影
- **圖片比例**：商品圖統一 3:4 比例，banner 採 16:9 或全幅

---

## 三、前端架構 (Vue 3 + Vite + Pinia + Tailwind)

### 3.1 專案目錄結構

```
frontend/
├── public/
│   └── favicon.ico
├── src/
│   ├── api/                  # Axios 封裝 & API 模組
│   │   ├── axios.js          # Axios 實例 (攔截器、baseURL、JWT)
│   │   ├── auth.js           # 登入/註冊/登出 API
│   │   ├── product.js        # 商品相關 API
│   │   ├── cart.js           # 購物車 API
│   │   ├── order.js          # 訂單 API
│   │   ├── user.js           # 使用者資料 API
│   │   └── admin.js          # 管理後台 API
│   ├── assets/               # 靜態資源 (圖片、字體)
│   ├── components/           # 共用元件
│   │   ├── layout/
│   │   │   ├── Navbar.vue           # 頂部導航列
│   │   │   ├── Footer.vue           # 頁尾
│   │   │   ├── Sidebar.vue          # 側邊欄 (篩選)
│   │   │   └── AnnouncementBar.vue  # 頂部公告列
│   │   ├── product/
│   │   │   ├── ProductCard.vue      # 商品卡片
│   │   │   ├── ProductGrid.vue      # 商品網格
│   │   │   ├── ProductGallery.vue   # 商品圖片輪播
│   │   │   ├── SizeSelector.vue     # 尺寸選擇
│   │   │   └── ProductFilter.vue    # 篩選面板
│   │   ├── cart/
│   │   │   ├── CartItem.vue         # 購物車項目
│   │   │   ├── CartSummary.vue      # 購物車摘要
│   │   │   └── CartDrawer.vue       # 側滑購物車
│   │   ├── checkout/
│   │   │   ├── AddressForm.vue      # 地址表單
│   │   │   ├── PaymentForm.vue      # 付款表單
│   │   │   └── OrderReview.vue      # 訂單確認
│   │   ├── ui/
│   │   │   ├── BaseButton.vue       # 按鈕
│   │   │   ├── BaseInput.vue        # 輸入框
│   │   │   ├── BaseModal.vue        # 彈窗
│   │   │   ├── BaseBreadcrumb.vue   # 麵包屑
│   │   │   ├── LoadingSpinner.vue   # 載入動畫
│   │   │   ├── Toast.vue            # 提示訊息
│   │   │   └── Pagination.vue       # 分頁
│   │   └── admin/
│   │       ├── AdminSidebar.vue     # 管理側邊欄
│   │       ├── DataTable.vue        # 資料表格
│   │       ├── StatsCard.vue        # 統計卡片
│   │       └── ChartWidget.vue      # 圖表元件
│   ├── composables/          # 組合式函式
│   │   ├── useAuth.js        # 認證邏輯
│   │   ├── useCart.js        # 購物車邏輯
│   │   ├── useProduct.js     # 商品邏輯
│   │   └── useNotification.js # 通知邏輯
│   ├── layouts/              # 頁面佈局
│   │   ├── DefaultLayout.vue # 前台佈局
│   │   └── AdminLayout.vue   # 管理後台佈局
│   ├── pages/                # 頁面 (路由對應)
│   │   ├── Home.vue                 # 首頁
│   │   ├── ProductList.vue          # 商品列表 (分類頁)
│   │   ├── ProductDetail.vue        # 商品詳情頁
│   │   ├── Cart.vue                 # 購物車頁
│   │   ├── Checkout.vue             # 結帳頁
│   │   ├── OrderConfirmation.vue    # 訂單確認頁
│   │   ├── Login.vue                # 登入頁
│   │   ├── Register.vue             # 註冊頁
│   │   ├── ForgotPassword.vue       # 忘記密碼
│   │   ├── Account.vue              # 會員中心
│   │   ├── OrderHistory.vue         # 訂單紀錄
│   │   ├── Wishlist.vue             # 收藏清單
│   │   ├── About.vue                # 關於我們
│   │   ├── Contact.vue              # 聯繫客服
│   │   ├── PrivacyPolicy.vue        # 隱私權政策
│   │   ├── TermsConditions.vue      # 使用條款
│   │   └── admin/
│   │       ├── Dashboard.vue        # 管理儀表板
│   │       ├── ProductManage.vue    # 商品管理
│   │       ├── OrderManage.vue      # 訂單管理
│   │       ├── UserManage.vue       # 用戶管理
│   │       ├── CategoryManage.vue   # 分類管理
│   │       ├── CouponManage.vue     # 優惠券管理
│   │       ├── BannerManage.vue     # 橫幅/廣告管理
│   │       ├── ReviewManage.vue     # 評論管理
│   │       └── Settings.vue         # 系統設定
│   ├── router/
│   │   └── index.js          # Vue Router 設定 (含路由守衛)
│   ├── stores/               # Pinia stores
│   │   ├── authStore.js      # 認證狀態
│   │   ├── cartStore.js      # 購物車狀態
│   │   ├── productStore.js   # 商品狀態
│   │   ├── orderStore.js     # 訂單狀態
│   │   ├── wishlistStore.js  # 收藏狀態
│   │   └── adminStore.js     # 管理後台狀態
│   ├── utils/
│   │   ├── validators.js     # 表單驗證
│   │   ├── formatters.js     # 格式化工具 (貨幣、日期)
│   │   ├── constants.js      # 常數定義
│   │   └── crypto.js         # 前端加密工具
│   ├── App.vue
│   └── main.js
├── .env.development
├── .env.production
├── index.html
├── package.json
├── tailwind.config.js
├── postcss.config.js
└── vite.config.js
```

### 3.2 核心前端功能清單

#### 前台 (顧客端)

| 功能模組       | 說明                                                                         |
| -------------- | ---------------------------------------------------------------------------- |
| **首頁**       | Hero banner 輪播、精選分類入口 (Women/Men/Kids/Life)、新品上架區、品牌展示列 |
| **商品瀏覽**   | 分類導航 (多層級)、篩選排序 (價格/品牌/尺寸/顏色)、無限滾動或分頁載入        |
| **商品詳情**   | 多圖輪播 + 放大鏡、尺寸選擇、加入購物車/收藏、商品描述 Tab、相關推薦         |
| **搜尋功能**   | 即時搜尋建議 (debounce)、搜尋結果頁含篩選                                    |
| **購物車**     | 側滑式 CartDrawer + 獨立購物車頁、數量增減、刪除、小計計算                   |
| **結帳流程**   | 多步驟結帳 (地址 → 運送 → 付款 → 確認)、表單驗證、加密傳輸                   |
| **會員系統**   | 註冊/登入/登出、JWT Token 管理、忘記密碼 (Email 驗證)、個人資料編輯          |
| **訂單中心**   | 訂單列表、訂單詳情、訂單狀態追蹤                                             |
| **收藏清單**   | 加入/移除收藏、收藏商品一鍵加入購物車                                        |
| **多語系**     | 使用 Vue I18n，支援繁體中文 / English                                        |
| **響應式設計** | Mobile First，支援 mobile / tablet / desktop 三層斷點                        |

#### 管理後台 (Admin)

| 功能模組       | 說明                                                                 |
| -------------- | -------------------------------------------------------------------- |
| **儀表板**     | 銷售統計 (日/週/月)、訂單數量圖表、營收趨勢、熱門商品排行            |
| **商品管理**   | CRUD 商品、批量上傳圖片、庫存管理、商品上下架、分類指派              |
| **分類管理**   | 多層級分類樹、新增/編輯/刪除分類、排序拖曳                           |
| **訂單管理**   | 訂單列表 (篩選/搜尋)、訂單狀態更新 (待處理→出貨→完成→退貨)、出貨通知 |
| **用戶管理**   | 會員列表、角色變更 (USER/ADMIN)、停權/啟用、查看訂單紀錄             |
| **優惠券管理** | 建立折扣碼 (百分比/固定金額)、設定使用期限、使用次數限制             |
| **橫幅管理**   | 首頁 Banner 圖上傳，排序、啟用/停用                                  |
| **評論管理**   | 審核商品評論、隱藏/刪除不當內容                                      |
| **系統設定**   | 網站名稱、運費規則、金流設定、SEO meta                               |

### 3.3 Pinia Store 設計要點

- `authStore`：管理 `user`, `token`, `refreshToken`, `isAuthenticated`, `role`
- `cartStore`：`items[]`, `totalAmount`, `itemCount`，持久化至 `localStorage`
- `productStore`：商品列表快取、篩選條件、分頁資訊
- `wishlistStore`：登入後同步至後端，未登入暫存 localStorage

### 3.4 路由守衛策略

```
router.beforeEach:
  ├── /admin/*     → 需 role === 'ADMIN'，否則 redirect /403
  ├── /account/*   → 需 isAuthenticated，否則 redirect /login
  ├── /checkout    → 需 isAuthenticated + cart 不為空
  └── 其他頁面     → 公開存取
```

### 3.5 Axios 攔截器

- **Request**：自動附加 `Authorization: Bearer <token>`
- **Response 401**：嘗試使用 refreshToken 刷新，失敗則跳轉 login
- **Response 403**：顯示無權限提示
- **CSRF Token**：從 cookie 讀取附加至 header

---

## 四、後端架構 (Spring Boot + Security + PostgreSQL)

### 4.1 專案目錄結構

```
backend/
├── src/main/java/com/popularshop/
│   ├── PopularShopApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java        # Spring Security 設定
│   │   ├── JwtConfig.java             # JWT 設定
│   │   ├── CorsConfig.java            # CORS 跨域設定
│   │   ├── WebConfig.java             # Web MVC 設定
│   │   ├── AuditConfig.java           # 審計設定
│   │   └── SwaggerConfig.java         # API 文件設定
│   ├── security/
│   │   ├── JwtTokenProvider.java      # JWT 產生 & 驗證
│   │   ├── JwtAuthenticationFilter.java # JWT 過濾器
│   │   ├── CustomUserDetailsService.java
│   │   ├── SecurityUtils.java         # 安全工具類
│   │   └── RateLimitFilter.java       # 速率限制過濾器
│   ├── controller/
│   │   ├── AuthController.java        # 認證 API
│   │   ├── ProductController.java     # 商品 API
│   │   ├── CategoryController.java    # 分類 API
│   │   ├── CartController.java        # 購物車 API
│   │   ├── OrderController.java       # 訂單 API
│   │   ├── UserController.java        # 用戶 API
│   │   ├── WishlistController.java    # 收藏 API
│   │   ├── PaymentController.java     # 金流 API
│   │   ├── FileController.java        # 檔案上傳 API
│   │   └── admin/
│   │       ├── AdminProductController.java
│   │       ├── AdminOrderController.java
│   │       ├── AdminUserController.java
│   │       ├── AdminCategoryController.java
│   │       ├── AdminCouponController.java
│   │       ├── AdminBannerController.java
│   │       ├── AdminReviewController.java
│   │       ├── AdminDashboardController.java
│   │       └── AdminSettingsController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── ProductService.java
│   │   ├── CategoryService.java
│   │   ├── CartService.java
│   │   ├── OrderService.java
│   │   ├── UserService.java
│   │   ├── WishlistService.java
│   │   ├── PaymentService.java
│   │   ├── EmailService.java          # 郵件通知
│   │   ├── FileStorageService.java    # 檔案儲存
│   │   ├── CouponService.java
│   │   └── DashboardService.java      # 統計報表
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   ├── CategoryRepository.java
│   │   ├── CartRepository.java
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   ├── WishlistRepository.java
│   │   ├── CouponRepository.java
│   │   ├── BannerRepository.java
│   │   ├── ReviewRepository.java
│   │   └── SettingRepository.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Role.java                  # Enum: USER, ADMIN
│   │   ├── Product.java
│   │   ├── ProductImage.java
│   │   ├── Category.java
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderStatus.java           # Enum: PENDING, PAID, SHIPPED, DELIVERED, CANCELLED, RETURNED
│   │   ├── Address.java
│   │   ├── Wishlist.java
│   │   ├── Coupon.java
│   │   ├── Banner.java
│   │   ├── Review.java
│   │   ├── Payment.java
│   │   ├── Setting.java
│   │   └── AuditLog.java             # 操作審計紀錄
│   ├── dto/
│   │   ├── request/
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── ProductRequest.java
│   │   │   ├── CartItemRequest.java
│   │   │   ├── OrderRequest.java
│   │   │   ├── AddressRequest.java
│   │   │   └── CouponRequest.java
│   │   └── response/
│   │       ├── JwtResponse.java
│   │       ├── ProductResponse.java
│   │       ├── CartResponse.java
│   │       ├── OrderResponse.java
│   │       ├── UserResponse.java
│   │       ├── DashboardResponse.java
│   │       └── ApiResponse.java       # 統一回應格式
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java # @ControllerAdvice
│   │   ├── ResourceNotFoundException.java
│   │   ├── BadRequestException.java
│   │   ├── UnauthorizedException.java
│   │   └── PaymentException.java
│   ├── mapper/
│   │   └── EntityMapper.java          # MapStruct 轉換
│   └── util/
│       ├── SlugUtils.java             # URL slug 產生
│       └── PaginationUtils.java       # 分頁工具
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   ├── db/migration/                  # Flyway 資料庫遷移
│   │   ├── V1__init_schema.sql
│   │   ├── V2__seed_categories.sql
│   │   └── V3__seed_admin_user.sql
│   └── templates/                     # Email 模板
│       ├── welcome.html
│       ├── order-confirmation.html
│       ├── password-reset.html
│       └── shipping-notification.html
├── src/test/java/com/popularshop/
│   ├── controller/
│   ├── service/
│   └── repository/
├── Dockerfile
├── pom.xml
└── docker-compose.yml
```

### 4.2 資料庫 Schema (PostgreSQL) — 核心表

```
┌──────────────┐     ┌──────────────────┐     ┌──────────────┐
│   users       │     │   products        │     │  categories   │
├──────────────┤     ├──────────────────┤     ├──────────────┤
│ id (PK)      │     │ id (PK)          │     │ id (PK)      │
│ email (UQ)   │     │ name             │     │ name         │
│ password_hash│     │ slug (UQ)        │     │ slug (UQ)    │
│ first_name   │     │ description      │     │ parent_id(FK)│
│ last_name    │     │ price            │     │ sort_order   │
│ phone        │     │ sale_price       │     │ image_url    │
│ role         │     │ sku (UQ)         │     │ is_active    │
│ is_active    │     │ stock_quantity   │     │ created_at   │
│ email_verified│    │ category_id (FK) │     └──────────────┘
│ created_at   │     │ brand            │
│ updated_at   │     │ is_active        │     ┌──────────────┐
│ last_login_at│     │ is_featured      │     │  addresses   │
└──────┬───────┘     │ created_at       │     ├──────────────┤
       │             │ updated_at       │     │ id (PK)      │
       │             └──────┬───────────┘     │ user_id (FK) │
       │                    │                  │ label        │
       │             ┌──────┴───────────┐     │ recipient    │
       │             │ product_images    │     │ phone        │
       │             ├──────────────────┤     │ address_line1│
       │             │ id (PK)          │     │ address_line2│
       │             │ product_id (FK)  │     │ city         │
       │             │ image_url        │     │ state        │
       │             │ sort_order       │     │ postal_code  │
       │             └──────────────────┘     │ country      │
       │                                       │ is_default   │
┌──────┴───────┐     ┌──────────────────┐     └──────────────┘
│   orders      │     │   order_items     │
├──────────────┤     ├──────────────────┤     ┌──────────────┐
│ id (PK)      │     │ id (PK)          │     │  wishlists   │
│ user_id (FK) │     │ order_id (FK)    │     ├──────────────┤
│ order_number │     │ product_id (FK)  │     │ id (PK)      │
│ status       │     │ product_name     │     │ user_id (FK) │
│ total_amount │     │ quantity         │     │ product_id(FK)│
│ shipping_fee │     │ unit_price       │     │ created_at   │
│ discount     │     │ subtotal         │     └──────────────┘
│ coupon_id(FK)│     └──────────────────┘
│ address_json │                               ┌──────────────┐
│ payment_method│    ┌──────────────────┐     │   coupons    │
│ payment_status│    │   cart_items      │     ├──────────────┤
│ paid_at      │     ├──────────────────┤     │ id (PK)      │
│ shipped_at   │     │ id (PK)          │     │ code (UQ)    │
│ delivered_at │     │ user_id (FK)     │     │ type         │
│ created_at   │     │ product_id (FK)  │     │ value        │
│ updated_at   │     │ quantity         │     │ min_purchase │
└──────────────┘     │ created_at       │     │ max_uses     │
                     └──────────────────┘     │ used_count   │
                                               │ starts_at    │
┌──────────────────┐  ┌─────────────────┐     │ expires_at   │
│   reviews         │  │   banners       │     │ is_active    │
├──────────────────┤  ├─────────────────┤     └──────────────┘
│ id (PK)          │  │ id (PK)         │
│ user_id (FK)     │  │ title           │     ┌──────────────┐
│ product_id (FK)  │  │ image_url       │     │  audit_logs  │
│ rating (1-5)     │  │ link_url        │     ├──────────────┤
│ comment          │  │ sort_order      │     │ id (PK)      │
│ is_approved      │  │ is_active       │     │ user_id (FK) │
│ created_at       │  │ starts_at       │     │ action       │
└──────────────────┘  │ ends_at         │     │ entity_type  │
                      └─────────────────┘     │ entity_id    │
                                               │ details_json │
┌──────────────────┐                          │ ip_address   │
│   payments        │                          │ created_at   │
├──────────────────┤                          └──────────────┘
│ id (PK)          │
│ order_id (FK)    │  ┌─────────────────┐
│ method           │  │   settings      │
│ transaction_id   │  ├─────────────────┤
│ amount           │  │ id (PK)         │
│ status           │  │ key (UQ)        │
│ provider_response│  │ value           │
│ created_at       │  │ updated_at      │
└──────────────────┘  └─────────────────┘
```

### 4.3 API 端點規劃

#### 公開 API (無需認證)

| Method | Endpoint                          | 說明                 |
| ------ | --------------------------------- | -------------------- |
| POST   | `/api/auth/register`              | 會員註冊             |
| POST   | `/api/auth/login`                 | 登入取得 JWT         |
| POST   | `/api/auth/refresh`               | 刷新 Token           |
| POST   | `/api/auth/forgot-password`       | 寄送重設密碼信       |
| POST   | `/api/auth/reset-password`        | 重設密碼             |
| GET    | `/api/products`                   | 商品列表 (分頁+篩選) |
| GET    | `/api/products/{slug}`            | 商品詳情             |
| GET    | `/api/categories`                 | 分類樹               |
| GET    | `/api/categories/{slug}/products` | 分類商品             |
| GET    | `/api/banners`                    | 取得啟用中 Banner    |
| GET    | `/api/products/search?q=`         | 搜尋商品             |

#### 會員 API (需 USER / ADMIN 角色)

| Method | Endpoint                    | 說明           |
| ------ | --------------------------- | -------------- |
| GET    | `/api/user/profile`         | 取得個人資料   |
| PUT    | `/api/user/profile`         | 更新個人資料   |
| PUT    | `/api/user/password`        | 變更密碼       |
| GET    | `/api/user/addresses`       | 地址列表       |
| POST   | `/api/user/addresses`       | 新增地址       |
| PUT    | `/api/user/addresses/{id}`  | 更新地址       |
| DELETE | `/api/user/addresses/{id}`  | 刪除地址       |
| GET    | `/api/cart`                 | 取得購物車     |
| POST   | `/api/cart/items`           | 加入購物車     |
| PUT    | `/api/cart/items/{id}`      | 更新數量       |
| DELETE | `/api/cart/items/{id}`      | 移除購物車項目 |
| DELETE | `/api/cart`                 | 清空購物車     |
| POST   | `/api/orders`               | 建立訂單       |
| GET    | `/api/orders`               | 訂單列表       |
| GET    | `/api/orders/{id}`          | 訂單詳情       |
| POST   | `/api/orders/{id}/cancel`   | 取消訂單       |
| GET    | `/api/wishlist`             | 收藏列表       |
| POST   | `/api/wishlist/{productId}` | 加到收藏       |
| DELETE | `/api/wishlist/{productId}` | 取消收藏       |
| POST   | `/api/reviews`              | 發表評論       |
| POST   | `/api/coupons/validate`     | 驗證優惠券     |

#### 管理 API (需 ADMIN 角色)

| Method | Endpoint                          | 說明              |
| ------ | --------------------------------- | ----------------- |
| GET    | `/api/admin/dashboard`            | 統計數據          |
| GET    | `/api/admin/products`             | 商品列表 (含下架) |
| POST   | `/api/admin/products`             | 新增商品          |
| PUT    | `/api/admin/products/{id}`        | 編輯商品          |
| DELETE | `/api/admin/products/{id}`        | 刪除商品          |
| PATCH  | `/api/admin/products/{id}/toggle` | 上下架切換        |
| POST   | `/api/admin/products/{id}/images` | 上傳商品圖片      |
| GET    | `/api/admin/categories`           | 分類管理列表      |
| POST   | `/api/admin/categories`           | 新增分類          |
| PUT    | `/api/admin/categories/{id}`      | 編輯分類          |
| DELETE | `/api/admin/categories/{id}`      | 刪除分類          |
| GET    | `/api/admin/orders`               | 所有訂單列表      |
| PUT    | `/api/admin/orders/{id}/status`   | 更新訂單狀態      |
| GET    | `/api/admin/users`                | 用戶列表          |
| PUT    | `/api/admin/users/{id}/role`      | 變更用戶角色      |
| PUT    | `/api/admin/users/{id}/toggle`    | 停權/啟用         |
| GET    | `/api/admin/coupons`              | 優惠券列表        |
| POST   | `/api/admin/coupons`              | 建立優惠券        |
| PUT    | `/api/admin/coupons/{id}`         | 編輯優惠券        |
| DELETE | `/api/admin/coupons/{id}`         | 刪除優惠券        |
| GET    | `/api/admin/banners`              | Banner 列表       |
| POST   | `/api/admin/banners`              | 新增 Banner       |
| PUT    | `/api/admin/banners/{id}`         | 編輯 Banner       |
| DELETE | `/api/admin/banners/{id}`         | 刪除 Banner       |
| GET    | `/api/admin/reviews`              | 評論管理列表      |
| PUT    | `/api/admin/reviews/{id}/approve` | 審核通過          |
| DELETE | `/api/admin/reviews/{id}`         | 刪除評論          |
| GET    | `/api/admin/settings`             | 系統設定          |
| PUT    | `/api/admin/settings`             | 更新設定          |
| GET    | `/api/admin/audit-logs`           | 審計日誌          |

---

## 五、安全規範與個資保護

### 5.1 Spring Security 設定

- **認證方式**：JWT (Access Token 15 分鐘 + Refresh Token 7 天)
- **密碼加密**：BCryptPasswordEncoder (strength=12)
- **CORS**：僅允許前端 origin，限制 methods/headers
- **CSRF**：SPA 模式下使用 `CookieCsrfTokenRepository.withHttpOnlyFalse()` 或 JWT 免 CSRF
- **Session**：無狀態 (`SessionCreationPolicy.STATELESS`)
- **路徑授權**：
  - `/api/auth/**` → permitAll
  - `/api/admin/**` → hasRole('ADMIN')
  - `/api/user/**`, `/api/cart/**`, `/api/orders/**` → authenticated
  - `/api/products/**`, `/api/categories/**` GET → permitAll

### 5.2 資料安全措施

| 安全項目               | 實作方式                                                         |
| ---------------------- | ---------------------------------------------------------------- |
| **SQL Injection 防護** | Spring Data JPA 參數化查詢，禁止拼接 SQL                         |
| **XSS 防護**           | 後端輸入清理 (OWASP Java HTML Sanitizer)；前端 Vue 自動 escape   |
| **HTTPS 強制**         | 全站 TLS 1.2+，HSTS header 設定                                  |
| **密碼強度驗證**       | 前後端雙重驗證：至少 8 字元、含大小寫、數字、特殊字元            |
| **登入防暴力破解**     | 速率限制 (RateLimitFilter)：同 IP 5 次失敗後鎖定 15 分鐘         |
| **JWT 安全**           | HttpOnly cookie 存放 Refresh Token；Access Token 短效期          |
| **敏感資料加密**       | 資料庫中信用卡資訊不存儲 (交由第三方金流)；個資欄位可選 AES 加密 |
| **API 速率限制**       | Bucket4j 或 Spring Cloud Gateway Rate Limiter                    |
| **檔案上傳驗證**       | 限制檔案類型 (jpg/png/webp)、大小 (≤5MB)、防止路徑穿越           |
| **CORS 嚴格設定**      | 白名單 origin，禁止 `*`                                          |

### 5.3 個資保護 (GDPR / 台灣個資法 合規)

| 項目                  | 實作方式                                           |
| --------------------- | -------------------------------------------------- |
| **最小化收集**        | 僅收集必要個資：email、姓名、電話、地址            |
| **隱私權政策頁**      | `/privacy-policy` 頁面完整說明資料收集用途         |
| **Cookie 同意機制**   | 首次訪問顯示 Cookie Banner，需明確同意             |
| **資料可攜權**        | 提供匯出個人資料功能 (JSON/CSV)                    |
| **刪除權 (被遺忘權)** | 用戶可請求刪除帳號，系統匿名化處理歷史訂單         |
| **存取日誌**          | 記錄所有個資存取/修改至 `audit_logs` 表            |
| **資料加密傳輸**      | 全程 HTTPS，敏感 API payload 額外加密              |
| **資料保留期限**      | 訂單資料保留 5 年 (法規要求)，其餘個資隨帳號刪除   |
| **第三方分享限制**    | 不將個資分享予非必要第三方，金流僅傳送交易必要資訊 |
| **管理員存取控制**    | Admin 存取個資需稽核紀錄，不可匯出完整個資         |

### 5.4 安全 Headers

```yaml
# application.yml 或 SecurityConfig
Content-Security-Policy: default-src 'self'; img-src 'self' https://cdn.popularshop.com; script-src 'self'
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
Referrer-Policy: strict-origin-when-cross-origin
Permissions-Policy: camera=(), microphone=(), geolocation=()
```

---

## 六、金流與物流整合

### 6.1 金流 (Payment Gateway)

- **建議整合**：綠界 ECPay / 藍新 NewebPay (台灣市場) 或 Stripe (國際)
- **PCI DSS 合規**：不在自家伺服器存儲信用卡號，使用第三方代管
- **付款方式**：信用卡、ATM 轉帳、超商代碼、LINE Pay、Apple Pay
- **3D Secure 驗證**：強制啟用 3DS 2.0 雙重驗證

### 6.2 物流

- **運送方式**：宅配到府、超商取貨 (7-11 / 全家)
- **運費計算**：依地區/訂單金額設定，滿額免運

---

## 七、部署架構

```
                    ┌───────────────┐
                    │   Cloudflare   │  CDN + DDoS 防護 + SSL
                    └───────┬───────┘
                            │
                    ┌───────▼───────┐
                    │    Nginx       │  反向代理 + 靜態資源
                    └───┬───────┬───┘
                        │       │
              ┌─────────▼─┐ ┌──▼──────────┐
              │  Vue SPA   │ │ Spring Boot  │  (Docker Container)
              │  (Static)  │ │  :8080       │
              └────────────┘ └──────┬───────┘
                                    │
                            ┌───────▼───────┐
                            │  PostgreSQL    │  (Docker Container)
                            │   :5432        │
                            └───────────────┘
                            ┌───────────────┐
                            │     Redis      │  Session / Cache
                            │   :6379        │
                            └───────────────┘
```

### 7.1 Docker Compose 服務

- `frontend` — Nginx serve Vue build artifacts
- `backend` — Spring Boot JAR
- `postgres` — PostgreSQL 16 with volume mount
- `redis` — 快取 & 速率限制計數器

---

## 八、開發規範

### 8.1 Git 分支策略

- `main` — 生產環境
- `develop` — 開發整合
- `feature/*` — 功能分支
- `hotfix/*` — 緊急修復

### 8.2 命名規範

| 類型         | 規範                   | 範例                    |
| ------------ | ---------------------- | ----------------------- |
| Vue 元件     | PascalCase             | `ProductCard.vue`       |
| JS 變數/函式 | camelCase              | `fetchProducts()`       |
| Pinia Store  | camelCase + Store 後綴 | `useCartStore`          |
| CSS class    | Tailwind utility 為主  | `text-brand-black`      |
| Java 類別    | PascalCase             | `ProductService.java`   |
| Java 方法    | camelCase              | `findBySlug()`          |
| DB 表名      | snake_case (複數)      | `order_items`           |
| DB 欄位      | snake_case             | `created_at`            |
| API URL      | kebab-case             | `/api/admin/audit-logs` |
| 環境變數     | UPPER_SNAKE_CASE       | `JWT_SECRET_KEY`        |

### 8.3 Code Quality

- **前端**：ESLint + Prettier，Husky pre-commit hook
- **後端**：Checkstyle + SpotBugs，JUnit 5 + Mockito 單元測試
- **API 文件**：Springdoc OpenAPI (Swagger UI at `/swagger-ui.html`)
- **資料庫遷移**：Flyway 版本控制

---

## 九、效能優化

- **圖片**：WebP 格式、lazy loading、CDN 託管、responsive srcset
- **前端**：Vite code splitting、路由懶載入、Pinia 持久化減少 API 請求
- **後端**：Redis 快取熱門商品 & 分類、Spring Data JPA 二級快取
- **資料庫**：索引優化 (slug, email, category_id, created_at)、分頁查詢
- **搜尋**：PostgreSQL Full-Text Search 或未來整合 Elasticsearch

---

## 十、開發階段規劃

| 階段        | 內容                                                        | 時間估算 |
| ----------- | ----------------------------------------------------------- | -------- |
| **Phase 1** | 專案初始化、DB schema、Spring Security + JWT、會員註冊/登入 | 1-2 週   |
| **Phase 2** | 商品 CRUD、分類管理、商品圖片上傳 (Admin API + 前台展示)    | 2-3 週   |
| **Phase 3** | 購物車、收藏清單、結帳流程                                  | 2 週     |
| **Phase 4** | 訂單管理、金流串接 (ECPay/Stripe Sandbox)                   | 2 週     |
| **Phase 5** | Admin 後台完整 UI (儀表板、用戶管理、優惠券、Banner、評論)  | 2-3 週   |
| **Phase 6** | 安全加固、個資合規、SEO、效能優化、響應式完善               | 1-2 週   |
| **Phase 7** | 測試 (單元 + E2E)、部署 (Docker + CI/CD)、上線準備          | 1-2 週   |
