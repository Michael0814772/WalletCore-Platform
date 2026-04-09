# WalletCore Platform Architecture

## Overview

WalletCore is a Maven multi-module monorepo that hosts multiple Spring Boot services plus a shared Java library.

## Repo modules

- **`auth-service`** (`walletcore-auth-service`)
  - Base package: `com.walletcore.auth`
- **`wallet-service`** (`walletcore-wallet-service`)
  - Base package: `com.walletcore.wallet`
- **`transaction-service`** (`walletcore-transaction-service`)
  - Base package: `com.walletcore.transaction`
- **`notification-service`** (`walletcore-notification-service`)
  - Base package: `com.walletcore.notification`
- **`api-gateway`** (`walletcore-api-gateway`)
  - Base package: `com.walletcore.gateway`
- **`common-lib`** (`walletcore-common-lib`)
  - Base package: `com.walletcore.common`
  - Plain Java library (shared models/utilities later)

## Local infrastructure (Docker Compose)

Local orchestration lives in:

- `infrastructure/docker-compose.yml`

It runs:

- **Postgres** (single container, multiple databases)
  - One database per service:
    - `auth_db`, `wallet_db`, `transaction_db`, `notification_db`, `gateway_db`
  - Databases are created via init script:
    - `infrastructure/postgres/init/01-create-databases.sql`
- **Redis** for caching / ephemeral data
- **Kafka** for event-driven messaging (with Zookeeper in this dev setup)

### Service connectivity

Inside Docker:

- Postgres: `postgres:5432`
- Redis: `redis:6379`
- Kafka: `kafka:9092`

From your local machine:

- Postgres: `localhost:5432`
- Redis: `localhost:6379`
- Kafka: `localhost:29092`

### Reliability defaults (dev-friendly)

- **Health checks** are defined for Postgres/Redis/Zookeeper/Kafka to avoid startup race conditions.
- **`depends_on` conditions** are used so services wait for healthy dependencies.
- **Restart policy**: `restart: unless-stopped`
- **Volumes** persist Postgres/Redis data across restarts.

## Next steps (intentionally not implemented yet)

This repo currently contains only **project skeletons** (no controllers/services/business logic yet). Future work typically adds:

- DB migrations per service
- Kafka topics + event contracts
- Service-to-service auth strategy
- Gateway routing and rate limiting
