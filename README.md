## WalletCore

WalletCore is a backend-focused digital wallet platform designed to simulate how modern payment systems handle transactions, security, scalability, and reliability. The system is built using a microservices architecture where each service manages a specific business domain such as authentication, wallet management, transaction processing, and notifications.

The project focuses on solving real backend engineering problems such as preventing double spending, handling concurrent transfers safely, enforcing access control, processing asynchronous events, and maintaining system consistency across services.

The platform demonstrates practical implementation of:

- Role-based authentication and authorization using JWT
- Wallet balance management with concurrency control
- Transaction lifecycle management with idempotency protection
- Event-driven communication using Kafka
- Redis caching for performance improvement
- Rate limiting at the API gateway
- Database locking strategies (optimistic and pessimistic)
- Distributed request tracing with correlation IDs
- Structured logging and centralized error handling
- Dockerized infrastructure for local orchestration

WalletCore is structured as a monorepo containing independent deployable services supported by shared libraries for consistent response handling, logging, security utilities, and event models.

This project was built as a deep dive into backend architecture patterns commonly discussed in Java backend interviews and used in financial systems.

