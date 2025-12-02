<template>
  <div class="category-container">
    <div class="header-actions">
      <el-button type="primary" @click="handleAdd(1)">
        <el-icon><Plus /></el-icon>新增菜品分类
      </el-button>
      <el-button type="primary" @click="handleAdd(2)">
        <el-icon><Plus /></el-icon>新增套餐分类
      </el-button>
    </div>

    <el-table :data="categoryList" border stripe v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="name" label="分类名称" align="center"></el-table-column>
      <el-table-column prop="type" label="分类类型" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.type === 1 ? 'success' : 'warning'">
            {{ scope.row.type === 1 ? '菜品分类' : '套餐分类' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="操作时间" align="center" width="180"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>修改
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">
            <el-icon><Delete /></el-icon>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="400px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import axios from 'axios'

const loading = ref(false)
const categoryList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  type: 1,
  sort: 0
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
}

// 获取分类列表
const fetchCategories = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/category/list', {
      headers: { token }
    })
    if (res.data.code === 1) {
      categoryList.value = res.data.data
    }
  } catch (error) {
    console.error('获取分类列表失败', error)
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 新增分类
const handleAdd = (type) => {
  dialogTitle.value = type === 1 ? '新增菜品分类' : '新增套餐分类'
  resetForm()
  form.type = type
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = (row) => {
  dialogTitle.value = '修改分类'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const token = localStorage.getItem('token')
        const method = form.id ? 'put' : 'post'
        const url = '/api/admin/category'
        
        const res = await axios({
          method,
          url,
          data: form,
          headers: { token }
        })
        
        if (res.data.code === 1) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          fetchCategories()
        } else {
          ElMessage.error(res.data.msg || '操作失败')
        }
      } catch (error) {
        console.error('提交失败', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

// 删除分类
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const token = localStorage.getItem('token')
      const res = await axios.delete(`/api/admin/category/${row.id}`, {
        headers: { token }
      })
      if (res.data.code === 1) {
        ElMessage.success('删除成功')
        fetchCategories()
      } else {
        ElMessage.error(res.data.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 状态变更
const handleStatusChange = async (row) => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.put(`/api/admin/category/status/${row.status}`, null, {
      params: { id: row.id },
      headers: { token }
    })
    if (res.data.code === 1) {
      ElMessage.success('状态已更新')
    } else {
      row.status = row.status === 1 ? 0 : 1
      ElMessage.error(res.data.msg || '状态更新失败')
    }
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.sort = 0
  // type is set in handleAdd
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.header-actions {
  margin-bottom: 20px;
}
</style>
