# 踩坑记录

## [2026-05-26] `<t-upload>` action 缺少前导 `/` 导致上传失败
- **问题：** 个人中心图片上传始终失败，显示"头像为空，请上传头像"，其它页面中的图片上传同样失败
- **原因：** `<t-upload>` 的 `action` 属性值为 `api/common/upload`（相对路径，无前导 `/`）。当用户在子路由（如 `/user/personal`）下操作时，浏览器将相对路径解析为 `/user/personal/api/common/upload`，无法匹配 Vite 代理规则 `/api`，导致请求 404/失败
- **解决方案：** 将所有 `action="api/common/upload"` 改为 `action="/api/common/upload"`（添加前导 `/`），确保始终从域名根路径解析。总计修复 14 个文件中的 16 处
- **备注：** 这是 TDesign 组件常见问题 — 所有使用 `<t-upload>` 并指定相对 `action` URL 的地方都需要注意。Vite proxy 的 rewrite 规则会自动将 `/api` 前缀去掉，所以后端实际收到的是 `/common/upload`
