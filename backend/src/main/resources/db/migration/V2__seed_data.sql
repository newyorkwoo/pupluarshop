-- V2: Seed data for PopularShop

-- Admin user (password: Admin@123)
INSERT INTO users (name, email, password, phone, role, active) VALUES
('Admin', 'admin@popularshop.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iaj6dFpJLnBGHE856IjgN2GHPbu6', '0912345678', 'ADMIN', true);

-- Demo user (password: User@123)
INSERT INTO users (name, email, password, phone, role, active) VALUES
('Demo User', 'user@popularshop.com', '$2a$10$DowJonesSvNqGFeLmkLWaeK8j7VQcfPaQIwB0HeUfjinVnI7ql5lG', '0987654321', 'USER', true);

-- Categories
INSERT INTO categories (name, slug, description, image_url, sort_order) VALUES
('女裝', 'women', '女性精品服飾', 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=600', 1),
('男裝', 'men', '男性精品服飾', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600', 2),
('包款', 'bags', '精品包款', 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=600', 3),
('鞋履', 'shoes', '精品鞋履', 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=600', 4),
('配件', 'accessories', '精品配件', 'https://images.unsplash.com/photo-1611923134239-b9be5816e23c?w=600', 5),
('新品上市', 'new-arrivals', '最新商品', 'https://images.unsplash.com/photo-1483985988355-763728e1935b?w=600', 6);

-- Sub-categories (using sub-selects for parent_id)
INSERT INTO categories (name, slug, description, parent_id, sort_order) VALUES
('洋裝', 'women-dresses', '女性洋裝', (SELECT id FROM categories WHERE slug='women'), 1),
('上衣', 'women-tops', '女性上衣', (SELECT id FROM categories WHERE slug='women'), 2),
('外套', 'women-coats', '女性外套', (SELECT id FROM categories WHERE slug='women'), 3),
('西裝', 'men-suits', '男性西裝', (SELECT id FROM categories WHERE slug='men'), 1),
('襯衫', 'men-shirts', '男性襯衫', (SELECT id FROM categories WHERE slug='men'), 2),
('手提包', 'handbags', '精品手提包', (SELECT id FROM categories WHERE slug='bags'), 1),
('肩背包', 'shoulder-bags', '精品肩背包', (SELECT id FROM categories WHERE slug='bags'), 2),
('高跟鞋', 'heels', '精品高跟鞋', (SELECT id FROM categories WHERE slug='shoes'), 1),
('平底鞋', 'flats', '精品平底鞋', (SELECT id FROM categories WHERE slug='shoes'), 2),
('太陽眼鏡', 'sunglasses', '精品太陽眼鏡', (SELECT id FROM categories WHERE slug='accessories'), 1),
('珠寶', 'jewelry', '精品珠寶', (SELECT id FROM categories WHERE slug='accessories'), 2);

-- Products (using sub-selects for category_id)
INSERT INTO products (name, slug, brand, description, material, price, sale_price, stock, active, category_id) VALUES
('經典黑色絲綢洋裝', 'classic-black-silk-dress', 'SAINT LAURENT', '優雅的經典黑色絲綢洋裝，適合各種正式場合。採用頂級真絲面料，剪裁精緻，展現女性優美線條。', '100% 真絲', 45800, 38900, 15, true, (SELECT id FROM categories WHERE slug='women-dresses')),
('精緻蕾絲晚禮服', 'delicate-lace-evening-gown', 'VALENTINO', '精緻的蕾絲晚禮服，完美的工藝展現出無與倫比的優雅。手工縫製蕾絲細節，搭配真絲內襯。', '蕾絲 / 真絲', 89000, NULL, 8, true, (SELECT id FROM categories WHERE slug='women-dresses')),
('經典雙排扣風衣', 'classic-double-breasted-trench', 'BURBERRY', '英倫經典雙排扣風衣，防風防雨的同時展現時尚品味。招牌格紋內裡，可拆卸腰帶設計。', '棉混紡', 68500, 58200, 20, true, (SELECT id FROM categories WHERE slug='women-coats')),
('真絲襯衫', 'silk-blouse', 'CELINE', '簡約優雅的真絲襯衫，柔軟光滑的觸感帶來極致舒適。寬鬆版型，適合日常與辦公場合。', '100% 真絲', 28500, NULL, 25, true, (SELECT id FROM categories WHERE slug='women-tops')),
('修身西裝套裝', 'slim-fit-suit', 'TOM FORD', '精緻裁剪的修身西裝，展現男性魅力與專業形象。義大利進口面料，手工縫製。', '羊毛混紡', 125000, 99800, 10, true, (SELECT id FROM categories WHERE slug='men-suits')),
('義大利純棉襯衫', 'italian-cotton-shirt', 'BRIONI', '頂級義大利純棉襯衫，細膩的織紋展現品味。法式袖扣設計，商務休閒兩相宜。', '100% 棉', 18500, 15800, 30, true, (SELECT id FROM categories WHERE slug='men-shirts')),
('經典菱格紋手提包', 'classic-quilted-handbag', 'CHANEL', '經典菱格紋設計手提包，永恆的時尚經典。小羊皮材質，鏈條肩帶設計。', '小羊皮', 198000, NULL, 5, true, (SELECT id FROM categories WHERE slug='handbags')),
('GG Marmont 肩背包', 'gg-marmont-shoulder-bag', 'GUCCI', 'GG Marmont系列肩背包，標誌性的雙G金屬扣環。柔軟小牛皮，內含多個收納隔層。', '小牛皮', 75800, 68200, 12, true, (SELECT id FROM categories WHERE slug='shoulder-bags')),
('Triomphe 手提包', 'triomphe-handbag', 'CELINE', 'Triomphe系列手提包，經典的凱旋門扣設計。光滑小牛皮，可手提可肩背。', '小牛皮', 128000, NULL, 7, true, (SELECT id FROM categories WHERE slug='handbags')),
('經典紅底高跟鞋', 'classic-red-sole-heels', 'CHRISTIAN LOUBOUTIN', '標誌性紅底高跟鞋，10cm完美高度。專利漆皮材質，經典尖頭設計。', '漆皮', 32800, NULL, 18, true, (SELECT id FROM categories WHERE slug='heels')),
('Rockstud 平底鞋', 'rockstud-flats', 'VALENTINO', 'Rockstud系列平底鞋，標誌性金屬鉚釘裝飾。柔軟皮革，舒適內裡。', '牛皮', 28500, 24200, 20, true, (SELECT id FROM categories WHERE slug='flats')),
('飛行員太陽眼鏡', 'aviator-sunglasses', 'RAY-BAN', '經典飛行員款太陽眼鏡，偏光鏡片設計。金色金屬鏡框，防UV400。', '金屬 / 玻璃', 8500, 7200, 50, true, (SELECT id FROM categories WHERE slug='sunglasses')),
('Serpenti 手鍊', 'serpenti-bracelet', 'BVLGARI', 'Serpenti系列蛇形手鍊，18K玫瑰金材質。鑲嵌天然鑽石，靈蛇造型設計。', '18K玫瑰金 / 鑽石', 285000, NULL, 3, true, (SELECT id FROM categories WHERE slug='jewelry')),
('Juste un Clou 手環', 'juste-un-clou-bracelet', 'CARTIER', 'Juste un Clou系列釘子手環，18K黃金材質。前衛與經典的完美結合。', '18K黃金', 235000, 210000, 4, true, (SELECT id FROM categories WHERE slug='jewelry')),
('經典格紋圍巾', 'classic-check-scarf', 'BURBERRY', '經典格紋羊絨圍巾，溫暖柔軟的觸感。英國製造，100%喀什米爾羊絨。', '100% 喀什米爾', 25800, NULL, 35, true, (SELECT id FROM categories WHERE slug='accessories'));

-- Product Images (using sub-selects for product_id)
INSERT INTO product_images (product_id, image_url, sort_order) VALUES
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800', 0),
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=800', 1),
((SELECT id FROM products WHERE slug='delicate-lace-evening-gown'), 'https://images.unsplash.com/photo-1518622358385-8ea7d0794bf6?w=800', 0),
((SELECT id FROM products WHERE slug='classic-double-breasted-trench'), 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800', 0),
((SELECT id FROM products WHERE slug='silk-blouse'), 'https://images.unsplash.com/photo-1598554747436-c9293d6a588f?w=800', 0),
((SELECT id FROM products WHERE slug='slim-fit-suit'), 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=800', 0),
((SELECT id FROM products WHERE slug='italian-cotton-shirt'), 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800', 0),
((SELECT id FROM products WHERE slug='classic-quilted-handbag'), 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800', 0),
((SELECT id FROM products WHERE slug='gg-marmont-shoulder-bag'), 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=800', 0),
((SELECT id FROM products WHERE slug='triomphe-handbag'), 'https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=800', 0),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=800', 0),
((SELECT id FROM products WHERE slug='rockstud-flats'), 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=800', 0),
((SELECT id FROM products WHERE slug='aviator-sunglasses'), 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=800', 0),
((SELECT id FROM products WHERE slug='serpenti-bracelet'), 'https://images.unsplash.com/photo-1611923134239-b9be5816e23c?w=800', 0),
((SELECT id FROM products WHERE slug='juste-un-clou-bracelet'), 'https://images.unsplash.com/photo-1611923134239-b9be5816e23c?w=800', 0),
((SELECT id FROM products WHERE slug='classic-check-scarf'), 'https://images.unsplash.com/photo-1601924994987-69e26d50dc26?w=800', 0);

-- Product Sizes (using sub-selects for product_id)
INSERT INTO product_sizes (product_id, size, stock) VALUES
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'XS', 3),
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'S', 4),
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'M', 4),
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'L', 3),
((SELECT id FROM products WHERE slug='classic-black-silk-dress'), 'XL', 1),
((SELECT id FROM products WHERE slug='delicate-lace-evening-gown'), 'S', 2),
((SELECT id FROM products WHERE slug='delicate-lace-evening-gown'), 'M', 3),
((SELECT id FROM products WHERE slug='delicate-lace-evening-gown'), 'L', 2),
((SELECT id FROM products WHERE slug='delicate-lace-evening-gown'), 'XL', 1),
((SELECT id FROM products WHERE slug='classic-double-breasted-trench'), 'S', 5),
((SELECT id FROM products WHERE slug='classic-double-breasted-trench'), 'M', 6),
((SELECT id FROM products WHERE slug='classic-double-breasted-trench'), 'L', 5),
((SELECT id FROM products WHERE slug='classic-double-breasted-trench'), 'XL', 4),
((SELECT id FROM products WHERE slug='silk-blouse'), 'XS', 5),
((SELECT id FROM products WHERE slug='silk-blouse'), 'S', 7),
((SELECT id FROM products WHERE slug='silk-blouse'), 'M', 7),
((SELECT id FROM products WHERE slug='silk-blouse'), 'L', 4),
((SELECT id FROM products WHERE slug='silk-blouse'), 'XL', 2),
((SELECT id FROM products WHERE slug='slim-fit-suit'), '46', 2),
((SELECT id FROM products WHERE slug='slim-fit-suit'), '48', 3),
((SELECT id FROM products WHERE slug='slim-fit-suit'), '50', 3),
((SELECT id FROM products WHERE slug='slim-fit-suit'), '52', 2),
((SELECT id FROM products WHERE slug='italian-cotton-shirt'), 'S', 8),
((SELECT id FROM products WHERE slug='italian-cotton-shirt'), 'M', 10),
((SELECT id FROM products WHERE slug='italian-cotton-shirt'), 'L', 8),
((SELECT id FROM products WHERE slug='italian-cotton-shirt'), 'XL', 4),
((SELECT id FROM products WHERE slug='classic-quilted-handbag'), 'ONE SIZE', 5),
((SELECT id FROM products WHERE slug='gg-marmont-shoulder-bag'), 'ONE SIZE', 12),
((SELECT id FROM products WHERE slug='triomphe-handbag'), 'ONE SIZE', 7),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '35', 3),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '36', 3),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '37', 4),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '38', 4),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '39', 2),
((SELECT id FROM products WHERE slug='classic-red-sole-heels'), '40', 2),
((SELECT id FROM products WHERE slug='rockstud-flats'), '35', 4),
((SELECT id FROM products WHERE slug='rockstud-flats'), '36', 4),
((SELECT id FROM products WHERE slug='rockstud-flats'), '37', 5),
((SELECT id FROM products WHERE slug='rockstud-flats'), '38', 4),
((SELECT id FROM products WHERE slug='rockstud-flats'), '39', 2),
((SELECT id FROM products WHERE slug='rockstud-flats'), '40', 1),
((SELECT id FROM products WHERE slug='aviator-sunglasses'), 'ONE SIZE', 50),
((SELECT id FROM products WHERE slug='serpenti-bracelet'), 'ONE SIZE', 3),
((SELECT id FROM products WHERE slug='juste-un-clou-bracelet'), 'S', 1),
((SELECT id FROM products WHERE slug='juste-un-clou-bracelet'), 'M', 2),
((SELECT id FROM products WHERE slug='juste-un-clou-bracelet'), 'L', 1),
((SELECT id FROM products WHERE slug='classic-check-scarf'), 'ONE SIZE', 35);

-- Banners
INSERT INTO banners (title, subtitle, image_url, link, button_text, position, sort_order, active) VALUES
('2024 秋冬新品', '探索最新精品系列', 'https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=1600', '/products?category=new-arrivals', '立即選購', 'hero', 1, true),
('獨家限定折扣', '精選商品最高 5 折', 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=1600', '/products?category=women', '探索更多', 'hero', 2, true),
('精品包款特輯', '經典與當季新品', 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=1600', '/products?category=bags', '瀏覽系列', 'hero', 3, true);

-- Coupons
INSERT INTO coupons (code, type, value, min_purchase, max_discount, max_uses, active, start_date, end_date) VALUES
('WELCOME10', 'PERCENTAGE', 10, 3000, 5000, 1000, true, '2024-01-01', '2027-12-31'),
('SAVE500', 'FIXED', 500, 5000, NULL, 500, true, '2024-01-01', '2027-12-31');

-- Default Settings
INSERT INTO settings (setting_key, setting_value, setting_group, description) VALUES
('site_name', 'PopularShop', 'general', '網站名稱'),
('site_description', '頂級精品購物平台', 'general', '網站描述'),
('currency', 'TWD', 'general', '貨幣'),
('free_shipping_threshold', '3000', 'shipping', '免運門檻'),
('default_shipping_fee', '120', 'shipping', '預設運費');
