<template>
  <div class="admin-container">
    <div class="form-section card">
      <h2>{{ isEdit ? 'Edit User Information' : 'Create New User' }}</h2>
      <div class='form-grid'>
        <input v-model='user.username' placeholder='Username' :disabled='isEdit' />
        <input v-model='user.password' type='password' placeholder='Password' />
        <input v-model='user.fullName' placeholder='Full Name' />
        <input v-model='user.email' type='email' placeholder='Email' />
        <input v-model='user.phone' placeholder='Phone' />
        <input v-model='user.address' placeholder='Address' />
        <select v-model='user.role'>
          <option value='EMPLOYEE'>EMPLOYEE</option>
          <option value='ADMIN'>ADMIN</option>
          <option value='FOUNDER'>FOUNDER</option>
        </select>
        <select v-model='user.status'>
          <option value='ACTIVE'>ACTIVE</option>
          <option value='INACTIVE'>INACTIVE</option>
        </select>
      </div>
      <div class='btn-group'>
        <button class="btn btn-primary" @click='submitForm'>
          {{ isEdit ? "Update User" : "Create User" }}
        </button>
        <button class="btn btn-secondary" @click='resetForm'>Reset</button>
      </div>
    </div>

    <div class="table-section card">
      <div class="table-header">
        <h3>User List</h3>
        <input type="text" v-model="searchQuery" class="search-input" placeholder="Search by name, email, username...">
      </div>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th class="col-no">No</th>
              <th class="col-username">Username</th>
              <th class="col-fullname">Full Name</th>
              <th class="col-email">Email</th>
              <th class="col-role">Role</th>
              <th class="col-status">Status</th>
              <th class="col-action">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for='(u, i) in filteredUsers' :key='u.username'>
              <td>{{ i + 1 }}</td>
              <td>{{ u.username }}</td>
              <td>{{ u.fullName }}</td>
              <td>{{ u.email }}</td>
              <td>{{ u.role }}</td>
              <td class="text-center">
                <span :class="['status', u.status === 'ACTIVE' ? 'status-active' : 'status-inactive']">
                  {{ u.status }}
                </span>
              </td>
              <td class="action-buttons">
                <button class="btn-icon" @click='editUser(u)' title="Edit User">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 00-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
                </button>
                <button class="btn-icon btn-danger-icon" @click='deleteUser(u.username)' title="Delete User">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
// Script không thay đổi, giữ nguyên như cũ
import { ref, onMounted, computed } from 'vue';

// --- API URL ---
const API_URL = 'http://localhost:8080/api/users';

// --- State ---
const users = ref([]);
const user = ref({
  username: '',
  password: '',
  fullName: '',
  email: '',
  phone: '',
  address: '',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
});
const isEdit = ref(false);
const searchQuery = ref('');

// --- Computed: Logic lọc dữ liệu ---
const filteredUsers = computed(() => {
  if (!searchQuery.value) {
    return users.value;
  }
  const lowerCaseQuery = searchQuery.value.toLowerCase();
  return users.value.filter(u =>
    (u.username ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (u.fullName ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (u.email ?? '').toLowerCase().includes(lowerCaseQuery)
  );
});

// --- Lấy danh sách users ---
const getUsers = async () => {
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error('Failed to fetch users');
    users.value = await res.json();
  } catch (err) {
    console.error('Error fetching users:', err);
    alert('Error fetching users. Check console.');
  }
};

// --- Reset form ---
const resetForm = () => {
  user.value = {
    username: '',
    password: '',
    fullName: '',
    email: '',
    phone: '',
    address: '',
    role: 'EMPLOYEE',
    status: 'ACTIVE',
  };
  isEdit.value = false;
};

// --- Create / Update ---
const submitForm = async () => {
  if (!user.value.username || (!user.value.password && !isEdit.value)) {
      alert('Username and Password are required for new users.');
      return;
  }
  try {
    const method = isEdit.value ? 'PUT' : 'POST';
    const url = isEdit.value ? `${API_URL}/${user.value.username}` : API_URL;

    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user.value),
    });

    if (!res.ok) throw new Error(`${isEdit.value ? 'Update' : 'Create'} failed`);

    alert(`${isEdit.value ? 'User updated' : 'User created'} successfully!`);
    resetForm();
    getUsers();
  } catch (err) {
    console.error(err);
    alert('Error. Check console.');
  }
};

// --- Delete user ---
const deleteUser = async (username) => {
  if (!confirm(`Are you sure you want to delete user: ${username}?`)) return;
  try {
    const res = await fetch(`${API_URL}/${username}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Delete failed');
    alert('User deleted successfully!');
    getUsers();
  } catch (err) {
    console.error(err);
    alert('Error deleting user. Check console.');
  }
};

// --- Edit user ---
const editUser = (u) => {
  const { password, ...userToEdit } = u;
  user.value = userToEdit;
  isEdit.value = true;
  const formElement = document.querySelector('.form-section');
  if (formElement) {
    formElement.scrollIntoView({ behavior: 'smooth' });
  }
};

// --- Load users khi component mounted ---
onMounted(() => {
  getUsers();
});
</script>

<style scoped>
/* THAY ĐỔI: Cập nhật CSS đồng bộ */
/* GENERAL LAYOUT */
.admin-container {
  padding: 24px;
  background-color: #f0f2f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
.card {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}
.form-section {
  margin-bottom: 24px;
}

/* TYPOGRAPHY & HEADER */
h2 {
  color: #333;
  margin: 0 0 24px 0;
  font-weight: 600;
}
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.table-header h3 {
  margin: 0;
  color: #333;
  font-weight: 600;
}
.search-input {
  width: 300px;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
.search-input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}


/* FORM STYLES */
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
input, select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
input:focus, select:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* BUTTON STYLES */
.btn-group {
  display: flex;
  gap: 12px;
}
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-primary {
  background-color: #1890ff;
  color: white;
}
.btn-primary:hover {
  background-color: #40a9ff;
}
.btn-secondary {
  background-color: #fff;
  color: #333;
  border: 1px solid #d9d9d9;
}
.btn-secondary:hover {
  border-color: #1890ff;
  color: #1890ff;
}

/* TABLE STYLES */
.table-wrapper {
  overflow-x: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}
th, td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
  vertical-align: middle;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
thead th {
  background-color: #fafafa;
  font-weight: 600;
  color: #000;
  text-transform: uppercase;
  font-size: 12px;
}
tbody tr:hover {
  background-color: #f5f5f5;
}
/* Column Widths */
.col-no { width: 5%; }
.col-username { width: 15%; }
.col-fullname { width: 20%; }
.col-email { width: 25%; }
.col-role { width: 10%; }
.col-status { width: 10%; text-align: center; }
.col-action { width: 15%; text-align: center; }

.text-center {
    text-align: center;
}
.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-weight: 600;
  font-size: 12px;
  color: white;
  text-transform: uppercase;
}
.status-active {
  background-color: #52c41a;
}
.status-inactive {
  background-color: #bfbfbf;
}
/* Icon Button Styles */
.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}
.btn-icon {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 6px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.2s;
}
.btn-icon:hover {
    background-color: #e9ecef;
}
.btn-icon svg {
    width: 20px;
    height: 20px;
    fill: #6c757d;
    transition: fill 0.2s;
}
.btn-icon:hover svg {
    fill: #1890ff;
}
.btn-icon.btn-danger-icon:hover svg {
    fill: #f5222d;
}
</style>