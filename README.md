# Các hệ thống phân tán
Đề tài 10: Xây dựng Middleware tương tác cơ sở dữ liệu và ứng dụng sử dụng

## Cài đặt phần mềm

Cài đặt NodeJS:
https://openplanning.net/11921/cai-dat-nodejs-tren-windows

Cài đặt hệ quản trị CSDL PostgeSQL
https://openplanning.net/10713/cai-dat-co-so-du-lieu-postgresql-tren-windows

## Tạo cơ sở dữ liệu

1. Cấu hình tài khoản

Sau khi cài đặt hệ quản trị CSDL PostgreSQL, tiến hành cài đặt cấu hình tài khoản:

- User: postgres (mặc định)
- Pass: 123456
- Port: 5432 (mặc định)

2. Tạo database và table

Tạo database có tên `HTPT` và table có tên là `SINHVIEN`

```sql
CREATE DATABASE HTPT;

CREATE TABLE SINHVIEN (
	MSV varchar(10) PRIMARY KEY,
	HO varchar(30) NOT NULL,
	TEN varchar(10) NOT NULL,
	NGAYSINH date,
	GIOITINH int default 0,
	QUEQUAN text
);
```

## Chạy webservice chứa middleware

Mở thư mục `Middleware/` trong Command Prompt hoặc bất cứ nơi nào có thể chạy terminal, nhập lệnh `npm start` để cho chạy server


## Chạy ứng dụng Java sử dụng Middleware

Double click vào ứng dụng JAVA-APP.jar và sử dụng.

*Lưu ý:* Bắt buộc phải cho server chứa middleware chạy trước khi chạy ứng dụng java.


## Github

https://github.com/VinhVIP/HTPT-Middleware