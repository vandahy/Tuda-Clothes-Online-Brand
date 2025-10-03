<template>
  <div class="container">
    <h1>My Account</h1>

    <div class="tabs">
      <button type="button" :class="['tab', activeTab === 'info' ? 'active' : '']" @click="activeTab = 'info'">Account Information</button>
      <button type="button" :class="['tab', activeTab === 'security' ? 'active' : '']" @click="activeTab = 'security'">Security</button>
    </div>

    <!-- Tab: Account Info -->
    <div class="card" v-show="activeTab === 'info'">
      <form class="form info-form" @submit.prevent="submitForm">

        <div class="form-group full-width">
          <label>Name</label>
          <input type="text"  placeholder="Your name"/>
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email"   placeholder="Your email"/>
        </div>

        <div class="form-group">
          <label>Phone Number</label>
          <input type="text"  placeholder="Your number" />
        </div>

        <div class="form-group full-width">
          <label>Address</label>
          <input type="text"  placeholder="Your address" />
        </div>

        <button type="submit" class="update-button">Update</button>
      </form>
    </div>

    <!-- Tab: Security -->
    <div class="card" v-show="activeTab === 'security'">
      <form class="form security-form" @submit.prevent="changePassword">
        <div class="form-group">
          <label>Current Passwordi</label>
          <input type="password"  placeholder="Current Password" />
        </div>

        <div class="form-group">
          <label>New Password</label>
          <input type="password"  placeholder="New Password"/>
        </div>

        <div class="form-group full-width">
          <label>Confirm New Password</label>
          <input type="password" placeholder="Confirm New Password"/>
        </div>

        <button type="submit" class="update-button">Change Password</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('info')

const form = ref({
  org: 'Thời Trang Nitimo',
  fullName: 'Nguyễn Trung Nghĩa',
  email: 'nghianitimo@gmail.com',
  phone: '',
  address: '',
})

const passwordForm = ref({
  current: '',
  newPassword: '',
  confirmPassword: '',
})


</script>

<style scoped>
.container {
  width: 800px;
  margin: 120px auto;
  background-color: #fff;
  padding: 32px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h1 {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
}

.tabs {
  display: flex;
  justify-content: flex-start; 
  border-bottom: 2px solid #ddd;
  margin-bottom: 40px; 
  position: relative;
  gap: 16px;
}

.tab {
  padding: 10px 24px;
  background: none;
  border: none;
  cursor: pointer;
  font-weight: 600;
  color: #555;
  font-size: 16px;
  position: relative;
}

.tab.active {
  color: orange;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 3px;
  background-color: orange;
}

.card {
  min-height: 360px; 
  display: flex;
  flex-direction: column;
  justify-content: flex-start; 
  gap: 16px;
}

.form {
  display: grid;
  grid-template-columns: 1fr; 
  gap: 24px;
}

.info-form {
  grid-template-columns: 1fr 1fr;
}

.security-form {
  display: grid;
  grid-template-columns: 1fr 1fr; 
  gap: 24px;
}
.security-form .full-width {
  grid-column: span 2;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 6px;
  font-weight: 500;
  font-size: 15px;
}

input, select {
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 16px;
  width: 100%;
  box-sizing: border-box;
}
.full-width {
  grid-column: 1;
}

.info-form .full-width {
  grid-column: span 2;
}

.update-button {
  grid-column: span 2;
  padding: 12px 20px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

.update-button:hover {
  background-color: black;
}
</style>

