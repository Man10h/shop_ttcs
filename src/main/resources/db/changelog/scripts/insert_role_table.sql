--liquibase formatted sql
--changelog manh:002

INSERT INTO role(name) VALUES ('CUSTOMER'), ('MANAGER');