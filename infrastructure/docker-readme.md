# Docker Infrastructure (WalletCore Platform)

This folder contains everything needed to run WalletCore locally with Docker Compose: Postgres, Redis, Kafka, and all Spring Boot services.

## What runs in `docker-compose.yml`

- **Postgres (`walletcore-postgres`)**
  - **Why**: relational storage for services.
  - **Ports**: `5432:5432` so you can connect from your machine (IDE tools, psql, migrations).
  - **User/password**: `walletcore` / `walletcore` (dev-only defaults).

- **Redis (`walletcore-redis`)**
  - **Why**: caching / ephemeral data (sessions, rate limits, etc.).
  - **Ports**: `6379:6379` for local debugging and inspection.

- **Zookeeper (`walletcore-zookeeper`)**
  - **Why**: required by the chosen Kafka image in this compose setup.

- **Kafka (`walletcore-kafka`)**
  - **Why**: event streaming / async communication between services.
  - **Internal vs external connectivity (important)**:
    - Containers should connect to **`kafka:9092`**
    - Your local machine should connect to **`localhost:29092`**

## Network: service-to-service communication

Compose creates a dedicated network:

- **Network name**: `walletcore-net`
- **Why**: keeps all containers on the same isolated network so they can resolve each other by service name (e.g. `postgres`, `redis`, `kafka`) and communicate reliably.

## Database per service (no shared DB)

Each service gets its own database:

- `auth_db`
- `wallet_db`
- `transaction_db`
- `notification_db`
- `gateway_db`

**Why**: this matches real microservice boundaries and reduces coupling (schema changes and migrations don’t collide across services).

### How the DBs are created

Postgres runs an init script on first startup:

- `infrastructure/postgres/init/01-create-databases.sql`

This is mounted into the container at:

- `/docker-entrypoint-initdb.d`

## Environment variables injected into services

Each Spring Boot service container has environment variables so it knows how to connect:

- **Postgres**
  - `SPRING_DATASOURCE_URL` (service-specific DB name)
  - `SPRING_DATASOURCE_USERNAME=walletcore`
  - `SPRING_DATASOURCE_PASSWORD=walletcore`
- **Redis**
  - `SPRING_REDIS_HOST=redis`
- **Kafka**
  - `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`

**Why**: containers should use Docker DNS names (`postgres`, `redis`, `kafka`) instead of `localhost`.

## Kafka listener configuration (why it looks like that)

Kafka uses these settings:

- `KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092`
- `KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,PLAINTEXT_HOST://0.0.0.0:29092`
- `KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT`
- `KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT`

**Why**:

- **Inside Docker**: other containers must reach Kafka at `kafka:9092`.
- **From your laptop**: tools must reach Kafka at `localhost:29092`.

This avoids the common “clients get metadata with the wrong host” issue.

## Health checks + startup ordering (race condition protection)

Infra services define health checks:

- **Postgres**: `pg_isready -U walletcore`
- **Redis**: `redis-cli ping`
- **Zookeeper**: `ruok` probe
- **Kafka**: `kafka-broker-api-versions`

Service containers use:

- `depends_on: <dependency>: { condition: service_healthy }`

**Why**: prevents the classic startup race where app containers boot before Postgres/Kafka/Redis are actually ready.

## Restart policy

Every container has:

- `restart: unless-stopped`

**Why**: makes local infrastructure more realistic and resilient (containers restart automatically after crashes or reboots unless you explicitly stop them).

## Persistent volumes (data survives restarts)

Named volumes are declared:

- `postgres-data`
- `redis-data`

Mounted as:

- Postgres: `postgres-data:/var/lib/postgresql/data`
- Redis: `redis-data:/data`

**Why**: you don’t lose local data every time you restart compose.

## How to run

From repo root:

```bash
docker compose -f infrastructure/docker-compose.yml up -d --build
```

Stop:

```bash
docker compose -f infrastructure/docker-compose.yml down
```

If you want to also delete persisted data:

```bash
docker compose -f infrastructure/docker-compose.yml down -v
```

## Common troubleshooting

- **“DBs not created”**
  - The init scripts only run on the first startup of a *fresh* Postgres data directory.
  - If you already have `postgres-data`, reset with `down -v` and start again.

- **Kafka client can’t connect from host**
  - Use `localhost:29092` from your machine.
  - Use `kafka:9092` from other containers.

