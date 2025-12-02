<template>
  <div class="dish-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜品管理</span>
          <el-button type="primary" @click="handleAdd">新增菜品</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜品名称">
          <el-input v-model="searchForm.name" placeholder="请输入菜品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="菜品名称" />
        <el-table-column prop="categoryId" label="分类ID" width="100" />
        <el-table-column prop="price" label="价格(元)" width="120">
          <template #default="{ row }">
            ¥{{ (row.price / 100).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启售' : '停售' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜品名称" />
        </el-form-item>
        <el-form-item label="分类ID" prop="categoryId">
          <el-input-number v-model="form.categoryId" :min="1" />
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启售</el-radio>
            <el-radio :label="0">停售</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜品')
const formRef = ref()
const submitting = ref(false)

const searchForm = reactive({
  name: ''
})

const form = reactive({
  id: null,
  name: '',
  categoryId: 1,
  price: 0,
  status: 1,
  description: ''
})

const rules = {
  name: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请输入分类ID', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/dish/list', {
      headers: { 'token': token }
    })
    if (res.data.code === 1) {
      tableData.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    console.error('加载数据失败:', e)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  searchForm.name = ''
  loadData()
}

function handleAdd() {
  dialogTitle.value = '新增菜品'
  Object.assign(form, {
    id: null,
    name: '',
    categoryId: 1,
    price: 0,
    status: 1,
    description: ''
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑菜品'
  Object.assign(form, {
    ...row,
    price: row.price / 100 // 转为元
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  
  try {
    const token = localStorage.getItem('token')
    const data = {
      ...form,
      price: Math.round(form.price * 100) // 转为分
    }
    
    let res
    if (form.id) {
      res = await axios.put('/api/admin/dish', data, {
        headers: { 'token': token, 'Content-Type': 'application/json' }
      })
    } else {
      delete data.id
      res = await axios.post('/api/admin/dish', data, {
        headers: { 'token': token, 'Content-Type': 'application/json' }
      })
    }
    
    if (res.data.code === 1) {
      ElMessage.success(form.id ? '更新成功' : '新增成功')
      dialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    console.error('提交失败:', e)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确认删除该菜品吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const token = localStorage.getItem('token')
    const res = await axios.delete(`/api/admin/dish/${row.id}`, {
      headers: { 'token': token }
    })
    
    if (res.data.code === 1) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      console.error('删除失败:', e)
      ElMessage.error('删除失败')
    }
  }
}
</script>

<style scoped>
.dish-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
