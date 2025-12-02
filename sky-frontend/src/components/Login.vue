<template>
  <div class="login-page">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>苍穹外卖管理系统</h2>
        </div>
      </template>

      <!-- 后端连接测试 -->
      <el-alert
        v-if="testResult"
        :title="testResult"
        :type="testSuccess ? 'success' : 'error'"
        :closable="false"
        style="margin-bottom: 20px"
      />
      
      <el-button 
        type="warning" 
        @click="testBackend" 
        style="width: 100%; margin-bottom: 20px"
        :icon="Connection"
      >
        🔍 测试后端连接
      </el-button>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleLogin"
            :loading="loading"
            style="width: 100%"
            size="large"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Connection } from '@element-plus/icons-vue'
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

// 测试后端连接
async function testBackend() {
  testResult.value = '正在测试...'
  testSuccess.value = false
  
  try {
    const res = await axios.post('http://localhost:8080/admin/employee/login', 
      { username: 'admin', password: '123456' },
      { headers: { 'Content-Type': 'application/json' }, timeout: 3000 }
    )
    testResult.value = `✅ 后端正常运行! 状态码: ${res.status}`
    testSuccess.value = true
    ElMessage.success('后端连接正常')
  } catch (err) {
    if (err.code === 'ECONNABORTED') {
      testResult.value = '❌ 连接超时: 后端可能未启动'
    } else if (err.response) {
      testResult.value = `❌ HTTP ${err.response.status}: ${err.response.statusText}`
    } else {
      testResult.value = '❌ 网络错误: 无法连接到 localhost:8080'
    }
    ElMessage.error('后端连接失败')
  }
}

async function handleLogin() {
  await loginFormRef.value.validate()
  
  loading.value = true
  try {
    const res = await axios.post('/api/admin/employee/login', {
      username: loginForm.username,
      password: loginForm.password
    })
    
    if (res.data.code === 1 && res.data.data?.token) {
      const { token, name, username } = res.data.data
      localStorage.setItem('token', token)
      localStorage.setItem('userName', name || username)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.data.msg || '登录失败')
    }
  } catch (e) {
    console.error('登录异常:', e)
    if (e.response) {
      ElMessage.error(`HTTP ${e.response.status}: ${e.response.data?.msg || '登录失败'}`)
    } else {
      ElMessage.error('网络错误,请检查后端是否启动')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}
</style>
