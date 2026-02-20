<template>
  <div class="flex flex-col lg:flex-row gap-6">
    <!-- Thumbnails -->
    <div class="hidden lg:flex flex-col gap-2 w-20">
      <button v-for="(img, i) in images" :key="i" @click="selected = i"
        :class="['border overflow-hidden aspect-square', selected === i ? 'border-brand-black' : 'border-gray-200']">
        <img :src="img" class="w-full h-full object-cover" :alt="`Thumbnail ${i+1}`" />
      </button>
    </div>
    <!-- Main image -->
    <div class="flex-1 bg-brand-cream aspect-[3/4] overflow-hidden cursor-zoom-in" @click="$emit('zoom', images[selected])">
      <img :src="images[selected] || '/placeholder.jpg'" class="w-full h-full object-cover" :alt="alt" />
    </div>
    <!-- Mobile dots -->
    <div class="flex lg:hidden justify-center gap-2 mt-2">
      <button v-for="(_, i) in images" :key="i" @click="selected = i"
        :class="['w-2 h-2 rounded-full', selected === i ? 'bg-brand-black' : 'bg-gray-300']" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  images: { type: Array, default: () => ['/placeholder.jpg'] },
  alt: { type: String, default: '' },
})
defineEmits(['zoom'])

const selected = ref(0)
</script>
