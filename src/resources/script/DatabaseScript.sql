-- Tạo database
use master
go
DROP database QuanLyNhaThuoc;
GO
CREATE DATABASE QuanLyNhaThuoc;
GO
USE QuanLyNhaThuoc;
GO
-- Tạo bảng NhaSanXuat
CREATE TABLE NhaSanXuat (
    maNSX VARCHAR(50) PRIMARY KEY,
    tenNSX NVARCHAR(100),
    diaChiNSX NVARCHAR(200),
    soDienThoai VARCHAR(20),
	show bit DEFAULT 1,
);
GO

-- Tạo bảng LoThuoc
CREATE TABLE LoThuoc (
    maLo VARCHAR(50) PRIMARY KEY,
    maNSX VARCHAR(50),
	show bit DEFAULT 1,
    FOREIGN KEY (maNSX) REFERENCES NhaSanXuat(maNSX)
);
GO


-- Tạo bảng Thuoc
CREATE TABLE Thuoc (
    maThuoc VARCHAR(50) PRIMARY KEY,
    maLo VARCHAR(50),
    tenThuoc NVARCHAR(100),
    soLuongTon INT,
    giaBan FLOAT,
    donVi NVARCHAR(50),
    soLuongToiThieu INT,
	maNSX VARCHAR(50),
	show bit DEFAULT 1,
	FOREIGN KEY (maNSX) REFERENCES NhaSanXuat(maNSX)
);
GO
-- Tạo bảng ChiTietLoThuoc
CREATE TABLE ChiTietLoThuoc (
    maLo VARCHAR(50),
    maThuoc VARCHAR(50),
    ngaySanXuat DATE,
    hanSuDung DATE,
	show bit DEFAULT 1,
    PRIMARY KEY (maLo, maThuoc),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo),

);
GO
-- Tạo bảng KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(50) PRIMARY KEY,
    tenKH NVARCHAR(100),
    soDienThoai VARCHAR(20),
    diemTichLuy INT,
	show bit DEFAULT 1,
);
GO
-- Tạo bảng NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(50) PRIMARY KEY,
    tenNV NVARCHAR(100),
    chucVu NVARCHAR(50),
    soDienThoai VARCHAR(20),
	show bit DEFAULT 1,
);
GO
-- Tạo bảng TaiKhoan
CREATE TABLE TaiKhoan(
	maNV VARCHAR(50) PRIMARY KEY,
	matKhau VARCHAR(50),
	show bit DEFAULT 1,
	FOREIGN KEY (maNV)	REFERENCES NhanVien(maNV)
);
GO
-- Tạo bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM VARCHAR(50) PRIMARY KEY,
    mucGiamGia FLOAT,
	ngayApDung DATE,
	ngayKetThuc DATE,
	show bit DEFAULT 1,
);
GO
-- Tạo bảng Thue
CREATE TABLE Thue (
    maThue VARCHAR(50) PRIMARY KEY,
	mucThue FLOAT,
    ngayApDung DATE,
    ngayKetThuc DATE,
	show bit DEFAULT 1,
);
GO
-- Tạo bảng HoaDon
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(50) PRIMARY KEY,
    ngayBan DATE,
    ghiChu NVARCHAR(200),
    maNV VARCHAR(50),
    maKH VARCHAR(50),
	maKM VARCHAR(50),
	maThue VARCHAR(50),
	show bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
	FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue)
);
GO
-- Tạo bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(50),
    maThuoc VARCHAR(50),
    soLuong INT,
	donGia FLOAT,
	show bit DEFAULT 1,
    PRIMARY KEY (maHoaDon, maThuoc),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);
