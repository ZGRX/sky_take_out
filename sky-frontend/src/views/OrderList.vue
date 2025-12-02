<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary" @click="loadData">刷新</el-button>
        </div>
      </template>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="订单ID" width="80" />
        <el-table-column prop="number" label="订单号" width="180" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额(元)" width="120">
          <template #default="{ row }">
            ¥{{ (row.amount / 100).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderTime" label="下单时间" width="180" />
        <el-table-column prop="address" label="收货地址" show-overflow-tooltip />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 2" 
              type="success" 
              size="small" 
              @click="handleConfirm(row)"
            >
              接单
            </el-button>
            <el-button 
              v-if="row.status === 2" 
              type="danger" 
              size="small" 
              @click="handleReject(row)"
            >
              拒单
            </el-button>
            <el-button 
              v-if="row.status === 3" 
              type="primary" 
              size="small" 
              @click="handleComplete(row)"
            >
              完成
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const tableData = ref([])

const statusMap = {
  1: '待付款',
  2: '待接单',
  3: '已接单',
  4: '派送中',
  5: '已完成',
  6: '已取消'
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/order/list', {
      headers: { 'token': token }
    })
    if (res.data.code === 1) {
      tableData.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    console.error('加载订单失败:', e)
    ElMessage.error('加载订单失败')
  } finally {
    loading.value = false
  }
}

function getStatusText(status) {
  return statusMap[status] || '未知'
}

function getStatusType(status) {
  const typeMap = {
    1: 'warning',
    2: 'info',
    3: 'primary',
    4: 'primary',
    5: 'success',
    6: 'danger'
  }
  return typeMap[status] || 'info'
}

async function handleConfirm(row) {
  try {
    await ElMessageBox.confirm('确认接单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    
    const token = localStorage.getItem('token')
    const res = await axios.put(`/api/admin/order/confirm/${row.id}`, {}, {
      headers: { 'token': token }
    })
    
    if (res.data.code === 1) {
      ElMessage.success('接单成功')
      loadData()
    } else {
      ElMessage.error(res.data.msg || '接单失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      console.error('接单失败:', e)
      ElMessage.error('接单失败')
    }
  }
}

async function handleReject(row) {
  try {
    await ElMessageBox.confirm('确认拒单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const token = localStorage.getItem('token')
    const res = await axios.put(`/api/admin/order/rejection/${row.id}`, {}, {
      headers: { 'token': token }
    })
    
    if (res.data.code === 1) {
      ElMessage.success('拒单成功')
      loadData()
    } else {
      ElMessage.error(res.data.msg || '拒单失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      console.error('拒单失败:', e)
      ElMessage.error('拒单失败')
    }
  }
}

async function handleComplete(row) {
  try {
    await ElMessageBox.confirm('确认完成订单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    
    const token = localStorage.getItem('token')
    const res = await axios.put(`/api/admin/order/complete/${row.id}`, {}, {
      headers: { 'token': token }
    })
    
    if (res.data.code === 1) {
      ElMessage.success('订单已完成')
      loadData()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      console.error('完成订单失败:', e)
      ElMessage.error('操作失败')
    }
  }
}
</script>

<style scoped>
.order-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
