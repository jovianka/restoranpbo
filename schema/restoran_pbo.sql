# ************************************************************
# Antares - SQL Client
# Version 0.7.29
# 
# https://antares-sql.app/
# https://github.com/antares-sql/antares
# 
# Host: 127.0.0.1 (MySQL Community Server - GPL 9.1.0)
# Database: restoran_pbo
# Generation time: 2024-12-12T12:54:33+08:00
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
  `id_jenis_inventory` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_inventory_jenis_inventory` (`id_jenis_inventory`),
  CONSTRAINT `FK_inventory_jenis_inventory` FOREIGN KEY (`id_jenis_inventory`) REFERENCES `jenis_inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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





# Dump of table jenis_inventory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jenis_inventory`;

CREATE TABLE `jenis_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `jenis_inventory` WRITE;
/*!40000 ALTER TABLE `jenis_inventory` DISABLE KEYS */;

INSERT INTO `jenis_inventory` (`id`, `nama`) VALUES
	(2, "bahan_makanan"),
	(3, "perabotan"),
	(4, "peralatan_dapur");

/*!40000 ALTER TABLE `jenis_inventory` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table jenis_transaksi
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jenis_transaksi`;

CREATE TABLE `jenis_transaksi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `jenis_transaksi` WRITE;
/*!40000 ALTER TABLE `jenis_transaksi` DISABLE KEYS */;

INSERT INTO `jenis_transaksi` (`id`, `nama`) VALUES
	(1, "pemasukan"),
	(2, "pengeluaran");

/*!40000 ALTER TABLE `jenis_transaksi` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table meja
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meja`;

CREATE TABLE `meja` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `no_meja` int NOT NULL,
  `tersedia` tinyint(1) NOT NULL,
  `kapasitas` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





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





# Dump of table metode_pembayaran
# ------------------------------------------------------------

DROP TABLE IF EXISTS `metode_pembayaran`;

CREATE TABLE `metode_pembayaran` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `metode_pembayaran` WRITE;
/*!40000 ALTER TABLE `metode_pembayaran` DISABLE KEYS */;

INSERT INTO `metode_pembayaran` (`id`, `nama`) VALUES
	(1, "cash"),
	(2, "debit"),
	(3, "kredit");

/*!40000 ALTER TABLE `metode_pembayaran` ENABLE KEYS */;
UNLOCK TABLES;



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
  PRIMARY KEY (`id`),
  KEY `FK_pesanan_meja` (`id_meja`),
  KEY `FK_pesanan_mutasi_inventory` (`id_mutasi_inventory`),
  KEY `FK_pesanan_transaksi` (`id_transaksi`),
  CONSTRAINT `FK_pesanan_meja` FOREIGN KEY (`id_meja`) REFERENCES `meja` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pesanan_mutasi_inventory` FOREIGN KEY (`id_mutasi_inventory`) REFERENCES `mutasi_inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pesanan_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `id_meja` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_reservasi_meja` (`id_meja`),
  CONSTRAINT `FK_reservasi_meja` FOREIGN KEY (`id_meja`) REFERENCES `meja` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `nama`) VALUES
	(1, "pemilik"),
	(2, "kasir"),
	(3, "chef");

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table transaksi
# ------------------------------------------------------------

DROP TABLE IF EXISTS `transaksi`;

CREATE TABLE `transaksi` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nominal` double NOT NULL,
  `tanggal` datetime NOT NULL,
  `id_jenis_transaksi` bigint NOT NULL,
  `id_metode_pembayaran` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_transaksi_jenis_transaksi` (`id_jenis_transaksi`),
  KEY `FK_transaksi_metode_pembayaran` (`id_metode_pembayaran`),
  CONSTRAINT `FK_transaksi_jenis_transaksi` FOREIGN KEY (`id_jenis_transaksi`) REFERENCES `jenis_transaksi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_transaksi_metode_pembayaran` FOREIGN KEY (`id_metode_pembayaran`) REFERENCES `metode_pembayaran` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nama` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gaji_pokok` double NOT NULL,
  `id_role` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_role` (`id_role`),
  CONSTRAINT `FK_user_role` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
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

# Dump completed on 2024-12-12T12:54:33+08:00