GO
-- Tạo bảng PhieuDoiTra
CREATE TABLE PhieuDoiTra (
    maPhieuDoiTra VARCHAR(50) PRIMARY KEY,
    ngayDoiTra DATE,
	maNV VARCHAR(50),
	maKH VARCHAR(50),
	show bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
GO
-- Tạo bảng ChiTietPhieuDoiTra
CREATE TABLE ChiTietPhieuDoiTra (
    maPhieuDoiTra VARCHAR(50),
    maThuoc VARCHAR(50),
    soLuong INT,
    donGia FLOAT,
    maLo VARCHAR(50),
    lyDo NVARCHAR(200),
	show bit DEFAULT 1,
    PRIMARY KEY (maPhieuDoiTra, maThuoc),
    FOREIGN KEY (maPhieuDoiTra) REFERENCES PhieuDoiTra(maPhieuDoiTra),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);
GO
-- Tạo bảng PhieuDat
CREATE TABLE PhieuDat (
    maPhieuDat VARCHAR(50) PRIMARY KEY,
    maNV VARCHAR(50),
    ngayDat DATE,
    maKH VARCHAR(50),
	show bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
GO
-- Tạo bảng ChiTietPhieuDat
CREATE TABLE ChiTietPhieuDat (
    maPhieuDat VARCHAR(50),
    maThuoc VARCHAR(50),
    tenThuoc NVARCHAR(100),
    soLuong INT,
	show bit DEFAULT 1,
    PRIMARY KEY (maPhieuDat, maThuoc),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDat(maPhieuDat),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);
GO
-- Tạo bảng PhieuNhapThuoc
CREATE TABLE PhieuNhapThuoc (
    maPhieuNhapThuoc VARCHAR(50) PRIMARY KEY,
    maNV VARCHAR(50),
    ngayNhap DATE,
	show bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
GO
-- Tạo bảng ChiTietPhieuNhap
CREATE TABLE ChiTietPhieuNhap (
    maPhieuNhapThuoc VARCHAR(50),
    maLo VARCHAR(50),
	soLuong INT,
	donGia FLOAT,
	show bit DEFAULT 1,
    PRIMARY KEY (maPhieuNhapThuoc, maLo),
    FOREIGN KEY (maPhieuNhapThuoc) REFERENCES PhieuNhapThuoc(maPhieuNhapThuoc),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);
GO

USE QuanLyNhaThuoc;
GO

-- Xóa dữ liệu cũ nếu có
DELETE FROM ChiTietPhieuNhap;
DELETE FROM PhieuNhapThuoc;
DELETE FROM ChiTietPhieuDat;
DELETE FROM PhieuDat;
DELETE FROM ChiTietPhieuDoiTra;
DELETE FROM PhieuDoiTra;
DELETE FROM ChiTietHoaDon;
DELETE FROM HoaDon;
DELETE FROM TaiKhoan;
DELETE FROM ChiTietLoThuoc;
DELETE FROM Thuoc;
DELETE FROM LoThuoc;
DELETE FROM Thue;
DELETE FROM KhuyenMai;
DELETE FROM NhanVien;
DELETE FROM KhachHang;
DELETE FROM NhaSanXuat;
GO

use QuanLyNhaThuoc
go
-- Chèn dữ liệu vào bảng NhaSanXuat
INSERT INTO NhaSanXuat (maNSX, tenNSX, diaChiNSX, soDienThoai) VALUES
('NSX001', N'Công ty Cổ phần Dược Hậu Giang', N'288 Bis Nguyễn Văn Cừ, P. An Hòa, Q. Ninh Kiều, Cần Thơ', '0292123456'),
('NSX002', N'Công ty Cổ phần Dược phẩm Imexpharm', N'Số 4 Đường 30/4, P.1, TP. Cao Lãnh, Đồng Tháp', '0277123456'),
('NSX003', N'Công ty Cổ phần Dược phẩm OPC', N'1017 Hồng Bàng, P.12, Q.6, TP.HCM', '028123456'),
('NSX004', N'Công ty Cổ phần Dược phẩm Traphaco', N'75 Yên Ninh, Ba Đình, Hà Nội', '024123456'),
('NSX005', N'Công ty Cổ phần Dược Mekophar', N'297/5 Lý Thường Kiệt, P.15, Q.11, TP.HCM', '028123457'),
('NSX006', N'Sanofi-Aventis Việt Nam', N'Tòa nhà Mê Linh Point, 2 Ngô Đức Kế, Q.1, TP.HCM', '028123458'),
('NSX007', N'Novartis Việt Nam', N'Tòa nhà Metropolitan, 235 Đồng Khởi, Q.1, TP.HCM', '028123459'),
('NSX008', N'GlaxoSmithKline (GSK) Việt Nam', N'Tòa nhà The Metropolitan, 235 Đồng Khởi, Q.1, TP.HCM', '028123460'),
('NSX009', N'Công ty Dược phẩm Nam Hà', N'415 Hàn Thuyên, Nam Định', '0228123456'),
('NSX010', N'Công ty Cổ phần Pymepharco', N'166-170 Nguyễn Huệ, Tuy Hòa, Phú Yên', '0257123456');
GO

-- Chèn dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (maKH, tenKH, soDienThoai, diemTichLuy) VALUES
('KH001', N'Nguyễn Văn An', '0901234567', 150),
('KH002', N'Trần Thị Bình', '0912345678', 200),
('KH003', N'Lê Văn Cường', '0923456789', 100),
('KH004', N'Phạm Thị Dung', '0934567890', 300),
('KH005', N'Võ Văn Em', '0945678901', 50),
('KH006', N'Hoàng Thị Giang', '0956789012', 180),
('KH007', N'Đặng Văn Hải', '0967890123', 220),
('KH008', N'Bùi Thị Lan', '0978901234', 90),
('KH009', N'Đỗ Văn Minh', '0989012345', 270),
('KH010', N'Ngô Thị Nga', '0990123456', 120),
('KH011', N'Trịnh Văn Phát', '0901234568', 160),
('KH012', N'Mai Thị Quyên', '0912345679', 210),
('KH013', N'Vũ Văn Sơn', '0923456780', 110),
('KH014', N'Lý Thị Tâm', '0934567891', 320),
('KH015', N'Phan Văn Ứng', '0945678902', 60),
('KH016', N'Dương Thị Vân', '0956789013', 190),
('KH017', N'Huỳnh Văn Xuân', '0967890124', 230),
('KH018', N'Châu Thị Yến', '0978901235', 95),
('KH019', N'Đinh Văn Anh', '0989012346', 280),
('KH020', N'Lương Thị Bảo', '0990123457', 130),
('KH021', N'Tống Văn Cẩm', '0901234569', 170),
('KH022', N'Lâm Thị Diệu', '0912345670', 220),
('KH023', N'Thạch Văn Giang', '0923456781', 120),
('KH024', N'Phùng Thị Hằng', '0934567892', 330),
('KH025', N'Quách Văn Khôi', '0945678903', 70),
('KH026', N'Thái Thị Linh', '0956789014', 200),
('KH027', N'Hà Văn Mạnh', '0967890125', 240),
('KH028', N'Lưu Thị Ngọc', '0978901236', 100),
('KH029', N'Đường Văn Phú', '0989012347', 290),
('KH030', N'Mạc Thị Quỳnh', '0990123458', 140),
('KH031', N'Tạ Văn Sáng', '0901234570', 180),
('KH032', N'Tiêu Thị Thảo', '0912345671', 230),
('KH033', N'Chu Văn Uy', '0923456782', 130),
('KH034', N'Trình Thị Vân', '0934567893', 340),
('KH035', N'Diệp Văn Xuân', '0945678904', 80);
GO

-- Chèn dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (maNV, tenNV, chucVu, soDienThoai) VALUES
('NV001', N'Trương Minh Hùng', N'Nhân viên quản lý', '0901111345'),
('NV002', N'Nguyễn Thị Lan Anh', N'Nhân viên bán hàng', '0902222123'),
('NV003', N'Phạm Văn Bình', N'Nhân viên quản lý', '0903333333'),
('NV004', N'Lê Thị Cẩm Tú', N'Nhân viên bán hàng', '0904444678'),
('NV005', N'Võ Đình Danh', N'Nhân viên quản lý', '0904555555'),
('NV006', N'Hoàng Thị Hồng', N'Nhân viên bán hàng', '0905666666'),
('NV007', N'Đặng Minh Khôi', N'Nhân viên quản lý', '0907777777'),
('NV008', N'Bùi Thị Ngọc Mai', N'Nhân viên bán hàng', '0907888888'),
('NV009', N'Trần Quốc Thái', N'Nhân viên quản lý', '0909999999'),
('NV010', N'Ngô Phương Thảo', N'Nhân viên bán hàng', '0900012200');
GO

-- Chèn dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (maNV, matKhau) VALUES
('NV001', 'admin123'),
('NV002', 'duocsi123'),
('NV003', 'duocsi456'),
('NV004', 'banhang123'),
('NV005', 'banhang456'),
('NV006', 'kho123'),
('NV007', 'ketoan123'),
('NV008', 'duocsi789'),
('NV009', 'depchai123'),
('NV010', 'banhang000');
GO

-- Chèn dữ liệu vào bảng KhuyenMai
INSERT INTO KhuyenMai (maKM, mucGiamGia, ngayApDung, ngayKetThuc) VALUES
('KM001', 0.05, '2025-05-01', '2025-05-15'),
('KM002', 0.10, '2025-06-01', '2025-06-15'),
('KM003', 0.15, '2025-07-01', '2025-07-15'),
('KM004', 0.07, '2025-08-01', '2025-08-15'),
('KM005', 0.12, '2025-09-01', '2025-09-15'),
('KM006', 0.05, '2025-05-16', '2025-05-31'),
('KM007', 0.08, '2025-06-16', '2025-06-30'),
('KM008', 0.10, '2025-07-16', '2025-07-31'),
('KM009', 0.06, '2025-08-16', '2025-08-31'),
('KM010', 0.20, '2025-09-16', '2025-09-30');
GO

-- Chèn dữ liệu vào bảng Thue
INSERT INTO Thue (maThue, mucThue, ngayApDung, ngayKetThuc) VALUES
('THUE01', 0.05, '2025-01-01', '2025-12-31'),
('THUE02', 0.08, '2025-01-01', '2025-12-31'),
('THUE03', 0.10, '2025-01-01', '2025-12-31');
GO

-- Chèn dữ liệu vào bảng LoThuoc
INSERT INTO LoThuoc (maLo, maNSX) VALUES
('LO001', 'NSX001'),
('LO002', 'NSX002'),
('LO003', 'NSX003'),
('LO004', 'NSX004'),
('LO005', 'NSX005'),
('LO006', 'NSX006'),
('LO007', 'NSX007'),
('LO008', 'NSX008'),
('LO009', 'NSX009'),
('LO010', 'NSX010'),
('LO011', 'NSX001'),
('LO012', 'NSX002'),
('LO013', 'NSX003'),
('LO014', 'NSX004'),
('LO015', 'NSX005'),
('LO016', 'NSX006'),
('LO017', 'NSX007'),
('LO018', 'NSX008'),
('LO019', 'NSX009'),
('LO020', 'NSX010'),
('LO021', 'NSX001'),
('LO022', 'NSX002'),
('LO023', 'NSX003'),
('LO024', 'NSX004'),
('LO025', 'NSX005');
GO

-- Chèn dữ liệu vào bảng Thuoc
INSERT INTO Thuoc (maThuoc, maLo, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX) VALUES
-- Nhóm thuốc giảm đau, hạ sốt
('T001', 'LO001', N'Paracetamol 500mg', 1000, 5000, N'Hộp', 100, 'NSX001'),
('T002', 'LO001', N'Efferalgan 500mg', 800, 7500, N'Vỉ', 80, 'NSX001'),
('T003', 'LO002', N'Ibuprofen 400mg', 600, 8000, N'Hộp', 60, 'NSX002'),
('T004', 'LO002', N'Aspirin 100mg', 900, 6500, N'Lọ', 90, 'NSX002'),
('T005', 'LO003', N'Mobic 7.5mg', 500, 12000, N'Hộp', 50, 'NSX003'),

-- Nhóm kháng sinh
('T006', 'LO003', N'Amoxicillin 500mg', 700, 15000, N'Hộp', 70, 'NSX003'),
('T007', 'LO004', N'Augmentin 625mg', 400, 25000, N'Vỉ', 40, 'NSX004'),
('T008', 'LO004', N'Cefuroxime 500mg', 350, 22000, N'Hộp', 35, 'NSX004'),
('T009', 'LO005', N'Azithromycin 250mg', 450, 18000, N'Lọ', 45, 'NSX005'),
('T010', 'LO005', N'Ciprofloxacin 500mg', 550, 16000, N'Hộp', 55, 'NSX005'),

-- Nhóm thuốc đau dạ dày
('T011', 'LO006', N'Omeprazole 20mg', 600, 14000, N'Vỉ', 60, 'NSX006'),
('T012', 'LO006', N'Nexium 40mg', 400, 32000, N'Hộp', 40, 'NSX006'),
('T013', 'LO007', N'Pantoprazole 40mg', 450, 28000, N'Lọ', 45, 'NSX007'),
('T014', 'LO007', N'Maalox', 800, 8500, N'Chai', 80, 'NSX007'),
('T015', 'LO008', N'Phosphalugel', 700, 9500, N'Gói', 70, 'NSX008'),

-- Nhóm thuốc trị tiêu chảy
('T016', 'LO008', N'Smecta', 900, 7000, N'Gói', 90, 'NSX008'),
('T017', 'LO009', N'Imodium 2mg', 600, 12500, N'Vỉ', 60, 'NSX009'),
('T018', 'LO009', N'Bioflora', 500, 11000, N'Hộp', 50, 'NSX009'),
('T019', 'LO010', N'Lacteol Fort', 700, 9000, N'Hộp', 70, 'NSX010'),
('T020', 'LO010', N'Enterogermina', 650, 10500, N'Hộp', 65, 'NSX010'),

-- Nhóm vitamin và khoáng chất
('T021', 'LO011', N'Vitamin C 500mg', 1200, 8000, N'Hộp', 120, 'NSX001'),
('T022', 'LO011', N'Vitamin E 400UI', 950, 12000, N'Lọ', 95, 'NSX001'),
('T023', 'LO012', N'Multivitamin', 800, 15000, N'Hộp', 80, 'NSX002'),
('T024', 'LO012', N'Canxi D3', 1000, 13500, N'Chai', 100, 'NSX002'),
('T025', 'LO013', N'Zinc 20mg', 750, 9500, N'Vỉ', 75, 'NSX003'),

-- Nhóm thuốc chống dị ứng
('T026', 'LO013', N'Loratadine 10mg', 850, 7500, N'Hộp', 85, 'NSX003'),
('T027', 'LO014', N'Cetirizine 10mg', 900, 8000, N'Vỉ', 90, 'NSX004'),
('T028', 'LO014', N'Fexofenadine 60mg', 700, 12500, N'Hộp', 70, 'NSX004'),
('T029', 'LO015', N'Claritine', 600, 14000, N'Hộp', 60, 'NSX005'),
('T030', 'LO015', N'Telfast 180mg', 500, 18000, N'Vỉ', 50, 'NSX005'),

-- Nhóm thuốc huyết áp
('T031', 'LO016', N'Amlodipine 5mg', 450, 8500, N'Hộp', 45, 'NSX006'),
('T032', 'LO016', N'Amlodipine 10mg', 400, 10500, N'Vỉ', 40, 'NSX006'),
('T033', 'LO017', N'Bisoprolol 2.5mg', 350, 12000, N'Hộp', 35, 'NSX007'),
('T034', 'LO017', N'Enalapril 5mg', 380, 9500, N'Vỉ', 38, 'NSX007'),
('T035', 'LO018', N'Losartan 50mg', 420, 11000, N'Hộp', 42, 'NSX008'),

-- Nhóm thuốc tiểu đường
('T036', 'LO018', N'Metformin 500mg', 480, 7000, N'Hộp', 48, 'NSX008'),
('T037', 'LO019', N'Metformin 850mg', 450, 8500, N'Vỉ', 45, 'NSX009'),
('T038', 'LO019', N'Glibenclamide 5mg', 400, 6500, N'Hộp', 40, 'NSX009'),
('T039', 'LO020', N'Acarbose 50mg', 350, 13000, N'Vỉ', 35, 'NSX010'),
('T040', 'LO020', N'Januvia 100mg', 300, 28000, N'Hộp', 30, 'NSX010'),

-- Nhóm thuốc giảm mỡ máu
('T041', 'LO021', N'Atorvastatin 10mg', 480, 15000, N'Hộp', 48, 'NSX001'),
('T042', 'LO021', N'Atorvastatin 20mg', 450, 19000, N'Vỉ', 45, 'NSX001'),
('T043', 'LO022', N'Rosuvastatin 10mg', 420, 22000, N'Hộp', 42, 'NSX002'),
('T044', 'LO022', N'Simvastatin 20mg', 400, 14000, N'Vỉ', 40, 'NSX002'),
('T045', 'LO023', N'Lipitor 10mg', 380, 26000, N'Hộp', 38, 'NSX003'),

-- Nhóm thuốc chống viêm
('T046', 'LO023', N'Meloxicam 7.5mg', 500, 11000, N'Hộp', 50, 'NSX003'),
('T047', 'LO024', N'Diclofenac 50mg', 550, 8500, N'Vỉ', 55, 'NSX004'),
('T048', 'LO024', N'Celecoxib 200mg', 450, 16000, N'Hộp', 45, 'NSX004'),
('T049', 'LO025', N'Methylprednisolone 16mg', 400, 18000, N'Vỉ', 40, 'NSX005'),
('T050', 'LO025', N'Prednisone 5mg', 500, 9500, N'Hộp', 50, 'NSX005'),

-- Các thuốc khác
('T051', 'LO001', N'Salbutamol 2mg', 600, 7500, N'Vỉ', 60, 'NSX001'),
('T052', 'LO001', N'Ventolin inhaler', 350, 85000, N'Chai', 35, 'NSX001'),
('T053', 'LO002', N'Seretide 250mcg', 300, 145000, N'Bình', 30, 'NSX002'),
('T054', 'LO002', N'Combivent', 320, 120000, N'Chai', 32, 'NSX002'),
('T055', 'LO003', N'Bromhexine 8mg', 700, 6500, N'Vỉ', 70, 'NSX003'),
('T056', 'LO003', N'Ambroxol 30mg', 750, 7000, N'Hộp', 75, 'NSX003'),
('T057', 'LO004', N'N-acetylcysteine 200mg', 600, 12000, N'Vỉ', 60, 'NSX004'),
('T058', 'LO004', N'Bisolvon', 650, 9500, N'Chai', 65, 'NSX004'),
('T059', 'LO005', N'Fluconazole 150mg', 400, 18000, N'Viên', 40, 'NSX005'),
('T060', 'LO005', N'Ketoconazole 200mg', 450, 16000, N'Hộp', 45, 'NSX005'),
('T061', 'LO006', N'Metronidazole 250mg', 800, 5500, N'Vỉ', 80, 'NSX006'),
('T062', 'LO006', N'Albendazole 400mg', 600, 7500, N'Viên', 60, 'NSX006'),
('T063', 'LO007', N'Mebendazole 500mg', 550, 8000, N'Hộp', 55, 'NSX007'),
('T064', 'LO007', N'Domperidone 10mg', 650, 9500, N'Vỉ', 65, 'NSX007'),
('T065', 'LO008', N'Metoclopramide 10mg', 700, 6000, N'Hộp', 70, 'NSX008'),
('T066', 'LO008', N'Ondansetron 8mg', 500, 14000, N'Vỉ', 50, 'NSX008'),
('T067', 'LO009', N'Dexamethasone 0.5mg', 450, 7500, N'Hộp', 45, 'NSX009'),
('T068', 'LO009', N'Betamethasone 0.5mg', 400, 8500, N'Vỉ', 40, 'NSX009'),
('T069', 'LO010', N'Hydrocortisone 10mg', 350, 9500, N'Tuýp', 35, 'NSX010'),
('T070', 'LO010', N'Triamcinolone 4mg', 300, 12000, N'Hộp', 30, 'NSX010'),
('T071', 'LO011', N'Furosemide 40mg', 450, 6500, N'Vỉ', 45, 'NSX001'),
('T072', 'LO011', N'Spironolactone 25mg', 400, 9500, N'Hộp', 40, 'NSX001'),
('T073', 'LO012', N'Indapamide 1.5mg', 350, 11000, N'Vỉ', 35, 'NSX002'),
('T074', 'LO012', N'Hydrochlorothiazide 25mg', 400, 7500, N'Hộp', 40, 'NSX002'),
('T075', 'LO013', N'Isosorbide dinitrate 10mg', 350, 10500, N'Vỉ', 35, 'NSX003'),
('T076', 'LO013', N'Nitroglycerin 0.5mg', 300, 12500, N'Hộp', 30, 'NSX003'),
('T077', 'LO014', N'Digoxin 0.25mg', 250, 8500, N'Vỉ', 25, 'NSX004'),
('T078', 'LO014', N'Verapamil 40mg', 300, 11000, N'Hộp', 30, 'NSX004'),
('T079', 'LO015', N'Diltiazem 60mg', 280, 12500, N'Vỉ', 28, 'NSX005'),
('T080', 'LO015', N'Atenolol 50mg', 320, 9500, N'Hộp', 32, 'NSX005'),
('T081', 'LO016', N'Propranolol 40mg', 350, 7500, N'Vỉ', 35, 'NSX006'),
('T082', 'LO016', N'Carvedilol 6.25mg', 300, 13500, N'Hộp', 30, 'NSX006'),
('T083', 'LO017', N'Tamsulosin 0.4mg', 250, 16000, N'Vỉ', 25, 'NSX007'),
('T084', 'LO017', N'Sildenafil 50mg', 200, 25000, N'Hộp', 20, 'NSX007'),
('T085', 'LO018', N'Tadalafil 10mg', 180, 28000, N'Vỉ', 18, 'NSX008'),
('T086', 'LO018', N'Finasteride 5mg', 220, 18000, N'Hộp', 22, 'NSX008'),
('T087', 'LO019', N'Ethinylestradiol 35mcg', 300, 12000, N'Vỉ', 30, 'NSX009'),
('T088', 'LO019', N'Medroxyprogesterone 5mg', 280, 14000, N'Hộp', 28, 'NSX009'),
('T089', 'LO020', N'Clomiphene citrate 50mg', 220, 22000, N'Vỉ', 22, 'NSX010'),
('T090', 'LO020', N'Levothyroxine 50mcg', 350, 9500, N'Hộp', 35, 'NSX010'),
('T091', 'LO021', N'Levothyroxine 100mcg', 320, 11000, N'Vỉ', 32, 'NSX001'),
('T092', 'LO021', N'Methimazole 5mg', 280, 13500, N'Hộp', 28, 'NSX001'),
('T093', 'LO022', N'Gliclazide 30mg', 350, 15000, N'Vỉ', 35, 'NSX002'),
('T094', 'LO022', N'Pioglitazone 15mg', 300, 18000, N'Hộp', 30, 'NSX002'),
('T095', 'LO023', N'Gabapentin 300mg', 250, 16000, N'Vỉ', 25, 'NSX003'),
('T096', 'LO023', N'Pregabalin 75mg', 220, 22000, N'Hộp', 22, 'NSX003'),
('T097', 'LO024', N'Alprazolam 0.5mg', 300, 8500, N'Vỉ', 30, 'NSX004'),
('T098', 'LO024', N'Diazepam 5mg', 250, 7000, N'Hộp', 25, 'NSX004'),
('T099', 'LO025', N'Fluoxetine 20mg', 280, 12500, N'Vỉ', 28, 'NSX005'),
('T100', 'LO025', N'Sertraline 50mg', 260, 14000, N'Hộp', 26, 'NSX005'),
('T101', 'LO001', N'Esomeprazole 20mg', 320, 16500, N'Vỉ', 32, 'NSX001'),
('T102', 'LO002', N'Lansoprazole 30mg', 290, 15500, N'Hộp', 29, 'NSX002'),
('T103', 'LO003', N'Rabeprazole 20mg', 270, 17500, N'Vỉ', 27, 'NSX003'),
('T104', 'LO004', N'Famotidine 40mg', 310, 9500, N'Hộp', 31, 'NSX004'),
('T105', 'LO005', N'Ranitidine 150mg', 350, 8500, N'Vỉ', 35, 'NSX005');
GO

-- Chèn dữ liệu vào bảng ChiTietLoThuoc
INSERT INTO ChiTietLoThuoc (maLo, maThuoc, ngaySanXuat, hanSuDung) VALUES
('LO001', 'T001', '2025-01-10', '2027-01-10'),
('LO001', 'T002', '2025-01-15', '2027-01-15'),
('LO001', 'T051', '2025-01-20', '2027-01-20'),
('LO001', 'T052', '2025-01-25', '2027-01-25'),
('LO001', 'T101', '2025-02-01', '2027-02-01'),
('LO002', 'T003', '2025-01-12', '2027-01-12'),
('LO002', 'T004', '2025-01-17', '2027-01-17'),
('LO002', 'T053', '2025-01-22', '2027-01-22'),
('LO002', 'T054', '2025-01-27', '2027-01-27'),
('LO002', 'T102', '2025-02-02', '2027-02-02'),
('LO003', 'T005', '2025-01-05', '2027-01-05'),
('LO003', 'T006', '2025-01-07', '2027-01-07'),
('LO003', 'T055', '2025-01-09', '2027-01-09'),
('LO003', 'T056', '2025-01-11', '2027-01-11'),
('LO003', 'T103', '2025-01-13', '2027-01-13'),

-- Tiếp tục với các lô và thuốc khác
('LO004', 'T007', '2025-02-01', '2027-02-01'),
('LO004', 'T008', '2025-02-03', '2027-02-03'),
('LO005', 'T009', '2025-02-05', '2027-02-05'),
('LO005', 'T010', '2025-02-07', '2027-02-07'),
('LO006', 'T011', '2025-02-09', '2027-02-09'),
('LO006', 'T012', '2025-02-11', '2027-02-11'),
('LO007', 'T013', '2025-02-13', '2027-02-13'),
('LO007', 'T014', '2025-02-15', '2027-02-15'),
('LO008', 'T015', '2025-02-17', '2027-02-17'),
('LO008', 'T016', '2025-02-19', '2027-02-19'),
('LO009', 'T017', '2025-02-21', '2027-02-21'),
('LO009', 'T018', '2025-02-23', '2027-02-23'),
('LO010', 'T019', '2025-02-25', '2027-02-25'),
('LO010', 'T020', '2025-02-27', '2027-02-27'),

-- Thêm các chi tiết lô thuốc khác
('LO011', 'T021', '2025-03-01', '2027-03-01'),
('LO011', 'T022', '2025-03-03', '2027-03-03'),
('LO012', 'T023', '2025-03-05', '2027-03-05'),
('LO012', 'T024', '2025-03-07', '2027-03-07'),
('LO013', 'T025', '2025-03-09', '2027-03-09'),
('LO013', 'T026', '2025-03-11', '2027-03-11'),
('LO014', 'T027', '2025-03-13', '2027-03-13'),
('LO014', 'T028', '2025-03-15', '2027-03-15'),
('LO015', 'T029', '2025-03-17', '2027-03-17'),
('LO015', 'T030', '2025-03-19', '2027-03-19'),
('LO016', 'T031', '2025-03-21', '2027-03-21'),
('LO016', 'T032', '2025-03-23', '2027-03-23'),
('LO017', 'T033', '2025-03-25', '2027-03-25'),
('LO017', 'T034', '2025-03-27', '2027-03-27'),
('LO018', 'T035', '2025-03-29', '2027-03-29'),
('LO018', 'T036', '2025-03-31', '2027-03-31'),
('LO019', 'T037', '2025-04-02', '2027-04-02'),
('LO019', 'T038', '2025-04-04', '2027-04-04'),
('LO020', 'T039', '2025-04-06', '2027-04-06'),
('LO020', 'T040', '2025-04-08', '2027-04-08'),
('LO021', 'T041', '2025-04-10', '2027-04-10'),
('LO021', 'T042', '2025-04-12', '2027-04-12'),
('LO022', 'T043', '2025-04-14', '2027-04-14'),
('LO022', 'T044', '2025-04-16', '2027-04-16'),
('LO023', 'T045', '2025-04-18', '2027-04-18'),
('LO023', 'T046', '2025-04-20', '2027-04-20'),
('LO024', 'T047', '2025-04-22', '2027-04-22'),
('LO024', 'T048', '2025-04-24', '2027-04-24'),
('LO025', 'T049', '2025-04-26', '2027-04-26'),
('LO025', 'T050', '2025-04-28', '2027-04-28');
GO

-- Chèn dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (maHoaDon, ngayBan, ghiChu, maNV, maKH, maKM, maThue) VALUES
('HD001', '2025-05-02', N'Khách hàng mua thuốc cho gia đình', 'NV004', 'KH001', 'KM001', 'THUE01'),
('HD002', '2025-05-05', N'Khách hàng mua thuốc theo toa bác sĩ', 'NV009', 'KH005', 'KM001', 'THUE01'),
('HD003', '2025-05-08', N'Khách hàng mua thuốc định kỳ hàng tháng', 'NV002', 'KH010', 'KM001', 'THUE01'),
('HD004', '2025-05-12', N'Khách hàng mua thuốc cho người thân', 'NV004', 'KH015', 'KM001', 'THUE01'),
('HD005', '2025-05-15', N'Khách hàng mua thuốc cho bệnh mãn tính', 'NV009', 'KH020', 'KM001', 'THUE02'),
('HD006', '2025-05-18', N'Khách hàng mua thuốc theo đơn', 'NV003', 'KH025', 'KM006', 'THUE01'),
('HD007', '2025-05-22', N'Khách hàng mua thuốc không theo đơn', 'NV005', 'KH030', 'KM006', 'THUE01'),
('HD008', '2025-05-25', N'Khách hàng mua vitamin tăng sức đề kháng', 'NV002', 'KH002', 'KM006', 'THUE01'),
('HD009', '2025-05-28', N'Khách hàng mua thuốc chữa cảm cúm', 'NV010', 'KH007', 'KM006', 'THUE01'),
('HD010', '2025-05-31', N'Khách hàng mua thuốc kháng sinh', 'NV008', 'KH012', 'KM006', 'THUE02'),

('HD011', '2025-06-03', N'Khách hàng mua thuốc hạ sốt', 'NV005', 'KH017', 'KM002', 'THUE01'),
('HD012', '2025-06-06', N'Khách hàng mua thuốc điều trị dị ứng', 'NV003', 'KH022', 'KM002', 'THUE01'),
('HD013', '2025-06-09', N'Khách hàng mua thuốc đau dạ dày', 'NV010', 'KH027', 'KM002', 'THUE01'),
('HD014', '2025-06-12', N'Khách hàng mua thuốc trị táo bón', 'NV002', 'KH032', 'KM002', 'THUE01'),
('HD015', '2025-06-15', N'Khách hàng mua thuốc theo toa bác sĩ', 'NV004', 'KH003', 'KM002', 'THUE02'),
('HD016', '2025-06-18', N'Khách hàng mua thuốc chữa viêm họng', 'NV009', 'KH008', 'KM007', 'THUE01'),
('HD017', '2025-06-21', N'Khách hàng mua thuốc giảm đau', 'NV008', 'KH013', 'KM007', 'THUE01'),
('HD018', '2025-06-24', N'Khách hàng mua thuốc hạ huyết áp', 'NV005', 'KH018', 'KM007', 'THUE01'),
('HD019', '2025-06-27', N'Khách hàng mua thuốc tiểu đường', 'NV003', 'KH023', 'KM007', 'THUE01'),
('HD020', '2025-06-30', N'Khách hàng mua thuốc giảm mỡ máu', 'NV010', 'KH028', 'KM007', 'THUE02'),

('HD021', '2025-07-03', N'Khách hàng mua thuốc chống dị ứng', 'NV002', 'KH033', 'KM003', 'THUE01'),
('HD022', '2025-07-06', N'Khách hàng mua thuốc giảm đau khớp', 'NV004', 'KH004', 'KM003', 'THUE01'),
('HD023', '2025-07-09', N'Khách hàng mua thuốc trị viêm xoang', 'NV009', 'KH009', 'KM003', 'THUE01'),
('HD024', '2025-07-12', N'Khách hàng mua thuốc chữa đau răng', 'NV008', 'KH014', 'KM003', 'THUE01'),
('HD025', '2025-07-15', N'Khách hàng mua vitamin tổng hợp', 'NV005', 'KH019', 'KM003', 'THUE02'),
('HD026', '2025-07-18', N'Khách hàng mua thuốc ho', 'NV003', 'KH024', 'KM008', 'THUE01'),
('HD027', '2025-07-21', N'Khách hàng mua thuốc mũi', 'NV010', 'KH029', 'KM008', 'THUE01'),
('HD028', '2025-07-24', N'Khách hàng mua thuốc theo đơn', 'NV002', 'KH034', 'KM008', 'THUE01'),
('HD029', '2025-07-27', N'Khách hàng mua thuốc trị nấm', 'NV004', 'KH001', 'KM008', 'THUE01'),
('HD030', '2025-07-30', N'Khách hàng mua thuốc kháng sinh', 'NV009', 'KH005', 'KM008', 'THUE02'),

('HD031', '2025-08-02', N'Khách hàng mua thuốc chữa đau bụng', 'NV008', 'KH010', 'KM004', 'THUE01'),
('HD032', '2025-08-05', N'Khách hàng mua thuốc trị tiêu chảy', 'NV005', 'KH015', 'KM004', 'THUE01'),
('HD033', '2025-08-08', N'Khách hàng mua thuốc theo toa bác sĩ', 'NV003', 'KH020', 'KM004', 'THUE01'),
('HD034', '2025-08-11', N'Khách hàng mua thuốc tim mạch', 'NV010', 'KH025', 'KM004', 'THUE01'),
('HD035', '2025-08-14', N'Khách hàng mua thuốc mua định kỳ', 'NV002', 'KH030', 'KM004', 'THUE02'),
('HD036', '2025-08-17', N'Khách hàng mua thuốc giảm mỡ máu', 'NV004', 'KH002', 'KM009', 'THUE01'),
('HD037', '2025-08-20', N'Khách hàng mua thuốc kháng viêm', 'NV009', 'KH007', 'KM009', 'THUE01'),
('HD038', '2025-08-23', N'Khách hàng mua thuốc đường tiêu hóa', 'NV008', 'KH012', 'KM009', 'THUE01'),
('HD039', '2025-08-26', N'Khách hàng mua thuốc kháng histamine', 'NV005', 'KH017', 'KM009', 'THUE01'),
('HD040', '2025-08-29', N'Khách hàng mua thuốc chống buồn nôn', 'NV003', 'KH022', 'KM009', 'THUE02'),

('HD041', '2025-09-01', N'Khách hàng mua thuốc giảm đau', 'NV010', 'KH027', 'KM005', 'THUE01'),
('HD042', '2025-09-04', N'Khách hàng mua thuốc kháng sinh', 'NV002', 'KH032', 'KM005', 'THUE01'),
('HD043', '2025-09-07', N'Khách hàng mua thuốc chữa dị ứng', 'NV004', 'KH003', 'KM005', 'THUE01'),
('HD044', '2025-09-10', N'Khách hàng mua thuốc hạ sốt', 'NV009', 'KH008', 'KM005', 'THUE01'),
('HD045', '2025-09-13', N'Khách hàng mua thuốc theo đơn', 'NV008', 'KH013', 'KM005', 'THUE02'),
('HD046', '2025-09-16', N'Khách hàng mua thuốc trị đau đầu', 'NV005', 'KH018', 'KM010', 'THUE01'),
('HD047', '2025-09-19', N'Khách hàng mua thuốc mua định kỳ', 'NV003', 'KH023', 'KM010', 'THUE01'),
('HD048', '2025-09-22', N'Khách hàng mua thuốc điều trị đau khớp', 'NV010', 'KH028', 'KM010', 'THUE01'),
('HD049', '2025-09-25', N'Khách hàng mua thuốc giảm mỡ máu', 'NV002', 'KH033', 'KM010', 'THUE01'),
('HD050', '2025-09-28', N'Khách hàng mua thuốc theo toa bác sĩ', 'NV004', 'KH004', 'KM010', 'THUE02');
GO

-- Chèn dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, soLuong, donGia) VALUES
-- Hóa đơn 1
('HD001', 'T001', 2, 5000),
('HD001', 'T021', 1, 8000),
('HD001', 'T026', 1, 7500),

-- Hóa đơn 2
('HD002', 'T006', 1, 15000),
('HD002', 'T011', 2, 14000),

-- Hóa đơn 3
('HD003', 'T031', 2, 8500),
('HD003', 'T036', 1, 7000),
('HD003', 'T041', 1, 15000),

-- Hóa đơn 4
('HD004', 'T002', 1, 7500),
('HD004', 'T014', 1, 8500),
('HD004', 'T022', 2, 12000),

-- Hóa đơn 5
('HD005', 'T007', 1, 25000),
('HD005', 'T029', 2, 14000),

-- Thêm các chi tiết hóa đơn khác
('HD006', 'T003', 2, 8000),
('HD006', 'T016', 3, 7000),

('HD007', 'T009', 1, 18000),
('HD007', 'T020', 1, 10500),
('HD007', 'T023', 1, 15000),

('HD008', 'T021', 3, 8000),
('HD008', 'T022', 2, 12000),
('HD008', 'T024', 1, 13500),

('HD009', 'T001', 2, 5000),
('HD009', 'T006', 1, 15000),
('HD009', 'T056', 1, 7000),

('HD010', 'T007', 1, 25000),
('HD010', 'T008', 1, 22000),
('HD010', 'T010', 1, 16000),

('HD011', 'T001', 2, 5000),
('HD011', 'T002', 1, 7500),
('HD011', 'T004', 1, 6500),

('HD012', 'T026', 1, 7500),
('HD012', 'T027', 1, 8000),
('HD012', 'T030', 1, 18000),

('HD013', 'T011', 1, 14000),
('HD013', 'T012', 1, 32000),
('HD013', 'T014', 1, 8500),

('HD014', 'T016', 2, 7000),
('HD014', 'T018', 1, 11000),
('HD014', 'T020', 1, 10500),

('HD015', 'T031', 2, 8500),
('HD015', 'T033', 1, 12000),
('HD015', 'T035', 1, 11000),

('HD016', 'T006', 1, 15000),
('HD016', 'T056', 1, 7000),
('HD016', 'T055', 2, 6500),

('HD017', 'T046', 1, 11000),
('HD017', 'T047', 2, 8500),
('HD017', 'T003', 1, 8000),

('HD018', 'T031', 2, 8500),
('HD018', 'T032', 1, 10500),
('HD018', 'T034', 1, 9500),

('HD019', 'T036', 2, 7000),
('HD019', 'T037', 1, 8500),
('HD019', 'T040', 1, 28000),

('HD020', 'T041', 1, 15000),
('HD020', 'T043', 1, 22000),
('HD020', 'T045', 1, 26000),

('HD021', 'T026', 1, 7500),
('HD021', 'T028', 1, 12500),
('HD021', 'T029', 1, 14000),

('HD022', 'T046', 2, 11000),
('HD022', 'T048', 1, 16000),
('HD022', 'T050', 1, 9500),

('HD023', 'T055', 2, 6500),
('HD023', 'T056', 1, 7000),
('HD023', 'T057', 1, 12000),

('HD024', 'T001', 1, 5000),
('HD024', 'T047', 1, 8500),
('HD024', 'T049', 1, 18000),

('HD025', 'T021', 2, 8000),
('HD025', 'T023', 1, 15000),
('HD025', 'T025', 1, 9500),

('HD026', 'T055', 2, 6500),
('HD026', 'T056', 1, 7000),
('HD026', 'T058', 1, 9500),

('HD027', 'T057', 1, 12000),
('HD027', 'T058', 1, 9500),
('HD027', 'T055', 1, 6500),

('HD028', 'T031', 1, 8500),
('HD028', 'T036', 1, 7000),
('HD028', 'T041', 1, 15000),
('HD028', 'T046', 1, 11000),

('HD029', 'T059', 1, 18000),
('HD029', 'T060', 1, 16000),

('HD030', 'T006', 1, 15000),
('HD030', 'T008', 1, 22000),
('HD030', 'T009', 1, 18000),

('HD031', 'T011', 2, 14000),
('HD031', 'T013', 1, 28000),
('HD031', 'T014', 1, 8500),

('HD032', 'T016', 2, 7000),
('HD032', 'T017', 1, 12500),
('HD032', 'T019', 1, 9000),

('HD033', 'T036', 1, 7000),
('HD033', 'T039', 1, 13000),
('HD033', 'T042', 1, 19000),

('HD034', 'T031', 1, 8500),
('HD034', 'T033', 1, 12000),
('HD034', 'T076', 1, 12500),
('HD034', 'T078', 1, 11000),

('HD035', 'T041', 1, 15000),
('HD035', 'T044', 1, 14000),
('HD035', 'T036', 1, 7000),
('HD035', 'T038', 1, 6500),

('HD036', 'T043', 1, 22000),
('HD036', 'T044', 1, 14000),
('HD036', 'T045', 1, 26000),

('HD037', 'T046', 2, 11000),
('HD037', 'T047', 1, 8500),
('HD037', 'T048', 1, 16000),

('HD038', 'T011', 1, 14000),
('HD038', 'T016', 1, 7000),
('HD038', 'T020', 1, 10500),

('HD039', 'T026', 2, 7500),
('HD039', 'T027', 1, 8000),
('HD039', 'T029', 1, 14000),

('HD040', 'T064', 1, 9500),
('HD040', 'T065', 1, 6000),
('HD040', 'T066', 1, 14000);

GO
-- Thêm chi tiết hóa đơn cho hóa đơn HD041 - HD050
INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, soLuong, donGia) VALUES
-- HD041 (8 sản phẩm)
('HD041', 'T023', 2, 35000),
('HD041', 'T045', 1, 72500),
('HD041', 'T012', 3, 12000),
('HD041', 'T078', 1, 98000),
('HD041', 'T056', 2, 45000),
('HD041', 'T089', 1, 125000),
('HD041', 'T034', 1, 65000),
('HD041', 'T067', 2, 28000),

-- HD042 (6 sản phẩm)
('HD042', 'T015', 2, 42000),
('HD042', 'T032', 1, 85000),
('HD042', 'T058', 3, 18000),
('HD042', 'T091', 1, 135000),
('HD042', 'T007', 2, 26000),
('HD042', 'T063', 1, 75000),

-- HD043 (9 sản phẩm)
('HD043', 'T019', 1, 58000),
('HD043', 'T037', 2, 32000),
('HD043', 'T052', 1, 145000),
('HD043', 'T084', 1, 92000),
('HD043', 'T021', 3, 15000),
('HD043', 'T048', 2, 48000),
('HD043', 'T073', 1, 105000),
('HD043', 'T095', 1, 78000),
('HD043', 'T005', 2, 22000),

-- HD044 (5 sản phẩm)
('HD044', 'T042', 1, 62000),
('HD044', 'T081', 2, 38000),
('HD044', 'T027', 1, 115000),
('HD044', 'T069', 3, 25000),
('HD044', 'T053', 1, 95000),

-- HD045 (7 sản phẩm)
('HD045', 'T011', 2, 32000),
('HD045', 'T038', 1, 85000),
('HD045', 'T074', 1, 118000),
('HD045', 'T029', 3, 28000),
('HD045', 'T047', 1, 75000),
('HD045', 'T062', 2, 42000),
('HD045', 'T093', 1, 155000),

-- HD046 (10 sản phẩm)
('HD046', 'T003', 2, 18000),
('HD046', 'T024', 1, 65000),
('HD046', 'T041', 1, 95000),
('HD046', 'T057', 3, 22000),
('HD046', 'T082', 1, 112000),
('HD046', 'T018', 2, 28000),
('HD046', 'T035', 1, 78000),
('HD046', 'T064', 1, 48000),
('HD046', 'T099', 2, 32000),
('HD046', 'T076', 1, 125000),

-- HD047 (6 sản phẩm)
('HD047', 'T008', 2, 25000),
('HD047', 'T031', 1, 78000),
('HD047', 'T059', 3, 19000),
('HD047', 'T086', 1, 95000),
('HD047', 'T046', 2, 45000),
('HD047', 'T071', 1, 88000),

-- HD048 (8 sản phẩm)
('HD048', 'T013', 1, 35000),
('HD048', 'T028', 2, 42000),
('HD048', 'T055', 1, 68000),
('HD048', 'T079', 1, 115000),
('HD048', 'T004', 3, 15000),
('HD048', 'T039', 1, 75000),
('HD048', 'T072', 2, 58000),
('HD048', 'T097', 1, 135000),

-- HD049 (7 sản phẩm)
('HD049', 'T017', 2, 28000),
('HD049', 'T043', 1, 95000),
('HD049', 'T068', 1, 48000),
('HD049', 'T092', 1, 145000),
('HD049', 'T025', 3, 22000),
('HD049', 'T051', 2, 65000),
('HD049', 'T087', 1, 118000),

-- HD050 (9 sản phẩm)
('HD050', 'T006', 2, 19000),
('HD050', 'T033', 1, 85000),
('HD050', 'T054', 1, 78000),
('HD050', 'T017', 2, 28000);

GO
-- Insert dữ liệu vào bảng PhieuDat
INSERT INTO PhieuDat (maPhieuDat, maNV, ngayDat, maKH) VALUES
('PD001', 'NV004', '2025-09-01', 'KH001'),
('PD002', 'NV005', '2025-09-02', 'KH004'),
('PD003', 'NV009', '2025-09-03', 'KH007'),
('PD004', 'NV010', '2025-09-05', 'KH012'),
('PD005', 'NV004', '2025-09-06', 'KH015'),
('PD006', 'NV005', '2025-09-08', 'KH019'),
('PD007', 'NV009', '2025-09-10', 'KH024'),
('PD008', 'NV010', '2025-09-11', 'KH028'),
('PD009', 'NV004', '2025-09-13', 'KH002'),
('PD010', 'NV005', '2025-09-14', 'KH006'),
('PD011', 'NV009', '2025-09-16', 'KH010'),
('PD012', 'NV010', '2025-09-17', 'KH014'),
('PD013', 'NV004', '2025-09-19', 'KH018'),
('PD014', 'NV005', '2025-09-20', 'KH022'),
('PD015', 'NV009', '2025-09-22', 'KH026'),
('PD016', 'NV010', '2025-09-23', 'KH030'),
('PD017', 'NV004', '2025-09-25', 'KH034'),
('PD018', 'NV005', '2025-09-26', 'KH003'),
('PD019', 'NV009', '2025-09-28', 'KH009'),
('PD020', 'NV010', '2025-09-29', 'KH016');
GO




-- Insert dữ liệu vào bảng ChiTietPhieuDat
-- Phiếu đặt 1: Thuốc giảm đau và vitamin
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD001', 'T001', N'Paracetamol 500mg', 5),
('PD001', 'T021', N'Vitamin C 500mg', 3),
('PD001', 'T026', N'Loratadine 10mg', 2);
GO

-- Phiếu đặt 2: Kháng sinh và thuốc dạ dày
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD002', 'T006', N'Amoxicillin 500mg', 4),
('PD002', 'T011', N'Omeprazole 20mg', 3),
('PD002', 'T014', N'Maalox', 2);
GO

-- Phiếu đặt 3: Thuốc tiêu chảy
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD003', 'T016', N'Smecta', 6),
('PD003', 'T017', N'Imodium 2mg', 2),
('PD003', 'T019', N'Lacteol Fort', 3);
GO

-- Phiếu đặt 4: Thuốc huyết áp và tim mạch
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD004', 'T031', N'Amlodipine 5mg', 3),
('PD004', 'T033', N'Bisoprolol 2.5mg', 2),
('PD004', 'T041', N'Atorvastatin 10mg', 2);
GO

-- Phiếu đặt 5: Thuốc tiểu đường
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD005', 'T036', N'Metformin 500mg', 5),
('PD005', 'T038', N'Glibenclamide 5mg', 3),
('PD005', 'T024', N'Canxi D3', 2);
GO

-- Phiếu đặt 6: Thuốc chống dị ứng và vitamin
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD006', 'T027', N'Cetirizine 10mg', 4),
('PD006', 'T029', N'Claritine', 2),
('PD006', 'T022', N'Vitamin E 400UI', 3);
GO

-- Phiếu đặt 7: Kháng sinh cao cấp
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD007', 'T007', N'Augmentin 625mg', 3),
('PD007', 'T009', N'Azithromycin 250mg', 2),
('PD007', 'T012', N'Nexium 40mg', 2);
GO

-- Phiếu đặt 8: Thuốc ho và đường hô hấp
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD008', 'T055', N'Bromhexine 8mg', 4),
('PD008', 'T056', N'Ambroxol 30mg', 3),
('PD008', 'T051', N'Salbutamol 2mg', 2),
('PD008', 'T052', N'Ventolin inhaler', 1);
GO

-- Phiếu đặt 9: Thuốc giảm đau và chống viêm
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD009', 'T003', N'Ibuprofen 400mg', 3),
('PD009', 'T046', N'Meloxicam 7.5mg', 2),
('PD009', 'T047', N'Diclofenac 50mg', 3);
GO

-- Phiếu đặt 10: Thuốc dạ dày cao cấp
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD010', 'T012', N'Nexium 40mg', 2),
('PD010', 'T013', N'Pantoprazole 40mg', 2),
('PD010', 'T101', N'Esomeprazole 20mg', 3);
GO

-- Phiếu đặt 11: Vitamin tổng hợp
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD011', 'T023', N'Multivitamin', 5),
('PD011', 'T024', N'Canxi D3', 4),
('PD011', 'T025', N'Zinc 20mg', 3);
GO

-- Phiếu đặt 12: Thuốc giảm mỡ máu
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD012', 'T042', N'Atorvastatin 20mg', 3),
('PD012', 'T043', N'Rosuvastatin 10mg', 2),
('PD012', 'T044', N'Simvastatin 20mg', 2);
GO

-- Phiếu đặt 13: Thuốc nấm và ký sinh trùng
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD013', 'T059', N'Fluconazole 150mg', 2),
('PD013', 'T062', N'Albendazole 400mg', 4),
('PD013', 'T063', N'Mebendazole 500mg', 3);
GO

-- Phiếu đặt 14: Thuốc giảm đau hạ sốt
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD014', 'T001', N'Paracetamol 500mg', 8),
('PD014', 'T002', N'Efferalgan 500mg', 5),
('PD014', 'T004', N'Aspirin 100mg', 3);
GO

-- Phiếu đặt 15: Thuốc lợi tiểu và tim mạch
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD015', 'T071', N'Furosemide 40mg', 3),
('PD015', 'T072', N'Spironolactone 25mg', 2),
('PD015', 'T080', N'Atenolol 50mg', 2);
GO

-- Phiếu đặt 16: Thuốc chống nôn
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD016', 'T064', N'Domperidone 10mg', 4),
('PD016', 'T065', N'Metoclopramide 10mg', 3),
('PD016', 'T066', N'Ondansetron 8mg', 2);
GO

-- Phiếu đặt 17: Thuốc tiểu đường combo
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD017', 'T037', N'Metformin 850mg', 4),
('PD017', 'T039', N'Acarbose 50mg', 2),
('PD017', 'T093', N'Gliclazide 30mg', 2);
GO

-- Phiếu đặt 18: Thuốc hen suyễn
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD018', 'T052', N'Ventolin inhaler', 2),
('PD018', 'T053', N'Seretide 250mcg', 1),
('PD018', 'T054', N'Combivent', 1);
GO

-- Phiếu đặt 19: Thuốc tiêu chảy và men vi sinh
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD019', 'T016', N'Smecta', 8),
('PD019', 'T018', N'Bioflora', 4),
('PD019', 'T020', N'Enterogermina', 3);
GO

-- Phiếu đặt 20: Thuốc huyết áp combo
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong) VALUES
('PD020', 'T032', N'Amlodipine 10mg', 3),
('PD020', 'T034', N'Enalapril 5mg', 2),
('PD020', 'T035', N'Losartan 50mg', 2),
('PD020', 'T041', N'Atorvastatin 10mg', 2);


