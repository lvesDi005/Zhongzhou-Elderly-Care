# 踩坑记录

## [2026/05/23] MySQL 8.0 导出的 SQL 文件无法在 5.7 导入
- **问题：** 从 MySQL 8.0 导出的 `zzyl.sql` 文件，在 MySQL 5.7 执行时报错，提示排序规则 `utf8mb4_0900_ai_ci` 不存在
- **原因：** `utf8mb4_0900_ai_ci` 是 MySQL 8.0 新增的排序规则，基于 Unicode 9.0 算法，MySQL 5.7 不支持
- **解决方案：** 将 SQL 文件中所有 `utf8mb4_0900_ai_ci` 替换为 MySQL 5.7 默认的 `utf8mb4_general_ci`（共 208 处）
- **备注：** `sed -i 's/utf8mb4_0900_ai_ci/utf8mb4_general_ci/g' zzyl.sql` 一行命令即可批量替换
