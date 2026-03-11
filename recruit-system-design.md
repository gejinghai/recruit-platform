# 临时工招聘平台 - 系统设计方案

## 一、项目概述

### 1.1 项目背景

微信朋友圈发布招聘信息容易被封号，个人小程序招聘类目审核严格（需要人力资源服务许可证）。本项目旨在开发一个H5招聘展示平台，后期可无缝迁移至微信小程序。

### 1.2 技术选型

| 层级 | 技术栈 | 说明 |
|-----|-------|------|
| 前端 | uni-app | 一套代码同时支持H5和微信小程序 |
| 后端 | Spring Boot | Java生态最成熟的Web框架 |
| 持久层 | MyBatis | 轻量级ORM框架 |
| 数据库 | MySQL | 关系型数据库 |

### 1.3 系统目标

- 提供招聘信息展示功能
- 预留广告位支持后续变现
- 支持H5/小程序多端部署

---

## 二、系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        用户端（uni-app）                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │   H5页面    │  │ 微信小程序  │  │   App端     │  ← 同一套代码
│  └─────────────┘  └─────────────┘  └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Spring Boot 后端                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ 招聘管理  │  │ 用户管理  │  │ 广告管理  │  │ 数据统计  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    MySQL + MyBatis                          │
└─────────────────────────────────────────────────────────────┘
```

---

## 三、数据库设计

### 3.1 招聘信息表 (job_info)

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | bigint | 主键，自增 |
| title | varchar(100) | 招聘标题，如"石岩电子厂日结" |
| company_name | varchar(100) | 企业名称 |
| salary | varchar(50) | 薪资，如"20元/小时" |
| work_type | varchar(50) | 班次（长白班/夜班） |
| work_time | varchar(50) | 工作时间 |
| welfare | text | 福利待遇，JSON数组格式 |
| description | text | 企业介绍 |
| requirements | text | 招聘要求 |
| collection_time | varchar(50) | 集合时间 |
| address | varchar(200) | 面试地址 |
| status | tinyint | 状态：0-下架 1-上架 |
| view_count | int | 浏览量，默认0 |
| is_top | tinyint | 是否置顶：0-否 1-是 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

```sql
CREATE TABLE `job_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL COMMENT '招聘标题',
  `company_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `salary` varchar(50) DEFAULT NULL COMMENT '薪资',
  `work_type` varchar(50) DEFAULT NULL COMMENT '班次',
  `work_time` varchar(50) DEFAULT NULL COMMENT '工作时间',
  `welfare` text COMMENT '福利待遇(JSON)',
  `description` text COMMENT '企业介绍',
  `requirements` text COMMENT '招聘要求',
  `collection_time` varchar(50) DEFAULT NULL COMMENT '集合时间',
  `address` varchar(200) DEFAULT NULL COMMENT '面试地址',
  `status` tinyint DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `is_top` tinyint DEFAULT 0 COMMENT '是否置顶',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘信息表';
```

### 3.2 管理员表 (admin)

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | bigint | 主键 |
| username | varchar(50) | 用户名，唯一 |
| password | varchar(100) | 密码（BCrypt加密） |
| role | varchar(20) | 角色：admin-超级管理员 editor-编辑 |
| create_time | datetime | 创建时间 |

```sql
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(20) DEFAULT 'editor' COMMENT '角色',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
```

### 3.3 广告位表 (ad_position)

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | bigint | 主键 |
| position_key | varchar(50) | 位置标识，唯一 |
| position_name | varchar(100) | 位置名称 |
| width | int | 建议宽度 |
| height | int | 建议高度 |
| status | tinyint | 状态：0-禁用 1-启用 |

```sql
CREATE TABLE `ad_position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_key` varchar(50) NOT NULL COMMENT '位置标识',
  `position_name` varchar(100) NOT NULL COMMENT '位置名称',
  `width` int DEFAULT 750 COMMENT '建议宽度',
  `height` int DEFAULT 300 COMMENT '建议高度',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_position_key` (`position_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告位表';
```

### 3.4 广告内容表 (ad_content)

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | bigint | 主键 |
| position_id | bigint | 广告位ID，关联ad_position |
| title | varchar(100) | 广告标题 |
| image_url | varchar(200) | 图片地址 |
| link_url | varchar(200) | 跳转链接 |
| sort_order | int | 排序，数字越大越靠前 |
| status | tinyint | 状态：0-禁用 1-启用 |
| start_time | datetime | 开始展示时间 |
| end_time | datetime | 结束展示时间 |
| create_time | datetime | 创建时间 |

```sql
CREATE TABLE `ad_content` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_id` bigint NOT NULL COMMENT '广告位ID',
  `title` varchar(100) DEFAULT NULL COMMENT '广告标题',
  `image_url` varchar(200) NOT NULL COMMENT '图片地址',
  `link_url` varchar(200) DEFAULT NULL COMMENT '跳转链接',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告内容表';
```