GO

-- Insert dữ liệu vào bảng PhieuNhapThuoc
INSERT INTO PhieuNhapThuoc (maPhieuNhapThuoc, maNV, ngayNhap) VALUES
('PNT001', 'NV006', '2025-08-01'),
('PNT002', 'NV003', '2025-08-05'),
('PNT003', 'NV006', '2025-08-10'),
('PNT004', 'NV008', '2025-08-15'),
('PNT005', 'NV009', '2025-08-20'),
('PNT006', 'NV001', '2025-08-25'),
('PNT007', 'NV002', '2025-09-01'),
('PNT008', 'NV004', '2025-09-05'),
('PNT009', 'NV005', '2025-09-10'),
('PNT010', 'NV007', '2025-09-15'),
('PNT011', 'NV007', '2025-09-18'),
('PNT012', 'NV008', '2025-09-20'),
('PNT013', 'NV009', '2025-09-22'),
('PNT014', 'NV002', '2025-09-24'),
('PNT015', 'NV001', '2025-09-26');
GO

-- Insert dữ liệu vào bảng ChiTietPhieuNhap
-- Phiếu nhập 1: Nhập lô thuốc giảm đau và vitamin
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT001', 'LO001', 500, 3500),
('PNT001', 'LO011', 600, 5500);
GO

