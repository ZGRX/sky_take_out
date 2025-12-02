# sky-frontend (Vue 3 + Vite)

这是为 `sky_take_out` 项目创建的独立前端示例（最小模板）。

快速运行：

PowerShell:
```
cd E:\github_repository\sky_take_out\sky-frontend
npm install
npm run dev
```

说明：
- 开发服务器默认端口 `5173`。
- 配置了 Vite 代理：`/api/*` 将代理到 `http://localhost:8080/*`，开发时可通过 `axios` 请求 `/api/...` 访问后端。

示例：`App.vue` 中有一个调用 `/api/admin/dish/list` 的示例按钮。若后端需要 `token` 认证，请把 token 添加到请求头。
