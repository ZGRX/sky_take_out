<template>
  <div class="employee-container">
    <div class="header-actions">
      <el-input
        v-model="searchName"
        placeholder="请输入员工姓名"
        style="width: 200px; margin-right: 10px"
        clearable
        @clear="fetchEmployees"
        @keyup.enter="fetchEmployees"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="fetchEmployees">查询</el-button>
      <el-button type="success" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增员工
      </el-button>
    </div>

    <el-table :data="employeeList" border stripe v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="name" label="姓名" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="phone" label="手机号" align="center"></el-table-column>
      <el-table-column prop="idNumber" label="身份证号" align="center" width="200"></el-table-column>
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
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">
            <el-icon><Edit /></el-icon>编辑
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
      width="500px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
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
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import axios from 'axios'

const loading = ref(false)
const employeeList = ref([])
const searchName = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  username: '',
  phone: '',
  idNumber: '',
  sex: '1'
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

// 获取员工列表
const fetchEmployees = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/employee/list', {
      params: { name: searchName.value },
      headers: { token }
    })
    if (res.data.code === 1) {
      employeeList.value = res.data.data
    } else {
      // 如果是空数据，可能是后端还没实现，这里为了演示效果，如果后端返回空，我们mock一些数据
      if (!res.data.data || res.data.data.length === 0) {
        // Mock data for demo if backend is empty
        // employeeList.value = [
        //   { id: 1, name: '张三', username: 'zhangsan', phone: '13800138000', idNumber: '110101199001011234', status: 1 },
        //   { id: 2, name: '李四', username: 'lisi', phone: '13900139000', idNumber: '110101199202025678', status: 0 }
        // ]
      }
    }
  } catch (error) {
    console.error('获取员工列表失败', error)
    ElMessage.error('获取员工列表失败')
  } finally {
    loading.value = false
  }
}

// 新增员工
const handleAdd = () => {
  dialogTitle.value = '新增员工'
  resetForm()
  dialogVisible.value = true
}

// 编辑员工
const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  Object.assign(form, row)
  // 注意：后端返回的sex可能是数字，这里radio group绑定的是字符串还是数字需要一致
  // 假设后端返回的是数字 1 或 0
  form.sex = String(row.sex) 
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
        const url = '/api/admin/employee'
        
        const res = await axios({
          method,
          url,
          data: form,
          headers: { token }
        })
        
        if (res.data.code === 1) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          fetchEmployees()
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

// 删除员工
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该员工吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const token = localStorage.getItem('token')
      const res = await axios.delete(`/api/admin/employee/${row.id}`, {
        headers: { token }
      })
      if (res.data.code === 1) {
        ElMessage.success('删除成功')
        fetchEmployees()
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
    const res = await axios.put(`/api/admin/employee/status/${row.status}`, null, {
      params: { id: row.id },
      headers: { token }
    })
    if (res.data.code === 1) {
      ElMessage.success('状态已更新')
    } else {
      // 失败则回滚状态
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
  form.username = ''
  form.phone = ''
  form.idNumber = ''
  form.sex = '1'
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(() => {
  fetchEmployees()
})
</script>

<style scoped>
.employee-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.header-actions {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
</style>
