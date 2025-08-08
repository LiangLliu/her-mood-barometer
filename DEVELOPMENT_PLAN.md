# 「她的晴雨表」详细开发计划

## 📋 项目概述

**项目名称**: 她的晴雨表 (Her Mood Barometer)  
**技术栈**: Android + Jetpack Compose + Clean Architecture + MVVM  
**目标**: 开发一个多语言情绪记录和统计应用，支持国际化、数据可视化、自定义设置等功能。

---

## ✅ 第一阶段：项目基础搭建（已完成）

### 1.1 项目依赖配置 ✅

- [x] 配置 Gradle 依赖（Compose, Room, Hilt, Navigation）
- [x] 设置 Kotlin DSL 构建脚本
- [x] 配置多语言支持基础
- [x] 集成 Vico Charts 图表库
- [x] 配置 DataStore 偏好设置
- [x] 添加 Security Crypto 依赖
- [x] 配置 Kotlinx Serialization

### 1.2 国际化资源 ✅

- [x] 创建英文资源文件 (`values/strings.xml`)
- [x] 创建简体中文资源文件 (`values-zh/strings.xml`)
- [x] 创建繁体中文资源文件 (`values-zh-rTW/strings.xml`)
- [x] 创建日文资源文件 (`values-ja/strings.xml`)
- [x] 创建韩文资源文件 (`values-ko/strings.xml`)
- [x] 100+ 字符串资源覆盖所有功能
- [x] 包含导航、页面、错误信息、确认对话框等完整文本
- [x] 配置 `locales_config.xml` 支持Android 13+语言设置

### 1.3 基础架构搭建 ✅

- [x] 创建 Clean Architecture 包结构
    - `data/` - 数据层 (database, repository, preferences)
    - `domain/` - 领域层 (model, repository, usecase)
    - `ui/` - 表现层 (components, navigation, screen, theme)
    - `di/` - 依赖注入模块
- [x] 定义领域模型（支持Kotlinx Serialization）
    - `EmotionType` - 情绪类型枚举
    - `EmotionIntensity` - 情绪强度等级
    - `EmotionRecord` - 情绪记录模型
    - `CustomEmotion` - 自定义情绪模型（支持Emoji）
    - `TimeRange` - 时间范围枚举
- [x] 定义 Repository 接口
    - `EmotionRepository` - 情绪记录仓库
    - `CustomEmotionRepository` - 自定义情绪仓库
    - `PreferencesRepository` - 偏好设置仓库
- [x] 创建基础 Use Cases
    - `AddEmotionRecordUseCase` - 添加情绪记录
    - `GetEmotionRecordsUseCase` - 获取情绪记录
    - `GetEmotionStatisticsUseCase` - 获取统计数据

---

## ✅ 第二阶段：基础UI框架（已完成）

### 2.1 导航系统 ✅

- [x] 创建 Screen 路由定义
- [x] 实现 Navigation Compose 导航
- [x] 底部导航栏实现
- [x] 页面间导航逻辑

### 2.2 页面框架 ✅

- [x] 记录页面基础UI
    - 情绪类型选择（基于Emoji而非Material Icons）
    - 情绪强度滑块（Slider）
    - 备注输入框（OutlinedTextField）
    - 保存按钮
- [x] 统计页面基础UI
    - 时间范围选择（FilterChip）
    - 图表类型选择（FilterChip）
    - 统计概览卡片
    - 图表占位符区域
- [x] 设置页面基础UI
    - 外观设置（主题、语言）
    - 通知设置（提醒开关、时间）
    - 自定义设置（自定义情绪）
    - 数据管理（导入导出）
    - 关于信息

### 2.3 应用入口 ✅

- [x] MainActivity 配置（支持语言切换和Edge-to-Edge）
- [x] MoodApp 主组件
- [x] Material 3 主题配置
- [x] LocaleManager 语言管理

---

## ✅ 第三阶段：数据层实现（已完成）

### 3.1 Room 数据库 ✅

- [x] 定义数据库实体
    - `EmotionRecordEntity` - 情绪记录实体
    - `CustomEmotionEntity` - 自定义情绪实体（支持Emoji）
