<template>
  <div class="user-manager-container">
    <div class="manager-header">
      <h1>User Management</h1>
      <button @click="showCreateModal" class="btn-primary">
        <i class="fas fa-plus"></i> Add New User
      </button>
    </div>

    <!-- Users Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in paginatedUsers" :key="user.username">
            <td>{{ user.username }}</td>
            <td>{{ user.fullName || 'N/A' }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone || 'N/A' }}</td>
            <td>
              <span class="badge" :class="'badge-' + user.role.toLowerCase()">
                {{ user.role }}
              </span>
            </td>
            <td>
              <span class="badge" :class="'badge-' + user.status.toLowerCase()">
                {{ user.status }}
              </span>
            </td>
            <td>{{ formatDate(user.createdAt) }}</td>
            <td class="action-buttons">
              <button @click="editUser(user)" class="btn-edit" title="Edit">
                <i class="fas fa-edit"></i>
              </button>
              <button @click="deleteUser(user.username)" class="btn-delete" title="Delete">
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
          Page {{ currentPage }} of {{ totalPages }} (Total: {{ users.length }} users)
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

    <!-- Create/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ isEditing ? 'Edit User' : 'Create New User' }}</h2>
          <button @click="closeModal" class="btn-close">&times;</button>
        </div>
        <form @submit.prevent="saveUser" class="modal-form">
          <div class="form-group">
            <label>Username *</label>
            <input 
              v-model="currentUser.username" 
              type="text" 
              required 
              :disabled="isEditing"
              placeholder="Username"
            />
          </div>
          <div class="form-group">
            <label>Password {{ isEditing ? '(Leave blank to keep current)' : '*' }}</label>
            <input 
              v-model="currentUser.password" 
              type="password" 
              :required="!isEditing"
              placeholder="Password"
            />
          </div>
          <div class="form-group">
            <label>Email *</label>
            <input 
              v-model="currentUser.email" 
              type="email" 
              required 
              placeholder="email@example.com"
            />
          </div>
          <div class="form-group">
            <label>Full Name</label>
            <input 
              v-model="currentUser.fullName" 
              type="text" 
              placeholder="Full Name"
            />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Phone</label>
              <input 
                v-model="currentUser.phone" 
                type="tel" 
                placeholder="Phone number"
              />
            </div>
            <div class="form-group">
              <label>Role *</label>
              <select v-model="currentUser.role" required :disabled="isEmployee">
                <option value="USER">USER</option>
                <option v-if="!isEmployee" value="EMPLOYEE">EMPLOYEE</option>
                <option v-if="isFounder" value="ADMIN">ADMIN</option>
                <option v-if="isFounder" value="FOUNDER">FOUNDER</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Status *</label>
              <select v-model="currentUser.status" required>
                <option value="ACTIVE">ACTIVE</option>
                <option value="INACTIVE">INACTIVE</option>
              </select>
            </div>
            <div class="form-group">
              <label>Address</label>
              <input 
                v-model="currentUser.address" 
                type="text" 
                placeholder="Address"
              />
            </div>
          </div>
          
          <div class="modal-actions">
            <button type="button" @click="closeModal" class="btn-cancel">Cancel</button>
            <button type="submit" class="btn-submit">{{ isEditing ? 'Update' : 'Create' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '@/utils/api.js';

const users = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentPage = ref(1);
const itemsPerPage = 20;

// Check user role
const currentUserRole = ref(localStorage.getItem('userRole'));
const isEmployee = computed(() => currentUserRole.value === 'EMPLOYEE');
const isAdmin = computed(() => currentUserRole.value === 'ADMIN');
const isFounder = computed(() => currentUserRole.value === 'FOUNDER');

const currentUser = ref({
  username: '',
  password: '',
  email: '',
  fullName: '',
  phone: '',
  address: '',
  role: 'USER',
  status: 'ACTIVE'
});

// Pagination computed properties
const totalPages = computed(() => {
  return Math.ceil(users.value.length / itemsPerPage);
});

const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return users.value.slice(start, end);
});

