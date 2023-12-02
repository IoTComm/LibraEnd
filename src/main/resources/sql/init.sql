-- MySQL dump 10.13  Distrib 8.0.34, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: iotcomm
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `desk`
--

DROP TABLE IF EXISTS `desk`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `desk`
(
    `id`    int NOT NULL,
    `state` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idDesk_UNIQUE` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `library`
(
    `user_id`     int       NOT NULL,
    `start_at`    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `seat_id`     int       NOT NULL,
    `warn_count`  int       NOT NULL DEFAULT '0',
    `warn_reason` varchar(50)        DEFAULT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `user_id_UNIQUE` (`user_id`),
    UNIQUE KEY `seat_id_UNIQUE` (`seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation`
(
    `user_id`      int       NOT NULL,
    `reserve_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `reserve_seat` int       NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat`
(
    `id`           int     NOT NULL,
    `last_user_id` int              DEFAULT NULL,
    `is_using`     tinyint NOT NULL DEFAULT '0',
    `desk_id`      int              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `seat_id_UNIQUE` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `id`          int         NOT NULL,
    `pw`          varchar(20) NOT NULL,
    `session_key` varchar(36)          DEFAULT 'null',
    `refd_at`     timestamp   NULL     DEFAULT NULL,
    `type`        tinyint     NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idUser_UNIQUE` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'iotcomm'
--

--
-- Dumping routines for database 'iotcomm'
--
/*!50003 DROP FUNCTION IF EXISTS `get_user_id` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` FUNCTION `get_user_id`(sess_id VARCHAR(36)) RETURNS int READS SQL DATA
    READS SQL DATA
BEGIN
    DECLARE user_id INT DEFAULT null;
    SET user_id = (SELECT id FROM IoTComm.`User` WHERE session_key = sess_id);
    RETURN user_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP FUNCTION IF EXISTS `get_user_seat` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`%` FUNCTION `get_user_seat`(user_id int) RETURNS int READS SQL DATA
BEGIN
    DECLARE seat_id INT DEFAULT NULL;

    SELECT id
    INTO seat_id
    FROM IoTcomm.Seat
    WHERE last_user_id = user_id
      AND is_using = true;

    RETURN seat_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `is_admin` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `is_admin`(
    sess_id VARCHAR(36),
    OUT result INT
)
BODY:
BEGIN
    DECLARE user_id INT DEFAULT null;
    -- 유저 존재 확인
    IF not EXISTS(SELECT * FROM IoTComm.`User` WHERE session_key = sess_id) THEN
        SET result = 401;
        LEAVE BODY;
    END IF;
    -- 유저 토큰 valid한지 확인
    IF TIMESTAMPDIFF(HOUR, CURRENT_TIMESTAMP(),
                     (SELECT refd_at FROM IoTComm.`User` WHERE session_key = sess_id)) > 6 THEN
        SET result = 401;
        LEAVE BODY;
    END IF;
    SET user_id = (SELECT id FROM IoTComm.`User` WHERE session_key = sess_id);


    IF not EXISTS(SELECT `type` FROM IoTComm.`User` WHERE id = user_id and IoTComm.`User`.`type` = true) THEN
        SET result = 403;
        LEAVE BODY;
    END IF;

    SET result = 200;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `library_logout` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `library_logout`(
    sess_id VARCHAR(36),
    OUT result INT
)
BEGIN
    DECLARE exit_code INT DEFAULT 0;
    DECLARE user_id INT DEFAULT null;
    DECLARE used_seat_id INT DEFAULT null;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 0;

    SET user_id = (SELECT id FROM IoTComm.`User` WHERE session_key = sess_id);
    SET used_seat_id = (SELECT seat_id FROM IoTComm.Library WHERE Library.user_id = user_id);

    START TRANSACTION;

    UPDATE IoTComm.`User`
    SET refd_at = CURRENT_TIMESTAMP()
    WHERE id = user_id;

    DELETE
    FROM IoTComm.Library
    WHERE Library.user_id = user_id;

    UPDATE IoTComm.Seat
    SET is_using = false
    WHERE id = used_seat_id;

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
    ELSE
        SET result = 200;
        COMMIT;
    END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `library_reservation` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `library_reservation`(
    seat_id INT,
    start_time TIMESTAMP,
    sess_id VARCHAR(36),
    OUT result INT
)
BODY:
BEGIN
    DECLARE exit_code INT DEFAULT 500;
    DECLARE user_id INT DEFAULT null;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 0;

    -- 유저 존재 확인
    IF not EXISTS(SELECT * FROM IoTComm.`User` WHERE session_key = sess_id) THEN
        SET result = 401;
        LEAVE BODY;
    END IF;
    -- 유저 토큰 valid한지 확인
    IF TIMESTAMPDIFF(HOUR, CURRENT_TIMESTAMP(),
                     (SELECT refd_at FROM IoTComm.`User` WHERE session_key = sess_id)) > 6 THEN
        SET result = 401;
        LEAVE BODY;
    END IF;

    SET user_id = get_user_id(sess_id);

    -- 동일인물이 다른 자리 예약 금지
    IF EXISTS(SELECT *
              FROM IoTComm.Library
              WHERE Library.user_id = user_id) THEN
        SET result = 403;
        LEAVE BODY;
    END IF;

    -- 입력한 자리가 존재하고 예약 가능한지 확인
    IF not EXISTS(SELECT * FROM IoTComm.Seat WHERE id = seat_id and is_using = false) THEN
        SET result = 404;
        LEAVE BODY;
    END IF;
    -- -- 예약이 가능한지 확인
    -- IF EXISTS(SELECT * FROM IoTComm.Library
    -- WHERE seat_id = Library.seat_id
    -- and (TIMESTAMPDIFF(HOUR, reserve_time, start_time) BETWEEN -2 AND 2)) THEN
    -- 	SET result = 409;
    --    LEAVE BODY;
    -- END IF;
    -- 입력한 시각이 정확한지 확인
    IF not ((start_time > CURRENT_TIMESTAMP()) and TIMESTAMPDIFF(DAY, start_time, CURRENT_TIMESTAMP()) <= 30) THEN
        SET result = 400;
        LEAVE BODY;
    END IF;

    START TRANSACTION;

    UPDATE IoTComm.`User`
    SET refd_at = CURRENT_TIMESTAMP()
    WHERE id = user_id;

    -- INSERT INTO IoTComm.Reservation(user_id, reserve_time, reserve_seat)
    -- VALUES (user_id, start_time, seat_id);

    INSERT INTO IoTComm.Library(user_id, seat_id)
    VALUES (user_id, seat_id);

    UPDATE IoTComm.Seat
    SET is_using     = true,
        last_user_id = user_id
    WHERE id = seat_id;

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
        LEAVE BODY;
    ELSE
        SET result = 200;
        COMMIT;
    END IF;


END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `library_seat_list` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `library_seat_list`(
    seat_id INT,
    is_using BOOL,
    desk_id INT
)
BEGIN
    DECLARE seat_bool BOOL DEFAULT (seat_id is null);
    DECLARE state_bool BOOL DEFAULT (is_using is null);
    DECLARE desk_bool BOOL DEFAULT (desk_id is null);

    SELECT Seat.id, Seat.is_using, Seat.desk_id
    FROM IoTComm.Seat
    WHERE (seat_bool or id = seat_id)
      and (state_bool or Seat.is_using = is_using)
      and (desk_bool or Seat.desk_id = desk_id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `register_user` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `register_user`(
    new_id INT,
    new_pw VARCHAR(20),
    OUT result INT
)
BODY:
BEGIN
    DECLARE exit_code INT DEFAULT 500;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 0;

    IF EXISTS(SELECT id FROM IoTComm.`User` WHERE id = new_id) THEN
        SET result = 409;
        LEAVE BODY;
    END IF;

    START TRANSACTION;

    INSERT INTO IoTComm.`User`(id, pw) VALUES (new_id, new_pw);

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
        LEAVE BODY;
    ELSE
        COMMIT;
    END IF;

    SET result = 200;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `signup` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `signup`(
    new_id INT,
    new_pw VARCHAR(20),
    OUT result INT
)
BODY:
BEGIN
    DECLARE exit_code INT DEFAULT 500;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 0;

    IF EXISTS(SELECT id FROM IoTComm.`User` WHERE id = new_id) THEN
        SET result = 409;
        LEAVE BODY;
    END IF;

    START TRANSACTION;

    INSERT INTO IoTComm.`User`(id, pw) VALUES (new_id, new_pw);

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
        LEAVE BODY;
    ELSE
        COMMIT;
    END IF;

    SET result = 200;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `sudo_library_clear_seat` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `sudo_library_clear_seat`(
    sess_id VARCHAR(36),
    used_seat_id INT,
    OUT result INT,
    OUT last_user_id INT
)
BODY:
BEGIN
    DECLARE exit_code INT DEFAULT 0;
    DECLARE admin_user_id INT DEFAULT null;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 500;
    SET last_user_id = null;

    set @p_result = 0;
    call IoTComm.is_admin(sess_id, @p_result);
    SET result = @p_result;
    IF result != 200 THEN LEAVE BODY; END IF;

    IF not EXISTS(SELECT * FROM IoTComm.Seat WHERE used_seat_id = id and is_using = true) THEN
        SET result = 404;
        LEAVE BODY;
    END IF;

    SET admin_user_id = get_user_id(sess_id);
    (SELECT Seat.last_user_id INTO last_user_id FROM IoTComm.Seat WHERE used_seat_id = id and is_using = true);

    START TRANSACTION;

    UPDATE IoTComm.`User`
    SET refd_at = CURRENT_TIMESTAMP()
    WHERE id = admin_user_id;

    DELETE
    FROM IoTComm.Library
    WHERE Library.seat_id = used_seat_id;

    UPDATE IoTComm.Seat
    SET is_using = false
    WHERE id = used_seat_id;

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
    ELSE
        SET result = 200;
        COMMIT;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!50003 DROP PROCEDURE IF EXISTS `user_login` */;
/*!50003 SET @saved_cs_client = @@character_set_client */;
/*!50003 SET @saved_cs_results = @@character_set_results */;
/*!50003 SET @saved_col_connection = @@collation_connection */;
/*!50003 SET character_set_client = utf8mb4 */;
/*!50003 SET character_set_results = utf8mb4 */;
/*!50003 SET collation_connection = utf8mb4_0900_ai_ci */;
/*!50003 SET @saved_sql_mode = @@sql_mode */;
/*!50003 SET sql_mode =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */;
DELIMITER ;;
CREATE
    DEFINER = `root`@`localhost` PROCEDURE `user_login`(
    login_id INT,
    login_pw VARCHAR(20),
    OUT result INT,
    OUT sess_key VARCHAR(36)
)
BODY:
BEGIN
    DECLARE exit_code INT DEFAULT 500;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET exit_code = -1;
    SET result = 0;
    SET sess_key = null;

    IF not EXISTS(SELECT id FROM IoTComm.`User` WHERE id = login_id and pw = login_pw) THEN
        SET result = 401;
        LEAVE BODY;
    END IF;

    SET sess_key = UUID();

    START TRANSACTION;

    UPDATE IoTComm.`User`
    SET session_key = sess_key,
        refd_at     = CURRENT_TIMESTAMP()
    WHERE id = login_id
      and pw = login_pw;

    IF exit_code < 0 THEN
        ROLLBACK;
        SET result = 500;
        SET sess_key = null;
        LEAVE BODY;
    ELSE
        SET result = 200;
        COMMIT;
    END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode = @saved_sql_mode */;
/*!50003 SET character_set_client = @saved_cs_client */;
/*!50003 SET character_set_results = @saved_cs_results */;
/*!50003 SET collation_connection = @saved_col_connection */;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2023-12-02 21:45:50