- [x] 创建 DAO 接口
    - `EmotionRecordDao` - 情绪记录数据访问
    - `CustomEmotionDao` - 自定义情绪数据访问
- [x] 配置数据库类 `MoodDatabase`
- [x] 实体与领域模型转换扩展函数

### 3.2 DataStore 偏好设置 ✅

- [x] `PreferencesManager` 实现
- [x] 语言设置存储
- [x] 主题设置存储
- [x] 提醒设置存储
- [x] 首次启动标记

### 3.3 Repository 实现 ✅

- [x] `EmotionRepositoryImpl` - 情绪记录仓库实现
- [x] `CustomEmotionRepositoryImpl` - 自定义情绪仓库实现
- [x] `PreferencesRepositoryImpl` - 偏好设置仓库实现

### 3.4 依赖注入 ✅

- [x] 配置 Hilt 应用类 `MoodApplication`
- [x] 数据库模块配置 `DatabaseModule`
- [x] Repository 绑定配置 `RepositoryModule`
- [x] UseCase 模块配置 `UseCaseModule`

---

## ✅ 第四阶段：业务逻辑实现（已完成）

### 4.1 记录功能实现 ✅

- [x] 创建 `RecordViewModel`
- [x] 实现情绪记录保存逻辑（支持预定义和自定义情绪）
- [x] 添加表单验证
- [x] 实现基于Emoji的情绪选择
- [x] 添加记录成功反馈
- [x] 解决选择状态问题

### 4.2 统计功能实现 ✅

- [x] 创建 `StatisticsViewModel`
- [x] 实现数据查询和统计逻辑
- [x] 时间范围筛选（一周、一月、三个月）
- [x] 图表类型切换（柱状图、折线图、饼图）
- [x] 统计数据计算（平均强度、最常见情绪等）

### 4.3 设置功能实现 ✅

- [x] 创建 `SettingsViewModel`
- [x] 实现语言切换功能（立即生效）
- [x] 实现主题切换功能
- [x] 实现提醒设置功能
- [x] 实现自定义情绪管理（支持Emoji选择、描述字段）
- [x] 模块化重构（拆分为多个组件文件）

---

## ⏳ 第五阶段：UI 完善和优化（基本完成）

### 5.1 记录页面完善 ✅

- [x] 优化情绪选择UI（使用Emoji替代Material Icons）
- [x] 实现情绪选择状态正确更新
- [x] 实现自定义情绪支持
- [x] 添加表单验证和错误处理
- [x] 保存按钮状态管理

### 5.2 统计页面完善 ⚠️

- [x] 实现简化版图表显示（LinearProgressIndicator等）
- [x] 时间范围选择功能
- [x] 图表类型切换功能
- [x] 统计数据展示
- [ ] **待完成**: Vico Charts真实图表集成
- [ ] **待完成**: 图表交互功能
- [ ] **待完成**: 数据导出功能

### 5.3 设置页面完善 ✅

- [x] 实现语言选择对话框
- [x] 实现自定义情绪管理（Emoji选择器）
- [x] 模块化组件拆分
    - `AppearanceSection.kt` - 外观设置
    - `NotificationSection.kt` - 通知设置
    - `CustomEmotionSection.kt` - 自定义情绪
    - `AboutSection.kt` - 关于信息
    - `SharedComponents.kt` - 共享组件

---

## ⏳ 第六阶段：高级功能（进行中）

### 6.1 通知系统 ⚠️

- [x] 添加通知权限声明
- [x] 添加通知设置UI
- [ ] **待完成**: 配置通知渠道
- [ ] **待完成**: 实现通知历史
- [ ] **待完成**: 添加通知权限检查

### 6.2 数据管理 ⚠️

- [x] 添加数据导入导出UI框架
- [ ] **待完成**: 实现数据备份功能
- [ ] **待完成**: 实现数据恢复功能
- [ ] **待完成**: 实现数据同步功能
- [ ] **待完成**: 添加数据加密（Security Crypto已添加依赖）

