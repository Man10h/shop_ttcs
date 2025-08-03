--liquibase formatted sql
--changeset manh:39103

ALTER TABLE user CHANGE phoneNumber phone_number VARCHAR(20);
ALTER TABLE user CHANGE roleId role_id BIGINT;