-- Phiếu nhập 2: Nhập lô kháng sinh
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT002', 'LO003', 300, 10000),
('PNT002', 'LO004', 250, 17000),
('PNT002', 'LO005', 200, 12000);
GO

-- Phiếu nhập 3: Nhập lô thuốc dạ dày
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT003', 'LO006', 400, 9000),
('PNT003', 'LO007', 300, 6000);
GO

-- Phiếu nhập 4: Nhập lô thuốc tiêu chảy và men vi sinh
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT004', 'LO008', 500, 5000),
('PNT004', 'LO009', 350, 8000),
('PNT004', 'LO010', 400, 6500);
GO

-- Phiếu nhập 5: Nhập lô vitamin và khoáng chất
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT005', 'LO011', 700, 5500),
('PNT005', 'LO012', 500, 10000),
('PNT005', 'LO013', 450, 6500);
GO

-- Phiếu nhập 6: Nhập lô thuốc chống dị ứng
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT006', 'LO013', 400, 5000),
('PNT006', 'LO014', 350, 9000),
('PNT006', 'LO015', 300, 12000);
GO

-- Phiếu nhập 7: Nhập lô thuốc huyết áp
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT007', 'LO016', 300, 6000),
('PNT007', 'LO017', 250, 8000),
('PNT007', 'LO018', 280, 7500);
GO

