<template>
  <div class="product-manager-container">
    <div class="manager-header">
      <h1>Product Management</h1>
      <button @click="showCreateModal" class="btn-primary">
        <i class="fas fa-plus"></i> Add New Product
      </button>
    </div>

    <!-- Products Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>Image</th>
            <th>Code</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Discount</th>
            <th>Stock</th>
            <th>Created At</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in paginatedProducts" :key="product.productCode">
            <td>
              <img 
                v-if="getPrimaryImage(product)" 
                :src="getPrimaryImage(product)" 
                :alt="product.name"
                class="product-thumbnail"
              />
              <span v-else class="no-image">No Image</span>
            </td>
            <td>{{ product.productCode }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.category?.name || 'N/A' }}</td>
            <td>{{ formatPrice(product.price) }}</td>
            <td>{{ formatPrice(product.discount) }}</td>
            <td>{{ product.stockQuantity }}</td>
            <td>{{ formatDate(product.createdAt) }}</td>
            <td class="action-buttons">
              <button @click="editProduct(product)" class="btn-edit" title="Edit">
                <i class="fas fa-edit"></i>
              </button>
              <button @click="deleteProduct(product.productCode)" class="btn-delete" title="Delete">
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
          Page {{ currentPage }} of {{ totalPages }} (Total: {{ products.length }} products)
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
          <h2>{{ isEditing ? 'Edit Product' : 'Create New Product' }}</h2>
          <button @click="closeModal" class="btn-close">&times;</button>
        </div>
        <form @submit.prevent="saveProduct" class="modal-form">
          <div class="form-group">
            <label>Product Code *</label>
            <input 
              v-model="currentProduct.productCode" 
              type="text" 
              required 
              :disabled="isEditing"
              placeholder="e.g., PRD001"
            />
          </div>
          <div class="form-group">
            <label>Name *</label>
            <input 
              v-model="currentProduct.name" 
              type="text" 
              required 
              placeholder="Product name"
            />
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea 
              v-model="currentProduct.description" 
              rows="4"
              placeholder="Product description"
            ></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Category *</label>
              <select v-model="currentProduct.categoryCode" required>
                <option value="">Select Category</option>
                <option v-for="cat in categories" :key="cat.categoryCode" :value="cat.categoryCode">
                  {{ cat.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>Stock Quantity *</label>
              <input 
                v-model.number="currentProduct.stockQuantity" 
                type="number" 
                required 
                min="0"
                placeholder="0"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Price (VND) *</label>
              <input 
                v-model.number="currentProduct.price" 
                type="number" 
                required 
                min="0"
                step="1000"
                placeholder="0"
              />
            </div>
            <div class="form-group">
              <label>Discount (VND)</label>
              <input 
                v-model.number="currentProduct.discount" 
                type="number" 
                min="0"
                step="1000"
                placeholder="0"
              />
            </div>
          </div>
          
          <!-- Image Upload Section -->
          <div class="form-group">
            <label>Product Images * (Select exactly 4 images)</label>
            <input 
              type="file"
              @change="handleImageUpload"
              accept="image/*"
              multiple
              ref="imageInput"
              class="file-input"
            />
            <p class="image-hint">Selected: {{ imagePreview.length }}/4 images</p>
            <div v-if="imagePreview.length > 0" class="image-preview-container">
              <div v-for="(img, index) in imagePreview" :key="index" class="image-preview-item">
                <span v-if="img.isExisting" class="existing-badge">Existing</span>
                <img :src="img.url" :alt="`Preview ${index + 1}`" />
                <div class="image-controls">
                  <label class="primary-checkbox">
                    <input 
                      type="radio" 
                      :name="'primary-image'"
                      :value="index"
                      v-model="primaryImageIndex"
                    />
                    Primary
                  </label>
                  <button type="button" @click="removeImage(index)" class="btn-remove-img">×</button>
                </div>
              </div>
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

const products = ref([]);
const categories = ref([]);
const showModal = ref(false);
const isEditing = ref(false);
const currentPage = ref(1);
const itemsPerPage = 20;
const selectedImages = ref([]);
const imagePreview = ref([]);
const primaryImageIndex = ref(0);
const imageInput = ref(null);
const deletedImageIds = ref([]); // Track deleted existing images
const originalPrimaryImageId = ref(null); // Track original primary image

const currentProduct = ref({
  productCode: '',
  name: '',
  description: '',
  price: 0,
  discount: 0,
  stockQuantity: 0,
  categoryCode: ''
});

// Pagination computed properties
const totalPages = computed(() => {
  return Math.ceil(products.value.length / itemsPerPage);
});

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return products.value.slice(start, end);
});

