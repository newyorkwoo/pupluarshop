<template>
  <footer class="bg-brand-black text-white">
    <!-- Newsletter -->
    <div class="border-b border-gray-800">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
        <h3 class="section-title text-white mb-4">訂閱電子報</h3>
        <p class="text-sm text-gray-400 mb-6">搶先獲得新品上架、獨家優惠與時尚靈感</p>
        <form @submit.prevent class="flex max-w-md mx-auto">
          <input id="newsletter-email" name="newsletter-email" type="email" placeholder="輸入您的 Email" class="flex-1 px-4 py-3 bg-transparent border border-gray-700 text-white text-sm focus:border-brand-gold focus:outline-none" />
          <button type="submit" class="px-6 py-3 bg-white text-brand-black text-xs uppercase tracking-widest hover:bg-brand-gold hover:text-white transition-all">
            訂閱
          </button>
        </form>
      </div>
    </div>

    <!-- Links -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-2 md:grid-cols-4 gap-8">
        <div>
          <h4 class="text-xs uppercase tracking-widest mb-4 font-medium">客戶服務</h4>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><router-link to="/contact" class="hover:text-white transition-colors">聯繫我們</router-link></li>
            <li><a href="#" class="hover:text-white transition-colors">配送資訊</a></li>
            <li><a href="#" class="hover:text-white transition-colors">退換貨政策</a></li>
            <li><a href="#" class="hover:text-white transition-colors">付款方式</a></li>
          </ul>
        </div>
        <div>
          <h4 class="text-xs uppercase tracking-widest mb-4 font-medium">關於我們</h4>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><router-link to="/about" class="hover:text-white transition-colors">品牌故事</router-link></li>
            <li><a href="#" class="hover:text-white transition-colors">永續發展</a></li>
            <li><a href="#" class="hover:text-white transition-colors">人才招募</a></li>
          </ul>
        </div>
        <div>
          <h4 class="text-xs uppercase tracking-widest mb-4 font-medium">法律資訊</h4>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><router-link to="/privacy-policy" class="hover:text-white transition-colors">隱私權政策</router-link></li>
            <li><router-link to="/terms" class="hover:text-white transition-colors">使用條款</router-link></li>
            <li><a href="#" class="hover:text-white transition-colors">Cookie 設定</a></li>
          </ul>
        </div>
        <div>
          <h4 class="text-xs uppercase tracking-widest mb-4 font-medium">社群媒體</h4>
          <ul class="space-y-2 text-sm text-gray-400">
            <li><a href="#" class="hover:text-white transition-colors">Instagram</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Facebook</a></li>
            <li><a href="#" class="hover:text-white transition-colors">LINE</a></li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Bottom -->
    <div class="border-t border-gray-800">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 flex flex-col sm:flex-row items-center justify-between text-xs text-gray-500">
        <p>&copy; {{ new Date().getFullYear() }} PopularShop. All rights reserved.</p>
        <div class="flex gap-4 mt-2 sm:mt-0">
          <span>VISA</span>
          <span>Mastercard</span>
          <span>JCB</span>
          <span>LINE Pay</span>
        </div>
      </div>
    </div>
  </footer>
</template>
