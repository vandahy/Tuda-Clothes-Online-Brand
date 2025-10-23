import { ref } from 'vue'

const productList = ref([])
const loading = ref(false)
const error = ref(null)

export function useProductHistory() {
    const fetchProductHistory = async () => {
        loading.value = true
        error.value = null

        try {
            const token = localStorage.getItem('token')

            const response = await fetch('/api/order-detail/product-list', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            })

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`)
            }

            const data = await response.json()
            console.log('Product History API Response:', data)

            productList.value = data || []

        } catch (err) {
            console.error('Error fetching product history:', err)
            error.value = err.message
            productList.value = []
        } finally {
            loading.value = false
        }
    }

    const resetProductHistory = () => {
        productList.value = []
        error.value = null
    }

    return {
        productList,
        loading,
        error,
        fetchProductHistory,
        resetProductHistory
    }
}