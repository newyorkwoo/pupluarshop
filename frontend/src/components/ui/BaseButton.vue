<template>
  <button :class="classes" :disabled="disabled || loading" v-bind="$attrs">
    <span v-if="loading" class="inline-block w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin mr-2" />
    <slot />
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  variant: { type: String, default: 'primary' },
  disabled: Boolean,
  loading: Boolean,
})

const classes = computed(() => {
  const base = 'inline-flex items-center justify-center transition-all duration-300 text-sm uppercase tracking-widest font-medium disabled:opacity-50 disabled:cursor-not-allowed'
  const variants = {
    primary: 'bg-brand-black text-white px-8 py-3 hover:bg-brand-gold',
    secondary: 'border border-brand-black text-brand-black px-8 py-3 hover:bg-brand-black hover:text-white',
    danger: 'bg-brand-error text-white px-8 py-3 hover:bg-red-700',
    ghost: 'text-brand-charcoal px-4 py-2 hover:text-brand-black',
  }
  return `${base} ${variants[props.variant] || variants.primary}`
})
</script>
