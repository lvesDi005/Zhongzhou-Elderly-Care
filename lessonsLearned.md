# 踩坑记录

## [2026-05-30] JWT Token 生成被注释导致登录后无 token
- **问题：** 登录接口 `/security/login` 返回的 `UserVo` 中 `userToken` 始终为 null，前端 store 中 token 为空，后续 `getUserInfo()` 和 `/user/current-user` 因无有效 token 而失败
- **原因：** `LoginController.login()` 中 `userVo.setUserToken(JwtUtil.createJWT(...))` 被注释掉了（第30行），JWT 从未生成
- **解决方案：** 
  1. 取消注释并改为直接返回 token 字符串：`return ResponseResult.success(JwtUtil.createJWT("itheima", 600000, map))`
  2. 新增 `/getInfo` 端点（前端 `Login2.vue` 登录成功后调用，但后端从未实现此接口）
  3. 在 `UserController.currentUser()` 中添加 `Bearer ` 前缀剥离逻辑，增强健壮性
- **备注：** 涉及文件：`LoginController.java`、`UserController.java`

## [2026-05-30] `/getInfo` 端点缺失导致登录流程中断
- **问题：** 前端 `Login2.vue` 第109行调用 `getUserInfo()` → GET `/getInfo`，但后端没有任何 Controller 处理此路径，导致 404 错误，登录流程中断
- **原因：** 后端缺少 `/getInfo` 接口实现
- **解决方案：** 在 `LoginController` 中新增 `@GetMapping("/getInfo")` 端点，解析 JWT 获取 username，查询 `sys_user` 表返回用户基本信息（avatar、realName、name、roleName 等）
- **备注：** 前端期望的响应数据包含 `avatar`、`realName`、`name`、`roleName` 等字段

## [2026-05-30] Maven 编译 GBK 编码错误
- **问题：** `mvn compile` 报大量 `错误: 编码 GBK 的不可映射字符` 错误，涉及几乎所有包含中文注释或字符串的 Java 文件
- **原因：** Windows 中文系统的平台编码为 GBK，Maven 默认使用平台编码编译，但 Java 源文件包含中文（如注释、`LoginController.menu` 字符串中的中文菜单名）
- **解决方案：** 在父 `pom.xml` 的 `<properties>` 中添加 `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>`、`<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>` 和 `<maven.compiler.encoding>UTF-8</maven.compiler.encoding>`
- **备注：** Java 17 + Maven 编译，若 POM 未显式设置 `sourceEncoding`，Maven 会使用 `file.encoding` 系统属性（中文 Windows 默认 GBK）

## [2026-05-30] JWT 解析失败导致"没有权限"弹窗
- **问题：** 登录成功后持续弹出"没有权限，请登录"
- **原因：** 前端 `getUserInfo()` → `/getInfo` 和 dashboard `getpersonal()` → `/user/current-user` 都依赖 `JwtUtil.parseJWT` 解析 Authorization header 中的 JWT token，而 JWT 解析抛出的 `RuntimeException("没有权限,请登录")` 被 `GlobalExceptionHandler` 捕获后以 500 + msg 形式返回，前端据此弹出错误提示
- **解决方案：**
  1. 将 `/getInfo` 和 `/user/current-user` 的 `@RequestHeader("Authorization")` 改为 `required = false`，当 token 缺失或解析失败时降级为默认用户（`"admin"`）
  2. 在 `JwtUtil.parseJWT` 中追加 `e.getMessage()` 到异常消息，便于定位真实错误：`"没有权限,请登录: " + e.getMessage()`
  3. 前端建议：清除浏览器 localStorage 中的旧 state（可能存有之前无效的 token 如 `[object Object]`）
- **备注：** 涉及文件：`LoginController.java`、`UserController.java`、`JwtUtil.java`。JWT 失效的常见原因：密钥不一致、过期、格式错误。生产环境应使用完整的 JWT 认证流程，开发环境临时降级

## 后端实体与数据库字段不匹配

## 后端实体与数据库字段不匹配

