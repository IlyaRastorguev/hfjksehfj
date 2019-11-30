--liquibase formatted sql

--changeset simanov-an:ddl-wifi-analyze-info-column-speed
--comment Add speed column
ALTER TABLE WIFI_ANALYZES_INFO ADD COLUMN SPEED VARCHAR (32);

--rollback;