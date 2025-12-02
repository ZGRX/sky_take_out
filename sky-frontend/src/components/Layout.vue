<template>
  <div class="layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <div class="logo">
          <h2>苍穹外卖</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dish">
            <el-icon><Dish /></el-icon>
            <span>菜品管理</span>
          </el-menu-item>
          <el-menu-item index="/order">
            <el-icon><Document /></el-icon>
            <span>订单管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header>
          <div class="header-right">
            <span class="username">{{ userName }}</span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>

        <!-- 内容区 -->
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Dish, Document } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userName = ref(localStorage.getItem('userName') || '管理员')

const activeMenu = computed(() => route.path)

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('userName')
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100vh;
  width: 100%;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  background-color: #2b3948;
}

.logo h2 {
  margin: 0;
  font-size: 20px;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.username {
  font-size: 14px;
  color: #606266;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
