import { ref } from 'vue'

export function useChangePassword() {
    const currentPassword = ref('')
    const newPassword = ref('')
    const confirmPassword = ref('')

    const loading = ref(false)
    const error = ref(null)
    const message = ref(null)

    async function changePassword() {
        loading.value = true
        error.value = null
        message.value = null

        try {
            // Validation
            if (!currentPassword.value) {
                throw new Error('Current password is required')
            }
            if (!newPassword.value) {
                throw new Error('New password is required')
            }
            if (!confirmPassword.value) {
                throw new Error('Confirm password is required')
            }
            if (newPassword.value !== confirmPassword.value) {
                throw new Error('New password and confirm password do not match')
            }
            if (newPassword.value.length < 6) {
                throw new Error('New password must be at least 6 characters')
            }

            const token = localStorage.getItem('token')
            if (!token) {
                throw new Error('Not authenticated')
            }

            const response = await fetch('/api/profile/change-password', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    oldPassword: currentPassword.value,
                    newPassword: newPassword.value,
                    confirmPassword: confirmPassword.value
                })
            })

            if (!response.ok) {
                throw new Error(`Failed to change password: ${response.status}`)
            }

            const data = await response.json()
            if (!data.success) {
                throw new Error(data.message || 'Failed to change password')
            }

            message.value = 'Password changed successfully!'

            // Clear form
            currentPassword.value = ''
            newPassword.value = ''
            confirmPassword.value = ''

            // Clear success message sau 3 giây
            setTimeout(() => {
                message.value = null
            }, 3000)
        } catch (err) {
            error.value = err.message
            console.error('Error changing password:', err)
        } finally {
            loading.value = false
        }
    }

    return {
        currentPassword,
        newPassword,
        confirmPassword,
        loading,
        error,
        message,
        changePassword
    }
}
