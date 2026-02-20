<template>
  <div>
    <h4 class="text-xs font-semibold uppercase tracking-wider mb-3">尺寸</h4>
    <div class="flex flex-wrap gap-2">
      <button v-for="size in sizes" :key="size"
        @click="$emit('update:modelValue', size)"
        :class="[
          'px-4 py-2 border text-xs uppercase transition-all',
          modelValue === size
            ? 'border-brand-black bg-brand-black text-white'
            : 'border-gray-300 hover:border-brand-black'
        ]">
        {{ size }}
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  sizes: { type: Array, default: () => [] },
  modelValue: { type: String, default: '' },
})
defineEmits(['update:modelValue'])
</script>