### 6.3 用户体验优化 ⚠️

- [x] 实现深色模式（立即生效）
- [x] 实现语言切换（立即生效）
- [x] Edge-to-Edge支持（基础配置）
- [x] 预测性返回手势支持（基础配置）
- [ ] **待完成**: 添加启动动画
- [ ] **待完成**: 添加无障碍支持
- [ ] **待完成**: 添加手势支持
- [ ] **待完成**: 实现动画效果

---

## 📋 第七阶段：现代化架构升级（部分完成）

### 7.1 Gradle和依赖现代化 ✅

- [x] 升级到Gradle 8.14
- [x] 更新所有依赖到最新稳定版本
- [x] 引入Compose BOM管理（2025.07.00）
- [x] 配置Gradle性能优化
- [x] 添加Security Crypto依赖
- [x] Kotlinx Serialization插件配置

### 7.2 序列化和数据处理现代化 ⚠️

- [ ] 所有领域模型支持Kotlinx Serialization
- [ ] **待完成**: 实现高性能LocalDateTime序列化器
- [ ] **待完成**: 创建完整的数据传输对象(DTO)系统
- [ ] **待完成**: 实现数据导出/导入服务

### 7.3 安全性增强 ⚠️

- [ ] 添加Security Crypto库依赖
- [ ] **待完成**: 实现加密服务
- [ ] **待完成**: 创建安全数据存储
- [ ] **待完成**: 配置加密依赖注入模块

### 7.4 性能优化 ⚠️

- [ ] **待完成**: 实现内存优化器
- [ ] **待完成**: 添加性能监控
- [ ] **待完成**: 实现对象池管理
- [ ] **待完成**: 优化启动时间

### 7.5 暂停的高级功能

由于复杂性和编译问题，以下功能暂时注释或延后实现：
- **自适应UI (Adaptive Layout)** - 编译冲突
- **App Startup集成** - 依赖问题
- **Splash Screen API** - 兼容性问题
- **窗口大小类** - API复杂性

---

## ⏳ 第八阶段：测试和发布（待开始）

### 8.1 测试

- [ ] 单元测试
    - Repository 测试
    - UseCase 测试
    - ViewModel 测试
- [ ] 集成测试
    - 数据库测试
    - 数据流测试
- [ ] UI 测试
    - 页面导航测试
    - 用户交互测试
- [ ] 多语言测试
    - 语言切换测试
    - 文本显示测试

### 8.2 发布准备

- [ ] 应用图标设计
- [ ] 应用描述和截图
- [ ] 隐私政策
- [ ] 应用商店发布
- [ ] 版本管理
- [ ] 用户反馈收集

---

## 📊 进度统计

| 阶段           | 状态    | 完成度  | 预计时间 |
|--------------|-------|------|------|
| 第一阶段：项目基础搭建  | ✅ 已完成 | 100% | 已完成  |
| 第二阶段：基础UI框架  | ✅ 已完成 | 100% | 已完成  |
| 第三阶段：数据层实现   | ✅ 已完成 | 100% | 已完成  |
| 第四阶段：业务逻辑实现  | ✅ 已完成 | 100% | 已完成  |
| 第五阶段：UI完善和优化 | ✅ 基本完成 | 85%  | 1-2天 |
| 第六阶段：高级功能    | ⏳ 进行中 | 35%  | 2-3天 |
| 第七阶段：现代化架构升级 | ⏳ 部分完成 | 45%  | 3-5天 |
| 第八阶段：测试和发布   | ⏳ 待开始 | 0%   | 2-3天 |

**总体进度**: 约 73%  
**已完成阶段**: 4/8 个阶段  
**进行中阶段**: 3/8 个阶段  
**待开始阶段**: 1/8 个阶段

---

## 🎯 下一步行动计划（更新）

### 立即执行（本周内）

1. **主题/语言现代化（高优先级）**
    - 主题选择保持三态：系统/浅色/深色；新增“动态色彩”开关（Android 12+）
    - 语言切换迁移至 per‑app locales（使用 AppCompatDelegate.setApplicationLocales，Android 13+ 原生支持），低版本沿用兼容路径
    - 移除 MainActivity 中语言 SharedPreferences 直读路径，统一 DataStore + per‑app locales 管理