// Fetch all products
const fetchProducts = async () => {
  try {
    const response = await api.get('/api/products');
    products.value = response.data;
    currentPage.value = 1; // Reset to first page when fetching
  } catch (error) {
    console.error('Error fetching products:', error);
    alert('Failed to load products');
  }
};

// Fetch all categories
const fetchCategories = async () => {
  try {
    const response = await api.get('/api/categories');
    categories.value = response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

// Show create modal
const showCreateModal = () => {
  isEditing.value = false;
  currentProduct.value = {
    productCode: '',
    name: '',
    description: '',
    price: 0,
    discount: 0,
    stockQuantity: 0,
    categoryCode: ''
  };
  selectedImages.value = [];
  imagePreview.value = [];
  primaryImageIndex.value = 0;
  deletedImageIds.value = [];
  originalPrimaryImageId.value = null;
  showModal.value = true;
};

// Edit product
const editProduct = (product) => {
  isEditing.value = true;
  currentProduct.value = {
    productCode: product.productCode,
    name: product.name,
    description: product.description,
    price: product.price,
    discount: product.discount,
    stockQuantity: product.stockQuantity,
    categoryCode: product.category?.categoryCode || ''
  };
  
  // Load existing images for preview
  selectedImages.value = [];
  imagePreview.value = [];
  deletedImageIds.value = [];
  
  if (product.images && product.images.length > 0) {
    imagePreview.value = product.images.map((img, index) => ({
      url: img.imageUrl,
      isExisting: true,
      imageId: img.imageId
    }));
    // Find primary image index
    const primaryIndex = product.images.findIndex(img => img.isPrimary);
    primaryImageIndex.value = primaryIndex >= 0 ? primaryIndex : 0;
    
    // Store original primary image ID
    const primaryImg = product.images.find(img => img.isPrimary);
    originalPrimaryImageId.value = primaryImg ? primaryImg.imageId : null;
  } else {
    primaryImageIndex.value = 0;
    originalPrimaryImageId.value = null;
  }
  
  showModal.value = true;
};

// Handle image upload with resize
const handleImageUpload = async (event) => {
  const files = Array.from(event.target.files);
  
  if (files.length + imagePreview.value.length > 4) {
    alert('You can only upload a maximum of 4 images');
    if (imageInput.value) imageInput.value.value = '';
    return;
  }
  
  for (const file of files) {
    if (imagePreview.value.length >= 4) break;
    
    // Resize image before adding
    const resizedFile = await resizeImage(file, 800, 800, 0.8);
    selectedImages.value.push(resizedFile);
    
    // Create preview
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value.push({
        url: e.target.result,
        file: resizedFile,
        isExisting: false
      });
    };
    reader.readAsDataURL(resizedFile);
  }
  
  // Clear input
  if (imageInput.value) imageInput.value.value = '';
};

// Resize image function
const resizeImage = (file, maxWidth, maxHeight, quality) => {
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = new Image();
      img.onload = () => {
        const canvas = document.createElement('canvas');
        let width = img.width;
        let height = img.height;
        
        if (width > height) {
          if (width > maxWidth) {
            height *= maxWidth / width;
            width = maxWidth;
          }
        } else {
          if (height > maxHeight) {
            width *= maxHeight / height;
            height = maxHeight;
          }
        }
        
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, width, height);
        
        canvas.toBlob((blob) => {
          resolve(new File([blob], file.name, {
            type: 'image/jpeg',
            lastModified: Date.now()
          }));
        }, 'image/jpeg', quality);
      };
      img.src = e.target.result;
    };
    reader.readAsDataURL(file);
  });
};

// Remove image
const removeImage = (index) => {
  const img = imagePreview.value[index];
  
  // If it's an existing image, track it for deletion
  if (img.isExisting && img.imageId) {
    deletedImageIds.value.push(img.imageId);
  } else {
    // If it's a new uploaded image, remove from selectedImages too
    // Count how many non-existing images come before this one
    let selectedIndex = 0;
    for (let i = 0; i < index; i++) {
      if (!imagePreview.value[i].isExisting) {
        selectedIndex++;
      }
    }
    selectedImages.value.splice(selectedIndex, 1);
  }
  
  imagePreview.value.splice(index, 1);
  
  // Adjust primary image index if needed
  if (primaryImageIndex.value >= imagePreview.value.length && imagePreview.value.length > 0) {
    primaryImageIndex.value = imagePreview.value.length - 1;
  } else if (imagePreview.value.length === 0) {
    primaryImageIndex.value = 0;
  }
};

