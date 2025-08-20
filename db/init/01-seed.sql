CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    isAdmin BIT(1) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

INSERT INTO users (name, email, password, isAdmin)
VALUES ('admin', 'admin@exemplo.com', '$2a$12$0y0VJqmv7kgtz2iujyTPjuVN4YqGlA4rg4FotfH81HUYBYt3jeJ42', 1)
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO users (name, email, password, isAdmin)
VALUES ('user', 'user@exemplo.com', '$2a$12$r/HQ72gyKyFJUmSPIFReAO56IgpG62zUOoBcd00zIunUVw0XX8cwm', 0)
ON DUPLICATE KEY UPDATE email = email;