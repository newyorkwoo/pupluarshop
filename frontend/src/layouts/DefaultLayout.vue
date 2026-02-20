<template>
  <div class="min-h-screen flex flex-col">
    <AnnouncementBar />
    <Navbar />
    <main class="flex-1">
      <router-view />
    </main>
    <Footer />
    <CartDrawer />
  </div>
</template>

<script setup>
import AnnouncementBar from '@/components/layout/AnnouncementBar.vue'
import Navbar from '@/components/layout/Navbar.vue'
import Footer from '@/components/layout/Footer.vue'
import CartDrawer from '@/components/cart/CartDrawer.vue'
</script>