2. **集成Vico Charts图表库（中优先级）**
    - 根据Vico 2.1.3文档重新实现图表组件
    - 实现真实的柱状图、折线图、饼图显示
    - 替换当前的简化UI组件

3. **完善现代化功能**
    - 实现 LocalDateTime/Instant 高性能序列化器（统一时区策略）
    - 创建数据导出/导入服务（JSON/加密文件）
    - 实现 Security Crypto 加密服务（密钥托管与密文存储）

---

## 🔧 结构化重构与优化建议（新增）

- 主题与外观
  - 增加主题三态选择（系统/浅色/深色）与“动态色彩”开关；`AppearanceSection` 使用 `@StringRes` 而非硬编码键值
  - `SettingsViewModel` 使用 `combine(...).stateIn(...)` 产出单源 `uiState`
  - 系统栏颜色适配建议使用官方 Edge‑to‑Edge API 配合 Material 3

- 国际化
  - 迁移到 per‑app locales（Android 13+），低版本走 `LocaleManager` 兼容路径
  - 移除 `MainActivity` 的 SharedPreferences 直读，统一 DataStore 管理

- 数据层（Room）
  - 为 `timestamp`、`emotionType` 添加索引
  - 开发期 `fallbackToDestructiveMigration()`，发版前补齐 `MIGRATIONS`
  - 统一时间存储语义：`Instant` 或 `LocalDateTime`+时区约定

- Repository 与 UseCase
  - 补齐 CRUD UseCase，精简 VM 逻辑
  - 统一错误语义（`Flow<Result<T>>` 或领域 `Result`）

- 通知与提醒
  - `WorkManager` 周期任务 + `ExistingPeriodicWorkPolicy.UPDATE`；如需严格整点评估 `AlarmManager`
  - 配置通知渠道并按类型区分重要度

- UI 与可访问性
  - 完成 `contentDescription`、语义标签、文本缩放与大屏适配

- 测试与CI
  - Repository/UseCase/VM/导航/Compose UI 测试
  - Kover 覆盖率与 CI 流水线

- 构建与质量
  - 恢复 Lint 规则并逐步收紧为 error
  - 引入 Baseline Profiles 提升性能

### 现代化重构计划（渐进式实现）

1. **优先实现影响用户体验的功能**
    - 主题即时切换
    - 真实图表显示
    - 数据导出功能

2. **逐步完善安全和性能功能**
    - Security Crypto加密服务
    - 内存优化器
    - 性能监控

3. **最后考虑高级功能**
    - 自适应UI（当技术成熟时）
    - App Startup优化
    - Splash Screen API

### 短期目标（1-2周内）

1. **完成第五阶段剩余工作**
    - 语言切换架构迁移至 per‑app locales
    - 完成Vico Charts集成

2. **推进第六阶段和第七阶段**
    - 通知系统完整实现
    - 现代化架构升级

### 中期目标（1个月内）

1. **完成第六阶段和第七阶段高级功能**
2. **开始第八阶段测试和发布准备**
3. **用户测试和反馈收集**

### 长期目标（2个月内）

1. **应用发布到应用商店**
2. **用户反馈收集和功能迭代**
3. **新功能开发和优化**

---

## 🚨 当前待解决的关键问题（更新）

### 1. 语言切换架构现代化（高优先级）

- 问题描述: 当前通过 Activity 重建与自定义 `LocaleManager`，建议迁移至 per‑app locales（Android 13+）并统一 DataStore，避免双写与状态不同步
- 优先级: 高 - 影响国际化一致性与维护性

### 2. Vico Charts集成问题（中优先级）

- **问题描述**: 统计页面使用简化UI组件代替真实图表
- **当前状态**: 
    - ✅ 已添加Vico Charts依赖（版本2.1.3）
    - ✅ 已实现简化图表UI
    - ❌ 真实图表渲染未实现
