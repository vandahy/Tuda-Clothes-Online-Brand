import { ref } from 'vue'
import axios from 'axios'

// --- Cấu hình API Client ---
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // << Đảm bảo URL này đúng
    headers: {
        'Content-Type': 'application/json'
    }
});

// *** ĐÂY LÀ PHẦN THÊM TOKEN ***
// Sử dụng interceptor để thêm token vào MỖI request
apiClient.interceptors.request.use(
    (config) => {
        // Lấy token từ localStorage
        const token = localStorage.getItem('token'); // <-- Giả sử key là 'token'

        // Nếu có token, gắn nó vào header Authorization
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // Xử lý lỗi request
        return Promise.reject(error);
    }
);


/**
 * Đây là composable để quản lý logic lịch sử đơn hàng
 */
export function useOrderHistory() {

    // State: Danh sách đơn hàng (cho trang lịch sử)
    const orders = ref([])

    // State: Loading (cho trang lịch sử)
    const isLoading = ref(false)

    // State: Giữ chi tiết đơn hàng đang xem (dữ liệu cho modal)
    const viewingOrder = ref(null)

    /**
     * (1) HÀM GỌI API CHI TIẾT ĐƠN HÀNG
     */
    async function viewOrderDetails(orderCode) {
        if (!orderCode) return
        console.log(`Đang tải chi tiết cho đơn hàng: ${orderCode}`)

        try {
            // Request này BÂY GIỜ SẼ TỰ ĐỘNG CÓ TOKEN
            const response = await apiClient.get(`/orders/${orderCode}`)

            const data = response.data
            console.log('Order Detail API Response:', data)

            viewingOrder.value = {
                orderId: data.orderCode,
                orderDate: data.orderDate,
                orderStatus: data.orderStatus,
                products: data.items || [],
                staffName: 'Admin',
                staffEmail: data.paymentMethod,
                recipientName: data.customerName,
                recipientPhone: data.customerPhone,
                recipientAddress: data.customerAddress,
                paymentMethod: data.paymentMethod,
                paymentStatus: data.paymentStatus,
                shippingFee: data.shippingFee || 0,
                discountAmount: data.discountAmount || 0,
                subtotal: data.subtotal || 0,
                total: data.totalAmount || 0
            }

            console.log('Đã tải xong chi tiết:', viewingOrder.value)

        } catch (error) {
            console.error(`Lỗi khi tải chi tiết đơn hàng ${orderCode}:`, error)
            // Xử lý lỗi 401/403 (Token hết hạn/không hợp lệ) nếu cần
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
                // Chuyển hướng về trang đăng nhập
                // window.location.href = '/login'; 
            } else {
                alert('Không thể tải chi tiết đơn hàng. Vui lòng thử lại.')
            }
            viewingOrder.value = null
        }
    }

    /**
     * (2) HÀM ĐÓNG MODAL
     */
    function closeModal() {
        viewingOrder.value = null
    }

    /**
     * (3) HÀM LẤY LỊCH SỬ ĐƠN HÀNG
     */
    async function fetchOrderHistory() {
        isLoading.value = true
        try {
            // Request này CŨNG SẼ TỰ ĐỘNG CÓ TOKEN
            const response = await apiClient.get('/orders/list')
            orders.value = response.data
        } catch (error) {
            console.error('Lỗi khi tải lịch sử đơn hàng:', error)
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
                // Chuyển hướng về trang đăng nhập
                // window.location.href = '/login';
            }
            orders.value = []
        } finally {
            isLoading.value = false
        }
    }

    // Trả về các state và hàm
    return {
        orders,
        isLoading,
        viewingOrder,
        fetchOrderHistory,
        viewOrderDetails,
        closeModal
    }
}