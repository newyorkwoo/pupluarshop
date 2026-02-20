import { ref } from 'vue'

const toasts = ref([])
let nextId = 1

export function useNotification () {
  function show (message, type = 'info', duration = 3000) {
    const id = nextId++
    toasts.value.push({ id, message, type })
    if (duration > 0) {
      setTimeout(() => remove(id), duration)
    }
    return id
  }

  function success (message, duration) {
    return show(message, 'success', duration)
  }

  function error (message, duration) {
    return show(message, 'error', duration)
  }

  function warning (message, duration) {
    return show(message, 'warning', duration)
  }

  function info (message, duration) {
    return show(message, 'info', duration)
  }

  function remove (id) {
    const idx = toasts.value.findIndex(t => t.id === id)
    if (idx > -1) toasts.value.splice(idx, 1)
  }

  return { toasts, show, success, error, warning, info, remove }
}
