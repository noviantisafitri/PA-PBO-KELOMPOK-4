-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 03, 2023 at 05:23 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `peminjaman_gedung`
--

-- --------------------------------------------------------

--
-- Table structure for table `gedung`
--

CREATE TABLE `gedung` (
  `id_gedung` char(5) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `kapasitas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gedung`
--

INSERT INTO `gedung` (`id_gedung`, `nama`, `kapasitas`) VALUES
('1120', 'Auditorium', 800),
('1121', 'GOR 27 September', 2000),
('1122', 'Gedung Masjaya', 400),
('1123', 'Gedung Rektorat Serbaguna', 500);

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `id_user` varchar(18) NOT NULL,
  `program_studi` varchar(30) NOT NULL,
  `fakultas` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`id_user`, `program_studi`, `fakultas`) VALUES
('2209116003', 'Sistem informasi', 'Teknik'),
('2209116004', 'Sistem Informasi', 'Teknik'),
('2209116005', 'Sistem Informasi', 'Teknik'),
('2209116006', 'Sistem Informasi', 'Teknik'),
('2209116007', 'Sistem Informasi', 'Teknik'),
('2209116018', 'Sistem Informasi', 'Teknik'),
('2209116020', 'Sistem Informasi', 'Teknik'),
('2209116021', 'Sistem Informasi', 'Teknik'),
('2209116022', 'Sistem Informasi', 'Teknik'),
('2209116033', 'Sistem Informasi', 'Teknik');

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` int(11) NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `tanggal_selesai` date NOT NULL,
  `keperluan` varchar(30) NOT NULL,
  `status` varchar(15) NOT NULL,
  `gedung_id_gedung` char(5) NOT NULL,
  `mahasiswa_id_user` varchar(18) NOT NULL,
  `staff_id_user` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `tanggal_peminjaman`, `tanggal_selesai`, `keperluan`, `status`, `gedung_id_gedung`, `mahasiswa_id_user`, `staff_id_user`) VALUES
(1, '2023-11-03', '2023-11-05', 'Seminar', 'Approved', '1120', '2209116004', '2019030336025'),
(2, '2023-11-04', '2023-11-05', 'Lomba', 'Approved', '1120', '2209116004', '2019070336026'),
(3, '2023-11-06', '2023-11-07', 'Seminar', 'Pending', '1122', '2209116020', '2019030336025'),
(4, '2023-11-11', '2023-11-12', 'Seminar', 'Pending', '1122', '2209116020', '2019070336026'),
(5, '2023-11-07', '2023-11-08', 'Rapat', 'Pending', '1123', '2209116005', '2019030336025'),
(6, '2023-11-20', '2023-11-21', 'Seminar', 'Pending', '1120', '2209116005', '2019070336026'),
(7, '2023-11-17', '2023-11-19', 'Kuliah Umum', 'Pending', '1121', '2209116022', '2019030336025'),
(8, '2023-11-25', '2023-11-26', 'Lomba', 'Pending', '1122', '2209116022', '2019070336026'),
(9, '2023-11-27', '2023-11-28', 'Seminar', 'Pending', '1123', '2209116033', '2019030336025'),
(10, '2023-12-01', '2023-12-03', 'Rapat', 'Pending', '1121', '2209116033', '2019070336026');

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `id_user` varchar(18) NOT NULL,
  `jabatan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`id_user`, `jabatan`) VALUES
('2019030336025', 'Staff Administrasi'),
('2019070336026', 'Staff Administrasi');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` varchar(18) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nama` varchar(35) NOT NULL,
  `no_telepon` varchar(12) NOT NULL,
  `role` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `password`, `nama`, `no_telepon`, `role`) VALUES
('2019030336025', 'l123', 'Luthfi Halimawan', '081258391821', 'Staff'),
('2019070336026', 'a123', 'Alfachri Gani', '082278156699', 'Staff'),
('2209116003', 's123', 'Sandrina Aulia', '082125529214', 'Mahasiswa'),
('2209116004', 'n123', 'Novianti Safitri', '082179194414', 'Mahasiswa'),
('2209116005', 'a123', 'Alisya Nisrina Sativa', '082168612073', 'Mahasiswa'),
('2209116006', 'd123', 'Dinnuhoni Trahutomo', '082142604251', 'Mahasiswa'),
('2209116007', 'v123', 'Vera Santi Wijaya', '082167399585', 'Mahasiswa'),
('2209116018', 'h123', 'M. Haykal Fajar Aulia', '082260375060', 'Mahasiswa'),
('2209116020', 'y123', 'Muhamad Yusuf', '082178881112', 'Mahasiswa'),
('2209116021', 'a123', 'Adham Khautsar Leswono', '082152092240', 'Mahasiswa'),
('2209116022', 'd123', 'Muhammad Dian Nurdiansyah', '082125303873', 'Mahasiswa'),
('2209116033', 'd123', 'Doni Arya Rachmadi', '082154380951', 'Mahasiswa');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `gedung`
--
ALTER TABLE `gedung`
  ADD PRIMARY KEY (`id_gedung`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id_user`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `peminjaman_gedung_fk` (`gedung_id_gedung`),
  ADD KEY `peminjaman_mahasiswa_fk` (`mahasiswa_id_user`),
  ADD KEY `peminjaman_staff_fk` (`staff_id_user`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`id_user`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `id_peminjaman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10029;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD CONSTRAINT `mahasiswa_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Constraints for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `peminjaman_gedung_fk` FOREIGN KEY (`gedung_id_gedung`) REFERENCES `gedung` (`id_gedung`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_mahasiswa_fk` FOREIGN KEY (`mahasiswa_id_user`) REFERENCES `mahasiswa` (`id_user`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_staff_fk` FOREIGN KEY (`staff_id_user`) REFERENCES `staff` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `staff_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
