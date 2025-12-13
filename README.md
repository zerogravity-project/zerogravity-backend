<div align="center">

# 🚀 ZeroGravity Backend

![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=24&pause=1000&color=6366F1&center=true&vCenter=true&width=500&lines=Spring+Boot+REST+API;Emotion+Tracking+Platform;Built+with+Java+17)

**Spring Boot REST API for Emotion Tracking & Personal Wellness Platform**

![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</div>

---

## 📑 Table of Contents

1. [📖 Overview](#-overview)
2. [✨ Key Features](#-key-features)
3. [🛠 Tech Stack](#-tech-stack)
4. [🏗 Architecture](#-architecture)
5. [📂 Project Structure](#-project-structure)
6. [📡 API Endpoints](#-api-endpoints)
7. [🔐 Authentication Flow](#-authentication-flow)
8. [🔧 Technical Challenges & Solutions](#-technical-challenges--solutions)
9. [🚀 Getting Started](#-getting-started)
10. [🗓 Roadmap](#-roadmap)
11. [🔗 Related](#-related)
12. [👤 Author](#-author)

---

## 📖 Overview

ZeroGravity Backend is a Spring Boot REST API that powers the emotion tracking and personal wellness platform. It provides secure authentication, emotion recording, analytics, and AI-powered insights.

> 📌 Part of the ZeroGravity full-stack project. Refactored from an incomplete collaborative Spring Boot project into a production-ready API.
> [Frontend Repository](https://github.com/zerogravity-project/zerogravity-react) | [Original Vue Version](https://github.com/zerogravity-project/zerogravity-frontend/tree/main)

### Why ZeroGravity Backend?

- 🔐 **Secure Authentication** - JWT integration with NextAuth, supporting Google & Kakao OAuth
- 📊 **Analytics Engine** - Timezone-aware emotion statistics and chart data
- 🤖 **AI-Powered Insights** - Google Gemini API for emotion prediction and period analysis
- 🚀 **Zero-Downtime Deploy** - Build-first strategy with automatic rollback

---

## ✨ Key Features

| Feature | Description | Tech |
|---------|-------------|------|
| 🔐 **JWT Authentication** | NextAuth integration with 15-min access / 30-day refresh tokens | jjwt, Spring Security |
| 👤 **User Management** | Profile, consent tracking, GDPR-compliant data deletion | MyBatis, Snowflake ID |
| 📊 **Emotion Analytics** | Daily/Moment records, level/count/reason statistics | MySQL, CONVERT_TZ |
| 🤖 **AI Predictions** | Emotion prediction from diary, period analysis | Google Gemini API |
| 🚀 **Zero-Downtime Deploy** | Build-first strategy with auto-rollback | Docker, GitHub Actions |
| 🔒 **API Security** | Rate limiting, caching, security headers | Nginx, Spring Security |

---

## 🛠 Tech Stack

| Category | Technologies |
|:--------:|:-------------|
| **Framework** | Spring Boot 3.2.5, Java 17 |
| **Database** | MySQL 8.0, MyBatis 3.0.3 |
| **Authentication** | JWT (jjwt 0.12.5), Spring Security, NextAuth Integration |
| **AI** | Google Gemini API |
| **Infrastructure** | Docker, Docker Compose, Nginx |
| **Cloud** | OCI (Ampere A1 ARM64, Flexible Load Balancer, Object Storage) |
| **IaC** | Terraform (Networking, Compute, LB, Monitoring modules) |
| **CI/CD** | GitHub Actions (Zero-Downtime, Auto-Rollback) |
| **DNS/SSL** | AWS Route53, Let's Encrypt (ACME, TLS 1.3) |
| **Monitoring** | OCI Monitoring (CPU/Memory/Container Health Alarms, Email Alerts) |
| **Documentation** | SpringDoc OpenAPI (Swagger) |

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                            OCI Cloud (Terraform-Managed)                                 │
│                                                                                          │
│  ┌────────────────────────────────────────────────────────────────────────────────────┐ │
│  │                           VCN (Virtual Cloud Network)                               │ │
│  │  ┌──────────────────────────────────────────────────────────────────────────────┐  │ │
│  │  │                              Public Subnet                                    │  │ │
│  │  │                                                                               │  │ │
│  │  │  ┌───────────────────┐       ┌─────────────────────────────────────────────┐ │  │ │
│  │  │  │   Load Balancer   │       │          Compute (Ampere A1 - ARM64)        │ │  │ │
│  │  │  │    (Flexible)     │       │                                             │ │  │ │
│  │  │  │                   │       │  ┌───────────────────────────────────────┐  │ │  │ │
│  │  │  │  - TLS 1.3        │──────▶│  │              Nginx                    │  │ │  │ │
│  │  │  │  - Let's Encrypt  │  :80  │  │         (Reverse Proxy)               │  │ │  │ │
│  │  │  │  - Health Check   │       │  │    Rate Limit: 500 req/min            │  │ │  │ │
│  │  │  └───────────────────┘       │  └─────────────────┬─────────────────────┘  │ │  │ │
│  │  │                              │                    │                        │ │  │ │
│  │  │                              │      ┌─────────────┴─────────────┐          │ │  │ │
│  │  │                              │      │                           │          │ │  │ │
│  │  │                              │      ▼                           ▼          │ │  │ │
│  │  │                              │  zerogv.com                 api.zerogv.com  │ │  │ │
│  │  │                              │  dev.zerogv.com         api-dev.zerogv.com  │ │  │ │
│  │  │                              │      │                           │          │ │  │ │
│  │  │                              │      ▼                           ▼          │ │  │ │
│  │  │                              │  ┌─────────────────────────────────────┐    │ │  │ │
│  │  │                              │  │           Docker Containers         │    │ │  │ │
│  │  │                              │  │                                     │    │ │  │ │
│  │  │                              │  │  ┌─────────────┐  ┌──────────────┐  │    │ │  │ │
│  │  │                              │  │  │  Frontend   │  │   Backend    │  │    │ │  │ │
│  │  │                              │  │  │  (Docker)   │  │   (Docker)   │  │    │ │  │ │
│  │  │                              │  │  │             │  │              │  │    │ │  │ │
│  │  │                              │  │  │  Next.js 15 │  │ Spring Boot  │  │    │ │  │ │
│  │  │                              │  │  │  standalone │  │    3.2.5     │  │    │ │  │ │
│  │  │                              │  │  │             │  │              │  │    │ │  │ │
│  │  │                              │  │  │ prod :3000  │  │  prod :8080  │  │    │ │  │ │
│  │  │                              │  │  │ dev  :3001  │  │  dev  :8081  │  │    │ │  │ │
│  │  │                              │  │  └─────────────┘  └──────┬───────┘  │    │ │  │ │
│  │  │                              │  │                          │          │    │ │  │ │
│  │  │                              │  │                   ┌──────┴───────┐  │    │ │  │ │
│  │  │                              │  │                   │    MySQL     │  │    │ │  │ │
│  │  │                              │  │                   │   (Docker)   │  │    │ │  │ │
│  │  │                              │  │                   │              │  │    │ │  │ │
│  │  │                              │  │                   │ prod :3306   │  │    │ │  │ │
│  │  │                              │  │                   │ dev  :3307   │  │    │ │  │ │
│  │  │                              │  │                   └──────────────┘  │    │ │  │ │
│  │  │                              │  └─────────────────────────────────────┘    │ │  │ │
│  │  │                              │                                             │ │  │ │
│  │  │                              └─────────────────────────────────────────────┘ │  │ │
│  │  └──────────────────────────────────────────────────────────────────────────────┘  │ │
│  └────────────────────────────────────────────────────────────────────────────────────┘ │
│                                                                                          │
│  ┌──────────────────┐  ┌────────────────────────────────────────────────────────────┐   │
│  │  Object Storage  │  │                    OCI Monitoring                          │   │
│  │  (Static Files)  │  │  CPU/Memory Alarms (>80%) · Container Health · Email Alert │   │
│  └──────────────────┘  └────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────────────┘

External Services:
├── AWS Route53 (DNS: zerogv.com, api.zerogv.com, dev.zerogv.com, api-dev.zerogv.com)
├── Google Gemini API (AI emotion prediction & period analysis)
└── OAuth Providers: Google, Kakao

CI/CD: GitHub Actions → SSH → Docker Build → Zero-Downtime Deploy (+ Auto-Rollback)
```

---

## 📁 Project Structure

```
zerogravity/src/main/java/com/zerogravity/myapp/
├── common/               # Shared infrastructure
│   ├── config/           # DB, Swagger, Web, Jackson configs
│   ├── security/         # JWT, @AuthUserId annotation
│   ├── exception/        # Global exception handler
│   └── util/             # TimezoneUtil
│
├── auth/                 # Authentication domain
│   ├── controller/       # OAuth2 endpoints
│   └── dto/              # AuthResponse
│
├── user/                 # User management domain
│   ├── controller/       # User profile, consent
│   ├── service/          # UserService
│   └── dao/              # MyBatis mapper
│
├── emotion/              # Core emotion tracking domain
│   ├── controller/       # Emotion records CRUD
│   ├── service/          # EmotionService, EmotionRecordService
│   └── dao/              # MyBatis mappers
│
├── chart/                # Analytics domain
│   ├── controller/       # Statistics endpoints
│   └── service/          # ChartService
│
└── ai/                   # AI features domain
    ├── controller/       # AI prediction endpoints
    └── service/          # Gemini API integration
```

### Why Domain-Driven Architecture?

**The Challenge**: The original Spring Boot project had a traditional layered architecture with all controllers, services, and DAOs grouped by technical concern. This made it difficult to understand the business logic and maintain feature boundaries.

**The Refactoring**: Instead of a complete rewrite, I analyzed the existing codebase, preserved the working layer structure, and reorganized code by business domain while adding new features like NextAuth integration and AI capabilities.

**The Result**:
- **`common/`**: Shared infrastructure (security, config, exceptions, utilities)
- **`auth/`**: Authentication & JWT token management
- **`user/`**: User profile and consent management
- **`emotion/`**: Core emotion tracking (records, emotions)
- **`chart/`**: Analytics and statistics
- **`ai/`**: Gemini-powered predictions and analysis

This organization allows each domain to evolve independently while sharing common infrastructure.

---

## 🔗 API Endpoints

Base URL: `https://api.zerogv.com`

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/verify` | Verify OAuth token and issue JWT |
| POST | `/auth/refresh` | Refresh access token |
| DELETE | `/auth/logout` | Logout and invalidate tokens |

### User

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/user/me` | Get current user profile |
| DELETE | `/user/me` | Delete account |
| POST | `/user/consent` | Update consent status |

### Emotion Records

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/emotions/records` | Get emotion records (with filters) |
| POST | `/emotions/records` | Create new emotion record |
| PUT | `/emotions/records/{id}` | Update emotion record |
| DELETE | `/emotions/records/{id}` | Delete emotion record |

### Charts & Analytics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/chart/level` | Get emotion level statistics |
| GET | `/chart/count` | Get emotion count by type |
| GET | `/chart/reason` | Get emotion reasons statistics |

### AI Features

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/ai/emotion-predictions` | Predict emotion from diary |
| GET | `/ai/period-analyses` | Get AI analysis for period |

---

## 🔐 Authentication Flow

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Client  │───▶│ NextAuth │───▶│  Backend │───▶│  MySQL   │
│(Frontend)│    │ (OAuth)  │    │  (JWT)   │    │  (User)  │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
     │               │               │               │
     │  1. OAuth     │               │               │
     │──────────────▶│               │               │
     │               │  2. Verify    │               │
     │               │──────────────▶│               │
     │               │               │  3. Upsert    │
     │               │               │──────────────▶│
     │               │               │  4. User      │
     │               │               │◀──────────────│
     │               │  5. JWT       │               │
     │               │◀──────────────│               │
     │  6. Session   │               │               │
     │◀──────────────│               │               │
```

---

## 🔧 Technical Challenges & Solutions

### 1. AI Token Optimization with Statistical Sampling

**Problem**: Sending all emotion records to Gemini API causes token overflow and high costs (Year period = 365+ records)

**Solution**:
- **Statistical Representative Sampling**: Select best-matching record per time bucket
  - Year: 365 → 12 records (1 per month)
  - Month: ~31 → 4 records (1 per week)
  - Week: 7 → 7 records (1 per day)
- **Smart Matching Algorithm**: 60% emotion level + 40% reason matching
- **Daily 1.5x Weighting**: Daily records weighted higher (more representative than moment)
- **Tie-breaking**: score → diary length → reason count → recency
- **Prompt Design**: JSON-only response, emotion level mapping (0-6), predefined reasons

**Outcome**: 97% token reduction for year period (365→12), accurate AI analysis maintained

```java
// Select best matching record per bucket using weighted scoring
private double calculateMatchScore(EmotionRecord record, Double targetLevel, String topReason) {
    // Daily records weighted 1.5x (more representative)
    double recordLevel = record.getEmotionId() *
        (record.getEmotionRecordType() == EmotionRecord.Type.DAILY ? 1.5 : 1.0);
    double levelScore = 1.0 - (Math.abs(recordLevel - targetLevel) / 9.0);

    // Reason matching
    double reasonScore = record.getEmotionReasons().contains(topReason) ? 1.0 : 0.0;

    // 60% level, 40% reason
    return (levelScore * 0.6) + (reasonScore * 0.4);
}
```

### 2. Timezone-Aware Data Handling

**Problem**: Chart grouping showed incorrect data due to server timezone (Asia/Seoul) vs user timezone mismatch

**Why This Architecture**:
- **Global User Support**: Users can access the app from anywhere (Korea → US travel scenario)
- **UTC Storage**: MySQL stores all timestamps in UTC for consistency
- **Automatic Detection**: Browser detects timezone via `Intl.DateTimeFormat().resolvedOptions().timeZone`
- **No User Selection**: Timezone changes automatically when user travels to a different timezone

**Solution**:
- **X-Timezone Header**: Frontend sends browser-detected timezone (e.g., `Asia/Seoul`, `America/New_York`)
- **SQL-level CONVERT_TZ**: For grouped data (charts) - converts UTC to user timezone in query
- **Java-level Conversion**: For raw timestamps - avoids JDBC double conversion issue

```java
// Frontend axios interceptor - automatic browser timezone detection
const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
config.headers['X-Timezone'] = timezone;
```

```sql
-- Chart grouping with user timezone (SQL-level conversion)
SELECT DATE_FORMAT(
  CONVERT_TZ(created_time, '+00:00', #{timezoneOffset}),
  '%Y-%m-%d'
) as label
FROM emotion_records
```

**Lessons Learned** (PR #62):
- **JDBC Double Conversion Issue**: When JDBC driver reads MySQL timestamp, it automatically converts to JVM timezone
- **Problem Scenario**: UTC in DB → JDBC converts to Asia/Seoul → Java CONVERT_TZ applies again = wrong time
- **Solution**: Use SQL `CONVERT_TZ` only for grouped/aggregated data, use Java `ZonedDateTime` conversion for raw timestamps

```java
// Raw timestamp conversion (Java-level to avoid JDBC double conversion)
ZonedDateTime userTime = utcTime
    .atZone(ZoneId.of("UTC"))
    .withZoneSameInstant(ZoneId.of(userTimezone));
```

**Outcome**: Correct chart grouping for users in any timezone, automatic adaptation when traveling

### 3. NextAuth OAuth Integration with JWT

**Problem**: Integrate NextAuth frontend with Spring Boot backend, supporting multiple OAuth providers

**Solution**:
- Provider-based user lookup (providerId + provider combination)
- Snowflake ID generation for distributed unique user IDs
- JWT tokens with 15-min access / 30-day refresh lifecycle
- Custom @AuthUserId annotation with ArgumentResolver

**Outcome**: Seamless OAuth login with Google/Kakao, secure JWT-based session

```java
// @AuthUserId annotation extracts user ID from JWT
@GetMapping("/me")
public ResponseEntity<UserResponse> getMe(@AuthUserId Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
}
```

### 4. Zero-Downtime Deployment Strategy

**Problem**: 502 errors during deployments when new container failed to start

**Solution**:
- Build-first strategy: Build new image while old container runs
- Image-based backup and instant rollback
- 150-second health check (30 attempts × 5 seconds)
- Auto-rollback on health check failure

**Outcome**: Old container keeps running if build fails, instant rollback from backup image

```yaml
# Build new image (old container still running)
docker build -t zerogv-backend:${ENV}-new .

# Backup current image
docker tag zerogv-backend:${ENV} zerogv-backend:${ENV}-backup

# Swap only after successful build
docker compose down && docker compose up -d
```

### 5. Refresh Token Security Evolution

**Problem**: Token rotation caused concurrent request failures and false security alerts

**Solution**:
- Initially: Token rotation with reuse detection and 5-second grace period
- Final: Simplified validation (no rotation) for stability
- Hourly cleanup of expired/revoked tokens
- Specific error codes (REFRESH_TOKEN_EXPIRED, REFRESH_TOKEN_INVALID)

**Outcome**: Stable token refresh without concurrent request errors

### 6. MyBatis Enum Type Handling

**Problem**: Database stores lowercase ('daily', 'moment') but Java uses uppercase (DAILY, MOMENT)

**Solution**: Custom TypeHandler extending BaseTypeHandler for case-insensitive conversion

**Outcome**: Seamless enum mapping between DB and Java layers

```java
public class EmotionRecordTypeHandler extends BaseTypeHandler<EmotionRecord.Type> {
    @Override
    public EmotionRecord.Type getNullableResult(ResultSet rs, String col) {
        String value = rs.getString(col);
        return value != null
            ? EmotionRecord.Type.valueOf(value.toUpperCase())
            : null;
    }
}
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- MySQL 8.0

### Installation

```bash
# Clone the repository
git clone https://github.com/zerogravity-project/zerogravity-backend.git
cd zerogravity-backend

# Set up environment variables
cp .env.example .env
```

### Development

```bash
# Local development
cd zerogravity
./mvnw spring-boot:run

# Docker deployment
docker-compose up -d

# API Documentation
open http://localhost:8080/swagger-ui.html
```

### Environment Variables

```env
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/zerogravity
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password

# JWT
SPRING_JWT_SECRET=your-256-bit-secret

# AI
GEMINI_API_KEY=your-gemini-api-key
```

---

## 🗓 Roadmap

- [ ] Advanced AI insights with trend prediction
- [ ] WebSocket for real-time sync
- [ ] Rate limiting per user (not just IP)
- [ ] Prometheus/Grafana monitoring
- [ ] Multi-language support (i18n)

---

## 🔗 Related

- [Frontend (Next.js)](https://github.com/zerogravity-project/zerogravity-react)
- [Original Vue Version](https://github.com/zerogravity-project/zerogravity-frontend/tree/main)

## 👤 Author

**Minuk Hwang** - Fullstack Developer

---

<div align="center">

Made with ❤️ and ☕

</div>