---

## 四、广告位设计

### 4.1 广告位列表

| 广告位Key | 位置 | 建议尺寸 | 说明 |
|----------|------|----------|------|
| home_banner | 首页顶部轮播 | 750x300 | 招聘企业展示/合作伙伴 |
| home_middle | 首页中部横幅 | 750x200 | 品牌推广 |
| job_list_ad | 职位列表信息流 | 750x150 | 穿插在职位列表中 |
| job_detail_bottom | 详情页底部 | 750x200 | 详情页广告 |

### 4.2 初始化广告位SQL

```sql
INSERT INTO `ad_position` (`position_key`, `position_name`, `width`, `height`) VALUES
('home_banner', '首页轮播', 750, 300),
('home_middle', '首页中部', 750, 200),
('job_list_ad', '列表信息流', 750, 150),
('job_detail_bottom', '详情底部', 750, 200);
```

---

## 五、后端目录结构

```
recruit-admin/
├── src/main/java/com/recruit/
│   ├── controller/
│   │   ├── JobController.java        # 招聘接口
│   │   ├── AdController.java         # 广告接口
│   │   ├── AdminController.java      # 后台管理接口
│   │   └── HomeController.java       # 首页接口
│   ├── service/
│   │   ├── JobService.java
│   │   ├── JobServiceImpl.java
│   │   ├── AdService.java
│   │   └── AdServiceImpl.java
│   ├── mapper/
│   │   ├── JobMapper.java
│   │   ├── AdMapper.java
│   │   └── AdminMapper.java
│   ├── entity/
│   │   ├── JobInfo.java
│   │   ├── AdPosition.java
│   │   └── AdContent.java
│   ├── dto/
│   │   ├── JobQueryDTO.java
│   │   ├── JobDetailDTO.java
│   │   └── Result.java
│   ├── config/
│   │   ├── CorsConfig.java           # 跨域配置
│   │   └── MyBatisConfig.java        # MyBatis配置
│   └── RecruitApplication.java
├── src/main/resources/
│   ├── mapper/
│   │   ├── JobMapper.xml
│   │   └── AdMapper.xml
│   └── application.yml
└── pom.xml
```

---

## 六、前端目录结构 (uni-app)

```
recruit-h5/
├── pages/
│   ├── index/
│   │   └── index.vue          # 首页（职位列表+广告位）
│   ├── job/
│   │   ├── list.vue          # 职位列表
│   │   └── detail.vue        # 职位详情
│   ├── about/
│   │   └── about.vue         # 关于/联系我们
│   └── admin/                       # 后台管理（独立模块）
│       ├── login.vue
│       ├── job/
│       │   ├── list.vue
│       │   └── edit.vue
│       └── ad/
│           ├── list.vue
│           └── edit.vue
├── components/
│   ├── JobCard.vue           # 职位卡片组件
│   ├── AdBanner.vue          # 广告横幅组件
│   └── TabBar.vue            # 底部导航组件
├── static/                          # 静态资源
├── App.vue
├── main.js
├── pages.json                 # uni-app路由配置
└── manifest.json              # 小程序配置
```

---

## 七、页面设计

### 7.1 首页 (index)

```
┌─────────────────────────────┐
│  [广告位：轮播图]              │ ← home_banner
├─────────────────────────────┤
│  🔍 搜索职位 / 筛选            │
├─────────────────────────────┤
│  [推荐职位]                   │
│  ┌─────────────────────────┐│
│  │ 石岩电子厂日结            ││
│  │ 20元/小时 | 长白班       ││
│  │ 150元/天 ▼日结           ││
│  └─────────────────────────┘│
│  ┌─────────────────────────┐│
│  │ 宝安电子招普工            ││
│  │ 22元/小时 | 夜班         ││
│  └─────────────────────────┘│
│  [广告位：信息流]              │ ← job_list_ad
│  ┌─────────────────────────┐│
│  │ 某公司招聘...             ││
│  └─────────────────────────┘│
├─────────────────────────────┤
│  [底部导航]                   │
│  首页  │  消息   │  我的     │
└─────────────────────────────┘
```

