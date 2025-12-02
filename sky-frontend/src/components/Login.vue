<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo-circle">
          <el-icon size="40" color="#fff"><Food /></el-icon>
        </div>
        <h2>苍穹外卖</h2>
        <p class="subtitle">管理系统</p>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            prefix-icon="User"
            size="large"
            class="custom-input"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            class="custom-input"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleLogin"
            :loading="loading"
            class="login-btn"
            size="large"
            round
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部调试工具 (鼠标悬停显示) -->
      <div class="debug-tools">
        <el-button 
          link 
          type="info" 
          size="small" 
          @click="testBackend" 
          :icon="Connection"
        >
          测试后端连接
        </el-button>
        <div v-if="testResult" :class="['test-result', testSuccess ? 'success' : 'error']">
          {{ testResult }}
        </div>
      </div>
    </div>
    
    <!-- 版权信息 -->
    <div class="copyright">
      Copyright © 2025 苍穹外卖. All Rights Reserved.
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Connection, Food, User, Lock } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const loginFormRef = ref()
const loginForm = reactive({
  username: 'admin',
  password: '123456'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loading = ref(false)
const testResult = ref('')
const testSuccess = ref(false)

// 登录逻辑
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 使用代理路径 /api
        const res = await axios.post('/api/admin/employee/login', {
          username: loginForm.username,
          password: loginForm.password
        })
        
        if (res.data.code === 1) {
          ElMessage.success('登录成功')
          localStorage.setItem('token', res.data.data.token)
          localStorage.setItem('userName', res.data.data.name)
          localStorage.setItem('userInfo', JSON.stringify(res.data.data))
          router.push('/')
        } else {
          ElMessage.error(res.data.msg || '登录失败')
        }
      } catch (error) {
        console.error('登录错误:', error)
        ElMessage.error('登录请求失败，请检查后端服务')
      } finally {
        loading.value = false
      }
    }
  })
}

// 测试后端连接
async function testBackend() {
  testResult.value = '正在连接...'
  testSuccess.value = false
  
  try {
    // 直接请求后端端口测试连通性，或者走代理
    const res = await axios.post('/api/admin/employee/login', 
      { username: 'admin', password: '123456' },
      { timeout: 3000 }
    )
    testResult.value = `✅ 连接成功 (Code: ${res.data.code})`
    testSuccess.value = true
  } catch (error) {
    testResult.value = `❌ 连接失败: ${error.message}`
    testSuccess.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100%;
  background-image: url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80');
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

/* 黑色遮罩层，让背景暗一点，突出登录框 */
.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(3px);
}

.login-box {
  position: relative;
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  transform: translateY(0);
  animation: floatUp 0.8s ease-out;
  text-align: center;
}

@keyframes floatUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  margin-bottom: 30px;
}

.logo-circle {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #409EFF, #36cfc9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.4);
}

.login-header h2 {
  margin: 0;
  font-size: 28px;
  color: #303133;
  font-weight: 600;
  letter-spacing: 2px;
}

.subtitle {
  margin: 5px 0 0;
  color: #909399;
  font-size: 14px;
  letter-spacing: 4px;
  text-transform: uppercase;
}

.login-form {
  margin-top: 30px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 25px;
  padding-left: 20px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  background-color: #f5f7fa;
  transition: all 0.3s;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409EFF inset;
  background-color: #fff;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  letter-spacing: 4px;
  background: linear-gradient(90deg, #409EFF, #36cfc9);
  border: none;
  margin-top: 10px;
  transition: transform 0.2s;
}

.login-btn:hover {
  transform: scale(1.02);
  background: linear-gradient(90deg, #66b1ff, #5cdbd3);
}

.login-btn:active {
  transform: scale(0.98);
}

.debug-tools {
  margin-top: 20px;
  font-size: 12px;
}

.test-result {
  margin-top: 5px;
  font-size: 12px;
}
.test-result.success { color: #67C23A; }
.test-result.error { color: #F56C6C; }

.copyright {
  position: absolute;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}
</style>