-- Phiếu nhập 8: Nhập lô thuốc tiểu đường
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT008', 'LO018', 320, 5000),
('PNT008', 'LO019', 280, 9500),
('PNT008', 'LO020', 220, 20000);
GO

-- Phiếu nhập 9: Nhập lô thuốc giảm mỡ máu
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT009', 'LO021', 300, 11000),
('PNT009', 'LO022', 280, 16000),
('PNT009', 'LO023', 250, 18000);
GO

-- Phiếu nhập 10: Nhập lô thuốc chống viêm
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT010', 'LO023', 350, 8000),
('PNT010', 'LO024', 300, 11000),
('PNT010', 'LO025', 280, 13000);
GO

-- Phiếu nhập 11: Nhập bổ sung lô giảm đau phổ biến
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT011', 'LO001', 800, 3500),
('PNT011', 'LO002', 600, 5500);
GO

-- Phiếu nhập 12: Nhập bổ sung lô kháng sinh
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT012', 'LO003', 400, 10000),
('PNT012', 'LO004', 300, 17000);
GO

-- Phiếu nhập 13: Nhập bổ sung lô thuốc dạ dày cao cấp
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT013', 'LO006', 500, 9000),
('PNT013', 'LO007', 400, 6000);
GO

-- Phiếu nhập 14: Nhập lô thuốc ho và đường hô hấp
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT014', 'LO001', 400, 3500),
('PNT014', 'LO002', 350, 5500),
('PNT014', 'LO003', 300, 10000),
('PNT014', 'LO004', 200, 17000);
GO

