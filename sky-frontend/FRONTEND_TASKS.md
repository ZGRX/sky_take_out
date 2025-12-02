# 苍穹外卖前端开发任务（Gemini 3）

## 🎯 任务概述

需要完成管理端的剩余功能模块，包括员工管理、分类管理、套餐管理和数据统计。

## 📋 已完成功能

- ✅ 登录页面（Login.vue）
- ✅ 管理后台布局（Layout.vue）
- ✅ 菜品管理（DishList.vue）
- ✅ 订单管理（OrderList.vue）
- ✅ 路由配置（router/index.js）

## 🚀 待开发功能

### 1. 员工管理模块（EmployeeList.vue）

**后端 API**：
```javascript
// 查询员工列表
GET /admin/employee/list
Headers: { 'token': localStorage.getItem('token') }
Response: { code: 1, data: [{ id, name, username, phone, sex, idNumber, status }] }

// 新增员工
POST /admin/employee
Body: { name, username, phone, sex, idNumber }

// 编辑员工
PUT /admin/employee
Body: { id, name, username, phone, sex, idNumber }

// 删除员工
DELETE /admin/employee/{id}

// 启用/禁用员工账号
PUT /admin/employee/status/{status}
Body: { id }
```

**功能要求**：
1. 表格展示员工列表（姓名、用户名、手机号、性别、身份证号、状态）
2. 新增员工对话框（表单验证：手机号11位、身份证18位）
3. 编辑员工功能
4. 删除员工（二次确认）
5. 启用/禁用开关（Switch 组件）
6. 搜索功能（按姓名或用户名）

**UI 参考**：
- 表格使用 `el-table`
- 对话框使用 `el-dialog`
- 表单使用 `el-form`
- 状态使用 `el-switch`

---

### 2. 分类管理模块（CategoryList.vue）

**后端 API**：
```javascript
// 查询分类列表
GET /admin/category/list
Response: { code: 1, data: [{ id, name, type, sort, status }] }

// 新增分类
POST /admin/category
Body: { name, type, sort }  // type: 1菜品分类 2套餐分类

// 编辑分类
PUT /admin/category
Body: { id, name, type, sort }

// 删除分类
DELETE /admin/category/{id}

// 启用/停用分类
PUT /admin/category/status/{status}
Body: { id }
```

**功能要求**：
1. 表格展示分类列表（分类名称、类型、排序、状态）
2. 类型显示（Tag 标签：菜品分类/套餐分类）
3. 新增分类对话框（选择类型、输入名称、设置排序）
4. 编辑分类功能
5. 删除分类（二次确认）
6. 启用/停用开关
7. 排序功能（支持拖拽或输入数字）

**UI 参考**：
- 类型使用 `el-tag` (type="success" / type="primary")
- 排序使用 `el-input-number`

---

### 3. 套餐管理模块（SetmealList.vue）

**后端 API**：
```javascript
// 查询套餐列表
GET /admin/setmeal/list
Response: { code: 1, data: [{ id, name, categoryId, price, status, description }] }

// 新增套餐
POST /admin/setmeal
Body: { name, categoryId, price, status, description, image, setmealDishes }
// setmealDishes: [{ dishId, name, price, copies }]

// 编辑套餐
PUT /admin/setmeal
Body: { id, name, categoryId, price, status, description }

// 删除套餐
DELETE /admin/setmeal/{id}

// 起售/停售套餐
PUT /admin/setmeal/status/{status}
Body: { id }

// 查询分类列表（用于下拉选择）
GET /admin/category/list?type=2  // type=2 表示套餐分类

// 查询菜品列表（用于选择套餐包含的菜品）
GET /admin/dish/list
```

**功能要求**：
1. 表格展示套餐列表（套餐名称、分类、价格、状态、描述）
2. 新增套餐对话框：
   - 基本信息（名称、分类下拉、价格、描述）
   - 选择菜品（多选表格，设置每个菜品的份数）
   - 图片上传（暂时留空或使用占位图）
3. 编辑套餐功能
4. 删除套餐（二次确认）
5. 起售/停售开关
6. 价格自动转换（元/分）

**UI 参考**：
- 分类选择使用 `el-select`
- 菜品选择使用 `el-table` + `selection` 类型
- 图片上传使用 `el-upload`（暂时可用占位图）

---

### 4. 数据统计模块（StatisticsDashboard.vue）

**后端 API**：
```javascript
// 营业额统计（需要后端实现，暂时可以模拟数据）
GET /admin/statistics/turnover?begin=2025-01-01&end=2025-01-31
Response: { code: 1, data: { dateList: [], amountList: [] } }

// 订单统计
GET /admin/statistics/orders?begin=2025-01-01&end=2025-01-31
Response: { code: 1, data: { dateList: [], orderCountList: [] } }

// 销量排行 Top10
GET /admin/statistics/top10?begin=2025-01-01&end=2025-01-31
Response: { code: 1, data: { nameList: [], numberList: [] } }
```

**功能要求**：
1. 日期范围选择器（DatePicker）
2. 数据卡片展示（总营业额、总订单数、今日订单）
3. 营业额趋势图（折线图 - ECharts）
4. 订单量趋势图（柱状图 - ECharts）
5. 销量排行榜（条形图 - ECharts）
6. 数据导出功能（Excel）

