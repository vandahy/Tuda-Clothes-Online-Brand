<template>
  <div class="account-page-bg">
    <div class="container-management">
      <h1 class="h1-account">My Account</h1>

      <div class="account-layout">

        <aside class="account-sidebar">
          <ul class="sidebar-menu">
            <li 
              :class="['sidebar-item', activeTab === 'info' ? 'active' : '']" 
              @click="activeTab = 'info'"
            >
              Account Information
            </li>
            
            <li 
              :class="['sidebar-item', activeTab === 'orders' ? 'active' : '']" 
              @click="activeTab = 'orders'"
            >
              Order History
            </li>

            <li 
              :class="['sidebar-item', activeTab === 'security' ? 'active' : '']" 
              @click="activeTab = 'security'"
            >
              Change Password
            </li>

            <li 
              :class="['sidebar-item', activeTab === 'production' ? 'active' : '']" 
              @click="activeTab = 'production'"
            >
              Purchased Products
            </li>

            <li class="sidebar-item" @click="logout">
              Logout
            </li>
          </ul>
        </aside>

        <main class="account-content">
          <div class="card" v-show="activeTab === 'info'">
            <h2 class="content-title">Account Information</h2>
            <form class="form info-form" @submit.prevent="submitForm">
              <div class="form-group full-width">
                <label>Name</label>
                <input type="text" placeholder="Your name"/>
              </div>
              <div class="form-group">
                <label>Email</label>
                <input type="email" placeholder="Your email"/>
              </div>
              <div class="form-group">
                <label>Phone Number</label>
                <input type="text" placeholder="Your number" />
              </div>
              <div class="form-group full-width">
                <label>Address</label>
                <input type="text" placeholder="Your address" />
              </div>
              <button type="submit" class="update-button">Update</button>
            </form>
          </div>

          <div class="card" v-show="activeTab === 'orders'">
              <h2 class="content-title">Order History</h2>
              
              <div v-if="isLoading" style="text-align: center; padding: 50px;">
                <p>Loading data...</p>
              </div>
              
              <div v-else-if="!orders.length" class="empty-orders" style="text-align: center; padding: 50px;">
                <img src="https://i.imgur.com/g8f0gN4.png" alt="No orders" style="width: 100px; opacity: 0.5;">
                <p style="color: #777; margin-top: 15px;">You have no orders yet.</p>
              </div>

              <div v-else class="table-responsive">
                <table class="table table-hover align-middle order-history-table">
                  <thead class="table-light">
                    <tr>
                      <th>Order Code</th>
                      <th>Order Date</th>
                      <th class="text-end">Total Amount</th>
                      <th class="text-center">Status</th>
                      <th class="text-center">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="order in orders" :key="order.orderCode">
                      <td class="fw-semibold">{{ order.orderCode || 'N/A' }}</td>
                      <td>{{ new Date(order.orderDate).toLocaleDateString('vi-VN') }}</td>
                      <td class="text-end fw-bold">{{ order.totalAmount.toLocaleString() }}₫</td>
                      
                      <td class="text-center">
                        <span :class="['badge', getStatusClass(order.orderStatus)]">
                          {{ order.orderStatus }}
                        </span>
                      </td>
                      <td class="text-center">
                        <button class="btn btn-sm btn-outline-dark"  @click="viewOrderDetails(order.orderCode)">
                          Detail
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="card" v-show="activeTab === 'security'">
              <h2 class="content-title">Change Password</h2>
              <form class="form security-form" @submit.prevent="changePassword">
                <div class="form-group">
                  <label>Current Password</label>
                  <input type="password" placeholder="Current Password" />
                </div>
                <div class="form-group">
                  <label>New Password</label>
                  <input type="password" placeholder="New Password"/>
                </div>
                <div class="form-group full-width">
                  <label>Confirm New Password</label>
                  <input type="password" placeholder="Confirm New Password"/>
                </div>
                <button type="submit" class="update-button">Change Password</button>
              </form>
            </div>

            <div class="card" v-show="activeTab === 'production'">
            <h2 class="content-title">Purchased Products</h2>
            
            <div v-if="loading" style="text-align: center; padding: 50px;">
              <p>Loading data...</p>
            </div>
            
            <div v-else-if="error" style="text-align: center; padding: 50px; color: red;">
              <p>Error: {{ error }}</p>
            </div>
            
            <div v-else-if="!productList.length" class="empty-orders" style="text-align: center; padding: 50px;">
              <img src="https://i.imgur.com/g8f0gN4.png" alt="No products" style="width: 100px; opacity: 0.5;">
              <p style="color: #777; margin-top: 15px;">You haven't purchased any products yet.</p>
            </div>

            <div v-else class="table-responsive">
              <table class="table table-hover align-middle order-history-table">
                <thead class="table-light">
                  <tr>
                    <th style="width: 10%;">Image</th>
                    <th style="width: 25%;">Product Name</th>
                    <th style="width: 15%;">Order Code</th>
                    <th style="width: 15%;">Order Date</th>
                    <th style="width: 10%;">Quantity</th>
                    <th style="width: 15%; text-align: right;">Price</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="product in productList" :key="product.id">
                    <!-- Image -->
                    <td>
                      <img 
                        :src="product.imageUrl || 'https://via.placeholder.com/50'" 
                        :alt="product.name"
                        style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;"
                      />
                    </td>
                    
                    <!-- Product Name -->
                    <td class="fw-semibold">{{ product.productName }}</td>
                    
                    <!-- Order Code -->
                    <td>{{ product.orderCode }}</td>
                    
                    <!-- Order Date -->
                    <td>{{ new Date(product.orderDate).toLocaleDateString('en-US') }}</td>
                    
                    <!-- Quantity -->
                    <td class="text-center">{{ product.quantity }}</td>
                    
                    <!-- Price -->
                    <td class="text-end fw-bold">{{ product.price.toLocaleString() }}₫</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
  <OrderDetailModal
    :isVisible="!!viewingOrder" 
    @close="closeModal"
    :orderId="viewingOrder?.orderId"
    :products="viewingOrder?.products"
    :recipientName="viewingOrder?.recipientName"
    :total="viewingOrder?.total"
    />
