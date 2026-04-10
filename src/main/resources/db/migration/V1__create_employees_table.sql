CREATE TABLE IF NOT EXISTS employees (
                                         id              BIGINT          NOT NULL AUTO_INCREMENT,
                                         first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    document_type   VARCHAR(20)     NOT NULL,
    document_number VARCHAR(50)     NOT NULL,
    birth_date      DATE            NOT NULL,
    hiring_date     DATE            NOT NULL,
    position        VARCHAR(100)    NOT NULL,
    salary          DECIMAL(15, 2)  NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT uq_employees_document_number UNIQUE (document_number)
    );