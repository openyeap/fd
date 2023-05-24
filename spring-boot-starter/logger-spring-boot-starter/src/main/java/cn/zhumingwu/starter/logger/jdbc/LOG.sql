BEGIN;
DROP TABLE IF EXISTS log_record;
COMMIT;


BEGIN;
CREATE TABLE log_record
(
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    time              DATETIME(6) NOT NULL,
    message           TEXT NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL

);
COMMIT;
