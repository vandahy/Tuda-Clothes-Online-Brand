<template>
  <div class="order-manager-container">
    <div class="manager-header">
      <h1>Order Management</h1>
    </div>

    <!-- Orders Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer</th>
            <th>Phone</th>
            <th>Total Amount</th>
            <th>Status</th>
            <th>Payment</th>
            <th>Order Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in paginatedOrders" :key="order.orderCode">
            <td>#{{ order.orderCode }}</td>
            <td>
              <div class="customer-info">
                <strong>{{ order.customerName }}</strong>
                <small>{{ order.customerEmail }}</small>
              </div>
            </td>
            <td>{{ order.customerPhone }}</td>
            <td class="amount">{{ formatPrice(order.totalAmount) }}</td>
            <td>
              <select 
                v-model="order.status" 
                @change="updateStatus(order.orderCode, order.status)"
                class="status-select"
                :class="'status-' + order.status.toLowerCase()"
              >
                <option value="PENDING">PENDING</option>
                <option value="CONFIRMED">CONFIRMED</option>
                <option value="SHIPPING">SHIPPING</option>
                <option value="COMPLETED">COMPLETED</option>
                <option value="CANCELLED">CANCELLED</option>
              </select>
            </td>
            <td>
              <div class="payment-info">
                <span class="badge badge-payment">{{ order.paymentMethod || 'N/A' }}</span>
                <span class="badge" :class="'badge-' + (order.paymentStatus || 'pending').toLowerCase()">
                  {{ order.paymentStatus || 'PENDING' }}
                </span>
              </div>
            </td>
            <td>{{ formatDate(order.orderDate) }}</td>
            <td class="action-buttons">
              <button @click="viewOrderDetails(order)" class="btn-view" title="View Details">
                <i class="fas fa-eye"></i>
              </button>
              <button @click="deleteOrder(order.orderCode)" class="btn-delete" title="Delete">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- Pagination -->
      <div class="pagination">
        <button 
          @click="currentPage = 1" 
          :disabled="currentPage === 1"
          class="page-btn"
        >
          First
        </button>
        <button 
          @click="currentPage--" 
          :disabled="currentPage === 1"
          class="page-btn"
        >
          Previous
        </button>
        
        <span class="page-info">
          Page {{ currentPage }} of {{ totalPages }} (Total: {{ orders.length }} orders)
        </span>
        
        <button 
          @click="currentPage++" 
          :disabled="currentPage === totalPages"
          class="page-btn"
        >
          Next
        </button>
        <button 
          @click="currentPage = totalPages" 
          :disabled="currentPage === totalPages"
          class="page-btn"
        >
          Last
        </button>
      </div>
    </div>

    <!-- Order Details Modal -->
    <div v-if="showDetailsModal" class="modal-overlay" @click.self="closeDetailsModal">
      <div class="modal-content modal-large">
        <div class="modal-header">
          <h2>Order Details #{{ selectedOrder?.orderCode }}</h2>
          <button @click="closeDetailsModal" class="btn-close">&times;</button>
        </div>
        <div class="modal-body" v-if="selectedOrder">
          <div class="order-info-grid">
            <div class="info-section">
              <h3>Customer Information</h3>
              <p><strong>Name:</strong> {{ selectedOrder.customerName }}</p>
              <p><strong>Email:</strong> {{ selectedOrder.customerEmail }}</p>
              <p><strong>Phone:</strong> {{ selectedOrder.customerPhone }}</p>
              <p><strong>Address:</strong> {{ selectedOrder.shippingAddress }}</p>
            </div>
            <div class="info-section">
              <h3>Order Information</h3>
              <p><strong>Order Date:</strong> {{ formatDateTime(selectedOrder.orderDate) }}</p>
              <p><strong>Status:</strong> <span class="badge" :class="'badge-' + selectedOrder.status.toLowerCase()">{{ selectedOrder.status }}</span></p>
              <p><strong>Payment Method:</strong> {{ selectedOrder.paymentMethod }}</p>
              <p><strong>Payment Status:</strong> <span class="badge" :class="'badge-' + (selectedOrder.paymentStatus || 'pending').toLowerCase()">{{ selectedOrder.paymentStatus }}</span></p>
            </div>
          </div>

          <h3>Order Items</h3>
          <table class="details-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Size</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in selectedOrder.orderDetails" :key="item.orderDetailId">
                <td>{{ item.productName }}</td>
                <td>{{ item.size }}</td>
                <td>{{ formatPrice(item.price) }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ formatPrice(item.subtotal) }}</td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td colspan="4" class="text-right"><strong>Total:</strong></td>
                <td><strong>{{ formatPrice(selectedOrder.totalAmount) }}</strong></td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '@/utils/api.js';

const orders = ref([]);
const showDetailsModal = ref(false);
const selectedOrder = ref(null);
const currentPage = ref(1);
const itemsPerPage = 20;

