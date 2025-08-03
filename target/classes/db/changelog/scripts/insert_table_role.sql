--liquibase formatted sql
--changeset manh:1313

INSERT INTO role(name) VALUES ('CUSTOMER'), ('MANAGER');