### 7.2 详情页 (job/detail)

```
┌─────────────────────────────┐
│  ← 返回                     │
│  石岩电子厂日结              │
│  20元/小时  长白班          │
│  ━━━━━━━━━━  150元/天 ▼日结 │
├─────────────────────────────┤
│  【企业简介】                 │
│  主要生产电子小线材...       │
│                             │
│  【招聘要求】                 │
│  1. 年龄18-50岁...           │
│                             │
│  【薪资待遇】                 │
│  员工纯20元/时...            │
│                             │
├─────────────────────────────┤
│  [广告位：详情底部]           │ ← job_detail_bottom
├─────────────────────────────┤
│  [立即报名]                   │
└─────────────────────────────┘
```

---

## 八、API 接口设计

### 8.1 公开接口（前端调用）

| 接口 | 方法 | 说明 |
|-----|------|------|
| /api/job/list | GET | 职位列表（支持分页、筛选） |
| /api/job/{id} | GET | 职位详情 |
| /api/job/{id}/view | POST | 增加浏览量 |
| /api/home/banner | GET | 首页轮播广告 |
| /api/home/ads | GET | 获取所有广告位内容 |

### 8.2 管理接口（后台调用）

| 接口 | 方法 | 说明 |
|-----|------|------|
| /api/admin/login | POST | 管理员登录 |
| /api/admin/job/list | GET | 管理职位列表 |
| /api/admin/job/add | POST | 新增职位 |
| /api/admin/job/update | POST | 更新职位 |
| /api/admin/job/delete | DELETE | 删除职位 |
| /api/admin/job/top/{id} | POST | 置顶/取消置顶 |
| /api/admin/ad/position/list | GET | 广告位列表 |
| /api/admin/ad/content/list | GET | 广告内容列表 |
| /api/admin/ad/content/save | POST | 保存广告内容 |
| /api/admin/ad/content/delete | DELETE | 删除广告内容 |

### 8.3 响应格式

```json
// 成功
{
  "code": 200,
  "message": "success",
  "data": {}
}

// 失败
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

---

## 九、后续小程序迁移注意事项

| 事项 | 说明 |
|-----|------|
| 资质要求 | 需办理《人力资源服务许可证》 |
| 登录授权 | 小程序使用 wx.login，企业H5使用微信登录 |
| 支付功能 | 如有报名费，需申请微信支付商户号 |
| 广告联盟 | 小程序接入腾讯广告（原广点通），代码几乎不变 |
| 审核类目 | 迁移前确认类目选择正确 |

---

## 十、开发阶段规划

### 第一阶段：H5基础版
- [ ] 招聘职位展示
- [ ] 职位详情页
- [ ] 后台管理（职位CRUD）
- [ ] 广告位预留

### 第二阶段：广告接入
- [ ] 接入腾讯优量汇/穿山甲SDK
- [ ] 广告位数据对接

### 第三阶段：小程序迁移
- [ ] 申请企业资质
- [ ] 小程序代码适配
- [ ] 提交审核上线

---

## 十一、示例数据

### 招聘信息示例

```json
{
  "title": "石岩电子厂日结",
  "company_name": "石岩电子厂",
  "salary": "20元/小时",
  "work_type": "长白班",
  "work_time": "工期最低一个月",
  "welfare": "[\"包住\", \"先吃后扣\", \"三天后日结\", \"可接收大龄工\"]",
  "description": "主要生产电子小线材，普通工衣，长白班，工时稳定，工作简单轻松，品质，贴标，剪线，套线材面试各格当天安排宿舍",
  "requirements": "1.年龄18-50岁，男女不限。\n2.需要本人有效身份证，视力正常，无大面积纹身，包安排",
  "collection_time": "中午13:30前",
  "address": "石岩街道XXX",
  "status": 1,
  "is_top": 1
}
```

---

## 十二、后台管理系统设计

### 12.1 技术选型

| 技术 | 说明 |
|-----|------|
| Vue 3 | 渐进式JavaScript框架 |
| Element Plus | Vue 3 组件库，界面美观 |
| Vite | 构建工具，启动快 |
| Pinia | 状态管理 |
| Axios | HTTP请求 |
| Vue Router | 路由管理 |

### 12.2 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                      后台管理系统 (Vue3)                       │
├─────────────────────────────────────────────────────────────┤
│  ┌───────────┐  ┌───────────┐  ┌───────────┐               │
│  │  登录模块  │  │  招聘管理  │  │  广告管理  │               │
│  └───────────┘  └───────────┘  └───────────┘               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Spring Boot API                         │
└─────────────────────────────────────────────────────────────┘
```