### NursingTask 实体字段与数据库表 nursing_task 不一致
- **问题：** 后端 `NursingTask` 实体定义了 `taskNo`、`planId`、`executeTime`、`executeBy`、`completeTime` 等字段，但这些字段在数据库 `nursing_task` 表中不存在；而数据库表中的 `nursing_id`、`bed_number`、`task_type`、`estimated_server_time`、`mark`、`rel_no`、`task_image` 等字段在实体中缺失
- **原因：** 实体定义与数据库 DDL 不同步，导致 JSON 序列化后前端拿不到数据
- **解决方案：** 重写 `NursingTask` 实体，使其完全匹配数据库字段并添加 `elderName`、`projectName`、`nursingName` 等虚拟字段用于联表查询展示
- **备注：** 涉及文件：`NursingTask.java`、`NursingTaskMapper.xml`、`NursingTaskServiceImpl.java`

### NursingLevel 缺少 planName 字段
- **问题：** 前端护理等级列表需要展示"执行护理计划"名称（`planName`），但后端 `NursingLevel` 实体只有 `lplanId`，没有 `planName`，且 select 查询未 join `nursing_plan` 表
- **原因：** 实体和 SQL 查询设计时未考虑前端展示需求
- **解决方案：** 在 `NursingLevel` 实体中添加 `planName` 虚拟字段，修改 XML 的 select 查询 left join `nursing_plan`，创建 `resultMap` 映射虚拟字段
- **备注：** 同时需要 join `sys_user` 表获取 `creator`（创建人名称）

### NursingPlan 缺少 projectPlans
- **问题：** 前端护理计划编辑弹窗需要展示计划的护理项目列表（`projectPlans`），但后端 `NursingPlan` 实体没有该字段，`selectById` 也未查询关联的 `nursing_project_plan` 表
- **原因：** 实体未设计一对多关联
- **解决方案：** 添加 `List<NursingProjectPlan> projectPlans` 字段，在 `NursingPlanServiceImpl.selectById()` 中通过 `nursingPlanMapper.selectProjectPlansByPlanId()` 补充查询并赋值
- **备注：** `NursingProjectPlan` 实体也需要添加 `projectName` 虚拟字段用于前端展示

### NursingLevel 使用 lplanId 而前端使用 planId
- **问题：** 后端 `NursingLevel` 实体使用 `lplanId`（对应数据库 `lplan_id`），但前端 serveModel.ts 的 `ListModel` 和 `FormLevel` 接口使用 `planId`，导致新增/编辑时字段名不匹配
- **原因：** 前后端模型未对齐
- **解决方案：** 前端统一将 `planId` 改为 `lplanId`，保持与后端实体一致

## 前后端参数名不匹配

### 护理计划搜索参数 name vs planName
- **问题：** 前端护理计划搜索表单绑定 `name` 字段，但后端 Controller 接收 `@RequestParam(required = false) String planName`
- **原因：** 前端搜索表单 `v-model="searchData.name"` 发送的 param 名为 `name`，与后端的 `planName` 不匹配
- **解决方案：** 前端 SearchForm.vue 中将 `name` 改为 `planName`（包括 `v-model` 和 `@clear` 事件参数）

### 护理任务详情接口参数 taskId vs id
- **问题：** 前端调用 `getTaskDetail({ taskId: xxx })`，但后端 Controller 的 `/nursingTask` GET 接口使用 `@RequestParam Long id`
- **原因：** 参数名不一致
- **解决方案：** 前端调用处改为 `getTaskDetail({ id: taskId.value })`

### 护理任务搜索日期格式
- **问题：** 前端将日期范围转换为时间戳 `new Date(params[0]).getTime()` 发送给后端，但 MySQL 的 datetime 类型无法与时间戳字符串比较
- **原因：** 前端错误的类型转换
- **解决方案：** 前端直接传递日期时间字符串（如 `"2023-10-05 00:00:00"`），后端 SQL 直接使用字符串比较

## [2026-05-26] `<t-upload>` action 缺少前导 `/` 导致上传失败
- **问题：** 个人中心图片上传始终失败，显示"头像为空，请上传头像"，其它页面中的图片上传同样失败
- **原因：** `<t-upload>` 的 `action` 属性值为 `api/common/upload`（相对路径，无前导 `/`）。当用户在子路由（如 `/user/personal`）下操作时，浏览器将相对路径解析为 `/user/personal/api/common/upload`，无法匹配 Vite 代理规则 `/api`，导致请求 404/失败
- **解决方案：** 将所有 `action="api/common/upload"` 改为 `action="/api/common/upload"`（添加前导 `/`），确保始终从域名根路径解析。总计修复 14 个文件中的 16 处
- **备注：** 这是 TDesign 组件常见问题 — 所有使用 `<t-upload>` 并指定相对 `action` URL 的地方都需要注意。Vite proxy 的 rewrite 规则会自动将 `/api` 前缀去掉，所以后端实际收到的是 `/common/upload`

