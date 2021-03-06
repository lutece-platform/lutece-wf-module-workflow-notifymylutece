--
-- Table structure for table tf_directory_cf
--
DROP TABLE IF EXISTS task_notify_mylutece_cf;
CREATE TABLE task_notify_mylutece_cf(
  id_task INT DEFAULT NULL,
  id_directory INT DEFAULT NULL,
  position_directory_entry_user_guid INT DEFAULT NULL,
  sender_name VARCHAR(255) DEFAULT NULL, 
  subject VARCHAR(255) DEFAULT NULL, 
  message long VARCHAR DEFAULT NULL,
  PRIMARY KEY (id_task)
);

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
