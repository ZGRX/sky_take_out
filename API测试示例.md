# 苍穹外卖 - API测试示例

## 一、管理端接口测试

### 1. 管理员登录
```http
POST http://localhost:8080/admin/employee/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

**返回示例**：
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 1,
    "username": "admin",
    "name": "管理员",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

**提取token**：将返回的token用于后续请求


### 2. 查询所有菜品
```http
GET http://localhost:8080/admin/dish/list
Content-Type: application/json
token: {上一步获取的token}
```

### 3. 新增菜品
```http
POST http://localhost:8080/admin/dish
Content-Type: application/json
token: {token}

{
  "name": "糖醋里脊",
  "categoryId": 4,
  "price": 35.00,
  "image": "https://via.placeholder.com/150",
  "description": "酸甜可口",
  "status": 1
}
```

### 4. 修改菜品
```http
PUT http://localhost:8080/admin/dish
Content-Type: application/json
token: {token}

{
  "id": 1,
  "name": "宫保鸡丁（改）",
  "categoryId": 1,
  "price": 42.00,
  "image": "https://via.placeholder.com/150",
  "description": "经典川菜 升级版",
  "status": 1
}
```

### 5. 删除菜品
```http
DELETE http://localhost:8080/admin/dish/1
Content-Type: application/json
token: {token}
```

### 6. 查询所有订单
```http
GET http://localhost:8080/admin/order/list
Content-Type: application/json
token: {token}
```

### 7. 接单
```http
PUT http://localhost:8080/admin/order/confirm/1
Content-Type: application/json
token: {token}
```

### 8. 完成订单
```http
PUT http://localhost:8080/admin/order/complete/1
Content-Type: application/json
token: {token}
```

---

## 二、用户端接口测试

### 1. 用户登录
```http
POST http://localhost:8080/user/user/login
Content-Type: application/json

{
  "phone": "13800138000"
}
```

**返回示例**：
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 1,
    "openid": null,
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### 2. 浏览所有菜品（无需登录）
```http
GET http://localhost:8080/user/dish/list
Content-Type: application/json
```

### 3. 按分类查询菜品（无需登录）
```http
GET http://localhost:8080/user/dish/category/1
Content-Type: application/json
```

### 4. 添加菜品到购物车
```http
POST http://localhost:8080/user/shoppingCart/add
Content-Type: application/json
authentication: {用户登录的token}

{
  "dishId": 2
}
```

### 5. 查看购物车
```http
GET http://localhost:8080/user/shoppingCart/list
Content-Type: application/json
authentication: {用户登录的token}
```

### 6. 减少购物车商品数量
```http
POST http://localhost:8080/user/shoppingCart/sub
Content-Type: application/json
authentication: {用户登录的token}

{
  "dishId": 2
}
```

### 7. 清空购物车
```http
DELETE http://localhost:8080/user/shoppingCart/clean
Content-Type: application/json
authentication: {用户登录的token}
```

### 8. 提交订单
```http
POST http://localhost:8080/user/order/submit
Content-Type: application/json
authentication: {用户登录的token}

{
  "consignee": "张三",
  "phone": "13800138000",
  "address": "北京市朝阳区望京SOHO",
  "remark": "请尽快送达，谢谢",
  "payMethod": 1
}
```

### 9. 查看我的订单
```http
GET http://localhost:8080/user/order/list
Content-Type: application/json
authentication: {用户登录的token}
```

---

## 三、完整测试流程

### 场景1：管理员操作流程
1. 管理员登录 → 获取token
2. 查看菜品列表
3. 新增一道菜品
4. 修改菜品信息
5. 查看订单列表
6. 接单并处理

### 场景2：用户下单流程
1. 用户登录（手机号） → 获取token
2. 浏览菜品列表
3. 将喜欢的菜品加入购物车（多个）
4. 查看购物车
5. 提交订单（填写收货信息）
6. 查看我的订单

### 场景3：完整业务流程
1. **用户端**：用户登录
2. **用户端**：浏览菜品，加入购物车
3. **用户端**：提交订单
4. **管理端**：管理员登录
5. **管理端**：查看新订单
6. **管理端**：接单
7. **管理端**：完成订单
8. **用户端**：查看订单状态

---

## 四、返回码说明

| code | 说明 |
|------|------|
| 1    | 成功 |
| 0    | 失败 |

---

## 五、订单状态说明

| 状态值 | 说明 |
|--------|------|
| 1      | 待付款 |
| 2      | 待接单 |
| 3      | 已接单 |
| 4      | 派送中 |
| 5      | 已完成 |
| 6      | 已取消 |

---

## 六、注意事项

1. **token的使用**
   - 管理端请求头：`token: {管理员token}`
   - 用户端请求头：`authentication: {用户token}`

2. **token有效期**
   - 默认2小时（7200000毫秒）
   - 过期后需要重新登录

3. **跨域问题**
   - 如果前端遇到跨域，需要在后端配置CORS

4. **数据库初始化**
   - 确保执行了数据库初始化脚本
   - 默认分类和菜品数据已经预置

5. **测试顺序**
   - 建议先测试管理端，确保基础数据正常
   - 再测试用户端的完整流程

---

## 七、使用Postman导入

1. 创建一个新的Postman Collection
2. 将上述请求添加到Collection中
3. 使用Environment变量管理token：
   - 创建变量：`admin_token`
   - 创建变量：`user_token`
   - 在请求中使用：`{{admin_token}}`

---

## 八、curl命令示例

### 管理员登录
```bash
curl -X POST http://localhost:8080/admin/employee/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

### 查询菜品（需要替换YOUR_TOKEN）
```bash
curl -X GET http://localhost:8080/admin/dish/list \
  -H "Content-Type: application/json" \
  -H "token: YOUR_TOKEN"
```

---

## 九、Swagger在线测试

推荐使用Swagger UI进行可视化测试：
1. 启动项目
2. 访问：http://localhost:8080/swagger-ui.html
3. 所有接口都可以在线测试
4. 支持Authorization配置
