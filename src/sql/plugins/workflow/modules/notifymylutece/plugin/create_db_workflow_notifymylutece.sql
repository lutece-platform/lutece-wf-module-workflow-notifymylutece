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