- **优先级**: 中 - 影响数据可视化效果

### 3. 现代化功能实现（中优先级）

- **待实现功能**:
    - LocalDateTime序列化器
    - 数据导出/导入服务
    - Security Crypto加密功能
    - 内存优化器
- **优先级**: 中 - 提升应用质量和安全性

---

## 📝 技术债务和注意事项（更新）

### 当前技术债务

1. 图表库实际集成（Vico Charts 真实渲染与交互）
2. 现代化功能实现（序列化、加密、性能优化等）
3. 通知系统完整实现（权限检查、渠道配置、计划策略）
4. 数据层迁移策略（Room 迁移 vs 破坏性迁移一致性）
5. 测试覆盖与CI（仓库/UseCase/VM/导航的系统化测试）

### 技术挑战总结

1. **依赖管理复杂性**: 现代化依赖之间存在复杂关系
2. **API兼容性**: 新特性与现有代码的兼容问题
3. **渐进式实现**: 需要采用更稳妥的渐进式方法

### 成功经验

1. **Emoji替代Material Icons**: 解决了图标兼容性问题
2. **模块化重构**: 提高了代码可维护性
3. **Clean Architecture**: 架构设计经受了开发过程的考验
4. **国际化支持**: 语言切换功能运行良好

---

## 🔧 开发环境要求

- **Android Studio**: 最新版本
- **Kotlin**: 2.2.0
- **Android SDK**: API 26+ (Android 8.0)
- **Target SDK**: API 36 (Android 14)
- **Gradle**: 8.14
- **设备**: 支持 Android 8.0 及以上版本

---

## 🎉 当前实现状态总结

### ✅ 已完成的核心功能

#### 1. 完整的Clean Architecture架构

- **数据层**: Room数据库、DataStore偏好设置、Repository实现
- **领域层**: 领域模型（支持Kotlinx Serialization）、UseCase、Repository接口
- **表现层**: ViewModel、Composable UI、Navigation
- **依赖注入**: Hilt模块配置、依赖管理

#### 2. 多语言国际化支持

- **支持语言**: 英文、简体中文、繁体中文、日文、韩文
- **完整翻译**: 100+ 字符串资源，覆盖所有功能
- **动态切换**: 语言设置功能完美运行（立即生效）
- **系统集成**: 支持Android 13+系统语言设置

#### 3. 情绪记录功能

- **情绪选择**: 基于Emoji的预定义情绪类型
- **自定义情绪**: 支持Emoji选择和描述字段
- **强度设置**: 1-5级滑动条
- **记录保存**: 完整的保存流程和表单验证
- **数据持久化**: Room数据库存储

#### 4. 统计功能

- **数据查询**: 按时间范围查询记录
- **统计计算**: 平均强度、最频繁情绪、情绪分布等
- **图表框架**: 简化版图表实现（待升级为Vico Charts）
- **筛选功能**: 时间范围和图表类型选择

#### 5. 设置功能

- **语言设置**: 5种语言动态切换
- **主题设置**: 浅色、深色、跟随系统（立即生效）
- **自定义情绪管理**: Emoji选择器、CRUD操作
- **模块化设计**: 组件拆分，代码可维护性高

#### 6. 现代化特性

- **Material 3设计**: 完整的设计令牌系统
- **Kotlinx Serialization**: 所有领域模型支持
- **Security Crypto**: 依赖已添加（待实现）
- **Edge-to-Edge**: 基础配置完成
- **预测性返回**: 基础配置完成

### 🚀 应用状态

- **编译状态**: ✅ 成功
- **安装状态**: ✅ 成功
- **基础功能**: ✅ 完全可用
- **架构完整性**: ✅ Clean Architecture完整实现
- **国际化**: ✅ 5种语言完整支持
- **情绪记录**: ✅ 预定义和自定义情绪都能正常记录
- **语言切换**: ✅ 立即生效
- **主题切换**: ✅ 立即生效
- **图表显示**: ⚠️ 简化版本（待升级）

---

*最后更新: 2025年08月*  
*版本: 3.1*  
*基于真实代码状态完全重写*