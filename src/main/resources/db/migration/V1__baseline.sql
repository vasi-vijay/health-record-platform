-- Week 1 baseline: identity tables only. Records (week 2), consent (week 3) and audit/outbox (week 4)
-- arrive in later migrations. Every person is keyed by an internal UUID.

CREATE TABLE patient (
    id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_subject VARCHAR(64)  UNIQUE,
    health_id_ref    VARCHAR(64)  UNIQUE,
    display_name     VARCHAR(200) NOT NULL,
    birth_date       DATE,
    gender           VARCHAR(16),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

COMMENT ON COLUMN patient.health_id_ref IS
    'Reference to an external health ID (ABHA-style). Raw Aadhaar numbers are never stored.';

CREATE TABLE practitioner (
    id                  UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_subject    VARCHAR(64)  UNIQUE,
    registration_number VARCHAR(64)  UNIQUE,
    display_name        VARCHAR(200) NOT NULL,
    specialty           VARCHAR(100),
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now()
);
