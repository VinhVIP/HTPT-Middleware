CREATE TABLE SINHVIEN (
	MSV varchar(10) PRIMARY KEY,
	HO varchar(30) NOT NULL,
	TEN varchar(10) NOT NULL,
	NGAYSINH date,
	GIOITINH int default 0,
	QUEQUAN text
);