// Pagination computed properties
const totalPages = computed(() => {
  return Math.ceil(orders.value.length / itemsPerPage);
});

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return orders.value.slice(start, end);
});

// Fetch all orders
const fetchOrders = async () => {
  try {
    const response = await api.get('/api/admin/orders');
    orders.value = response.data;
    currentPage.value = 1;
  } catch (error) {
    console.error('Error fetching orders:', error);
    alert('Failed to load orders');
  }
};

// View order details
const viewOrderDetails = async (order) => {
  try {
    const response = await api.get(`/api/admin/orders/${order.orderCode}`);
    selectedOrder.value = response.data;
    showDetailsModal.value = true;
  } catch (error) {
    console.error('Error fetching order details:', error);
    alert('Failed to load order details');
  }
};

// Close details modal
const closeDetailsModal = () => {
  showDetailsModal.value = false;
  selectedOrder.value = null;
};

// Update order status
const updateStatus = async (orderCode, status) => {
  try {
    await api.put(`/api/admin/orders/${orderCode}/status`, { status });
    alert('Order status updated successfully!');
    fetchOrders();
  } catch (error) {
    console.error('Error updating order status:', error);
    alert('Failed to update order status');
    fetchOrders(); // Reload to revert UI
  }
};

// Delete order
const deleteOrder = async (orderCode) => {
  if (!confirm(`Are you sure you want to delete order #${orderCode}?`)) {
    return;
  }
  try {
    await api.delete(`/api/admin/orders/${orderCode}`);
    alert('Order deleted successfully!');
    fetchOrders();
  } catch (error) {
    console.error('Error deleting order:', error);
    alert('Failed to delete order');
  }
};

// Format price
const formatPrice = (price) => {
  if (price === undefined || price === null || isNaN(price)) return '0₫';
  return Number(price).toLocaleString('vi-VN', {
    style: 'currency',
    currency: 'VND'
  });
};

// Format date
const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

// Format date time
const formatDateTime = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleString('vi-VN');
};

onMounted(() => {
  fetchOrders();
});
</script>

<style scoped>
.order-manager-container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 8rem 1rem 4rem 1rem;
  min-height: 100vh;
}

.manager-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.manager-header h1 {
  font-size: 2rem;
  color: #333;
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
}

.data-table thead {
  background-color: #f5f5f5;
}

.data-table th,
.data-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.data-table th {
  font-weight: 600;
  color: #555;
}

.data-table tbody tr:hover {
  background-color: #f9f9f9;
}

.customer-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.customer-info small {
  color: #666;
  font-size: 0.85rem;
}

.amount {
  font-weight: 600;
  color: #2e7d32;
}

.payment-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.status-select {
  padding: 0.5rem;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  text-transform: uppercase;
}

.status-pending { background-color: #fff3e0; color: #f57c00; }
.status-confirmed { background-color: #e3f2fd; color: #1976d2; }
.status-shipping { background-color: #e8eaf6; color: #5e35b1; }
.status-completed { background-color: #e8f5e9; color: #2e7d32; }
.status-cancelled { background-color: #ffebee; color: #c62828; }

.badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: uppercase;
  display: inline-block;
}

.badge-payment {
  background-color: #f5f5f5;
  color: #666;
}

.badge-pending {
  background-color: #fff3e0;
  color: #f57c00;
}

.badge-paid, .badge-completed {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.badge-failed {
  background-color: #ffebee;
  color: #c62828;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.btn-view,
.btn-delete {
  padding: 0.5rem 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: opacity 0.3s;
}

.btn-view {
  background-color: #2196F3;
  color: white;
}

.btn-view:hover {
  opacity: 0.8;
}

.btn-delete {
  background-color: #f44336;
  color: white;
}

.btn-delete:hover {
  opacity: 0.8;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 2rem;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-large {
  max-width: 1000px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #e0e0e0;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #333;
}

.btn-close {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: #999;
  line-height: 1;
}

.btn-close:hover {
  color: #333;
}

.modal-body {
  padding: 1.5rem;
}

.order-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.info-section h3 {
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.1rem;
  border-bottom: 2px solid #4CAF50;
  padding-bottom: 0.5rem;
}

.info-section p {
  margin: 0.5rem 0;
  color: #555;
}

.details-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.details-table th,
.details-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.details-table thead {
  background-color: #f5f5f5;
}

.details-table tfoot {
  background-color: #f9f9f9;
  font-weight: 600;
}

.text-right {
  text-align: right;
}

/* Pagination Styles */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid #e0e0e0;
}

.page-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background-color: #4CAF50;
  color: white;
  border-color: #4CAF50;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.875rem;
  color: #555;
  padding: 0 1rem;
}

@media (max-width: 768px) {
  .order-info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
