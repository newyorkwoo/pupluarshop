<template>
  <div>
    <h4 class="text-xs font-semibold uppercase tracking-wider mb-3">尺寸</h4>
    <div class="flex flex-wrap gap-2">
      <button v-for="s in sizes" :key="s.size || s"
        @click="$emit('update:modelValue', s.size || s)"
        :disabled="s.stock === 0"
        :class="[
          'px-4 py-2 border text-xs uppercase transition-all',
          modelValue === (s.size || s)
            ? 'border-brand-black bg-brand-black text-white'
            : s.stock === 0
              ? 'border-gray-200 text-gray-300 cursor-not-allowed line-through'
              : 'border-gray-300 hover:border-brand-black'
        ]">
        {{ s.size || s }}
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
