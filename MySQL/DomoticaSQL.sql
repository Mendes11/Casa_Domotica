-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 29-Mar-2017 às 21:47
-- Versão do servidor: 5.5.53-0+deb8u1
-- PHP Version: 5.6.29-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `Domotica`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `Casa`
--

CREATE TABLE IF NOT EXISTS `Casa` (
`iIDCasa` int(11) NOT NULL,
  `bChuva` tinyint(4) NOT NULL DEFAULT '0',
  `bMigue` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Casa`
--

INSERT INTO `Casa` (`iIDCasa`, `bChuva`, `bMigue`) VALUES
(1, 0, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Comodos`
--

CREATE TABLE IF NOT EXISTS `Comodos` (
`iIDComodo` int(11) NOT NULL,
  `cNome` text,
  `iUserCont` int(11) DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Comodos`
--

INSERT INTO `Comodos` (`iIDComodo`, `cNome`, `iUserCont`) VALUES
(1, 'Sala', 0),
(2, 'Lavanderia', 0),
(3, 'Piscina', 0),
(4, 'Entrada', 0);

-- --------------------------------------------------------

--
-- Estrutura da tabela `DeviceStatus`
--

CREATE TABLE IF NOT EXISTS `DeviceStatus` (
`iIDDeviceStatus` int(11) NOT NULL,
  `cNome` text
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `DeviceStatus`
--

INSERT INTO `DeviceStatus` (`iIDDeviceStatus`, `cNome`) VALUES
(1, 'Abrindo'),
(2, 'Fechando'),
(3, 'Obstruído'),
(4, 'Aberto'),
(5, 'Fechado'),
(6, 'Movendo'),
(7, 'Pronto');

-- --------------------------------------------------------

--
-- Estrutura da tabela `Janela`
--

CREATE TABLE IF NOT EXISTS `Janela` (
`iIDJanela` int(11) NOT NULL,
  `cNome` text,
  `iSetpoint` int(11) DEFAULT NULL,
  `iPos` int(11) DEFAULT NULL,
  `bAvancado` tinyint(1) DEFAULT '0',
  `iIDDeviceStatus` int(11) DEFAULT '7',
  `iIDComodo` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Janela`
--

INSERT INTO `Janela` (`iIDJanela`, `cNome`, `iSetpoint`, `iPos`, `bAvancado`, `iIDDeviceStatus`, `iIDComodo`) VALUES
(1, 'Janela Sala', 0, 0, 1, 7, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Luminosidade`
--

CREATE TABLE IF NOT EXISTS `Luminosidade` (
`iIDLuminosidade` int(11) NOT NULL,
  `iSetpoint` int(11) DEFAULT '0',
  `iSensor` int(11) DEFAULT '0',
  `bControle` tinyint(1) DEFAULT '1',
  `bManualOn` tinyint(1) DEFAULT '0',
  `iIDComodo` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Luminosidade`
--

INSERT INTO `Luminosidade` (`iIDLuminosidade`, `iSetpoint`, `iSensor`, `bControle`, `bManualOn`, `iIDComodo`) VALUES
(1, 20, -11, 1, 1, 1),
(2, 50, 50, 0, 0, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Piscina`
--

CREATE TABLE IF NOT EXISTS `Piscina` (
`iIDPiscina` int(11) NOT NULL,
  `cNome` text,
  `bOpen` tinyint(1) DEFAULT '0',
  `bPos` tinyint(1) DEFAULT '0',
  `bAvancado` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `Porta`
--

CREATE TABLE IF NOT EXISTS `Porta` (
`iIDPorta` int(11) NOT NULL,
  `cNome` text,
  `bOpen` tinyint(1) DEFAULT '1',
  `fAutoCloseTime` decimal(10,2) DEFAULT NULL,
  `bHabilitado` tinyint(1) DEFAULT '1',
  `iIDDeviceStatus` int(11) DEFAULT '5',
  `iIDComodo` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Porta`
--

INSERT INTO `Porta` (`iIDPorta`, `cNome`, `bOpen`, `fAutoCloseTime`, `bHabilitado`, `iIDDeviceStatus`, `iIDComodo`) VALUES
(1, 'Porta Casa', 0, 5.00, 1, 5, 4);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Portao`
--

CREATE TABLE IF NOT EXISTS `Portao` (
`iIDPortao` int(11) NOT NULL,
  `cNome` text,
  `bOpen` tinyint(1) DEFAULT '1',
  `bPos` tinyint(1) DEFAULT '1',
  `fAutoCloseTime` decimal(10,2) DEFAULT NULL,
  `bEnabled` tinyint(1) DEFAULT '1',
  `iIDDeviceStatus` int(11) DEFAULT '5',
  `iIDComodo` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Portao`
--

INSERT INTO `Portao` (`iIDPortao`, `cNome`, `bOpen`, `bPos`, `fAutoCloseTime`, `bEnabled`, `iIDDeviceStatus`, `iIDComodo`) VALUES
(1, 'Portão', 0, 127, 15.00, 1, 2, 4);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Teto`
--

CREATE TABLE IF NOT EXISTS `Teto` (
`iIDTeto` int(11) NOT NULL,
  `cNome` text,
  `iSetpoint` tinyint(1) DEFAULT '1',
  `iPos` tinyint(1) DEFAULT '1',
  `bAvancado` tinyint(1) DEFAULT '0',
  `iIDDeviceStatus` int(11) DEFAULT '5',
  `iIDComodo` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Teto`
--

INSERT INTO `Teto` (`iIDTeto`, `cNome`, `iSetpoint`, `iPos`, `bAvancado`, `iIDDeviceStatus`, `iIDComodo`) VALUES
(1, 'Teto', 0, 0, 1, 5, 2),
(2, 'Teto Piscina', 0, 0, 1, 5, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Tomadas`
--

CREATE TABLE IF NOT EXISTS `Tomadas` (
`iIDTomada` int(11) NOT NULL,
  `cNome` text,
  `bStatus` tinyint(1) DEFAULT '1',
  `iIDComodo` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Tomadas`
--

INSERT INTO `Tomadas` (`iIDTomada`, `cNome`, `bStatus`, `iIDComodo`) VALUES
(1, 'Tomada1', 0, 1),
(2, 'Tomada2', 1, 1),
(3, 'Tomada3', 1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserAccessLog`
--

CREATE TABLE IF NOT EXISTS `UserAccessLog` (
`iIDUserAccessLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserAccessLog`
--

INSERT INTO `UserAccessLog` (`iIDUserAccessLog`, `dDate`, `iIDUsuario`) VALUES
(1, '2017-03-22 03:12:24', 2),
(2, '2017-03-22 03:19:05', 2),
(3, '2017-03-22 03:19:50', 3),
(4, '2017-03-22 03:20:17', 2),
(5, '2017-03-22 04:04:48', 2),
(6, '2017-03-22 04:05:02', 3),
(7, '2017-03-22 06:24:40', 2),
(8, '2017-03-22 06:25:07', 2),
(9, '2017-03-22 06:26:38', 2),
(10, '2017-03-22 06:26:53', 2),
(11, '2017-03-22 06:27:12', 2),
(12, '2017-03-22 06:31:34', 2),
(13, '2017-03-22 18:17:55', 3),
(14, '2017-03-22 18:17:57', 3),
(15, '2017-03-22 18:17:58', 3),
(16, '2017-03-22 18:17:59', 3),
(17, '2017-03-22 18:18:00', 3),
(18, '2017-03-22 18:18:01', 3),
(19, '2017-03-22 18:18:03', 3),
(20, '2017-03-22 18:18:08', 3),
(21, '2017-03-22 18:18:08', 3),
(22, '2017-03-22 18:18:10', 3),
(23, '2017-03-22 18:18:34', 3),
(24, '2017-03-22 18:18:35', 3),
(25, '2017-03-22 18:18:37', 3),
(26, '2017-03-22 18:18:38', 3),
(27, '2017-03-22 18:18:39', 3),
(28, '2017-03-22 20:02:08', 3),
(29, '2017-03-22 20:22:07', 2),
(30, '2017-03-22 20:20:06', 3),
(31, '2017-03-22 20:20:24', 3),
(32, '2017-03-22 20:20:42', 3),
(33, '2017-03-26 19:10:17', 2),
(34, '2017-03-26 19:11:04', 2),
(35, '2017-03-26 19:11:34', 2),
(36, '2017-03-26 19:27:05', 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserJanelaLog`
--

CREATE TABLE IF NOT EXISTS `UserJanelaLog` (
`iIDUserJanelaLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `iSetpoint` int(11) DEFAULT NULL,
  `bAvancado` tinyint(1) DEFAULT NULL,
  `bNewAvancado` tinyint(1) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDJanela` int(11) DEFAULT NULL,
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserJanelaLog`
--

INSERT INTO `UserJanelaLog` (`iIDUserJanelaLog`, `dDate`, `iSetpoint`, `bAvancado`, `bNewAvancado`, `bPending`, `iIDJanela`, `iIDUsuario`) VALUES
(1, '2017-03-21 00:10:49', 50, 0, 0, b'1', 1, 1),
(2, '2017-03-21 00:53:25', 70, 0, 0, b'1', 1, 1),
(3, '2017-03-21 00:53:27', 40, 0, 0, b'1', 1, 1),
(4, '2017-03-21 02:29:43', 40, 0, 0, b'1', 1, 1),
(5, '2017-03-21 02:35:01', 70, 0, 0, b'1', 1, 1),
(6, '2017-03-21 03:04:54', 60, 0, 0, b'1', 1, 1),
(7, '2017-03-21 03:05:22', 70, 0, 0, b'1', 1, 1),
(8, '2017-03-21 03:06:10', 50, 0, 0, b'1', 1, 1),
(9, '2017-03-21 03:36:03', 80, 0, 0, b'1', 1, 1),
(10, '2017-03-21 04:03:03', 50, 0, 0, b'1', 1, 1),
(11, '2017-03-21 04:13:53', 50, 0, 0, b'1', 1, 1),
(12, '2017-03-21 04:14:49', 60, 0, 0, b'1', 1, 1),
(13, '2017-03-21 04:15:15', 80, 0, 0, b'1', 1, 1),
(14, '2017-03-21 04:26:48', 0, 0, 0, b'1', 1, 1),
(15, '2017-03-21 04:26:56', 100, 0, 0, b'1', 1, 1),
(16, '2017-03-21 04:27:07', 50, 0, 0, b'1', 1, 1),
(17, '2017-03-21 04:27:13', 50, 0, 0, b'1', 1, 1),
(18, '2017-03-21 04:27:24', 30, 0, 0, b'1', 1, 1),
(19, '2017-03-21 04:27:41', 50, 0, 0, b'1', 1, 1),
(20, '2017-03-21 04:29:41', 100, 0, 0, b'1', 1, 1),
(21, '2017-03-21 04:29:48', 20, 0, 0, b'1', 1, 1),
(22, '2017-03-21 04:30:03', 70, 0, 0, b'1', 1, 1),
(23, '2017-03-21 04:36:26', 40, 0, 0, b'1', 1, 1),
(24, '2017-03-21 04:36:30', 80, 0, 0, b'1', 1, 1),
(25, '2017-03-21 04:37:44', 40, 0, 0, b'1', 1, 1),
(26, '2017-03-21 04:38:15', 60, 0, 0, b'1', 1, 1),
(27, '2017-03-21 04:38:21', 80, 0, 0, b'1', 1, 1),
(28, '2017-03-21 04:38:37', 100, 0, 0, b'1', 1, 1),
(29, '2017-03-21 05:00:35', 60, 0, 0, b'1', 1, 1),
(30, '2017-03-21 05:05:09', 100, 0, 0, b'1', 1, 1),
(31, '2017-03-21 05:37:08', 40, 0, 0, b'1', 1, 1),
(32, '2017-03-21 06:01:47', 60, 0, 0, b'1', 1, 1),
(33, '2017-03-21 06:02:48', 70, 0, 0, b'1', 1, 1),
(34, '2017-03-21 08:22:31', 30, 0, 0, b'1', 1, 1),
(35, '2017-03-21 08:26:56', 30, 0, 0, b'1', 1, 1),
(36, '2017-03-21 08:30:41', 50, 0, 0, b'1', 1, 1),
(37, '2017-03-21 15:22:31', 20, 0, 0, b'1', 1, 1),
(38, '2017-03-21 16:21:20', 70, 0, 0, b'1', 1, 1),
(39, '2017-03-21 16:28:27', 30, 0, 0, b'1', 1, 1),
(40, '2017-03-22 04:33:54', 0, 1, 0, b'1', 1, 1),
(41, '2017-03-22 04:34:51', 40, 0, 0, b'1', 1, 1),
(42, '2017-03-22 04:35:28', 0, 1, 0, b'1', 1, 1),
(43, '2017-03-22 06:05:34', 70, 0, 0, b'1', 1, 1),
(44, '2017-03-22 06:05:46', 0, 1, 0, b'1', 1, 1),
(45, '2017-03-22 06:17:49', 40, 0, 0, b'1', 1, 1),
(46, '2017-03-22 07:33:04', 0, 1, 0, b'1', 1, 1),
(47, '2017-03-22 19:09:23', 60, 0, 0, b'1', 1, 1),
(48, '2017-03-22 19:26:36', 30, 0, 0, b'1', 1, 1),
(49, '2017-03-22 19:26:38', 80, 0, 0, b'1', 1, 1),
(50, '2017-03-22 19:28:45', 0, 1, 0, b'1', 1, 1),
(51, '2017-03-22 19:33:15', 60, 0, 0, b'1', 1, 1),
(52, '2017-03-22 19:35:26', 0, 1, 0, b'1', 1, 1),
(53, '2017-03-22 19:36:29', 60, 0, 0, b'1', 1, 1),
(54, '2017-03-22 19:36:39', 0, 1, 0, b'1', 1, 1),
(55, '2017-03-22 19:52:35', 50, 0, 0, b'1', 1, 1),
(56, '2017-03-22 20:00:24', 10, 0, 0, b'1', 1, 1),
(57, '2017-03-22 20:00:28', 90, 0, 0, b'1', 1, 1),
(58, '2017-03-22 20:00:40', 0, 1, 0, b'1', 1, 1),
(59, '2017-03-22 20:17:35', 40, 0, 0, b'1', 1, 1),
(60, '2017-03-22 20:18:56', 70, 0, 0, b'1', 1, 1),
(61, '2017-03-22 20:21:09', 0, 1, 0, b'1', 1, 1),
(62, '2017-03-22 20:23:02', 100, 0, 0, b'1', 1, 1),
(63, '2017-03-22 20:23:25', 0, 1, 0, b'1', 1, 1),
(64, '2017-03-22 20:25:51', 60, 0, 0, b'1', 1, 1),
(65, '2017-03-22 20:26:21', 100, 0, 0, b'1', 1, 1),
(66, '2017-03-22 20:26:31', 0, 1, 0, b'1', 1, 1),
(67, '2017-03-22 20:21:51', 30, 0, 0, b'1', 1, 1),
(68, '2017-03-22 20:21:58', 100, 0, 0, b'1', 1, 1),
(69, '2017-03-22 20:22:12', 40, 0, 0, b'1', 1, 1),
(70, '2017-03-22 20:22:19', 50, 0, 0, b'1', 1, 1),
(71, '2017-03-22 20:22:40', 30, 0, 0, b'1', 1, 1),
(72, '2017-03-22 20:22:49', 0, 1, 0, b'1', 1, 1),
(73, '2017-03-26 15:17:52', 40, 0, 0, b'1', 1, 1),
(74, '2017-03-26 15:19:34', 20, 0, 0, b'1', 1, 1),
(75, '2017-03-26 15:19:45', 0, 0, 0, b'1', 1, 1),
(76, '2017-03-26 15:21:18', 50, 0, 0, b'1', 1, 1),
(77, '2017-03-26 15:21:23', 0, 0, 0, b'1', 1, 1),
(78, '2017-03-26 15:21:50', 100, 0, 0, b'1', 1, 1),
(79, '2017-03-26 15:22:19', 0, 0, 0, b'1', 1, 1),
(80, '2017-03-26 15:23:38', 100, 0, 0, b'1', 1, 1),
(81, '2017-03-26 15:23:47', 10, 0, 0, b'1', 1, 1),
(82, '2017-03-26 15:23:53', 0, 0, 0, b'1', 1, 1),
(83, '2017-03-26 19:06:48', 40, 0, 0, b'1', 1, 1),
(84, '2017-03-26 19:07:12', 80, 0, 0, b'1', 1, 1),
(85, '2017-03-26 19:07:18', 100, 0, 0, b'1', 1, 1),
(86, '2017-03-26 19:07:26', 20, 0, 0, b'1', 1, 1),
(87, '2017-03-26 19:09:15', 0, 0, 0, b'1', 1, 1),
(88, '2017-03-26 19:09:27', 100, 0, 0, b'1', 1, 1),
(89, '2017-03-26 19:09:49', 0, 1, 0, b'1', 1, 1),
(90, '2017-03-26 19:25:22', 50, 0, 0, b'1', 1, 1),
(91, '2017-03-26 19:25:33', 100, 0, 0, b'1', 1, 1),
(92, '2017-03-26 19:27:38', 0, 1, 0, b'1', 1, 1),
(93, '2017-03-26 19:29:04', 90, 0, 0, b'1', 1, 1),
(94, '2017-03-26 19:29:18', 100, 0, 0, b'1', 1, 1),
(95, '2017-03-26 19:29:54', 0, 0, 0, b'1', 1, 1),
(96, '2017-03-26 19:30:07', 100, 0, 0, b'1', 1, 1),
(97, '2017-03-26 19:30:15', 0, 0, 0, b'1', 1, 1),
(98, '2017-03-26 19:32:19', 30, 0, 0, b'1', 1, 1),
(99, '2017-03-26 19:32:43', 30, 0, 0, b'1', 1, 1),
(100, '2017-03-26 19:32:45', 40, 0, 0, b'1', 1, 1),
(101, '2017-03-26 19:32:54', 50, 0, 0, b'1', 1, 1),
(102, '2017-03-26 19:33:10', 70, 0, 0, b'1', 1, 1),
(103, '2017-03-26 19:33:16', 80, 0, 0, b'1', 1, 1),
(104, '2017-03-26 19:33:19', 0, 0, 0, b'1', 1, 1),
(105, '2017-03-26 19:39:23', 60, 0, 0, b'1', 1, 1),
(106, '2017-03-26 19:39:42', 100, 0, 0, b'1', 1, 1),
(107, '2017-03-26 19:39:58', 10, 0, 0, b'1', 1, 1),
(108, '2017-03-26 19:40:04', 100, 0, 0, b'1', 1, 1),
(109, '2017-03-26 19:40:12', 10, 0, 0, b'1', 1, 1),
(110, '2017-03-26 19:40:22', 100, 0, 0, b'1', 1, 1),
(111, '2017-03-26 19:40:52', 40, 0, 0, b'1', 1, 1),
(112, '2017-03-26 19:42:19', 100, 0, 0, b'1', 1, 1),
(113, '2017-03-26 19:42:28', 50, 0, 0, b'1', 1, 1),
(114, '2017-03-26 19:42:35', 70, 0, 0, b'1', 1, 1),
(115, '2017-03-26 19:42:38', 80, 0, 0, b'1', 1, 1),
(116, '2017-03-26 19:42:56', 0, 1, 0, b'1', 1, 1),
(117, '2017-03-26 19:43:18', 80, 0, 0, b'1', 1, 1),
(118, '2017-03-26 19:43:48', 90, 0, 0, b'1', 1, 1),
(119, '2017-03-26 19:43:56', 0, 0, 0, b'1', 1, 1),
(120, '2017-03-26 19:44:06', 100, 0, 0, b'1', 1, 1),
(121, '2017-03-26 19:44:17', 0, 0, 0, b'1', 1, 1),
(122, '2017-03-26 19:44:31', 90, 0, 0, b'1', 1, 1),
(123, '2017-03-26 19:45:41', 10, 0, 0, b'1', 1, 1),
(124, '2017-03-26 19:45:59', 80, 0, 0, b'1', 1, 1),
(125, '2017-03-26 19:46:07', 90, 0, 0, b'1', 1, 1),
(126, '2017-03-26 19:46:10', 100, 0, 0, b'1', 1, 1),
(127, '2017-03-26 19:46:23', 0, 0, 0, b'1', 1, 1),
(128, '2017-03-26 19:46:35', 90, 0, 0, b'1', 1, 1),
(129, '2017-03-26 19:46:43', 100, 0, 0, b'1', 1, 1),
(130, '2017-03-26 19:46:48', 0, 0, 0, b'1', 1, 1),
(131, '2017-03-26 19:46:58', 20, 0, 0, b'1', 1, 1),
(132, '2017-03-26 19:47:04', 40, 0, 0, b'1', 1, 1),
(133, '2017-03-26 19:47:06', 50, 0, 0, b'1', 1, 1),
(134, '2017-03-26 19:47:09', 60, 0, 0, b'1', 1, 1),
(135, '2017-03-26 19:47:15', 100, 0, 0, b'1', 1, 1),
(136, '2017-03-26 19:47:26', 80, 0, 0, b'1', 1, 1),
(137, '2017-03-26 19:48:26', 0, 1, 0, b'1', 1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserLuminosidadeLog`
--

CREATE TABLE IF NOT EXISTS `UserLuminosidadeLog` (
`iIDUserLuminosidadeLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `iSetpoint` int(11) DEFAULT NULL,
  `bControle` tinyint(1) DEFAULT NULL,
  `bManualOn` tinyint(1) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDLuminosidade` int(11) DEFAULT NULL,
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserLuminosidadeLog`
--

INSERT INTO `UserLuminosidadeLog` (`iIDUserLuminosidadeLog`, `dDate`, `iSetpoint`, `bControle`, `bManualOn`, `bPending`, `iIDLuminosidade`, `iIDUsuario`) VALUES
(1, '2017-03-21 02:08:57', 40, 0, 0, b'1', 1, 1),
(2, '2017-03-21 02:09:07', 40, 0, 0, b'1', 1, 1),
(3, '2017-03-21 02:13:45', 50, 1, 0, b'1', 1, 1),
(4, '2017-03-21 02:14:41', 50, 1, 1, b'1', 1, 1),
(5, '2017-03-21 02:16:36', 50, 1, 0, b'1', 1, 1),
(6, '2017-03-21 02:16:54', 50, 1, 1, b'1', 1, 1),
(7, '2017-03-21 02:19:06', 50, 1, 0, b'1', 1, 1),
(8, '2017-03-21 02:22:13', 50, 1, 1, b'1', 1, 1),
(9, '2017-03-21 02:23:06', 50, 1, 0, b'1', 1, 1),
(10, '2017-03-21 02:24:48', 50, 1, 1, b'1', 1, 1),
(11, '2017-03-21 02:26:43', 50, 0, 1, b'1', 1, 1),
(12, '2017-03-21 02:27:46', 50, 1, 1, b'1', 1, 1),
(13, '2017-03-21 02:29:20', 50, 0, 1, b'1', 1, 1),
(14, '2017-03-21 02:29:28', 50, 1, 1, b'1', 1, 1),
(15, '2017-03-21 02:34:30', 50, 1, 0, b'1', 1, 1),
(16, '2017-03-21 02:34:37', 50, 1, 1, b'1', 1, 1),
(17, '2017-03-21 03:35:53', 50, 1, 0, b'1', 1, 1),
(18, '2017-03-21 03:35:58', 50, 1, 1, b'1', 1, 1),
(19, '2017-03-21 04:20:56', 50, 1, 0, b'1', 1, 1),
(20, '2017-03-21 04:20:59', 50, 0, 0, b'1', 1, 1),
(21, '2017-03-21 04:21:18', 80, 0, 0, b'1', 1, 1),
(22, '2017-03-21 04:21:28', 0, 0, 0, b'1', 1, 1),
(23, '2017-03-21 04:25:56', 0, 1, 0, b'1', 1, 1),
(24, '2017-03-21 04:26:17', 0, 1, 1, b'1', 1, 1),
(25, '2017-03-21 04:38:05', 0, 1, 0, b'1', 1, 1),
(26, '2017-03-21 04:38:11', 0, 1, 1, b'1', 1, 1),
(27, '2017-03-21 04:56:35', 0, 0, 1, b'1', 1, 1),
(28, '2017-03-21 04:56:49', 20, 0, 1, b'1', 1, 1),
(29, '2017-03-21 04:57:35', 60, 0, 1, b'1', 1, 1),
(30, '2017-03-21 04:57:48', 100, 0, 1, b'1', 1, 1),
(31, '2017-03-21 05:03:48', 20, 0, 1, b'1', 1, 1),
(32, '2017-03-21 05:05:59', 20, 1, 1, b'1', 1, 1),
(33, '2017-03-21 05:06:05', 20, 1, 0, b'1', 1, 1),
(34, '2017-03-21 05:06:14', 20, 1, 1, b'1', 1, 1),
(35, '2017-03-21 05:06:17', 20, 1, 0, b'1', 1, 1),
(36, '2017-03-21 05:06:18', 20, 0, 0, b'1', 1, 1),
(37, '2017-03-21 05:06:28', 50, 0, 0, b'1', 1, 1),
(38, '2017-03-21 06:01:02', 20, 0, 0, b'1', 1, 1),
(39, '2017-03-21 06:01:11', 20, 0, 0, b'1', 1, 1),
(40, '2017-03-21 08:21:45', 60, 0, 0, b'1', 1, 1),
(41, '2017-03-21 16:18:10', 10, 0, 0, b'1', 1, 1),
(42, '2017-03-21 16:21:45', 10, 1, 0, b'1', 1, 1),
(43, '2017-03-21 16:21:52', 10, 1, 1, b'1', 1, 1),
(44, '2017-03-21 16:26:50', 10, 0, 1, b'1', 1, 1),
(45, '2017-03-21 16:26:59', 10, 0, 1, b'1', 1, 1),
(46, '2017-03-22 06:08:18', 70, 0, 1, b'1', 1, 1),
(47, '2017-03-22 06:10:48', 100, 0, 1, b'1', 1, 1),
(48, '2017-03-22 06:11:20', 100, 0, 1, b'1', 1, 1),
(49, '2017-03-22 06:13:37', 80, 0, 1, b'1', 1, 1),
(50, '2017-03-22 07:34:02', 60, 0, 1, b'1', 1, 1),
(51, '2017-03-22 19:26:48', 60, 1, 1, b'1', 1, 1),
(52, '2017-03-22 19:26:53', 60, 1, 0, b'1', 1, 1),
(53, '2017-03-22 19:26:57', 60, 1, 1, b'1', 1, 1),
(54, '2017-03-22 19:26:59', 60, 0, 1, b'1', 1, 1),
(55, '2017-03-22 19:59:18', 60, 1, 1, b'1', 1, 1),
(56, '2017-03-22 19:59:22', 60, 1, 0, b'1', 1, 1),
(57, '2017-03-22 19:59:27', 60, 1, 1, b'1', 1, 1),
(58, '2017-03-22 20:14:45', 60, 1, 0, b'1', 1, 1),
(59, '2017-03-22 20:14:46', 60, 1, 1, b'1', 1, 1),
(60, '2017-03-22 20:14:53', 60, 1, 0, b'1', 1, 1),
(61, '2017-03-22 20:15:11', 60, 1, 1, b'1', 1, 1),
(62, '2017-03-22 20:19:04', 60, 1, 0, b'1', 1, 1),
(63, '2017-03-22 20:19:06', 60, 1, 1, b'1', 1, 1),
(64, '2017-03-26 15:18:22', 60, 0, 1, b'1', 1, 1),
(65, '2017-03-26 15:18:49', 40, 0, 1, b'1', 1, 1),
(66, '2017-03-26 19:05:14', 80, 0, 1, b'1', 1, 1),
(67, '2017-03-26 19:05:23', 0, 0, 1, b'1', 1, 1),
(68, '2017-03-26 19:05:39', 0, 1, 1, b'1', 1, 1),
(69, '2017-03-26 19:05:43', 0, 1, 0, b'1', 1, 1),
(70, '2017-03-26 19:05:45', 0, 1, 1, b'1', 1, 1),
(71, '2017-03-26 19:05:50', 0, 0, 1, b'1', 1, 1),
(72, '2017-03-26 19:05:56', 70, 0, 1, b'1', 1, 1),
(73, '2017-03-26 19:06:06', 90, 0, 1, b'1', 1, 1),
(74, '2017-03-26 19:06:19', 20, 0, 1, b'1', 1, 1),
(75, '2017-03-26 19:18:19', 60, 0, 1, b'1', 1, 1),
(76, '2017-03-26 19:18:32', 60, 1, 1, b'1', 1, 1),
(77, '2017-03-26 19:18:39', 60, 1, 0, b'1', 1, 1),
(78, '2017-03-26 19:18:41', 60, 1, 1, b'1', 1, 1),
(79, '2017-03-26 19:19:24', 60, 0, 1, b'1', 1, 1),
(80, '2017-03-26 19:24:17', 20, 0, 1, b'1', 1, 1),
(81, '2017-03-26 19:24:29', 20, 1, 1, b'1', 1, 1),
(82, '2017-03-26 19:24:39', 20, 1, 0, b'1', 1, 1),
(83, '2017-03-26 19:24:47', 20, 1, 1, b'1', 1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserPiscinaLog`
--

CREATE TABLE IF NOT EXISTS `UserPiscinaLog` (
`iIDUserPiscinaLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bOpen` tinyint(1) DEFAULT NULL,
  `bAvancado` tinyint(1) DEFAULT NULL,
  `bNewAvancado` tinyint(1) DEFAULT NULL,
  `iIDPiscina` int(11) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserPortaLog`
--

CREATE TABLE IF NOT EXISTS `UserPortaLog` (
`iIDUserPortaLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bOpen` tinyint(1) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `fAutoCloseTime` decimal(10,2) DEFAULT NULL,
  `iIDPorta` int(11) DEFAULT NULL,
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserPortaLog`
--

INSERT INTO `UserPortaLog` (`iIDUserPortaLog`, `dDate`, `bOpen`, `bPending`, `fAutoCloseTime`, `iIDPorta`, `iIDUsuario`) VALUES
(1, '2017-03-21 16:25:46', 1, b'1', 5.00, 1, 1),
(2, '2017-03-21 16:25:56', 1, b'1', 5.00, 1, 1),
(3, '2017-03-21 16:26:31', 1, b'1', 5.00, 1, 1),
(4, '2017-03-22 03:19:05', 1, b'1', 5.00, 1, 2),
(5, '2017-03-22 03:19:50', 1, b'1', 5.00, 1, 3),
(6, '2017-03-22 03:20:17', 1, b'1', 5.00, 1, 2),
(7, '2017-03-22 04:04:48', 1, b'1', 5.00, 1, 2),
(8, '2017-03-22 04:05:02', 1, b'1', 5.00, 1, 3),
(9, '2017-03-22 06:06:28', 1, b'1', 5.00, 1, 1),
(10, '2017-03-22 06:06:34', 1, b'1', 5.00, 1, 1),
(11, '2017-03-22 06:07:14', 1, b'1', 5.00, 1, 1),
(12, '2017-03-22 06:24:40', 1, b'1', 5.00, 1, 2),
(13, '2017-03-22 06:25:07', 1, b'1', 5.00, 1, 2),
(14, '2017-03-22 06:26:38', 1, b'1', 5.00, 1, 2),
(15, '2017-03-22 06:26:56', 1, b'1', 5.00, 1, 2),
(16, '2017-03-22 06:27:12', 1, b'1', 5.00, 1, 2),
(17, '2017-03-22 06:27:27', 0, b'1', 15.00, 1, 1),
(18, '2017-03-22 06:31:34', 1, b'1', 15.00, 1, 2),
(19, '2017-03-22 06:32:10', 1, b'1', 15.00, 1, 1),
(20, '2017-03-22 06:33:45', 1, b'1', 15.00, 1, 1),
(21, '2017-03-22 06:34:11', 0, b'1', 5.00, 1, 1),
(22, '2017-03-22 06:47:20', 1, b'1', 5.00, 1, 1),
(23, '2017-03-22 06:47:43', 1, b'1', 5.00, 1, 1),
(24, '2017-03-22 07:35:26', 1, b'1', 5.00, 1, 1),
(25, '2017-03-22 18:17:56', 1, b'1', 5.00, 1, 3),
(26, '2017-03-22 18:17:57', 1, b'1', 5.00, 1, 3),
(27, '2017-03-22 18:17:58', 1, b'1', 5.00, 1, 3),
(28, '2017-03-22 18:17:59', 1, b'1', 5.00, 1, 3),
(29, '2017-03-22 18:18:00', 1, b'1', 5.00, 1, 3),
(30, '2017-03-22 18:18:01', 1, b'1', 5.00, 1, 3),
(31, '2017-03-22 18:18:03', 1, b'1', 5.00, 1, 3),
(32, '2017-03-22 18:18:08', 1, b'1', 5.00, 1, 3),
(33, '2017-03-22 18:18:08', 1, b'1', 5.00, 1, 3),
(34, '2017-03-22 18:18:10', 1, b'1', 5.00, 1, 3),
(35, '2017-03-22 18:18:34', 1, b'1', 5.00, 1, 3),
(36, '2017-03-22 18:18:35', 1, b'1', 5.00, 1, 3),
(37, '2017-03-22 18:18:37', 1, b'1', 5.00, 1, 3),
(38, '2017-03-22 18:18:38', 1, b'1', 5.00, 1, 3),
(39, '2017-03-22 18:18:39', 1, b'1', 5.00, 1, 3),
(40, '2017-03-22 19:27:59', 1, b'1', 5.00, 1, 1),
(41, '2017-03-22 20:01:34', 1, b'1', 5.00, 1, 1),
(42, '2017-03-22 20:02:08', 1, b'1', 5.00, 1, 3),
(43, '2017-03-22 20:22:07', 1, b'1', 5.00, 1, 2),
(44, '2017-03-22 20:20:08', 1, b'1', 5.00, 1, 3),
(45, '2017-03-22 20:20:24', 1, b'1', 5.00, 1, 3),
(46, '2017-03-22 20:20:42', 1, b'1', 5.00, 1, 3),
(47, '2017-03-26 19:07:47', 1, b'1', 5.00, 1, 1),
(48, '2017-03-26 19:07:54', 1, b'1', 5.00, 1, 1),
(49, '2017-03-26 19:10:17', 1, b'1', 5.00, 1, 2),
(50, '2017-03-26 19:11:04', 1, b'1', 5.00, 1, 2),
(51, '2017-03-26 19:11:23', 0, b'1', 15.00, 1, 1),
(52, '2017-03-26 19:11:34', 1, b'1', 15.00, 1, 2),
(53, '2017-03-26 19:11:57', 0, b'1', 5.00, 1, 1),
(54, '2017-03-26 19:12:03', 1, b'1', 5.00, 1, 1),
(55, '2017-03-26 19:26:35', 1, b'1', 5.00, 1, 1),
(56, '2017-03-26 19:27:05', 1, b'1', 5.00, 1, 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserPortaoLog`
--

CREATE TABLE IF NOT EXISTS `UserPortaoLog` (
`iIDUserPortaoLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fAutoCloseTime` decimal(10,2) DEFAULT NULL,
  `bOpen` tinyint(1) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDPortao` int(11) DEFAULT NULL,
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserPortaoLog`
--

INSERT INTO `UserPortaoLog` (`iIDUserPortaoLog`, `dDate`, `fAutoCloseTime`, `bOpen`, `bPending`, `iIDPortao`, `iIDUsuario`) VALUES
(1, '2017-03-21 08:23:17', 10.00, 1, b'1', 1, 1),
(2, '2017-03-21 16:22:48', 15.00, 0, b'1', 1, 1),
(3, '2017-03-21 16:22:55', 15.00, 0, b'1', 1, 1),
(4, '2017-03-26 19:08:05', 15.00, 1, b'1', 1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserTetoLog`
--

CREATE TABLE IF NOT EXISTS `UserTetoLog` (
`iIDUserTetoLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bOpen` tinyint(1) DEFAULT NULL,
  `bAvancado` tinyint(1) DEFAULT NULL,
  `bNewAvancado` tinyint(1) DEFAULT NULL,
  `iIDTeto` int(11) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserTetoLog`
--

INSERT INTO `UserTetoLog` (`iIDUserTetoLog`, `dDate`, `bOpen`, `bAvancado`, `bNewAvancado`, `iIDTeto`, `bPending`, `iIDUsuario`) VALUES
(1, '2017-03-20 23:32:26', 1, 1, 0, 1, b'1', 1),
(2, '2017-03-20 23:32:36', 1, 1, 0, 1, b'1', 1),
(3, '2017-03-20 23:43:47', 1, 1, 0, 1, b'1', 1),
(4, '2017-03-20 23:45:15', 0, 1, 0, 1, b'1', 1),
(5, '2017-03-20 23:45:26', 0, 1, 0, 1, b'1', 1),
(6, '2017-03-21 00:52:46', 1, 1, 0, 1, b'1', 1),
(7, '2017-03-21 00:52:58', 0, 1, 0, 1, b'1', 1),
(8, '2017-03-21 01:24:38', 1, 1, 0, 1, b'1', 1),
(9, '2017-03-21 01:24:45', 0, 1, 0, 1, b'1', 1),
(10, '2017-03-21 03:36:17', 1, 1, 0, 1, b'1', 1),
(11, '2017-03-21 03:36:27', 0, 1, 0, 1, b'1', 1),
(12, '2017-03-21 05:01:19', 1, 1, 0, 1, b'1', 1),
(13, '2017-03-21 05:01:34', 0, 1, 0, 1, b'1', 1),
(14, '2017-03-21 08:22:51', 1, 1, 0, 1, b'1', 1),
(15, '2017-03-21 08:22:58', 0, 1, 0, 1, b'1', 1),
(16, '2017-03-21 15:19:29', 1, 1, 0, 1, b'1', 1),
(17, '2017-03-21 15:19:39', 1, 1, 0, 1, b'1', 1),
(18, '2017-03-21 15:21:35', 0, 1, 0, 1, b'1', 1),
(19, '2017-03-21 15:21:43', 1, 1, 0, 1, b'1', 1),
(20, '2017-03-21 15:22:09', 0, 1, 0, 1, b'1', 1),
(21, '2017-03-21 16:28:32', 1, 1, 0, 1, b'1', 1),
(22, '2017-03-21 16:28:38', 0, 1, 0, 1, b'1', 1),
(23, '2017-03-22 04:34:43', 1, 1, 0, 1, b'1', 1),
(24, '2017-03-22 06:05:07', 0, 1, 0, 1, b'1', 1),
(25, '2017-03-22 06:05:38', 1, 1, 0, 1, b'1', 1),
(26, '2017-03-22 06:05:46', 0, 1, 0, 1, b'1', 1),
(27, '2017-03-22 19:08:22', 1, 1, 0, 1, b'1', 1),
(28, '2017-03-22 19:08:33', 1, 1, 0, 1, b'1', 1),
(29, '2017-03-22 19:08:49', 0, 1, 0, 1, b'1', 1),
(30, '2017-03-22 19:27:04', 1, 1, 0, 1, b'1', 1),
(31, '2017-03-22 19:27:11', 0, 1, 0, 1, b'1', 1),
(32, '2017-03-22 19:28:29', 1, 1, 0, 1, b'1', 1),
(33, '2017-03-22 19:28:45', 0, 1, 0, 1, b'1', 1),
(34, '2017-03-22 19:36:32', 1, 1, 0, 1, b'1', 1),
(35, '2017-03-22 19:36:39', 0, 1, 0, 1, b'1', 1),
(36, '2017-03-22 19:53:07', 1, 1, 0, 1, b'1', 1),
(37, '2017-03-22 19:53:17', 1, 1, 0, 1, b'1', 1),
(39, '2017-03-22 19:55:17', 0, 1, 0, 1, b'1', 1),
(40, '2017-03-22 19:58:48', 1, 1, 0, 1, b'1', 1),
(41, '2017-03-22 20:00:40', 0, 1, 0, 1, b'1', 1),
(42, '2017-03-22 20:19:25', 1, 1, 0, 1, b'1', 1),
(43, '2017-03-22 20:19:31', 0, 1, 0, 1, b'1', 1),
(44, '2017-03-22 20:22:08', 0, 1, 0, 1, b'1', 1),
(45, '2017-03-22 20:22:11', 1, 1, 0, 1, b'1', 1),
(46, '2017-03-22 20:22:36', 0, 1, 0, 1, b'1', 1),
(47, '2017-03-22 20:22:54', 1, 1, 0, 1, b'1', 1),
(48, '2017-03-22 20:23:24', 0, 1, 0, 1, b'1', 1),
(49, '2017-03-22 20:26:08', 1, 1, 0, 1, b'1', 1),
(50, '2017-03-22 20:26:30', 0, 1, 0, 1, b'1', 1),
(51, '2017-03-22 20:21:38', 1, 1, 0, 1, b'1', 1),
(52, '2017-03-22 20:22:48', 0, 1, 0, 1, b'1', 1),
(53, '2017-03-26 19:08:41', 1, 1, 0, 1, b'1', 1),
(54, '2017-03-26 19:08:50', 0, 1, 0, 1, b'1', 1),
(55, '2017-03-26 19:09:03', 1, 1, 0, 1, b'1', 1),
(56, '2017-03-26 19:09:49', 0, 1, 0, 1, b'1', 1),
(57, '2017-03-26 19:25:48', 1, 1, 0, 1, b'1', 1),
(58, '2017-03-26 19:27:38', 0, 1, 0, 1, b'1', 1),
(59, '2017-03-26 19:47:33', 1, 1, 0, 1, b'1', 1),
(60, '2017-03-26 19:48:26', 0, 1, 0, 1, b'1', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `UserTomadaLog`
--

CREATE TABLE IF NOT EXISTS `UserTomadaLog` (
`iIDUserTomadaLog` bigint(20) NOT NULL,
  `dDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bStatus` tinyint(1) DEFAULT NULL,
  `iIDTomada` int(11) DEFAULT NULL,
  `bPending` bit(1) DEFAULT b'0',
  `iIDUsuario` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `UserTomadaLog`
--

INSERT INTO `UserTomadaLog` (`iIDUserTomadaLog`, `dDate`, `bStatus`, `iIDTomada`, `bPending`, `iIDUsuario`) VALUES
(1, '2017-03-21 02:07:55', 0, 1, b'1', 1),
(2, '2017-03-21 02:08:05', 0, 1, b'1', 1),
(3, '2017-03-21 02:08:18', 1, 1, b'1', 1),
(4, '2017-03-21 02:08:28', 1, 1, b'1', 1),
(5, '2017-03-21 02:35:41', 0, 2, b'1', 1),
(6, '2017-03-21 02:35:51', 0, 2, b'1', 1),
(7, '2017-03-21 03:06:16', 0, 1, b'1', 1),
(8, '2017-03-21 03:06:25', 0, 1, b'1', 1),
(9, '2017-03-21 03:35:34', 1, 1, b'1', 1),
(10, '2017-03-21 03:49:59', 0, 1, b'1', 1),
(11, '2017-03-21 03:50:09', 0, 1, b'1', 1),
(12, '2017-03-21 03:58:25', 0, 2, b'1', 1),
(13, '2017-03-21 04:02:04', 0, 2, b'1', 1),
(14, '2017-03-21 04:14:04', 0, 1, b'1', 1),
(15, '2017-03-21 04:14:14', 0, 1, b'1', 1),
(16, '2017-03-21 04:20:32', 1, 1, b'1', 1),
(17, '2017-03-21 04:20:41', 0, 3, b'1', 1),
(18, '2017-03-21 04:26:43', 1, 3, b'1', 1),
(19, '2017-03-21 04:36:35', 0, 1, b'1', 1),
(20, '2017-03-21 04:36:37', 0, 2, b'1', 1),
(21, '2017-03-21 04:36:39', 0, 3, b'1', 1),
(22, '2017-03-21 04:36:44', 1, 1, b'1', 1),
(23, '2017-03-21 04:36:49', 1, 3, b'1', 1),
(24, '2017-03-21 04:37:05', 1, 2, b'1', 1),
(25, '2017-03-21 05:02:37', 0, 1, b'1', 1),
(26, '2017-03-21 05:03:18', 1, 1, b'1', 1),
(27, '2017-03-21 06:01:23', 0, 1, b'1', 1),
(28, '2017-03-21 06:01:29', 0, 3, b'1', 1),
(29, '2017-03-21 08:22:21', 0, 2, b'1', 1),
(30, '2017-03-21 16:14:22', 1, 1, b'1', 1),
(31, '2017-03-21 16:14:29', 0, 1, b'1', 1),
(32, '2017-03-22 06:06:08', 1, 1, b'1', 1),
(33, '2017-03-22 06:06:15', 1, 2, b'1', 1),
(34, '2017-03-22 06:06:21', 1, 3, b'1', 1),
(35, '2017-03-22 06:18:23', 0, 1, b'1', 1),
(36, '2017-03-22 18:12:43', 0, 1, b'1', 1),
(37, '2017-03-22 18:12:48', 0, 2, b'1', 1),
(38, '2017-03-22 18:12:51', 0, 3, b'1', 1),
(39, '2017-03-22 18:12:55', 1, 3, b'1', 1),
(40, '2017-03-22 18:12:57', 1, 2, b'1', 1),
(41, '2017-03-22 18:12:59', 1, 1, b'1', 1),
(42, '2017-03-22 19:27:23', 0, 1, b'1', 1),
(43, '2017-03-22 19:27:25', 1, 1, b'1', 1),
(44, '2017-03-22 19:59:49', 0, 1, b'1', 1),
(45, '2017-03-22 19:59:55', 0, 3, b'1', 1),
(46, '2017-03-26 19:04:37', 1, 1, b'1', 1),
(47, '2017-03-26 19:04:40', 1, 3, b'1', 1),
(48, '2017-03-26 19:04:44', 0, 1, b'1', 1),
(49, '2017-03-26 19:04:47', 0, 2, b'1', 1),
(50, '2017-03-26 19:04:51', 0, 3, b'1', 1),
(51, '2017-03-26 19:06:35', 1, 1, b'1', 1),
(52, '2017-03-26 19:06:37', 0, 1, b'1', 1),
(53, '2017-03-26 19:12:23', 1, 1, b'1', 1),
(54, '2017-03-26 19:12:25', 1, 2, b'1', 1),
(55, '2017-03-26 19:12:27', 1, 3, b'1', 1),
(56, '2017-03-26 19:25:02', 0, 1, b'1', 1),
(57, '2017-03-26 19:25:06', 0, 2, b'1', 1),
(58, '2017-03-26 19:25:08', 1, 2, b'1', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `Usuarios`
--

CREATE TABLE IF NOT EXISTS `Usuarios` (
`iIDUsuario` int(11) NOT NULL,
  `cNome` text,
  `cRFID` text,
  `cRFID2` text NOT NULL,
  `cUserName` text,
  `cPassword` text,
  `iUserLevel` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `Usuarios`
--

INSERT INTO `Usuarios` (`iIDUsuario`, `cNome`, `cRFID`, `cRFID2`, `cUserName`, `cPassword`, `iUserLevel`) VALUES
(1, 'Sistema', NULL, '', 'admin', 'admin', 99),
(2, 'Rafael Mendes', 'D4449FFC', '54C97EFB', 'Mendes11', 'biboca1234', 99),
(3, 'Balak', '14DF46D5', '', 'balak', 'balak', 99);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Casa`
--
ALTER TABLE `Casa`
 ADD PRIMARY KEY (`iIDCasa`);

--
-- Indexes for table `Comodos`
--
ALTER TABLE `Comodos`
 ADD PRIMARY KEY (`iIDComodo`);

--
-- Indexes for table `DeviceStatus`
--
ALTER TABLE `DeviceStatus`
 ADD PRIMARY KEY (`iIDDeviceStatus`);

--
-- Indexes for table `Janela`
--
ALTER TABLE `Janela`
 ADD PRIMARY KEY (`iIDJanela`), ADD KEY `iIDComodo` (`iIDComodo`), ADD KEY `iIDDeviceStatus` (`iIDDeviceStatus`);

--
-- Indexes for table `Luminosidade`
--
ALTER TABLE `Luminosidade`
 ADD PRIMARY KEY (`iIDLuminosidade`), ADD KEY `iIDComodo` (`iIDComodo`);

--
-- Indexes for table `Piscina`
--
ALTER TABLE `Piscina`
 ADD PRIMARY KEY (`iIDPiscina`);

--
-- Indexes for table `Porta`
--
ALTER TABLE `Porta`
 ADD PRIMARY KEY (`iIDPorta`), ADD KEY `iIDDeviceStatus` (`iIDDeviceStatus`), ADD KEY `iIDComodo` (`iIDComodo`);

--
-- Indexes for table `Portao`
--
ALTER TABLE `Portao`
 ADD PRIMARY KEY (`iIDPortao`), ADD KEY `iIDDeviceStatus` (`iIDDeviceStatus`), ADD KEY `iIDComodo` (`iIDComodo`);

--
-- Indexes for table `Teto`
--
ALTER TABLE `Teto`
 ADD PRIMARY KEY (`iIDTeto`), ADD KEY `iIDComodo` (`iIDComodo`), ADD KEY `iIDDeviceStatus` (`iIDDeviceStatus`);

--
-- Indexes for table `Tomadas`
--
ALTER TABLE `Tomadas`
 ADD PRIMARY KEY (`iIDTomada`), ADD KEY `iIDComodo` (`iIDComodo`);

--
-- Indexes for table `UserAccessLog`
--
ALTER TABLE `UserAccessLog`
 ADD PRIMARY KEY (`iIDUserAccessLog`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserJanelaLog`
--
ALTER TABLE `UserJanelaLog`
 ADD PRIMARY KEY (`iIDUserJanelaLog`), ADD KEY `iIDJanela` (`iIDJanela`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserLuminosidadeLog`
--
ALTER TABLE `UserLuminosidadeLog`
 ADD PRIMARY KEY (`iIDUserLuminosidadeLog`), ADD KEY `iIDLuminosidade` (`iIDLuminosidade`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserPiscinaLog`
--
ALTER TABLE `UserPiscinaLog`
 ADD PRIMARY KEY (`iIDUserPiscinaLog`), ADD KEY `iIDPiscina` (`iIDPiscina`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserPortaLog`
--
ALTER TABLE `UserPortaLog`
 ADD PRIMARY KEY (`iIDUserPortaLog`), ADD KEY `iIDPorta` (`iIDPorta`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserPortaoLog`
--
ALTER TABLE `UserPortaoLog`
 ADD PRIMARY KEY (`iIDUserPortaoLog`), ADD KEY `iIDPortao` (`iIDPortao`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserTetoLog`
--
ALTER TABLE `UserTetoLog`
 ADD PRIMARY KEY (`iIDUserTetoLog`), ADD KEY `iIDTeto` (`iIDTeto`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `UserTomadaLog`
--
ALTER TABLE `UserTomadaLog`
 ADD PRIMARY KEY (`iIDUserTomadaLog`), ADD KEY `iIDTomada` (`iIDTomada`), ADD KEY `iIDUsuario` (`iIDUsuario`);

--
-- Indexes for table `Usuarios`
--
ALTER TABLE `Usuarios`
 ADD PRIMARY KEY (`iIDUsuario`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Casa`
--
ALTER TABLE `Casa`
MODIFY `iIDCasa` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Comodos`
--
ALTER TABLE `Comodos`
MODIFY `iIDComodo` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `DeviceStatus`
--
ALTER TABLE `DeviceStatus`
MODIFY `iIDDeviceStatus` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `Janela`
--
ALTER TABLE `Janela`
MODIFY `iIDJanela` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Luminosidade`
--
ALTER TABLE `Luminosidade`
MODIFY `iIDLuminosidade` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `Piscina`
--
ALTER TABLE `Piscina`
MODIFY `iIDPiscina` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Porta`
--
ALTER TABLE `Porta`
MODIFY `iIDPorta` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Portao`
--
ALTER TABLE `Portao`
MODIFY `iIDPortao` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Teto`
--
ALTER TABLE `Teto`
MODIFY `iIDTeto` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `Tomadas`
--
ALTER TABLE `Tomadas`
MODIFY `iIDTomada` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `UserAccessLog`
--
ALTER TABLE `UserAccessLog`
MODIFY `iIDUserAccessLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `UserJanelaLog`
--
ALTER TABLE `UserJanelaLog`
MODIFY `iIDUserJanelaLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=138;
--
-- AUTO_INCREMENT for table `UserLuminosidadeLog`
--
ALTER TABLE `UserLuminosidadeLog`
MODIFY `iIDUserLuminosidadeLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=84;
--
-- AUTO_INCREMENT for table `UserPiscinaLog`
--
ALTER TABLE `UserPiscinaLog`
MODIFY `iIDUserPiscinaLog` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `UserPortaLog`
--
ALTER TABLE `UserPortaLog`
MODIFY `iIDUserPortaLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=57;
--
-- AUTO_INCREMENT for table `UserPortaoLog`
--
ALTER TABLE `UserPortaoLog`
MODIFY `iIDUserPortaoLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `UserTetoLog`
--
ALTER TABLE `UserTetoLog`
MODIFY `iIDUserTetoLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=61;
--
-- AUTO_INCREMENT for table `UserTomadaLog`
--
ALTER TABLE `UserTomadaLog`
MODIFY `iIDUserTomadaLog` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT for table `Usuarios`
--
ALTER TABLE `Usuarios`
MODIFY `iIDUsuario` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `Janela`
--
ALTER TABLE `Janela`
ADD CONSTRAINT `Janela_ibfk_1` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`),
ADD CONSTRAINT `Janela_ibfk_2` FOREIGN KEY (`iIDDeviceStatus`) REFERENCES `DeviceStatus` (`iIDDeviceStatus`);

--
-- Limitadores para a tabela `Luminosidade`
--
ALTER TABLE `Luminosidade`
ADD CONSTRAINT `Luminosidade_ibfk_1` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`);

--
-- Limitadores para a tabela `Porta`
--
ALTER TABLE `Porta`
ADD CONSTRAINT `Porta_ibfk_1` FOREIGN KEY (`iIDDeviceStatus`) REFERENCES `DeviceStatus` (`iIDDeviceStatus`),
ADD CONSTRAINT `Porta_ibfk_2` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`);

--
-- Limitadores para a tabela `Portao`
--
ALTER TABLE `Portao`
ADD CONSTRAINT `Portao_ibfk_1` FOREIGN KEY (`iIDDeviceStatus`) REFERENCES `DeviceStatus` (`iIDDeviceStatus`),
ADD CONSTRAINT `Portao_ibfk_2` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`);

--
-- Limitadores para a tabela `Teto`
--
ALTER TABLE `Teto`
ADD CONSTRAINT `Teto_ibfk_1` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`),
ADD CONSTRAINT `Teto_ibfk_2` FOREIGN KEY (`iIDDeviceStatus`) REFERENCES `DeviceStatus` (`iIDDeviceStatus`);

--
-- Limitadores para a tabela `Tomadas`
--
ALTER TABLE `Tomadas`
ADD CONSTRAINT `Tomadas_ibfk_1` FOREIGN KEY (`iIDComodo`) REFERENCES `Comodos` (`iIDComodo`);

--
-- Limitadores para a tabela `UserAccessLog`
--
ALTER TABLE `UserAccessLog`
ADD CONSTRAINT `UserAccessLog_ibfk_1` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserJanelaLog`
--
ALTER TABLE `UserJanelaLog`
ADD CONSTRAINT `UserJanelaLog_ibfk_1` FOREIGN KEY (`iIDJanela`) REFERENCES `Janela` (`iIDJanela`),
ADD CONSTRAINT `UserJanelaLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserLuminosidadeLog`
--
ALTER TABLE `UserLuminosidadeLog`
ADD CONSTRAINT `UserLuminosidadeLog_ibfk_1` FOREIGN KEY (`iIDLuminosidade`) REFERENCES `Luminosidade` (`iIDLuminosidade`),
ADD CONSTRAINT `UserLuminosidadeLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserPiscinaLog`
--
ALTER TABLE `UserPiscinaLog`
ADD CONSTRAINT `UserPiscinaLog_ibfk_1` FOREIGN KEY (`iIDPiscina`) REFERENCES `Piscina` (`iIDPiscina`),
ADD CONSTRAINT `UserPiscinaLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserPortaLog`
--
ALTER TABLE `UserPortaLog`
ADD CONSTRAINT `UserPortaLog_ibfk_1` FOREIGN KEY (`iIDPorta`) REFERENCES `Porta` (`iIDPorta`),
ADD CONSTRAINT `UserPortaLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserPortaoLog`
--
ALTER TABLE `UserPortaoLog`
ADD CONSTRAINT `UserPortaoLog_ibfk_1` FOREIGN KEY (`iIDPortao`) REFERENCES `Portao` (`iIDPortao`),
ADD CONSTRAINT `UserPortaoLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserTetoLog`
--
ALTER TABLE `UserTetoLog`
ADD CONSTRAINT `UserTetoLog_ibfk_1` FOREIGN KEY (`iIDTeto`) REFERENCES `Teto` (`iIDTeto`),
ADD CONSTRAINT `UserTetoLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

--
-- Limitadores para a tabela `UserTomadaLog`
--
ALTER TABLE `UserTomadaLog`
ADD CONSTRAINT `UserTomadaLog_ibfk_1` FOREIGN KEY (`iIDTomada`) REFERENCES `Tomadas` (`iIDTomada`),
ADD CONSTRAINT `UserTomadaLog_ibfk_2` FOREIGN KEY (`iIDUsuario`) REFERENCES `Usuarios` (`iIDUsuario`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
