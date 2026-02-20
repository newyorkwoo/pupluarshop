<template>
  <div>
    <label v-if="label" :for="inputId" class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">{{ label }}</label>
    <input v-model="model" :id="inputId" :name="inputId" :type="type" :placeholder="placeholder" :class="['input-field', error ? 'border-brand-error' : '']" v-bind="$attrs" />
    <p v-if="error" class="text-xs text-brand-error mt-1">{{ error }}</p>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const model = defineModel()
const props = defineProps({
  label: String,
  type: { type: String, default: 'text' },
  placeholder: String,
  error: String,
})

const inputId = computed(() => {
  if (props.label) {
    return props.label.toLowerCase().replace(/[^a-z0-9\u4e00-\u9fff]+/gi, '-').replace(/^-|-$/g, '')
  }
  return undefined
})
</script>
