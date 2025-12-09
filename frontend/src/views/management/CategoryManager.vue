<template>
  <div class="category-manager-container">
    <div class="manager-header">
      <h1>Category Management</h1>
      <button @click="showCreateModal" class="btn-primary">
        <i class="fas fa-plus"></i> Add New Category
      </button>
    </div>

    <!-- Categories Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Description</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.categoryCode">
            <td>{{ category.categoryCode }}</td>
            <td>{{ category.name }}</td>
            <td>{{ category.description || 'N/A' }}</td>
            <td>
              <span :class="['status-badge', category.status ? category.status.toLowerCase() : 'inactive']">
                {{ category.status || 'INACTIVE' }}
              </span>
            </td>
            <td>{{ formatDate(category.createdAt) }}</td>
            <td class="action-buttons">
              <button @click="editCategory(category)" class="btn-edit" title="Edit">
                <i class="fas fa-edit"></i>
              </button>
              <button @click="deleteCategory(category.categoryCode)" class="btn-delete" title="Delete">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ isEditing ? 'Edit Category' : 'Create New Category' }}</h2>
          <button @click="closeModal" class="btn-close">&times;</button>
        </div>
        <form @submit.prevent="saveCategory" class="modal-form">
          <div class="form-group">
            <label>Category Code *</label>
            <input 
              v-model="currentCategory.categoryCode" 
              type="text" 
              required 
              :disabled="isEditing"
              placeholder="e.g., CAT001"
            />
          </div>
          <div class="form-group">
            <label>Name *</label>
            <input 
              v-model="currentCategory.name" 
              type="text" 
              required 
              placeholder="Category name"
            />
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea 
              v-model="currentCategory.description" 
              rows="4"
              placeholder="Category description"
            ></textarea>
          </div>
          <div class="form-group">
            <label>Status *</label>
            <select v-model="currentCategory.status" required>
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
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
import { ref, onMounted } from 'vue';
import api from '@/utils/api.js';
import emitter from '@/utils/emitter.js';

const categories = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentCategory = ref({
  categoryCode: '',
  name: '',
  description: '',
  status: 'ACTIVE'
});

// Fetch all categories
const fetchCategories = async () => {
  try {
    const response = await api.get('/api/categories');
    console.log('Categories API Response:', response);
    console.log('Categories Data:', response.data);
    categories.value = response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
    console.error('Error details:', error.response);
    alert('Failed to load categories: ' + (error.response?.data?.message || error.message));
  }
};

// Show create modal
const showCreateModal = () => {
  isEditing.value = false;
  currentCategory.value = {
    categoryCode: '',
    name: '',
    description: '',
    status: 'ACTIVE'
  };
  showModal.value = true;
};

// Edit category
const editCategory = (category) => {
  isEditing.value = true;
  currentCategory.value = { ...category };
  showModal.value = true;
};

// Save category (create or update)
const saveCategory = async () => {
  // Validate CategoryCode format (only for new categories)
  if (!isEditing.value) {
    const codePattern = /^CAT\d+$/;
    if (!codePattern.test(currentCategory.value.categoryCode)) {
      alert('Category Code must be in format "CAT" followed by numbers (e.g., CAT01, CAT11, CAT100)');
      return;
    }
    
    // Validate that new code is the next sequential number
    if (categories.value.length > 0) {
      // Extract all existing category numbers
      const existingNumbers = categories.value
        .map(cat => {
          const match = cat.categoryCode.match(/^CAT(\d+)$/);
          return match ? parseInt(match[1]) : 0;
        })
        .filter(num => num > 0);
      
      const maxNumber = Math.max(...existingNumbers);
      const newNumber = parseInt(currentCategory.value.categoryCode.replace('CAT', ''));
      
      if (newNumber !== maxNumber + 1) {
        alert(`Category Code must be sequential. Next available code is: CAT${String(maxNumber + 1).padStart(2, '0')}`);
        return;
      }
    }
  }
  
  try {
    if (isEditing.value) {
      // Update existing category
      await api.put(`/api/categories/${currentCategory.value.categoryCode}`, currentCategory.value);
      alert('Category updated successfully!');
    } else {
      // Create new category
      await api.post('/api/categories', currentCategory.value);
      alert('Category created successfully!');
    }
    closeModal();
    fetchCategories();
    // Emit event để Navbar reload categories
    emitter.emit('categories-updated');
  } catch (error) {
    console.error('Error saving category:', error);
    alert('Failed to save category: ' + (error.response?.data?.message || error.message));
  }
};

// Delete category
const deleteCategory = async (categoryCode) => {
  if (!confirm('Are you sure you want to delete this category?')) {
    return;
  }
  try {
    await api.delete(`/api/categories/${categoryCode}`);
    alert('Category deleted successfully!');
    fetchCategories();
    // Emit event để Navbar reload categories
    emitter.emit('categories-updated');
  } catch (error) {
    console.error('Error deleting category:', error);
    alert('Failed to delete category: ' + (error.response?.data?.message || error.message));
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
  fetchCategories();
});
</script>

<style scoped>
.category-manager-container {
  max-width: 1200px;
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

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-badge.active {
  background-color: #d4edda;
  color: #155724;
}

.status-badge.inactive {
  background-color: #f8d7da;
  color: #721c24;
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
.form-group textarea,
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
</style>
