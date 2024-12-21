# ************************************************************
# Antares - SQL Client
# Version 0.7.30
# 
# https://antares-sql.app/
# https://github.com/antares-sql/antares
# 
# Host: 127.0.0.1 (MySQL Community Server - GPL 9.1.0)
# Database: restoran_pbo
# Generation time: 2024-12-21T14:02:54+08:00
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table absensi_pegawai
# ------------------------------------------------------------

DROP TABLE IF EXISTS `absensi_pegawai`;

CREATE TABLE `absensi_pegawai` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tanggal` datetime NOT NULL,
  `hadir` tinyint(1) NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_absensi_pegawai_user` (`id_user`),
  CONSTRAINT `FK_absensi_pegawai_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table inventory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `inventory`;

CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `stok` int NOT NULL,
  `satuan` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jenis_inventory` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table item_mutasi_inventory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item_mutasi_inventory`;

CREATE TABLE `item_mutasi_inventory` (
  `id_mutasi_inventory` bigint NOT NULL,
  `id_inventory` bigint NOT NULL,
  `mutasi` int NOT NULL,
  PRIMARY KEY (`id_mutasi_inventory`,`id_inventory`),
  KEY `FK_item_mutasi_inventory_inventory` (`id_inventory`),
  CONSTRAINT `FK_item_mutasi_inventory_inventory` FOREIGN KEY (`id_inventory`) REFERENCES `inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_item_mutasi_inventory_mutasi_inventory` FOREIGN KEY (`id_mutasi_inventory`) REFERENCES `mutasi_inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table item_pesanan
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item_pesanan`;

CREATE TABLE `item_pesanan` (
  `id_pesanan` bigint NOT NULL,
  `id_menu` bigint NOT NULL,
  `jumlah` int NOT NULL,
  `status` tinyint(1) NOT NULL,
  `total_harga_item` double NOT NULL,
  PRIMARY KEY (`id_pesanan`,`id_menu`),
  KEY `FK_item_pesanan_menu` (`id_menu`),
  CONSTRAINT `FK_item_pesanan_menu` FOREIGN KEY (`id_menu`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_item_pesanan_pesanan` FOREIGN KEY (`id_pesanan`) REFERENCES `pesanan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table meja
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meja`;

CREATE TABLE `meja` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `no_meja` int NOT NULL,
  `tersedia` tinyint(1) NOT NULL,
  `kapasitas` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `meja` WRITE;
/*!40000 ALTER TABLE `meja` DISABLE KEYS */;

INSERT INTO `meja` (`id`, `no_meja`, `tersedia`, `kapasitas`) VALUES
	(6, 1, 1, 4),
	(7, 2, 1, 4),
	(8, 3, 1, 4),
	(9, 4, 1, 4),
	(10, 5, 1, 8),
	(11, 1, 1, 16);

/*!40000 ALTER TABLE `meja` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `harga` double NOT NULL,
  `deskripsi` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table mutasi_inventory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mutasi_inventory`;

CREATE TABLE `mutasi_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tanggal` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table penggajian
# ------------------------------------------------------------

DROP TABLE IF EXISTS `penggajian`;

CREATE TABLE `penggajian` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bonus` double NOT NULL,
  `potongan` double NOT NULL,
  `id_user` bigint NOT NULL,
  `id_transaksi` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_penggajian_transaksi` (`id_transaksi`),
  KEY `FK_penggajian_user` (`id_user`),
  CONSTRAINT `FK_penggajian_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_penggajian_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table pesanan
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pesanan`;

CREATE TABLE `pesanan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `struk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  `id_meja` bigint NOT NULL,
  `id_mutasi_inventory` bigint NOT NULL,
  `id_transaksi` bigint NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pesanan_meja` (`id_meja`),
  KEY `FK_pesanan_mutasi_inventory` (`id_mutasi_inventory`),
  KEY `FK_pesanan_transaksi` (`id_transaksi`),
  KEY `FK_pesanan_user` (`id_user`),
  CONSTRAINT `FK_pesanan_meja` FOREIGN KEY (`id_meja`) REFERENCES `meja` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pesanan_mutasi_inventory` FOREIGN KEY (`id_mutasi_inventory`) REFERENCES `mutasi_inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pesanan_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pesanan_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table resep
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resep`;

CREATE TABLE `resep` (
  `id_menu` bigint NOT NULL,
  `id_inventory` bigint NOT NULL,
  `jumlah` int NOT NULL,
  PRIMARY KEY (`id_menu`,`id_inventory`),
  KEY `FK_resep_inventory` (`id_inventory`),
  CONSTRAINT `FK_resep_inventory` FOREIGN KEY (`id_inventory`) REFERENCES `inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_resep_menu` FOREIGN KEY (`id_menu`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table reservasi
# ------------------------------------------------------------

DROP TABLE IF EXISTS `reservasi`;

CREATE TABLE `reservasi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `no_telp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `id_meja` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_reservasi_meja` (`id_meja`),
  CONSTRAINT `FK_reservasi_meja` FOREIGN KEY (`id_meja`) REFERENCES `meja` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table transaksi
# ------------------------------------------------------------

DROP TABLE IF EXISTS `transaksi`;

CREATE TABLE `transaksi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nominal` double NOT NULL,
  `tanggal` datetime NOT NULL,
  `metode_pembayaran` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `jenis_transaksi` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gaji_pokok` double NOT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of views
# ------------------------------------------------------------

# Creating temporary tables to overcome VIEW dependency errors


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# Dump completed on 2024-12-21T14:02:54+08:00