// Fetch all users
const fetchUsers = async () => {
  try {
    const response = await api.get('/api/users');
    
    // Phân quyền xem user theo role
    if (isEmployee.value) {
      // EMPLOYEE chỉ thấy user có role USER
      users.value = response.data.filter(user => user.role === 'USER');
    } else if (isAdmin.value) {
      // ADMIN chỉ thấy USER và EMPLOYEE
      users.value = response.data.filter(user => 
        user.role === 'USER' || user.role === 'EMPLOYEE'
      );
    } else {
      // FOUNDER thấy tất cả
      users.value = response.data;
    }
    
    currentPage.value = 1;
  } catch (error) {
    console.error('Error fetching users:', error);
    alert('Failed to load users');
  }
};

// Show create modal
const showCreateModal = () => {
  isEditing.value = false;
  currentUser.value = {
    username: '',
    password: '',
    email: '',
    fullName: '',
    phone: '',
    address: '',
    role: 'USER',
    status: 'ACTIVE'
  };
  showModal.value = true;
};

// Edit user
const editUser = (user) => {
  isEditing.value = true;
  currentUser.value = {
    username: user.username,
    password: '',
    email: user.email,
    fullName: user.fullName || '',
    phone: user.phone || '',
    address: user.address || '',
    role: user.role,
    status: user.status
  };
  showModal.value = true;
};

// Save user (create or update)
const saveUser = async () => {
  try {
    // Kiểm tra quyền tạo/sửa user theo role
    if (isEmployee.value && currentUser.value.role !== 'USER') {
      alert('You can only manage users with role USER');
      return;
    }
    
    if (isAdmin.value && !['USER', 'EMPLOYEE'].includes(currentUser.value.role)) {
      alert('You can only manage users with role USER or EMPLOYEE');
      return;
    }
    
    if (isEditing.value) {
      await api.put(`/api/users/${currentUser.value.username}`, currentUser.value);
      alert('User updated successfully!');
    } else {
      await api.post('/api/users', currentUser.value);
      alert('User created successfully!');
    }
    closeModal();
    fetchUsers();
  } catch (error) {
    console.error('Error saving user:', error);
    alert('Failed to save user: ' + (error.response?.data || error.message));
  }
};

// Delete user
const deleteUser = async (username) => {
  if (!confirm(`Are you sure you want to delete user "${username}"?`)) {
    return;
  }
  try {
    await api.delete(`/api/users/${username}`);
    alert('User deleted successfully!');
    fetchUsers();
  } catch (error) {
    console.error('Error deleting user:', error);
    alert('Failed to delete user: ' + (error.response?.data || error.message));
  }
};

// Close modal
const closeModal = () => {
  showModal.value = false;
};

// Format date
const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
.user-manager-container {
  max-width: 1400px;
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

.btn-primary {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #45a049;
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
  min-width: 1000px;
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

.badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.badge-user {
  background-color: #e3f2fd;
  color: #1976d2;
}

.badge-employee {
  background-color: #e8f5e9;
  color: #388e3c;
}

.badge-admin {
  background-color: #fff3e0;
  color: #f57c00;
}

.badge-founder {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

.badge-active {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.badge-inactive {
  background-color: #ffebee;
  color: #c62828;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.btn-edit,
.btn-delete {
  padding: 0.5rem 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: opacity 0.3s;
}

.btn-edit {
  background-color: #2196F3;
  color: white;
}

.btn-edit:hover {
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
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
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

.modal-form {
  padding: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #555;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-group input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-cancel,
.btn-submit {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: opacity 0.3s;
}

.btn-cancel {
  background-color: #999;
  color: white;
}

.btn-cancel:hover {
  opacity: 0.8;
}

.btn-submit {
  background-color: #4CAF50;
  color: white;
}

.btn-submit:hover {
  opacity: 0.8;
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
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