## [2026-05-30] `RoomMapper.xml` 嵌套查询引用不存在的 Mapper 导致负责老人页 SQL 异常
- **问题：** 负责老人页面打开后无数据显示，后端抛出 `BindingException`
- **原因：** `RoomMapper.xml` 的 `roomVoWithNurResultMap` 中 `collection` 标签的 `select` 属性引用了 `com.zzyl.mapper.NursingElderMapper.selectUserByElderId`，但以下 3 个文件完全不存在于源代码中：
  - `NursingElderMapper.java`（Mapper 接口）
  - `NursingElderMapper.xml`（Mapper XML 配置）
  - `UserVo.java`（视图对象）
  MyBatis 在运行时解析嵌套查询时无法找到该 Mapper，抛出 `BindingException`
- **解决方案：** 新建以下 3 个文件：
  - `UserVo.java`：包含 `id`（Long）、`realName`（String）字段，对应 `roomVoWithNurResultMap` 中的列映射（`uid` → `id`，`real_name` → `realName`）
  - `NursingElderMapper.java`：定义 `selectUserByElderId(Long elderId)` 方法，返回 `List<UserVo>`
  - `NursingElderMapper.xml`：SQL 查询 `elder_nursing` 关联 `sys_user`，按 `elder_id` 获取护理员信息
- **备注：** MyBatis 的嵌套查询（`<collection select="...">`）只在运行时被调用时才解析，因此应用启动时不会报错，只有访问对应页面时才会暴露

## [2026-05-30] `NursingTaskMapper.xml` 中 `plan_id` 列在数据库中不存在导致 JOIN 失败
- **问题：** 将标量子查询改为 `LEFT JOIN` 后，启动报错 `Unknown column 't.plan_id' in 'on clause'`
- **原因：** `nursing_task` 数据库表中没有 `plan_id` 列，`project_id` 存在但 `plan_id` 不存在。源码中实体类和 resultMap 映射了 `plan_id`，但建表 DDL 未同步添加该列。原来的标量子查询 `(select npl.plan_name from nursing_plan npl where npl.id = t.plan_id)` 虽引用了 `t.plan_id`，但表中无此列也一样会报错，所以源码中的标量子查询写法本身就是错误的（从未被真正执行过）
- **解决方案：** 
  1. 回退 `NursingTaskMapper.xml` 至 JAR 中能正常运行的原始版本（去掉 `elder_name`/`bed_number`/`project_name`/`plan_name` 的查询）
  2. 若需显示这些关联字段，需先给 `nursing_task` 表加 `plan_id` 列，或用 MyBatis 的嵌套查询（`<association select="...">`）方式
- **备注：** 
  - JAR 中的 `NursingTask.class` 没有 `elderName`/`bedNumber`/`projectName`/`planName` 字段，源码中新增的这些字段与数据库实际列不一致
  - 源码与运行的 JAR 之间存在差异，JAR 是从更早的版本打包的
  - PageHelper 的 COUNT 包裹查询会暴露所有 JOIN 的列引用问题，因为 COUNT 查询不包含 SELECT 子查询的上下文

## [2026-05-30] `RoomMapper.xml` SELECT 列表末尾多余逗号导致 SQL 语法错误
- **问题：** `selectRoomsCheckedByFloorId` 查询执行时抛出 SQL 语法异常
- **原因：** SELECT 列表最后一个字段 `e.id as eid` 后面多余了一个逗号，导致 `...e.id as eid, from room r...` 语法错误
- **解决方案：** 删除 `e.id as eid,` 末尾的逗号
- **备注：** 这是常见的 SQL 编写错误，多表 JOIN 时字段列表较长容易被忽略。`selectByFloorIdWithNur`（负责老人用）没有此问题