-- Phiếu nhập 15: Nhập bổ sung nhiều lô khác nhau
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia) VALUES
('PNT015', 'LO005', 300, 12000),
('PNT015', 'LO010', 350, 6500),
('PNT015', 'LO015', 250, 12000),
('PNT015', 'LO020', 180, 20000);
GO


-- Insert dữ liệu vào bảng PhieuDoiTra
INSERT INTO PhieuDoiTra (maPhieuDoiTra, ngayDoiTra, maNV, maKH) VALUES
('PDT001', '2025-09-03', 'NV002', 'KH001'),
('PDT002', '2025-09-07', 'NV003', 'KH004'),
('PDT003', '2025-09-09', 'NV008', 'KH007'),
('PDT004', '2025-09-12', 'NV002', 'KH012'),
('PDT005', '2025-09-14', 'NV003', 'KH015'),
('PDT006', '2025-09-16', 'NV008', 'KH002'),
('PDT007', '2025-09-18', 'NV002', 'KH019'),
('PDT008', '2025-09-21', 'NV003', 'KH024'),
('PDT009', '2025-09-23', 'NV008', 'KH006'),
('PDT010', '2025-09-25', 'NV002', 'KH028'),
('PDT011', '2025-09-26', 'NV003', 'KH010'),
('PDT012', '2025-09-27', 'NV008', 'KH014'),
('PDT013', '2025-09-28', 'NV002', 'KH022'),
('PDT014', '2025-09-29', 'NV003', 'KH018'),
('PDT015', '2025-09-30', 'NV008', 'KH026');
GO

