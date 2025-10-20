<template>
  <div class="register-container c-black">
    <h2 class="title">Sign Up</h2>
    <form class="register-form" @submit.prevent="logup">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Name *</label>
        <input type="text" v-model="fullName" placeholder="Enter your name" required />
      </div>

      <!-- Số điện thoại -->
      <div class="form-group">
        <label>Phone Number *</label>
        <input type="text" v-model="phone" placeholder="Enter your phone number" required />
      </div>

      <!-- Email -->
      <div class="form-group">
        <label>Email *</label>
        <input type="email" v-model="email" placeholder="This field is required" required />
      </div>

      <!-- Mật khẩu -->
      <div class="form-group">
        <label>Password *</label>
        <input type="password" v-model="password" placeholder="Enter your phone password" required />
      </div>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Sign Up</button>
      <router-link to="/login" class="move-login c-black">
          Already have an account? Log in now
      </router-link>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      fullName: "",
      phone: "",
      email: "",
      password: "",
      errorMessage: ""
    };
  },
  methods: {
    async logup() {
      try {
        const res = await axios.post("/api/signup", {
          fullName: this.fullName,
          phone: this.phone,
          email: this.email,
          password: this.password
        });
        if (res.data.success) {
          alert("Sign up thành công!");
          // redirect tới trang home
          this.$router.push("/login");
        } else {
          this.errorMessage = res.data.message;
        }
      } catch (err) {
        this.errorMessage = "Lỗi server, thử lại sau";
      }
    }
  }
};
</script>

