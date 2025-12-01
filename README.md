# 苍穹外卖系统

这是一个简化的外卖管理系统，包含用户端和管理端功能，采用前后端分离架构。

## 项目简介

本项目旨在帮助大学生学习完整的企业级项目开发流程，虽然功能简化，但麻雀虽小五脏俱全，包含了：
- Spring Boot 后端开发
- MyBatis 数据库操作
- JWT 认证授权
- RESTful API 设计
- 分层架构（Controller-Service-Mapper）
- 统一结果封装
- 全局异常处理

## 技术栈

### 后端
- **Spring Boot 2.7.3** - 核心框架
- **MyBatis** - 持久层框架
- **MySQL** - 数据库
- **Druid** - 数据库连接池
- **JWT** - 用户认证
- **Lombok** - 简化代码
- **Swagger** - API文档

### 数据库
- MySQL 5.7+

## 项目结构

```
sky-take-out/
├── sky-common/          # 公共模块（工具类、常量）
├── sky-pojo/            # 实体类模块（Entity、DTO、VO）
├── sky-server/          # 服务端模块
│   ├── controller/      # 控制器层
│   │   ├── admin/      # 管理端接口
│   │   └── user/       # 用户端接口
│   ├── service/        # 业务逻辑层
│   ├── mapper/         # 数据访问层
│   ├── config/         # 配置类
│   ├── interceptor/    # 拦截器
│   └── handler/        # 异常处理器
└── database/           # 数据库脚本
```

## 功能模块

### 管理端功能
1. **员工管理**
   - 员工登录/退出
   - 默认账号：admin / 123456

2. **菜品管理**
   - 菜品查询（全部/按分类）
   - 菜品新增
   - 菜品修改
   - 菜品删除

3. **订单管理**
   - 查看所有订单
   - 接单/拒单
   - 订单状态管理
   - 完成订单

### 用户端功能
1. **用户登录**
   - 手机号登录（自动注册）

2. **菜品浏览**
   - 查看菜品列表
   - 按分类查看菜品

3. **购物车**
   - 添加菜品到购物车
   - 查看购物车
   - 修改购物车数量
   - 清空购物车

4. **订单管理**
   - 提交订单
   - 查看我的订单

## 快速开始

### 1. 环境准备
- JDK 11+
- Maven 3.6+
- MySQL 5.7+
- IDEA 或其他 Java IDE

### 2. 数据库初始化

执行数据库脚本创建数据库和表：
```bash
mysql -u root -p < database/sky_take_out.sql
```

或者手动执行 `database/sky_take_out.sql` 文件中的SQL语句。

**注意**：请根据你的MySQL配置修改以下信息：
- 数据库用户名（默认：root）
- 数据库密码（默认：123456）

### 3. 修改配置

编辑 `sky-server/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/sky_take_out?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
      username: root        # 修改为你的数据库用户名
      password: 123456      # 修改为你的数据库密码
```

### 4. 编译项目

在项目根目录下执行：
```bash
mvn clean install
```

### 5. 启动项目

方式一：使用IDEA
1. 导入项目到IDEA
2. 找到 `sky-server` 模块下的 `SkyApplication` 类
3. 右键 -> Run 'SkyApplication'

方式二：使用Maven命令
```bash
cd sky-server
mvn spring-boot:run
```

### 6. 访问项目

- **后端接口**: http://localhost:8080
- **Swagger文档**: http://localhost:8080/swagger-ui.html

## API接口说明

### 管理端接口（/admin）

#### 员工登录
```
POST /admin/employee/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

#### 菜品管理
```
# 查询所有菜品
GET /admin/dish/list
Authorization: Bearer {token}

# 新增菜品
POST /admin/dish
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "红烧肉",
  "categoryId": 1,
  "price": 48.00,
  "image": "http://example.com/image.jpg",
  "description": "美味红烧肉",
  "status": 1
}

# 修改菜品
PUT /admin/dish
Authorization: Bearer {token}

# 删除菜品
DELETE /admin/dish/{id}
Authorization: Bearer {token}
```

#### 订单管理
```
# 查询所有订单
GET /admin/order/list
Authorization: Bearer {token}

# 接单
PUT /admin/order/confirm/{id}
Authorization: Bearer {token}

# 拒单
PUT /admin/order/rejection/{id}
Authorization: Bearer {token}

# 完成订单
PUT /admin/order/complete/{id}
Authorization: Bearer {token}
```

### 用户端接口（/user）

#### 用户登录
```
POST /user/user/login
Content-Type: application/json

