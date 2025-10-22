// src/composables/useCheckoutData.js
import { ref, computed, onMounted } from 'vue'
import emitter from '@/utils/emitter.js' // Import emitter

export function useCheckoutData() {

    // --- 1. TẤT CẢ STATE CỦA TRANG ---
    const products = ref([])
    const customer = ref({
        firstName: '', lastName: '', email: '', phone: '',
        company: '', address: '', apartment: '',
        city: '',
    })
    const payment = ref({
        method: 'cod', cardName: '', cardNumber: '', expiry: '', cvv: ''
    })
    const voucherCode = ref('')
    const voucherMessage = ref('')
    const discount = ref(0)
    const cartCode = ref(null)

    // --- 2. STATE TÍNH TOÁN ---
    const subtotal = computed(() =>
        products.value.reduce((s, it) => s + it.price * (it.quantity || 1), 0)
    )
    const total = computed(() => subtotal.value - discount.value)

    // --- 3. HÀM TẢI DỮ LIỆU CHÍNH (Fill Checkout) ---
    const fetchCheckoutData = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                voucherMessage.value = 'Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.';
                return;
            }
            const response = await fetch('/api/orders/check-out', {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                voucherMessage.value = 'Không thể tải thông tin giỏ hàng.';
                return;
            }
            const data = await response.json();

            // Điền thông tin
            customer.value.firstName = data.firstName || '';
            customer.value.email = data.email || '';
            customer.value.phone = data.phone || '';
            customer.value.address = data.address || '';
            customer.value.city = data.city || "";
            customer.value.ward = data.ward || "";
            cartCode.value = data.cartCode || null;

            products.value = data.items.map(item => ({
                name: item.productName || 'Product Name',
                price: Number(item.price) || 0,
                quantity: Number(item.quantity) || 1,
                imageUrl: item.imageUrl || '/images/placeholder.png',
                size: item.size || '',
                variantID: item.variantID
            }));

        } catch (error) {
            voucherMessage.value = 'Lỗi kết nối máy chủ.';
            console.error('Fetch Error:', error);
        }
    }

    // --- 4. HÀM CẬP NHẬT SỐ LƯỢNG  ---
    async function updateCartItemQuantity(index, newQuantity) {
        const item = products.value[index];
        if (!item || !item.variantID) {
            console.error("Không tìm thấy variantID của sản phẩm");
            return;
        }

        if (newQuantity < 1) {
            console.warn("Số lượng không thể nhỏ hơn 1.");
            return;
        }

        const variantId = item.variantID;
        const url = `/api/cart-items/update?variantId=${variantId}&quantity=${newQuantity}`;
        const token = localStorage.getItem('token');

        if (!token) {
            voucherMessage.value = 'Hết phiên đăng nhập, không thể cập nhật giỏ hàng.';
            return;
        }

        try {
            const response = await fetch(url, {
                method: 'PUT',
                credentials: 'include',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                // Cập nhật UI
                products.value[index].quantity = newQuantity;
                // Phát tín hiệu cho Navbar
                emitter.emit('cart-updated');
                console.log(`Đã cập nhật số lượng cho variant ${variantId} thành ${newQuantity}`);
            } else {
                console.error("Lỗi khi cập nhật số lượng:", response.statusText);
                voucherMessage.value = 'Lỗi: Không thể cập nhật giỏ hàng.';
            }
        } catch (error) {
            console.error("Lỗi nghiêm trọng khi cập nhật:", error);
            voucherMessage.value = 'Lỗi: Mất kết nối khi cập nhật giỏ hàng.';
        }
    }

    function increaseQty(i) {
        const newQuantity = (products.value[i].quantity || 1) + 1;
        updateCartItemQuantity(i, newQuantity);
    }

    function decreaseQty(i) {
        const currentQuantity = products.value[i].quantity || 1;
        if (currentQuantity <= 1) return;
        const newQuantity = currentQuantity - 1;
        updateCartItemQuantity(i, newQuantity);
    }

    function applyVoucher() {
        const code = voucherCode.value.trim().toUpperCase()
        if (code === 'SALE10') {
            discount.value = Math.round(subtotal.value * 0.1)
            voucherMessage.value = '🎉 SALE10 applied: 10% off!'
        } else if (code === 'FREESHIP') {
            discount.value = 20000
            voucherMessage.value = 'FREESHIP applied: 20,000₫ off!'
        } else {
            discount.value = 0
            voucherMessage.value = 'Invalid voucher code'
        }
    }

    // --- 5. LIFECYCLE ---
    // onMounted(() => {
    //     fetchCheckoutData();
    // });

    // --- 6. TRẢ VỀ ---
    return {
        products,
        customer,
        payment,
        voucherCode,
        voucherMessage,
        discount,
        cartCode,
        subtotal,
        total,
        applyVoucher,
        increaseQty,
        decreaseQty,
        fetchCheckoutData
    }
}