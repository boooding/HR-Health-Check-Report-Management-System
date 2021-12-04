-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hr_health_check
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('admin','{noop}123');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire` (
  `uuid` varchar(36) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `description` varchar(1023) NOT NULL,
  `questionnaire_name` varchar(255) NOT NULL,
  `servicing` bit(1) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire`
--

LOCK TABLES `questionnaire` WRITE;
/*!40000 ALTER TABLE `questionnaire` DISABLE KEYS */;
INSERT INTO `questionnaire` VALUES ('d177bdd8-754a-4301-b712-b284acb08bdf','2021-05-07 21:26:08.002756','描述','大学生心理健康调察问卷',_binary '','2021-05-07 21:26:08.002756');
/*!40000 ALTER TABLE `questionnaire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire_question`
--

DROP TABLE IF EXISTS `questionnaire_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire_question` (
  `uuid` varchar(36) NOT NULL,
  `module` varchar(255) NOT NULL,
  `options` varchar(1023) DEFAULT NULL,
  `page` tinyint(1) NOT NULL,
  `question` varchar(255) NOT NULL,
  `question_no` tinyint NOT NULL,
  `question_type` int NOT NULL,
  `questionnaire_uuid` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_questionnaire_uuid_question` (`questionnaire_uuid`,`question`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire_question`
--

LOCK TABLES `questionnaire_question` WRITE;
/*!40000 ALTER TABLE `questionnaire_question` DISABLE KEYS */;
INSERT INTO `questionnaire_question` VALUES ('000beb08-8dc4-4ad6-aceb-9cb09071d973','模块1','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',1,'问题2',3,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('0a16da3b-6c7c-45a2-9a84-0ffaf90a27fb','模块1','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',1,'问题1',2,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('281c7940-831d-4895-a46c-a8f2e15a5a9b','模块2','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',1,'问题3',4,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('333f1df1-f53f-47bd-89c9-3d4d6968bd02','模块4','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',2,'问题8',9,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('84ef167b-a2c3-40e1-b395-8ca4a96839e2','',NULL,0,'头2',1,0,'d177bdd8-754a-4301-b712-b284acb08bdf'),('86b9fbbc-1c84-486b-b264-95c63c59dbc7','模块3','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',2,'问题6',7,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('93a5f2c7-ea73-4817-af5d-af6ff9c72fe7','模块3','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',2,'问题5',6,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('a9638005-e9ba-4613-8b84-c295a6569187','',NULL,0,'头1',0,0,'d177bdd8-754a-4301-b712-b284acb08bdf'),('c2fc8575-edda-4d4f-a777-eaa7e0a2382c','模块2','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',1,'问题4',5,1,'d177bdd8-754a-4301-b712-b284acb08bdf'),('e9c3de8c-a033-459e-803d-45f8778f7d32','模块4','{0:\"选项1\",1:\"选项2\",2:\"选项3\"}',2,'问题7',8,1,'d177bdd8-754a-4301-b712-b284acb08bdf');
/*!40000 ALTER TABLE `questionnaire_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire_result`
--

DROP TABLE IF EXISTS `questionnaire_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire_result` (
  `uuid` varchar(36) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `contact_name` varchar(255) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `ip_addr` varchar(15) NOT NULL,
  `questionnaire_uuid` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_questionnaire_uuid_ip_addr` (`questionnaire_uuid`,`ip_addr`),
  KEY `ix_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire_result`
--

LOCK TABLES `questionnaire_result` WRITE;
/*!40000 ALTER TABLE `questionnaire_result` DISABLE KEYS */;
INSERT INTO `questionnaire_result` VALUES ('bfee3895-c079-411e-9a81-2ae12a1298be','公司1','陈大帅','2021-05-07 21:57:37.087885','0:0:0:0:0:0:0:1','d177bdd8-754a-4301-b712-b284acb08bdf');
/*!40000 ALTER TABLE `questionnaire_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `result_data`
--

DROP TABLE IF EXISTS `result_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `result_data` (
  `uuid` varchar(36) NOT NULL,
  `content` varchar(255) NOT NULL,
  `question_uuid` varchar(36) NOT NULL,
  `result_uuid` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_result_uuid_question_uuid` (`result_uuid`,`question_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `result_data`
--

LOCK TABLES `result_data` WRITE;
/*!40000 ALTER TABLE `result_data` DISABLE KEYS */;
INSERT INTO `result_data` VALUES ('0ef040a8-057e-4626-a7b5-67bce081d201','头回答2','84ef167b-a2c3-40e1-b395-8ca4a96839e2','bfee3895-c079-411e-9a81-2ae12a1298be'),('197058fe-03f9-4740-84a6-effd56565211','2','e9c3de8c-a033-459e-803d-45f8778f7d32','bfee3895-c079-411e-9a81-2ae12a1298be'),('401492bf-61d2-4266-b437-1380eca6a04b','2','c2fc8575-edda-4d4f-a777-eaa7e0a2382c','bfee3895-c079-411e-9a81-2ae12a1298be'),('442c7f2f-fe3e-42a5-b8f9-e168c9897403','1','333f1df1-f53f-47bd-89c9-3d4d6968bd02','bfee3895-c079-411e-9a81-2ae12a1298be'),('4f33360b-db51-4204-9f76-32a5582b7885','3','86b9fbbc-1c84-486b-b264-95c63c59dbc7','bfee3895-c079-411e-9a81-2ae12a1298be'),('910a8ef5-614e-4b07-9ad2-2c1d2a043d45','1','281c7940-831d-4895-a46c-a8f2e15a5a9b','bfee3895-c079-411e-9a81-2ae12a1298be'),('c3183c41-9219-4a79-8787-02ddfdb96a70','2','000beb08-8dc4-4ad6-aceb-9cb09071d973','bfee3895-c079-411e-9a81-2ae12a1298be'),('f2587dd7-d7ee-4ea8-8639-c72084a14446','1','0a16da3b-6c7c-45a2-9a84-0ffaf90a27fb','bfee3895-c079-411e-9a81-2ae12a1298be'),('f4c7df00-8a0b-47ec-9e33-3be5583cb6a4','3','93a5f2c7-ea73-4817-af5d-af6ff9c72fe7','bfee3895-c079-411e-9a81-2ae12a1298be'),('f5f035c6-2692-493d-9082-cc9ec04eb0ae','头回答1','a9638005-e9ba-4613-8b84-c295a6569187','bfee3895-c079-411e-9a81-2ae12a1298be');
/*!40000 ALTER TABLE `result_data` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-08  0:19:05