// Get primary image for display in table
const getPrimaryImage = (product) => {
  if (!product.images || product.images.length === 0) return null;
  const primary = product.images.find(img => img.isPrimary);
  return primary ? primary.imageUrl : product.images[0]?.imageUrl;
};

// Save product (create or update)
const saveProduct = async () => {
  // Validate images for new product
  if (!isEditing.value && selectedImages.value.length !== 4) {
    alert('Please select exactly 4 images for the product');
    return;
  }
  
  try {
    if (isEditing.value) {
      // Update existing product
      const payload = {
        ...currentProduct.value,
        category: { categoryCode: currentProduct.value.categoryCode }
      };
      await api.put(`/api/products/${currentProduct.value.productCode}`, payload);
      
      // Delete removed images
      for (const imageId of deletedImageIds.value) {
        await api.delete(`/api/products/${currentProduct.value.productCode}/images/${imageId}`);
      }
      
      // Add new images
      for (let i = 0; i < selectedImages.value.length; i++) {
        const formData = new FormData();
        formData.append('image', selectedImages.value[i]);
        
        // Check if this new image should be primary
        // Find the index of this new image in imagePreview
        let newImagePreviewIndex = 0;
        let countNewImages = 0;
        for (let j = 0; j < imagePreview.value.length; j++) {
          if (!imagePreview.value[j].isExisting) {
            if (countNewImages === i) {
              newImagePreviewIndex = j;
              break;
            }
            countNewImages++;
          }
        }
        
        const shouldBePrimary = newImagePreviewIndex === primaryImageIndex.value;
        formData.append('isPrimary', shouldBePrimary);
        
        await api.post(`/api/products/${currentProduct.value.productCode}/images`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
      }
      
      // Update primary image if it changed and is an existing image
      const currentPrimary = imagePreview.value[primaryImageIndex.value];
      if (currentPrimary && currentPrimary.isExisting && 
          currentPrimary.imageId && 
          currentPrimary.imageId !== originalPrimaryImageId.value) {
        await api.put(`/api/products/${currentProduct.value.productCode}/images/primary`, null, {
          params: { imageId: currentPrimary.imageId }
        });
      }
      
      alert('Product updated successfully!');
    } else {
      // Create new product with images
      const formData = new FormData();
      
      formData.append('productCode', currentProduct.value.productCode);
      formData.append('name', currentProduct.value.name);
      formData.append('description', currentProduct.value.description || '');
      formData.append('price', currentProduct.value.price);
      formData.append('discount', currentProduct.value.discount || 0);
      formData.append('stockQuantity', currentProduct.value.stockQuantity);
      formData.append('categoryCode', currentProduct.value.categoryCode);
      
      selectedImages.value.forEach((file, index) => {
        formData.append('images', file);
        formData.append(`isPrimary_${index}`, index === primaryImageIndex.value);
      });
      
      await api.post('/api/products', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      alert('Product created successfully!');
    }
    
    closeModal();
    fetchProducts();
  } catch (error) {
    console.error('Error saving product:', error);
    alert('Failed to save product: ' + (error.response?.data || error.message));
  }
};

// Delete product
const deleteProduct = async (productCode) => {
  if (!confirm('Are you sure you want to delete this product?')) {
    return;
  }
  try {
    await api.delete(`/api/products/${productCode}`);
    alert('Product deleted successfully!');
    fetchProducts();
  } catch (error) {
    console.error('Error deleting product:', error);
    alert('Failed to delete product: ' + (error.response?.data?.message || error.message));
  }
};

// Close modal
const closeModal = () => {
  showModal.value = false;
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

onMounted(() => {
  fetchProducts();
  fetchCategories();
});
</script>

<style scoped>
.product-manager-container {
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
  max-width: 700px;
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

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}

/* Image Upload & Preview Styles */
.file-input {
  cursor: pointer;
}

.image-hint {
  font-size: 0.875rem;
  color: #666;
  margin-top: 0.5rem;
}

.image-preview-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  margin-top: 1rem;
}

.image-preview-item {
  position: relative;
  border: 2px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.existing-badge {
  position: absolute;
  top: 5px;
  left: 5px;
  background: #2196F3;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.7rem;
  font-weight: 600;
  z-index: 1;
}

.image-preview-item img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.image-controls {
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.95);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.primary-checkbox {
  font-size: 0.75rem;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  cursor: pointer;
}

.btn-remove-img {
  background: #f44336;
  color: white;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1.2rem;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-remove-img:hover {
  background: #d32f2f;
}

/* Product Thumbnail in Table */
.product-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.no-image {
  display: inline-block;
  width: 60px;
  height: 60px;
  line-height: 60px;
  text-align: center;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 0.75rem;
  color: #999;
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
</style>
