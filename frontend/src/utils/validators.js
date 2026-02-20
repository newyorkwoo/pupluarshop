export const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
export const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,}$/
export const PHONE_REGEX = /^09\d{8}$/

export function validateEmail(email) {
  if (!email) return '請輸入 Email'
  if (!EMAIL_REGEX.test(email)) return 'Email 格式不正確'
  return ''
}

export function validatePassword(password) {
  if (!password) return '請輸入密碼'
  if (password.length < 8) return '密碼至少 8 個字元'
  if (!PASSWORD_REGEX.test(password)) {
    return '密碼需包含大小寫字母、數字及特殊字元'
  }
  return ''
}

export function validateRequired(value, fieldName = '此欄位') {
  if (!value || (typeof value === 'string' && !value.trim())) {
    return `${fieldName}為必填`
  }
  return ''
}

export function isValidEmail(email) {
  return EMAIL_REGEX.test(email)
}

export function isValidPassword(password) {
  return password && password.length >= 8 && /[a-z]/.test(password) && /[A-Z]/.test(password) && /\d/.test(password)
}

export function validatePhone(phone) {
  if (!phone) return '請輸入手機號碼'
  if (!PHONE_REGEX.test(phone)) return '手機號碼格式不正確'
  return ''
}
