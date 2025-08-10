# Spring Security + JWT 综合认证授权项目

本项目是一个基于 Spring Boot、Spring Security 和 JSON Web Token (JWT) 构建的现代化认证授权系统后端服务的完整示例。它演示了如何实现一个安全、健壮且可扩展的，基于角色和权限的访问控制（RBAC）体系。

## ✨ 项目特色

- **现代化的技术栈**：采用 Spring Boot 3, Spring Security 6, MyBatis-Plus 等主流框架。
- **无状态化认证**：使用 JWT 实现无状态（Stateless）认证，完美契合前后端分离和微服务架构。
- **灵活的权限控制**：同时支持基于角色（`@PreAuthorize("hasRole('ADMIN')")`）和基于细粒度权限（`@PreAuthorize("hasAuthority('sys:user:list')")`）的声明式权限控制。
- **清晰的职责分离**：代码结构清晰，遵循最佳实践，将安全配置、JWT工具、过滤器、业务逻辑等清晰分离。
- **统一的异常处理**：全局捕获认证和授权异常，向前端返回格式统一的 JSON 响应，提升用户体验。
- **数据库驱动**：所有用户、角色、权限数据均通过数据库进行持久化管理。
- **一键测试数据初始化**：提供了一个便捷的 API 接口，可一键生成完整的测试数据，方便快速验证。

## 🛠️ 技术栈

- **核心框架**: Spring Boot 3.x
- **安全框架**: Spring Security 6.x
- **Token方案**: JSON Web Token (jjwt-api)
- **持久层框架**: MyBatis-Plus
- **数据库连接池**: Druid
- **数据库**: MySQL 8.x
- **辅助工具**: Lombok

## 🚀 快速开始

### 1. 环境准备

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本

### 2. 数据库配置

1.  在您的 MySQL 中创建一个新的数据库，例如 `security`。
2.  打开项目中的 `src/main/resources/application.yml` 文件。
3.  根据您的数据库环境，修改 `spring.datasource` 下的 `url`, `username`, 和 `password`。

    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/security?useUnicode=true...
        username: your_username
        password: your_password
    ```

### 3. 项目启动

1.  克隆或下载本项目到本地。
2.  使用 IDE (如 IntelliJ IDEA) 打开项目，等待 Maven 自动下载依赖。
3.  找到 `org.example.ApplicationMain` 类，直接运行其 `main` 方法。
4.  当控制台输出 Spring Boot 的启动日志时，表示后端服务已成功启动，默认端口为 `8080`。

## 📋 API 端点说明

### 认证接口 (`/api/auth`)

#### 1. 用户注册

- **URL**: `POST /api/auth/register`
- **请求体 (Body)**:
  ```json
  {
    "username": "newuser",
    "password": "password123"
  }
  ```
- **成功响应**: `HTTP 200 OK`，响应体为字符串 "用户注册成功！"。
- **失败响应**: `HTTP 400 Bad Request`，例如当用户名已存在时。

#### 2. 用户登录

- **URL**: `POST /api/auth/login`
- **请求体 (Body)**:
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```
- **成功响应**: `HTTP 200 OK`，响应体为一个包含 JWT 的 JSON 对象。
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```
- **失败响应**: `HTTP 401 Unauthorized`，当用户名或密码错误时，返回统一的错误JSON。

### 辅助测试接口 (`/api/setup`)

#### 1. 一键初始化测试数据

- **URL**: `POST /api/setup/init`
- **说明**: 调用此接口会自动在数据库中创建一套完整的测试数据，包括 'admin' 和 'user' 两个用户，以及相关的角色和权限。此操作是幂等的，可重复调用。
- **成功响应**: `HTTP 200 OK`，响应体为字符串 "测试数据初始化成功！..."。

### 权限测试接口

所有测试接口都需要在请求头中携带有效的 JWT：`Authorization: Bearer <your_token>`

#### 1. 基于角色的访问控制 (`/api/test/role`)

- `GET /api/test/role/admin`: 仅限拥有 `ROLE_ADMIN` 角色的用户访问。
- `GET /api/test/role/user`: 拥有 `ROLE_ADMIN` 或 `ROLE_USER` 角色的用户均可访问。
- `GET /api/test/role/hello`: 任何已登录的用户均可访问。

#### 2. 基于权限的访问控制 (`/api/test/permission`)

- `GET /api/test/permission/user/list`: 仅限拥有 `sys:user:list` 权限的用户访问。
- `GET /api/test/permission/role/add`: 仅限拥有 `sys:role:add` 权限的用户访问。
- `GET /api/test/permission/admin/manage`: 仅限拥有 `sys:admin:manage` 权限的用户访问。

## 🔒 安全机制详解

### 认证流程

1.  客户端发送用户名和密码到 `/api/auth/login`。
2.  `AuthenticationController` 调用 `AuthenticationManager` 进行认证。
3.  `UserDetailsServiceImpl` 根据用户名从数据库加载用户信息（包括加密的密码和权限列表）。
4.  `AuthenticationManager` 比较提交的密码和数据库中的密码是否匹配。
5.  认证成功后，`JwtUtil` 生成一个包含用户名的 JWT。
6.  服务器将此 JWT 返回给客户端。

### 授权流程

1.  客户端在后续请求的 `Authorization` 请求头中携带 JWT。
2.  `JwtAuthenticationTokenFilter` 拦截请求，验证 JWT 的签名和有效期。
3.  验证通过后，从 JWT 中解析出用户名，并再次调用 `UserDetailsServiceImpl` 从数据库加载用户的权限信息。
4.  过滤器将用户信息和权限封装成一个 `Authentication` 对象，并存入 `SecurityContextHolder`。
5.  当请求到达受 `@PreAuthorize` 保护的方法时，Spring Security 会检查 `SecurityContextHolder` 中的权限信息是否满足注解的要求。
6.  如果权限不足，`AccessDeniedHandlerImpl` 会捕获异常，并返回 `403 Forbidden` 的 JSON 响应。

## 📖 数据库设计

本项目采用经典的 RBAC（基于角色的访问控制）模型设计，包含五张核心表：

- `t_user`: 用户表
- `t_role`: 角色表
- `t_permission`: 权限表
- `t_user_role`: 用户-角色关联表 (多对多)
- `t_role_permission`: 角色-权限关联表 (多对多)

详细的 `CREATE TABLE` 语句请参考项目初期的 SQL 文件。 