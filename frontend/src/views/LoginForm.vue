<template>
  <div class="register-container c-black">
    <h2 class="title">Đăng nhập</h2>
    <form class="register-form">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Email Hoặc Số Điện Thoại *</label>
        <input type="text" placeholder="Nhập email hoặc số điện thoại" required />
      </div>

      <!-- Mật khẩu -->
      <div class="form-group">
        <label>Mật khẩu *</label>
        <input type="password" placeholder="Mật khẩu" required />
      </div>
      <label class="t-bold">Quên thông tin tài khoản</label>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Đăng ký</button>
      <router-link to="/signup" class="move-sign-up c-black">
          Bạn đã có tài khoản? Đăng ký ngay
      </router-link>
    </form>
  </div>
</template>