### 12.3 目录结构

```
recruit-admin-web/
├── src/
│   ├── api/                    # API请求
│   │   ├── job.js             # 招聘相关API
│   │   ├── ad.js              # 广告相关API
│   │   └── auth.js            # 登录认证API
│   ├── assets/                # 静态资源
│   │   └── styles/
│   │       └── main.scss
│   ├── components/            # 公共组件
│   │   ├── Header.vue
│   │   ├── Sidebar.vue
│   │   └── Pagination.vue
│   ├── layouts/               # 布局组件
│   │   └── AdminLayout.vue
│   ├── router/                # 路由配置
│   │   └── index.js
│   ├── stores/                # Pinia状态管理
│   │   ├── user.js
│   │   └── app.js
│   ├── views/                 # 页面视图
│   │   ├── login/
│   │   │   └── index.vue
│   │   ├── dashboard/
│   │   │   └── index.vue      # 首页/仪表盘
│   │   ├── job/
│   │   │   ├── list.vue       # 职位列表
│   │   │   └── form.vue       # 职位编辑
│   │   ├── ad/
│   │   │   ├── position.vue  # 广告位管理
│   │   │   ├── content.vue   # 广告内容
│   │   │   └── form.vue       # 广告编辑
│   │   └── system/
│   │       └── password.vue   # 修改密码
│   ├── App.vue
│   └── main.js
├── index.html
├── vite.config.js
└── package.json
```

### 12.4 功能模块

#### 12.4.1 登录模块

| 功能 | 说明 |
|-----|------|
| 用户名/密码登录 | 验证管理员身份 |
| 验证码 | 防止暴力破解（可选） |
| 记住密码 | localStorage存储 |
| 退出登录 | 清除token |

#### 12.4.2 招聘管理

| 功能 | 说明 |
|-----|------|
| 职位列表 | 分页展示、支持搜索/筛选 |
| 新增职位 | 表单录入招聘信息 |
| 编辑职位 | 修改招聘信息 |
| 删除职位 | 逻辑删除 |
| 置顶/取消置顶 | 调整排序 |
| 上架/下架 | 控制展示 |

#### 12.4.3 广告管理

| 功能 | 说明 |
|-----|------|
| 广告位列表 | 查看所有广告位 |
| 广告内容列表 | 分广告位展示 |
| 新增广告 | 上传图片、填写链接 |
| 编辑广告 | 修改广告内容 |
| 删除广告 | 移除广告 |
| 上下架 | 控制展示时间 |

#### 12.4.4 系统管理

| 功能 | 说明 |
|-----|------|
| 修改密码 | 管理员修改密码 |
| 退出登录 | 安全退出 |

### 12.5 页面设计

#### 12.5.1 登录页

