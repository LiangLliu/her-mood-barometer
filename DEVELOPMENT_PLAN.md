# 她的晴雨表（Her Mood Barometer）开发蓝图（v4.2 | Checklist）

采用最新 Android 最佳实践：Compose、Material 3、Navigation Compose、Room、DataStore、Hilt、WorkManager、App Startup、SplashScreen、per‑app locales、Clean Architecture + MVVM。

---

## 1) 总览（模块清单）
- [x] 架构与分层（Clean Architecture + MVVM + Hilt）
- [x] 导航（单 Activity，Navigation Compose + BottomNav）
- [x] 主题（系统/浅/深；首帧前设置 NightMode，防闪烁）
- [x] 国际化引擎（per‑app locales；默认“跟随系统”；BCP‑47：`zh‑TW`）
- [x] 冷启动语言一致性（AndroidX Startup + MainActivity 首帧前同步应用）
- [x] 记录页（预定义/自定义情绪、强度、备注、保存）
- [ ] 统计页（图表占位；待接入 Vico Charts）
- [x] 设置页（主题、语言、提醒、自定义情绪）
- [x] 数据层（Room 实体/DAO/DB；DAO Flow）
- [x] 偏好（DataStore：主题/语言/提醒/图表偏好）
- [x] 通知渠道（App Startup 幂等创建）
- [ ] 提醒调度（One‑time 已有；周期/权限检查待补齐）
- [x] SplashScreen（系统级 Splash + Post 主题）
- [ ] 无障碍（contentDescription/语义/动态字体/对比度）
- [ ] 数据导出/导入（JSON；加密文件导入导出）
- [ ] 测试（Repo/UseCase/VM/UI/导航）
- [ ] 性能（Baseline Profiles、索引优化、监测）
- [ ] CI/CD & 覆盖率（GitHub Actions + Kover）

---

## 2) 架构与约定（基线能力）
- [x] 分层：Presentation(UI+VM) / Domain(UseCase+Model+Repository 接口) / Data(Room+DataStore+Repository 实现) / DI
- [x] 数据流：Repository → Flow；ViewModel → StateFlow；UI → collectAsStateWithLifecycle()
- [x] 启动：SplashScreen + AndroidX Startup；首帧前应用主题/语言
- [x] 国际化：per‑app locales；默认“system”；统一 BCP‑47（`zh‑TW`）
- [x] 代码规范：Kotlin 官方风格；语义化命名；早返回；UI 无业务

---

## 3) 已交付（Done）
- [x] 语言切换：默认跟随系统；切换即生效；重启保持一致
- [x] 主题切换：浅/深/系统；首帧防闪烁
- [x] 数据层：Room + Flow；DataStore 偏好
- [x] 通知：渠道在进程启动时创建；基础提醒调度
- [x] UI：记录/统计/设置页面骨架与底部导航；Material 3 主题
- [x] 启动：系统 Splash + Post 主题；AndroidX Startup 初始化（语言/通知渠道）

---

## 4) 待办（TODO by Priority）
高优先级
- [ ] 图表可视化（Vico Charts：柱状/折线/饼图；触摸高亮、图例、暗/亮适配）
- [ ] 可访问性（contentDescription/语义/动态字体/对比度/无障碍导航）

中优先级
- [ ] 数据导出/导入（JSON；加密文件导入导出，Security Crypto）
- [ ] 提醒完善（WorkManager 周期任务；权限检查；失败重试；系统重启恢复）
- [ ] 测试与质量（Repo/UseCase/VM/导航/Compose UI；覆盖率统计 Kover）
- [ ] 性能与体验（Baseline Profiles；Room 查询索引；关键交互动画）

---

## 5) 技术要点（关键实现）
- [x] 语言：AndroidX Startup `LocaleInitializer` 冷启动设置 locales；MainActivity 首帧前兜底；`MoodApp` 在 `uiState.isInitialized` 后 `ApplyLocale()`
- [x] 主题：MainActivity 首帧前 `AppCompatDelegate.setDefaultNightMode(...)`
- [x] 通知渠道：`NotificationInitializer` 幂等创建
- [ ] 数据迁移：开发期 `fallbackToDestructiveMigration()`；发版前完善 `MIGRATION`

---

## 6) 迭代计划（Milestones）
- 迭代 A（本周）
  - [ ] Vico Charts 至少 1 种图表落地
  - [ ] 可访问性基础（cd、语义、动态字体）
  - [ ] 冷启动回归（语言/主题一致性）
- 迭代 B（下周）
  - [ ] 数据导出（JSON）/导入（无加密）
  - [ ] Baseline Profiles；关键动效
  - [ ] 单元/集成/UI 测试补齐
- 迭代 C（再下一周）
  - [ ] 加密导出（Security Crypto）
  - [ ] 周期提醒/权限 & 开机恢复
  - [ ] Window Size Class 基础适配（平板/折叠屏）
  - [ ] 预发布清单与商店素材

---

## 7) 质量与规范
- [ ] 代码审查：命名清晰、早返回、异常处理
- [ ] 测试：Repo/UseCase/VM/UI/导航 用例
- [ ] Lint：保持零告警，逐步升级为错误
- [ ] CI：构建/测试/Lint；覆盖率 Kover（待建）

---

## 8) 版本信息
- 目标 SDK：36；min SDK：26；Kotlin：2.2.0；Gradle：8.14
- 文档版本：v4.2（2025‑08）


