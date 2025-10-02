<template>
  <div class="register-container c-black">
    <h2 class="title">Đăng nhập</h2>
    <form class="register-form" @submit.prevent="login">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Email Hoặc Số Điện Thoại *</label>
        <input type="text" v-model="username" placeholder="Nhập email hoặc số điện thoại" required />
      </div>

      <!-- Mật khẩu -->
      <div class="form-group">
        <label>Mật khẩu *</label>
        <input type="password" v-model="password" placeholder="Mật khẩu" required />
      </div>
      <label class="t-bold">Quên thông tin tài khoản</label>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Đăng nhập</button>
      <router-link to="/signup" class="move-sign-up c-black">
          Bạn đã có tài khoản? Đăng ký ngay
      </router-link>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      username: "",
      password: "",
      errorMessage: ""
    };
  },
  methods: {
    async login() {
      try {
        const res = await axios.post("/api/login", {
          username: this.username,
          password: this.password
        });
        if (res.data.success) {
          alert("Login thành công!");
          // redirect tới trang home
          this.$router.push("/");
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