**技术要求**：
- 安装 ECharts: `npm install echarts`
- 图表使用 `vue-echarts` 或直接使用 ECharts
- Excel 导出使用 `xlsx` 库: `npm install xlsx`

**UI 参考**：
```vue
<el-date-picker
  v-model="dateRange"
  type="daterange"
  range-separator="至"
  start-placeholder="开始日期"
  end-placeholder="结束日期"
/>
```

---

## 📂 文件结构

创建以下文件：

```
sky-frontend/src/
├── views/
│   ├── EmployeeList.vue      # 员工管理
│   ├── CategoryList.vue      # 分类管理
│   ├── SetmealList.vue       # 套餐管理
│   └── StatisticsDashboard.vue # 数据统计
└── router/index.js           # 添加新路由
```

---

## 🔧 路由配置

在 `router/index.js` 中添加：

```javascript
import EmployeeList from '../views/EmployeeList.vue'
import CategoryList from '../views/CategoryList.vue'
import SetmealList from '../views/SetmealList.vue'
import StatisticsDashboard from '../views/StatisticsDashboard.vue'

// 在 Layout 的 children 中添加：
{
  path: 'employee',
  name: 'Employee',
  component: EmployeeList
},
{
  path: 'category',
  name: 'Category',
  component: CategoryList
},
{
  path: 'setmeal',
  name: 'Setmeal',
  component: SetmealList
},
{
  path: 'statistics',
  name: 'Statistics',
  component: StatisticsDashboard
}
```

---

## 🎨 侧边栏菜单更新

在 `Layout.vue` 中添加菜单项：

```vue
<el-menu-item index="/employee">
  <el-icon><User /></el-icon>
  <span>员工管理</span>
</el-menu-item>

<el-menu-item index="/category">
  <el-icon><Menu /></el-icon>
  <span>分类管理</span>
</el-menu-item>

<el-menu-item index="/dish">
  <el-icon><Dish /></el-icon>
  <span>菜品管理</span>
</el-menu-item>

<el-menu-item index="/setmeal">
  <el-icon><Box /></el-icon>
  <span>套餐管理</span>
</el-menu-item>

<el-menu-item index="/order">
  <el-icon><Document /></el-icon>
  <span>订单管理</span>
</el-menu-item>

<el-menu-item index="/statistics">
  <el-icon><DataLine /></el-icon>
  <span>数据统计</span>
</el-menu-item>
```

记得导入新的图标：
```javascript
import { User, Menu, Dish, Box, Document, DataLine } from '@element-plus/icons-vue'
```

---

## 📝 开发规范

### 1. API 请求格式
所有请求都需要携带 token：
```javascript
const token = localStorage.getItem('token')
axios.get('/api/admin/xxx', {
  headers: { 'token': token }
})
```

### 2. 价格处理
- 后端存储单位：分（整数）
- 前端显示单位：元（保留2位小数）
- 转换公式：
  ```javascript
  // 元 → 分
  const cents = Math.round(yuan * 100)
  
  // 分 → 元
  const yuan = (cents / 100).toFixed(2)
  ```

### 3. 表单验证
使用 Element Plus 的表单验证：
```javascript
const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}
```

### 4. 错误处理
统一使用 ElMessage 提示：
```javascript
ElMessage.success('操作成功')
ElMessage.error('操作失败')
ElMessage.warning('请先选择数据')
```

---

## 🔍 测试要点

1. **列表加载**: 页面加载时自动请求数据
2. **新增功能**: 表单验证 → 提交 → 刷新列表 → 关闭对话框
3. **编辑功能**: 回填数据 → 修改 → 提交 → 刷新列表
4. **删除功能**: 二次确认 → 删除 → 刷新列表
5. **状态切换**: Switch 切换 → 请求接口 → 更新表格
6. **错误处理**: 捕获异常 → 显示错误提示

---

## 📦 需要安装的依赖

```bash
cd sky-frontend

# ECharts（数据统计模块需要）
npm install echarts

# Excel 导出（可选）
npm install xlsx
```

---

## 🎨 图片需求

如果需要图片资源，请告知用户获取以下图片：
1. 套餐默认占位图（推荐尺寸：300x200px）
2. 菜品默认占位图（推荐尺寸：300x200px）
3. Logo 图片（可选）

---

## 💡 开发提示

1. **复用已有组件**: 参考 `DishList.vue` 和 `OrderList.vue` 的实现
2. **保持代码风格一致**: 使用相同的命名规范和结构
3. **响应式处理**: 确保在不同屏幕尺寸下正常显示
4. **加载状态**: 使用 `v-loading` 指令显示加载动画
5. **空数据处理**: 表格数据为空时显示友好提示

---

## 🚦 优先级

1. **高优先级**: 员工管理、分类管理（基础功能）
2. **中优先级**: 套餐管理（依赖分类和菜品）
3. **低优先级**: 数据统计（需要 ECharts，可最后实现）

---

## 📞 联系方式

如有疑问，请查看：
- 已实现页面: `DishList.vue`, `OrderList.vue`
- Element Plus 文档: https://element-plus.org/zh-CN/
- ECharts 文档: https://echarts.apache.org/zh/index.html

开始开发吧！加油！🎉