-- Insert dữ liệu vào bảng ChiTietPhieuDoiTra
-- Phiếu đổi trả 1: Thuốc hết hạn
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT001', 'T001', 2, 5000, 'LO001', N'Thuốc sắp hết hạn sử dụng'),
('PDT001', 'T021', 1, 8000, 'LO011', N'Thuốc sắp hết hạn sử dụng');
GO

-- Phiếu đổi trả 2: Dị ứng thuốc
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT002', 'T006', 1, 15000, 'LO003', N'Khách hàng bị dị ứng với thuốc');
GO

-- Phiếu đổi trả 3: Thuốc không phù hợp
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT003', 'T016', 2, 7000, 'LO008', N'Thuốc không có hiệu quả như mong đợi'),
('PDT003', 'T017', 1, 12500, 'LO009', N'Bác sĩ đổi đơn thuốc');
GO

-- Phiếu đổi trả 4: Mua nhầm thuốc
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT004', 'T031', 1, 8500, 'LO016', N'Khách hàng mua nhầm liều lượng'),
('PDT004', 'T041', 1, 15000, 'LO021', N'Bác sĩ kê đơn thuốc khác');
GO

-- Phiếu đổi trả 5: Thuốc bị lỗi
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT005', 'T036', 2, 7000, 'LO018', N'Vỉ thuốc bị móp méo');
GO

