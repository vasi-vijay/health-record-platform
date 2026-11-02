# health-record-platform

Secure, consent-driven health record platform for managing patient records, medical documents, and authorized healthcare access.

> **Portfolio project on synthetic data.** No real patient data is used, and no security or compliance claim is verified until its test evidence exists. India already runs this model nationally through ABDM; this project shows how to build it properly.

## Status

**Week 1 of 8: setup.** The app starts, connects to Postgres and Kafka, applies the baseline schema, and accepts Keycloak tokens. There is no health data or consent logic yet.

| Week | Milestone | Status |
|---|---|---|
| 1 | Setup: app skeleton, local stack, CI | In progress |
| 2 | FHIR records stored as JSONB, read APIs, Synthea loader | Not started |
| 3 | Consent lifecycle, filter inside SQL, consent test matrix | Not started |
| 4 | Hash-chained audit log, outbox to Kafka, in-app notifications | Not started |
| 5 | Keycloak roles, Thymeleaf + HTMX views | Not started |
| 6 | Doctor write-back, consent expiry, emergency access (stretch) | Not started |
| 7 | AI digitizer for paper prescriptions | Not started |
| 8 | Azure deployment, demo video | Not started |

## Stack

Java 21 · Spring Boot 3 · Spring Modulith · PostgreSQL 16 + Flyway · Kafka (KRaft) · Keycloak · Testcontainers · GitHub Actions

## Run it locally

Prerequisites: JDK 21, Maven 3.9+, Docker.

```bash
# 1. Start Postgres, Kafka and Keycloak, and wait until all three are healthy
docker compose up -d --wait

# 2. Run the app on http://localhost:8080
mvn spring-boot:run

# 3. Check it
curl http://localhost:8080/actuator/health
```

### Call the API as a demo user

```bash
TOKEN=$(curl -s -X POST http://localhost:8180/realms/phr/protocol/openid-connect/token \
  -d grant_type=password -d client_id=phr-web \
  -d username=dr.rao -d password=demo-doctor | jq -r .access_token)

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/me
# {"subject":"…","username":"dr.rao","roles":["DOCTOR"]}
```

| Demo user | Password | Role |
|---|---|---|
| `lakshmi.demo` | `demo-patient` | PATIENT |
| `dr.rao` | `demo-doctor` | DOCTOR |

Keycloak admin console: http://localhost:8180 (admin / admin). These credentials are for local demo use only. The password grant is enabled only to make local testing easy.

## Tests

```bash
mvn verify
```

- `ModularityTests`: fails the build if a module uses another module's internals.
- `ApplicationSmokeTests`: boots the app against real Postgres and Kafka containers (needs Docker).
- `KeycloakRealmRoleConverterTests`, `MeControllerTests`: role mapping and token handling.

## Project layout

```
src/main/java/com/health/phr/
├── identity/      patient and doctor profiles, health-ID mapping
├── records/       FHIR resources stored as JSONB              (week 2)
├── consent/       lifecycle, ConsentPolicy, expiry, emergency  (weeks 3, 6)
├── access/        every record-reading endpoint                (week 3)
├── audit/         hash-chained audit log                       (week 4)
├── events/        outbox, scheduler, Kafka topics              (week 4)
├── notification/  in-app notifications                         (week 4)
├── digitizer/     photo to structured prescription             (week 7)
└── web/           Thymeleaf + HTMX views                       (week 5)
```

Each package is a Spring Modulith module; its `package-info.java` describes what belongs there.

## Week 1 exit evidence

- [ ] `docker compose up -d --wait` starts Postgres, Kafka and Keycloak on a clean machine with every health check passing
- [ ] CI runs build, tests and Snyk on every pull request, and a failure blocks the merge
- [ ] ABDM sandbox access requested (for the Later list)

## CI

`.github/workflows/ci.yml` runs three jobs on every push to `main` and every pull request:

1. **Build and test**: `mvn verify`, including Testcontainers tests.
2. **Local stack starts healthy**: `docker compose up --wait`, then checks the demo realm is served.
3. **Snyk dependency scan**: fails on high or critical issues. Add a `SNYK_TOKEN` repository secret to enable it.

To make a failing check block merges, add a branch protection rule on `main` that requires these checks.
