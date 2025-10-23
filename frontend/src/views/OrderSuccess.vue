<template>
  <div class="order-success">
    <div class="icon">
      <svg xmlns="http://www.w3.org/2000/svg" height="70px" viewBox="0 0 24 24" width="70px" fill="#000000">
        <path d="M0 0h24v24H0V0z" fill="none"/>
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-2.07-4.64l-3.53-3.53 1.41-1.41L9.93 12.5l5.65-5.65 1.41 1.41-7.06 7.07z"/>
      </svg>
    </div>

    <h2 class="title">Order Successful!</h2>

    <p class="message">
      Thank you for your purchase. We will contact you soon to confirm your order.
    </p>

    <div class="actions">
      <button
        class="btn btn-outline"
        @click="openOrderDetails"
      >
        View Order Details
      </button>

      <router-link to="/" class="btn btn-primary">
        Continue Shopping
      </router-link>
    </div>
  </div>

  <OrderDetailModal
    :isVisible="!!viewingOrder" 
    @close="closeModal"
    
    :orderId="viewingOrder?.orderId"
    :orderDate="viewingOrder?.orderDate"
    :staffName="viewingOrder?.staffName"
    :staffEmail="viewingOrder?.staffEmail"
    :customerName="viewingOrder?.customerName"
    :customerPhone="viewingOrder?.customerPhone"
    :recipientName="viewingOrder?.recipientName"
    :recipientPhone="viewingOrder?.recipientPhone"
    :recipientAddress="viewingOrder?.recipientAddress"
    :products="viewingOrder?.products"
    :paymentMethod="viewingOrder?.paymentMethod"
    :shippingFee="viewingOrder?.shippingFee"
    :discountAmount="viewingOrder?.discountAmount"
    :subtotal="viewingOrder?.subtotal"
    :total="viewingOrder?.total"
  />
</template>

<script setup>
import { onMounted } from 'vue'
import { useOrderHistory } from '../composables/userOrderHistory'
import OrderDetailModal from '../components/OrderDetailModal.vue'

onMounted(() => {
  console.log('OrderSuccess mounted - props.orderId =', props.orderId)
})

// --- 1. PROPS ---
const props = defineProps({
  orderId: { 
    type: String,
    required: true
  }
})

const {
  viewingOrder,     
  viewOrderDetails, 
  closeModal        
} = useOrderHistory()

const openOrderDetails = () => {
  viewOrderDetails(props.orderId)
}
</script>

<style scoped>
.order-success {
  text-align: center;
  margin: 150px auto;
  padding: 40px 20px;
  max-width: 600px;
  border: 1px solid #eee;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.icon {
  margin-bottom: 15px;
}
.icon svg {
  fill: #000000;
}

.title {
  color: black;
  font-weight: bold;
  margin: 10px 0;
  font-size: 28px;
}

.message {
  margin: 10px 0 30px;
  font-size: 16px;
  color: #555;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  flex-wrap: wrap;
}

.btn {
  padding: 12px 22px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.3s ease;
}

.btn-outline {
  border: 1px solid #555;
  background: #fff;
  color: #555;
}
.btn-outline:hover {
  background: #f4f4f4;
}

.btn-primary {
  background: black;
  color: #fff;
  border: 1px solid black;
}
.btn-primary:hover {
  opacity: 0.8;
}

.modal.show {
  display: block;
}
</style>