-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: caddieplusdatabase
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `historique_prix`
--

DROP TABLE IF EXISTS `historique_prix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historique_prix` (
  `hist_prix_id` int NOT NULL AUTO_INCREMENT,
  `date_changement` timestamp NULL DEFAULT NULL,
  `histo_prix` double DEFAULT NULL,
  `produit_id` int DEFAULT NULL,
  PRIMARY KEY (`hist_prix_id`),
  KEY `produit_id` (`produit_id`),
  CONSTRAINT `historique_prix_ibfk_1` FOREIGN KEY (`produit_id`) REFERENCES `produits` (`produit_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historique_prix`
--

LOCK TABLES `historique_prix` WRITE;
/*!40000 ALTER TABLE `historique_prix` DISABLE KEYS */;
INSERT INTO `historique_prix` VALUES (1,'2026-05-20 04:24:59',3.49,1),(2,'2026-05-20 04:24:59',1.99,2),(3,'2026-05-20 04:24:59',2.32,3),(4,'2026-05-20 04:24:59',1.79,4),(5,'2026-05-20 04:24:59',4.5,5),(6,'2026-05-20 04:24:59',5.2,6),(7,'2026-05-20 04:24:59',0.99,7),(8,'2026-05-20 04:24:59',3.23,6),(9,'2026-05-20 04:24:59',5.5,1),(10,'2026-05-20 04:24:59',1.95,4);
/*!40000 ALTER TABLE `historique_prix` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-20  6:25:33