-- Phiếu đổi trả 6: Dị ứng và đổi thuốc
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT006', 'T027', 2, 8000, 'LO014', N'Khách hàng bị buồn ngủ sau khi uống'),
('PDT006', 'T029', 1, 14000, 'LO015', N'Đổi sang thuốc chống dị ứng thế hệ mới');
GO

-- Phiếu đổi trả 7: Mua thừa
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT007', 'T007', 1, 25000, 'LO004', N'Khách hàng mua thừa, chưa sử dụng'),
('PDT007', 'T012', 1, 32000, 'LO006', N'Mua trùng với đơn thuốc cũ');
GO

-- Phiếu đổi trả 8: Thuốc không đúng chỉ định
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT008', 'T052', 1, 85000, 'LO001', N'Bác sĩ đổi phác đồ điều trị'),
('PDT008', 'T055', 1, 6500, 'LO003', N'Thuốc không phù hợp với triệu chứng');
GO

-- Phiếu đổi trả 9: Tác dụng phụ
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT009', 'T003', 1, 8000, 'LO002', N'Khách hàng bị đau dạ dày sau khi uống'),
('PDT009', 'T046', 1, 11000, 'LO023', N'Gây tác dụng phụ không mong muốn');
GO

-- Phiếu đổi trả 10: Thuốc sắp hết hạn
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT010', 'T056', 2, 7000, 'LO003', N'Phát hiện thuốc còn hạn dùng ngắn'),
('PDT010', 'T058', 1, 9500, 'LO004', N'Thuốc sắp hết hạn sử dụng');
GO

-- Phiếu đổi trả 11: Mua nhầm
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT011', 'T023', 2, 15000, 'LO012', N'Khách hàng mua nhầm loại vitamin'),
('PDT011', 'T024', 1, 13500, 'LO012', N'Đổi sang sản phẩm phù hợp hơn');
GO

-- Phiếu đổi trả 12: Không dung nạp thuốc
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT012', 'T042', 1, 19000, 'LO021', N'Khách hàng bị đau cơ sau khi dùng'),
('PDT012', 'T043', 1, 22000, 'LO022', N'Không dung nạp với thuốc');
GO

-- Phiếu đổi trả 13: Bác sĩ thay đổi đơn
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT013', 'T037', 2, 8500, 'LO019', N'Bác sĩ điều chỉnh liều lượng'),
('PDT013', 'T039', 1, 13000, 'LO020', N'Thay đổi phác đồ điều trị tiểu đường');
GO

-- Phiếu đổi trả 14: Thuốc bị lỗi bao bì
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT014', 'T011', 2, 14000, 'LO006', N'Bao bì thuốc bị rách'),
('PDT014', 'T013', 1, 28000, 'LO007', N'Lọ thuốc bị vỡ seal niêm phong');
GO

-- Phiếu đổi trả 15: Khách mua thừa và đổi trả
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT015', 'T026', 3, 7500, 'LO013', N'Khách hàng khỏi bệnh, còn thừa thuốc'),
('PDT015', 'T022', 1, 12000, 'LO011', N'Mua thừa, chưa mở hộp');
GO



-- Test dataset

-- 1
SELECT * FROM NhaSanXuat;

-- 2
SELECT * FROM LoThuoc;

-- 3
SELECT * FROM ChiTietLoThuoc;

-- 4
SELECT * FROM Thuoc;

-- 5
SELECT * FROM ChiTietPhieuDat;

-- 6
SELECT * FROM PhieuDat; 

-- 7
SELECT * FROM ChiTietPhieuNhap;

-- 8
SELECT * FROM PhieuNhapThuoc; 

-- 9
SELECT * FROM ChiTietPhieuDoiTra;

-- 10
SELECT * FROM PhieuDoiTra;

-- 11
SELECT * FROM ChiTietHoaDon;

-- 12
SELECT * FROM HoaDon;

-- 13
SELECT * FROM KhachHang;

-- 14
SELECT * FROM KhuyenMai;

-- 15
SELECT * FROM Thue;

-- 16
SELECT * FROM NhanVien;

-- 17
SELECT * FROM TaiKhoan;

