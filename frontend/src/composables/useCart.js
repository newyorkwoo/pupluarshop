import { computed } from 'vue'
import { useCartStore } from '@/stores/cartStore'

export function useCart () {
  const cartStore = useCartStore()

  const items = computed(() => cartStore.items)
  const itemCount = computed(() => cartStore.itemCount)
  const totalAmount = computed(() => cartStore.totalAmount)
  const isEmpty = computed(() => cartStore.items.length === 0)

  async function addItem (item) {
    await cartStore.addItem(item)
  }

  async function updateQuantity (productId, size, quantity) {
    await cartStore.updateItem(productId, size, quantity)
  }

  async function removeItem (productId, size) {
    await cartStore.removeItem(productId, size)
  }

  function clearCart () {
    cartStore.clearCart()
  }

  return { items, itemCount, totalAmount, isEmpty, addItem, updateQuantity, removeItem, clearCart }
}
