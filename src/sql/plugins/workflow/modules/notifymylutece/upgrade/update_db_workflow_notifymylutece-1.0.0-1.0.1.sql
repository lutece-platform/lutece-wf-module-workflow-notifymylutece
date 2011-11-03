--
-- Table structure for table task_notify_mylutece_notification_type
--
DROP TABLE IF EXISTS task_notify_mylutece_notification_type;
CREATE TABLE task_notify_mylutece_notification_type(
  id_task INT DEFAULT NULL,
  id_notification_type INT DEFAULT NULL,
  PRIMARY KEY (id_task, id_notification_type)
);

--
-- Table structure for table task_notify_mylutece_retrieval_type
--
DROP TABLE IF EXISTS task_notify_mylutece_retrieval_type;
CREATE TABLE task_notify_mylutece_retrieval_type(
  id_task INT DEFAULT NULL,
  id_retrieval_type INT DEFAULT NULL,
  PRIMARY KEY (id_task, id_retrieval_type)
);

--
-- Table structure for table task_notify_mylutece_user_guid
--
DROP TABLE IF EXISTS task_notify_mylutece_user_guid;
CREATE TABLE task_notify_mylutece_user_guid(
  id_task INT DEFAULT NULL,
  user_guid VARCHAR(255) DEFAULT '' NOT NULL,
  PRIMARY KEY (id_task, user_guid)
);
