<template>
  <div class="order-success">
    <!-- Icon check -->
    <div class="icon">
      ✅
    </div>

    <!-- Title -->
    <h2 class="title">Order Successful!</h2>

    <!-- Message -->
    <p class="message">
      Thank you for your purchase. We will contact you soon to confirm your order.
    </p>

    <!-- Actions -->
    <div class="actions">
      <router-link
        class="btn btn-outline"
        :to="`order-detail/${orderId}`"
      >
        View Order Details
      </router-link>

      <router-link to="/" class="btn btn-primary">
        Continue Shopping
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrderSuccess",
  props: {
    orderId: {
      type: Number,
      required: true
    }
  }
};
</script>

<style scoped>
.order-success {
  text-align: center;
  margin: 80px auto;
  padding: 40px 20px;
  max-width: 600px;
  border: 1px solid #eee;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.icon {
  font-size: 70px;
  margin-bottom: 15px;
}

.title {
  color: #e65c00;
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
  border: 1px solid #e65c00;
  background: #fff;
  color: #e65c00;
}

.btn-outline:hover {
  background: #fff5f0;
}

.btn-primary {
  background: #f36f21;
  color: #fff;
  border: none;
}

.btn-primary:hover {
  background: #d95c14;
}
</style>
