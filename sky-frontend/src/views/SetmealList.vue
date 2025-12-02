<template>
  <div class="setmeal-container">
    <div class="header-actions">
      <el-input
        v-model="searchName"
        placeholder="请输入套餐名称"
        style="width: 200px; margin-right: 10px"
        clearable
        @clear="fetchSetmeals"
        @keyup.enter="fetchSetmeals"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-select v-model="searchCategoryId" placeholder="请选择分类" style="width: 150px; margin-right: 10px" clearable @change="fetchSetmeals">
        <el-option
          v-for="item in categoryList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-select v-model="searchStatus" placeholder="请选择状态" style="width: 150px; margin-right: 10px" clearable @change="fetchSetmeals">
        <el-option label="起售" :value="1" />
        <el-option label="停售" :value="0" />
      </el-select>
      <el-button type="primary" @click="fetchSetmeals">查询</el-button>
      <el-button type="success" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增套餐
      </el-button>
    </div>

    <el-table :data="setmealList" border stripe v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="name" label="套餐名称" align="center"></el-table-column>
      <el-table-column label="图片" align="center" width="120">
        <template #default="scope">
          <el-image
            style="width: 80px; height: 60px"
            :src="scope.row.image"
            :preview-src-list="[scope.row.image]"
            fit="cover"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="套餐分类" align="center"></el-table-column>
      <el-table-column prop="price" label="套餐价格" align="center">
        <template #default="scope">
          ￥{{ (scope.row.price / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="售卖状态" align="center">
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
      <el-table-column prop="updateTime" label="最后操作时间" align="center" width="180"></el-table-column>
      <el-table-column label="操作" align="center" width="250">
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
      width="700px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="套餐名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入套餐名称"></el-input>
        </el-form-item>
        <el-form-item label="套餐分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择套餐分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="套餐价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" placeholder="请输入价格"></el-input-number>
        </el-form-item>
        <el-form-item label="套餐图片">
          <el-upload
            class="avatar-uploader"
            action="/api/common/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :headers="{ token: token }"
          >
            <img v-if="form.image" :src="form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">建议上传图片尺寸为 300x200</div>
        </el-form-item>
        <el-form-item label="套餐描述" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入套餐描述"></el-input>
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
import { Search, Plus, Edit, Delete, Picture } from '@element-plus/icons-vue'
import axios from 'axios'

const loading = ref(false)
const setmealList = ref([])
const categoryList = ref([])
const searchName = ref('')
const searchCategoryId = ref('')
const searchStatus = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增套餐')
const formRef = ref(null)
const token = localStorage.getItem('token')

const form = reactive({
  id: null,
  name: '',
  categoryId: '',
  price: 0,
  image: '',
  description: '',
  status: 1,
  setmealDishes: []
})

const rules = {
  name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择套餐分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }]
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await axios.get('/api/admin/category/list', {
      params: { type: 2 }, // 套餐分类
      headers: { token }
    })
    if (res.data.code === 1) {
      categoryList.value = res.data.data
    }
  } catch (error) {
    console.error('获取分类列表失败', error)
  }
}

// 获取套餐列表
const fetchSetmeals = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/admin/setmeal/list', {
      params: {
        name: searchName.value,
        categoryId: searchCategoryId.value,
        status: searchStatus.value
      },
      headers: { token }
    })
    if (res.data.code === 1) {
      setmealList.value = res.data.data
    }
  } catch (error) {
    console.error('获取套餐列表失败', error)
    ElMessage.error('获取套餐列表失败')
  } finally {
    loading.value = false
  }
}

// 新增套餐
const handleAdd = () => {
  dialogTitle.value = '新增套餐'
  resetForm()
  dialogVisible.value = true
}

// 编辑套餐
const handleEdit = (row) => {
  dialogTitle.value = '修改套餐'
  Object.assign(form, row)
  form.price = row.price / 100 // 转回元
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const method = form.id ? 'put' : 'post'
        const url = '/api/admin/setmeal'
        
        // 价格转分
        const submitData = { ...form }
        submitData.price = Math.round(form.price * 100)
        
        const res = await axios({
          method,
          url,
          data: submitData,
          headers: { token }
        })
        
        if (res.data.code === 1) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          fetchSetmeals()
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

// 删除套餐
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该套餐吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await axios.delete(`/api/admin/setmeal/${row.id}`, {
        headers: { token }
      })
      if (res.data.code === 1) {
        ElMessage.success('删除成功')
        fetchSetmeals()
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
    const res = await axios.put(`/api/admin/setmeal/status/${row.status}`, null, {
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

// 图片上传相关
const handleAvatarSuccess = (response, uploadFile) => {
  // 假设后端返回 { code: 1, data: 'url' }
  if (response.code === 1) {
    form.image = response.data
  } else {
    ElMessage.error('上传失败')
  }
}

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('图片格式必须为 JPG 或 PNG!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.categoryId = ''
  form.price = 0
  form.image = ''
  form.description = ''
  form.status = 1
  form.setmealDishes = []
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(() => {
  fetchCategories()
  fetchSetmeals()
})
</script>

<style scoped>
.setmeal-container {
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
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