{
  "phone": "13800138000"
}
```

#### 菜品浏览（无需登录）
```
# 查询所有菜品
GET /user/dish/list

# 按分类查询菜品
GET /user/dish/category/{categoryId}
```

#### 购物车
```
# 添加购物车
POST /user/shoppingCart/add
Authorization: Bearer {token}
Content-Type: application/json

{
  "dishId": 1
}

# 查看购物车
GET /user/shoppingCart/list
Authorization: Bearer {token}

# 减少数量
POST /user/shoppingCart/sub
Authorization: Bearer {token}

# 清空购物车
DELETE /user/shoppingCart/clean
Authorization: Bearer {token}
```

#### 订单
```
# 提交订单
POST /user/order/submit
Authorization: Bearer {token}
Content-Type: application/json

{
  "consignee": "张三",
  "phone": "13800138000",
  "address": "北京市朝阳区xx路xx号",
  "remark": "尽快送达",
  "payMethod": 1
}

# 查看我的订单
GET /user/order/list
Authorization: Bearer {token}
```

## 数据库设计

### 核心表说明

1. **employee** - 员工表（管理员）
   - 存储系统管理员信息
   - 默认账号：admin / 123456（密码已MD5加密）

2. **user** - 用户表
   - 存储C端用户信息
   - 手机号登录自动注册

3. **category** - 分类表
   - 菜品分类管理
   - 预置了川菜、湘菜、粤菜、家常菜等分类

4. **dish** - 菜品表
   - 存储菜品信息
   - 包含名称、价格、图片、描述等

5. **shopping_cart** - 购物车表
   - 用户购物车数据
   - 临时存储，下单后清空

6. **orders** - 订单表
   - 订单主表
   - 记录订单状态、金额等信息

7. **order_detail** - 订单明细表
   - 订单详情
   - 记录订单中的每个菜品

## 学习要点

### 1. 项目分层架构
- **Controller层**：接收请求，参数校验，调用Service
- **Service层**：业务逻辑处理
- **Mapper层**：数据库访问

### 2. JWT认证机制
- 登录成功生成token
- 请求头携带token访问受保护接口
- 拦截器验证token有效性

### 3. 统一结果封装
```java
Result.success(data)  // 成功响应
Result.error(msg)     // 失败响应
```

### 4. ThreadLocal应用
使用 `BaseContext` 在整个请求链路中传递当前用户ID

### 5. 事务管理
订单提交使用 `@Transactional` 保证数据一致性

## 常见问题

### 1. 启动报错：数据库连接失败
- 检查MySQL是否启动
- 确认数据库用户名密码是否正确
- 确认数据库 `sky_take_out` 是否已创建

### 2. 接口返回401
- 检查是否已登录获取token
- 确认请求头中是否携带了正确的token
- 检查token是否过期（默认2小时）

### 3. Swagger无法访问
- 确认项目已启动
- 访问：http://localhost:8080/swagger-ui.html
- 检查浏览器是否拦截

## 测试流程

### 管理端测试流程
1. 管理员登录（admin/123456）
2. 获取token
3. 查看菜品列表
4. 新增/修改/删除菜品
5. 查看订单列表
6. 处理订单（接单/拒单/完成）

### 用户端测试流程
1. 用户登录（任意手机号）
2. 获取token
3. 浏览菜品
4. 添加菜品到购物车
5. 查看购物车
6. 提交订单
7. 查看我的订单

## 扩展方向

如果想进一步学习，可以尝试以下扩展：
1. 添加地址簿管理功能
2. 实现真实的支付对接
3. 添加订单配送状态跟踪
4. 实现商家端实时订单推送
5. 添加用户评价功能
6. 开发前端页面（Vue.js）
7. 实现文件上传（菜品图片）
8. 添加Redis缓存
9. 实现分页查询
10. 添加数据统计报表

## 项目亮点

1. **完整的分层架构**：学习企业级项目分层设计
2. **JWT认证**：掌握主流的认证方案
3. **统一封装**：Result统一返回、全局异常处理
4. **双端设计**：管理端和用户端分离
5. **事务管理**：订单业务的事务处理
6. **拦截器应用**：JWT token拦截验证
7. **MyBatis实践**：注解和XML两种方式
8. **RESTful设计**：标准的API设计风格

## 联系与反馈

如果在学习过程中遇到问题，欢迎提issue或联系我。

祝学习愉快！🎉