```
┌─────────────────────────────────────────┐
│                                         │
│           招聘平台管理系统                │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 用户名                          │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────────────────────────┐   │
│  │ 密码                            │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────────────────────────┐   │
│  │           登 录                 │   │
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

#### 12.5.2 职位列表页

```
┌─────────────────────────────────────────────────────────────┐
│  当前位置: 招聘管理 > 职位列表                                │
├─────────────────────────────────────────────────────────────┤
│  [搜索框: 职位名称/企业名称]  [搜索] [重置]                   │
├─────────────────────────────────────────────────────────────┤
│  [+ 新增职位]                                                │
├─────────────────────────────────────────────────────────────┤
│  ID  │ 职位名称      │ 企业   │ 薪资     │ 状态 │ 操作      │
├──────┼───────────────┼────────┼──────────┼──────┼──────────┤
│  1   │ 石岩电子厂日结│ 石岩电子│ 20元/时  │ 上架 │ [编辑][删除]│
│  2   │ 宝安电子招普工│ 宝安电子│ 22元/时  │ 上架 │ [编辑][删除]│
│  3   │ 龙华临时工    │ xxx    │ 18元/时  │ 下架 │ [编辑][删除]│
├─────────────────────────────────────────────────────────────┤
│  共 3 条  第 1/1 页  [<] [>]                                │
└─────────────────────────────────────────────────────────────┘
```

#### 12.5.3 职位编辑页

```
┌─────────────────────────────────────────────────────────────┐
│  当前位置: 招聘管理 > 职位列表 > 新增/编辑                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  职位信息                                                   │
│  ┌────────────────────┐ ┌────────────────────┐            │
│  │ 职位标题 *         │ │ 企业名称 *         │            │
│  └────────────────────┘ └────────────────────┘            │
│  ┌────────────────────┐ ┌────────────────────┐            │
│  │ 薪资 *             │ │ 班次 *             │            │
│  └────────────────────┘ └────────────────────┘            │
│  ┌────────────────────┐ ┌────────────────────┐            │
│  │ 工作时间           │ │ 集合时间           │            │
│  └────────────────────┘ └────────────────────┘            │
│  ┌────────────────────┐ ┌────────────────────┐            │
│  │ 面试地址           │ │ 状态               │            │
│  └────────────────────┘ └────────────────────┘            │
│                                                             │
│  福利待遇 (多选)                                            │
│  ☑ 包住  ☑ 包吃  ☑ 日结  ☐ 夜班补贴  ☐ 其他                 │
│                                                             │
│  企业简介                                                   │
│  ┌─────────────────────────────────────────────────────┐  │
│  │                                                     │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  招聘要求                                                   │
│  ┌─────────────────────────────────────────────────────┐  │
│  │                                                     │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  [保存] [取消]                                              │
└─────────────────────────────────────────────────────────────┘
```

#### 12.5.4 广告内容页

```
┌─────────────────────────────────────────────────────────────┐
│  当前位置: 广告管理 > 广告内容                                │
├─────────────────────────────────────────────────────────────┤
│  广告位: [全部 ▼]  [+ 新增广告]                              │
├─────────────────────────────────────────────────────────────┤
│  ID  │ 广告位    │ 标题     │ 图片   │ 状态 │ 操作          │
├──────┼───────────┼──────────┼────────┼──────┼──────────────┤
│  1   │ 首页轮播  │ 石岩招聘 │ [预览] │ 启用 │ [编辑][删除]  │
│  2   │ 首页轮播  │ 宝安电子 │ [预览] │ 启用 │ [编辑][删除]  │
│  3   │ 详情底部  │ 某广告   │ [预览] │ 禁用 │ [编辑][删除]  │
├─────────────────────────────────────────────────────────────┤
│  共 3 条  第 1/1 页                                         │
└─────────────────────────────────────────────────────────────┘
```

#### 12.5.5 广告编辑弹窗

```
┌─────────────────────────────────────────┐
│  新增/编辑广告                           │
├─────────────────────────────────────────┤
│  广告位: [选择 ▼]                        │
│  标题:   [____________]                   │
│  图片:   [上传] [预览]                   │
│  跳转链接: [____________]                 │
│  排序:   [____] (数字越大越靠前)          │
│  显示时间: [开始日期] - [结束日期]        │
│  状态:   [○ 启用  ● 禁用]               │
│                                         │
│  [取消] [确定]                           │
└─────────────────────────────────────────┘
```

### 12.6 权限控制

| 角色 | 权限 |
|-----|------|
| admin | 全部权限 |
| editor | 招聘管理、广告管理（不可修改密码） |

---

## 十三、完整项目结构

```
recruit-project/
├── recruit-admin/              # Spring Boot 后端
│   ├── src/
│   └── pom.xml
│
├── recruit-h5/                 # uni-app 用户端
│   ├── pages/
│   ├── components/
│   ├── static/
│   └── manifest.json
│
├── recruit-admin-web/          # Vue3 后台管理系统
│   ├── src/
│   │   ├── api/
│   │   ├── views/
│   │   └── ...
│   ├── index.html
│   └── package.json
│
└── recruit-system-design.md    # 本设计文档
```

---

## 十四、部署方案

### 14.1 开发环境

| 组件 | 配置 |
|-----|------|
| JDK | 1.8+ |
| Node.js | 16+ |
| MySQL | 5.7+ |
| IDE | IDEA / VSCode |

### 14.2 生产环境（推荐配置）

| 组件 | 配置 |
|-----|------|
| 服务器 | 2核4G |
| 域名 | 已备案域名 |
| HTTPS | 必须（微信要求） |
| OSS | 阿里云/腾讯云（图片存储） |
| CDN | 加速静态资源 |

### 14.3 部署目录

```
/var/www/recruit/
├── backend/           # 后端jar包
│   └── recruit.jar
├── frontend/         # H5前端dist
│   └── index.html
├── admin/           # 后台管理
│   └── index.html
└── nginx.conf       # Nginx配置
```

---

*文档更新日期：2026-03-11*
*技术栈：Spring Boot + MyBatis + MySQL + uni-app + Vue3 + Element Plus*