</template>

<script setup>
import '../assets/css/accountManagement.css'
import { ref, watch } from 'vue'
import { useOrderHistory } from '../composables/userOrderHistory'
import OrderDetailModal from '../components/OrderDetailModal.vue'
import { useProductHistory } from '../composables/productHistory'

const { productList, loading, error, fetchProductHistory } = useProductHistory()
const isModalVisible = ref(false) 

const handleProductionTab = async () => {
  if (activeTab.value === 'production') {
    await fetchProductHistory()
  }
}

function toggleModal() {
  isModalVisible.value = !isModalVisible.value 
}

function getStatusClass(status) {
  switch (status?.toUpperCase()) {
    case 'PENDING':   return 'status-pending';
    case 'CONFIRMED': return 'status-confirmed';
    case 'SHIPPING':  return 'status-shipping';
    case 'COMPLETED': return 'status-completed';
    case 'CANCELLED': return 'status-cancelled';
    default:          return 'status-default';
  }
}

const {
  isLoading,
  viewingOrder, 
  orders,
  fetchOrderHistory,
  viewOrderDetails,
  closeModal     
} = useOrderHistory();


const activeTab = ref('info') 

// Logout function
function logout() {
  alert('Logged out!')
  // Add logic to remove token and redirect to home
  // localStorage.removeItem('token');
  // window.location.href = '/';
}

// 2 old submit functions
function submitForm() {
  alert('Information updated!');
}

function changePassword() {
  alert('Password changed!');
}

watch(activeTab, (newTab) => {
  if (newTab === 'orders') {
    fetchOrderHistory()
  } else if (newTab === 'production') {
    fetchProductHistory()
  }
})
</script>