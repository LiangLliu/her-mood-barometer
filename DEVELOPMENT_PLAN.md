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
- [x] 统计页（双图展示 + 概览指标 + 错误提示）
- [x] 设置页（主题、语言、提醒、自定义情绪）
- [x] 数据层（Room 实体/DAO/DB；DAO Flow）
- [x] 偏好（DataStore：主题/语言/提醒/图表偏好）
- [x] 通知渠道（App Startup 幂等创建）
- [x] 提醒调度（WorkManager 周期任务；设置变更即调度/取消；启动时自动恢复）
- [x] 权限最佳实践（通知权限、精准闹钟：设置页引导系统设置页）
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
  - [2025-08-22] 统计页重构：
    - 双图并列（折线：每日平均强度；柱状：情绪次数），去除图表类型切换
    - 概览指标改为纵向列表（总记录、平均强度、最常见情绪），避免窄屏换行
    - 错误提示卡片与空态；Compose 预览与基本 UI 测试
- [x] 启动：系统 Splash + Post 主题；AndroidX Startup 初始化（语言/通知渠道）

---

## 4) 待办（TODO by Priority）
高优先级
- [ ] 图表可视化（Vico Charts：柱状/折线；触摸高亮、图例、暗/亮适配）
  - [x] Vico 接入（2.1.3，compose-m3），实现柱状/折线（饼图取消）
  - [x] 统计用例扩展：countsByEmotion、dailyAverageIntensity
  - [x] UI 接入与无障碍（Semantics cd_ 前缀）
  - [x] 多语言文案（5 语言）
- [ ] 可访问性（contentDescription/语义/动态字体/对比度/无障碍导航）

中优先级
- [ ] 数据导出/导入（JSON；加密文件导入导出，Security Crypto）
- [x] 提醒完善（快捷时间 Chips + 自定义；Android 13+ 通知权限引导；S+ 精准闹钟设置引导）
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

---

## 9) 权限测试清单（开发模式）
- [ ] Android 13+ 通知权限（POST_NOTIFICATIONS）
  - [ ] 撤销权限：`adb shell pm revoke com.lianglliu.hermoodbarometer android.permission.POST_NOTIFICATIONS`
  - [ ] 在设置页开启提醒，应用应引导至系统“应用通知设置”页
  - [ ] 赋予权限：`adb shell pm grant com.lianglliu.hermoodbarometer android.permission.POST_NOTIFICATIONS`
  - [ ] 选择 1-2 分钟后的时间，收到通知
- [ ] Android 12+ 精准闹钟
  - [ ] 关闭精准闹钟（系统：特殊应用访问 → 闹钟与提醒），或：`adb shell appops set com.lianglliu.hermoodbarometer SCHEDULE_EXACT_ALARM ignore`
  - [ ] 在设置页开启提醒，应用应引导至精准闹钟设置页
  - [ ] 打开精准闹钟（或：`adb shell appops set com.lianglliu.hermoodbarometer SCHEDULE_EXACT_ALARM allow`）
  - [ ] 选择 1-2 分钟后的时间，并执行：`adb shell dumpsys deviceidle force-idle`，仍能按时收到通知
  - [ ] 校验已安排：`adb shell dumpsys alarm | grep com.lianglliu.hermoodbarometer`
  


