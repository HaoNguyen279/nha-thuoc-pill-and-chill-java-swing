USE master
GO

-- Kiểm tra nếu database tồn tại thì xóa (để làm sạch hoàn toàn)
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QuanLyNhaThuoc')
BEGIN
    ALTER DATABASE QuanLyNhaThuoc SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyNhaThuoc;
END
GO

CREATE DATABASE QuanLyNhaThuoc;
GO

USE QuanLyNhaThuoc;
GO

-- ===============================================
-- TẠO CẤU TRÚC BẢNG
-- ===============================================

-- Tạo bảng NhaSanXuat
CREATE TABLE NhaSanXuat (
    maNSX VARCHAR(50) PRIMARY KEY,
    tenNSX NVARCHAR(100),
    diaChiNSX NVARCHAR(200),
    soDienThoai VARCHAR(20),
	isActive bit DEFAULT 1,
);
GO

-- Tạo bảng LoThuoc
CREATE TABLE LoThuoc (
    maLo VARCHAR(50) PRIMARY KEY,
    maNSX VARCHAR(50),
	isActive bit DEFAULT 1,
    FOREIGN KEY (maNSX) REFERENCES NhaSanXuat(maNSX)
);
GO

-- Tạo bảng Thuoc
CREATE TABLE Thuoc (
    maThuoc VARCHAR(50) PRIMARY KEY,
    tenThuoc NVARCHAR(100),
    soLuongTon INT DEFAULT 0, -- Tổng số lượng tồn
    giaBan FLOAT,
    donVi NVARCHAR(50),
    soLuongToiThieu INT,
	maNSX VARCHAR(50),
	isActive bit DEFAULT 1,
	FOREIGN KEY (maNSX) REFERENCES NhaSanXuat(maNSX)
);
GO

-- Tạo bảng ChiTietLoThuoc (Chi tiết tồn kho theo lô)
CREATE TABLE ChiTietLoThuoc (
    maLo VARCHAR(50),
    maThuoc VARCHAR(50),
    ngaySanXuat DATE,
    hanSuDung DATE,
    soLuong INT NOT NULL,
    giaNhap FLOAT NOT NULL,
    isActive BIT DEFAULT 1,
    PRIMARY KEY (maLo, maThuoc),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);
GO

-- Tạo bảng KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(50) PRIMARY KEY,
    tenKH NVARCHAR(100),
    soDienThoai VARCHAR(20),
    diemTichLuy INT,
	isActive bit DEFAULT 1,
);
GO

-- TẠO BẢNG CHUCVU
CREATE TABLE ChucVu (
    maChucVu VARCHAR(50) PRIMARY KEY,
    tenChucVu NVARCHAR(100) NOT NULL,
	isActive bit DEFAULT 1,
);
GO

-- TẠO BẢNG NHANVIEN
CREATE TABLE NhanVien (
    maNV VARCHAR(50) PRIMARY KEY,
    tenNV NVARCHAR(100),
    maChucVu VARCHAR(50), 
    soDienThoai VARCHAR(20),
	isActive bit DEFAULT 1,
	FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
);
GO

-- Tạo bảng TaiKhoan
CREATE TABLE TaiKhoan(
	maNV VARCHAR(50) PRIMARY KEY,
	matKhau VARCHAR(50),
	isActive bit DEFAULT 1,
	FOREIGN KEY (maNV)	REFERENCES NhanVien(maNV)
);
GO

-- Tạo bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM VARCHAR(50) PRIMARY KEY,
    mucGiamGia FLOAT,
	ngayApDung DATE,
	ngayKetThuc DATE,
	isActive bit DEFAULT 1
);
GO

-- TẠO BẢNG HOADON
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(50) PRIMARY KEY,
    ngayBan DATE,
    ghiChu NVARCHAR(200),
    maNV VARCHAR(50),
    maKH VARCHAR(50),
	maKM VARCHAR(50),
	giaTriThue FLOAT, 
	tenLoaiThue NVARCHAR(50), 
	isActive bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
	FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);
GO

-- Tạo bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(50),
    maThuoc VARCHAR(50),
    maLo VARCHAR(50),
    soLuong INT,
	donGia FLOAT,
	isActive bit DEFAULT 1,
    PRIMARY KEY (maHoaDon, maThuoc, maLo),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
	FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);
GO

-- Tạo bảng PhieuDoiTra
CREATE TABLE PhieuDoiTra (
    maPhieuDoiTra VARCHAR(50) PRIMARY KEY,
    ngayDoiTra DATE,
	maNV VARCHAR(50),
	maKH VARCHAR(50),
	isActive bit DEFAULT 1,
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
	isActive bit DEFAULT 1,
    PRIMARY KEY (maPhieuDoiTra, maThuoc, maLo),
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
	ghiChu NVARCHAR(200),
    isReceived BIT DEFAULT 0, -- 0: Chưa nhận, 1: Đã nhận
	isActive bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
GO

-- Tạo bảng ChiTietPhieuDat
CREATE TABLE ChiTietPhieuDat (
    maPhieuDat VARCHAR(50),
    maThuoc VARCHAR(50),
    maLo VARCHAR(50), 
    tenThuoc NVARCHAR(100),
    soLuong INT,
	isActive bit DEFAULT 1,
    PRIMARY KEY (maPhieuDat, maThuoc, maLo),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDat(maPhieuDat),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
	FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);
GO

-- Tạo bảng PhieuNhapThuoc
CREATE TABLE PhieuNhapThuoc (
    maPhieuNhapThuoc VARCHAR(50) PRIMARY KEY,
    maNV VARCHAR(50),
    ngayNhap DATE,
	isActive bit DEFAULT 1,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);
GO

-- Tạo bảng ChiTietPhieuNhap
CREATE TABLE ChiTietPhieuNhap (
    maPhieuNhapThuoc VARCHAR(50),
    maLo VARCHAR(50),
	maThuoc VARCHAR(50),
	soLuong INT,
	donGia FLOAT,
	isActive bit DEFAULT 1,
    PRIMARY KEY (maPhieuNhapThuoc, maLo, maThuoc),
    FOREIGN KEY (maPhieuNhapThuoc) REFERENCES PhieuNhapThuoc(maPhieuNhapThuoc),
	FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc),
    FOREIGN KEY (maLo) REFERENCES LoThuoc(maLo)
);
GO

-- ===============================================
-- CHÈN DỮ LIỆU MẪU
-- ===============================================

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

-- Chèn dữ liệu vào bảng ChucVu
INSERT INTO ChucVu (maChucVu, tenChucVu) VALUES
('QLY', N'Quản Lý'),
('BHA', N'Nhân Viên Bán Hàng'),
('KHO', N'Nhân Viên Kho'),
('KTO', N'Kế Toán');
GO

-- Chèn dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (maNV, tenNV, maChucVu, soDienThoai) VALUES
('NV001', N'Trương Minh Hùng', 'QLY', '0901111345'),
('NV002', N'Nguyễn Thị Lan Anh', 'BHA', '0902222123'),
('NV003', N'Phạm Văn Bình', 'QLY', '0903333333'),
('NV004', N'Lê Thị Cẩm Tú', 'BHA', '0904444678'),
('NV005', N'Võ Đình Danh', 'QLY', '0904555555'),
('NV006', N'Hoàng Thị Hồng', 'BHA', '0905666666'),
('NV007', N'Đặng Minh Khôi', 'QLY', '0907777777'),
('NV008', N'Bùi Thị Ngọc Mai', 'BHA', '0907888888'),
('NV009', N'Trần Quốc Thái', 'QLY', '0909999999'),
('NV010', N'Ngô Phương Thảo', 'BHA', '0900012200');
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

-- Chèn dữ liệu vào bảng LoThuoc
INSERT INTO LoThuoc (maLo, maNSX) VALUES
('LO001', 'NSX001'), ('LO002', 'NSX002'), ('LO003', 'NSX003'), ('LO004', 'NSX004'),
('LO005', 'NSX005'), ('LO006', 'NSX006'), ('LO007', 'NSX007'), ('LO008', 'NSX008'),
('LO009', 'NSX009'), ('LO010', 'NSX010'), ('LO011', 'NSX001'), ('LO012', 'NSX002'),
('LO013', 'NSX003'), ('LO014', 'NSX004'), ('LO015', 'NSX005'), ('LO016', 'NSX006'),
('LO017', 'NSX007'), ('LO018', 'NSX008'), ('LO019', 'NSX009'), ('LO020', 'NSX010'),
('LO021', 'NSX001'), ('LO022', 'NSX002'), ('LO023', 'NSX003'), ('LO024', 'NSX004'),
('LO025', 'NSX005'), ('LO026', 'NSX001'), ('LO027', 'NSX002'), ('LO028', 'NSX003'),
('LO029', 'NSX004'), ('LO030', 'NSX005'), ('LO031', 'NSX001'), ('LO032', 'NSX002'),
('LO033', 'NSX003'), ('LO034', 'NSX004'), ('LO035', 'NSX005'), ('LO999', 'NSX001'); 
GO

-- Chèn dữ liệu bảng Thuoc
INSERT INTO Thuoc (maThuoc, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive) VALUES
('T001', N'Paracetamol 500mg', 0, 5000, N'Viên', 100, 'NSX001', 1),
('T002', N'Efferalgan 500mg', 0, 7500, N'Viên', 80, 'NSX001', 1),
('T003', N'Ibuprofen 400mg', 0, 8000, N'Viên', 60, 'NSX002', 1),
('T004', N'Aspirin 100mg', 0, 6500, N'Viên', 90, 'NSX002', 1),
('T005', N'Mobic 7.5mg', 0, 12000, N'Viên', 50, 'NSX003', 1),
('T006', N'Amoxicillin 500mg', 0, 15000, N'Viên', 70, 'NSX003', 1),
('T007', N'Augmentin 625mg', 0, 25000, N'Viên', 40, 'NSX004', 1),
('T008', N'Cefuroxime 500mg', 0, 22000, N'Viên', 35, 'NSX004', 1),
('T009', N'Azithromycin 250mg', 0, 18000, N'Viên', 45, 'NSX005', 1),
('T010', N'Ciprofloxacin 500mg', 0, 16000, N'Viên', 55, 'NSX005', 1),
('T011', N'Omeprazole 20mg', 0, 14000, N'Viên', 60, 'NSX006', 1),
('T012', N'Nexium 40mg', 0, 32000, N'Viên', 40, 'NSX006', 1),
('T013', N'Pantoprazole 40mg', 0, 28000, N'Viên', 45, 'NSX007', 1),
('T014', N'Maalox', 0, 8500, N'Chai', 80, 'NSX007', 1),
('T015', N'Phosphalugel', 0, 9500, N'Gói', 70, 'NSX008', 1),
('T016', N'Smecta', 0, 7000, N'Gói', 90, 'NSX008', 1),
('T017', N'Imodium 2mg', 0, 12500, N'Viên', 60, 'NSX009', 1),
('T018', N'Bioflora', 0, 11000, N'Gói', 50, 'NSX009', 1),
('T019', N'Lacteol Fort', 0, 9000, N'Gói', 70, 'NSX010', 1),
('T020', N'Enterogermina', 0, 10500, N'Ống', 65, 'NSX010', 1),
('T021', N'Vitamin C 500mg', 0, 8000, N'Viên', 120, 'NSX001', 1),
('T022', N'Vitamin E 400UI', 0, 12000, N'Viên', 95, 'NSX001', 1),
('T023', N'Multivitamin', 0, 15000, N'Viên', 80, 'NSX002', 1),
('T024', N'Canxi D3', 0, 13500, N'Viên', 100, 'NSX002', 1),
('T025', N'Zinc 20mg', 0, 9500, N'Viên', 75, 'NSX003', 1),
('T026', N'Loratadine 10mg', 0, 7500, N'Viên', 85, 'NSX003', 1),
('T027', N'Cetirizine 10mg', 0, 8000, N'Viên', 90, 'NSX004', 1),
('T028', N'Fexofenadine 60mg', 0, 12500, N'Viên', 70, 'NSX004', 1),
('T029', N'Claritine', 0, 14000, N'Viên', 60, 'NSX005', 1),
('T030', N'Telfast 180mg', 0, 18000, N'Viên', 50, 'NSX005', 1),
('T031', N'Amlodipine 5mg', 0, 8500, N'Viên', 45, 'NSX006', 1),
('T032', N'Amlodipine 10mg', 0, 10500, N'Viên', 40, 'NSX006', 1),
('T033', N'Bisoprolol 2.5mg', 0, 12000, N'Viên', 35, 'NSX007', 1),
('T034', N'Enalapril 5mg', 0, 9500, N'Viên', 38, 'NSX007', 1),
('T035', N'Losartan 50mg', 0, 11000, N'Viên', 42, 'NSX008', 1),
('T036', N'Metformin 500mg', 0, 7000, N'Viên', 48, 'NSX008', 1),
('T037', N'Metformin 850mg', 0, 8500, N'Viên', 45, 'NSX009', 1),
('T038', N'Glibenclamide 5mg', 0, 6500, N'Viên', 40, 'NSX009', 1),
('T039', N'Acarbose 50mg', 0, 13000, N'Viên', 35, 'NSX010', 1),
('T040', N'Januvia 100mg', 0, 28000, N'Viên', 30, 'NSX010', 1),
('T041', N'Atorvastatin 10mg', 0, 15000, N'Viên', 48, 'NSX001', 1),
('T042', N'Atorvastatin 20mg', 0, 19000, N'Viên', 45, 'NSX001', 1),
('T043', N'Rosuvastatin 10mg', 0, 22000, N'Viên', 42, 'NSX002', 1),
('T044', N'Simvastatin 20mg', 0, 14000, N'Viên', 40, 'NSX002', 1),
('T045', N'Lipitor 10mg', 0, 26000, N'Viên', 38, 'NSX003', 1),
('T046', N'Meloxicam 7.5mg', 0, 11000, N'Viên', 50, 'NSX003', 1),
('T047', N'Diclofenac 50mg', 0, 8500, N'Viên', 55, 'NSX004', 1),
('T048', N'Celecoxib 200mg', 0, 16000, N'Viên', 45, 'NSX004', 1),
('T049', N'Methylprednisolone 16mg', 0, 18000, N'Viên', 40, 'NSX005', 1),
('T050', N'Prednisone 5mg', 0, 9500, N'Viên', 50, 'NSX005', 1),
('T051', N'Salbutamol 2mg', 0, 7500, N'Viên', 60, 'NSX001', 1),
('T052', N'Ventolin inhaler', 0, 85000, N'Bình', 35, 'NSX001', 1),
('T053', N'Seretide 250mcg', 0, 145000, N'Bình', 30, 'NSX002', 1),
('T054', N'Combivent', 0, 120000, N'Bình', 32, 'NSX002', 1),
('T055', N'Bromhexine 8mg', 0, 6500, N'Viên', 70, 'NSX003', 1),
('T056', N'Ambroxol 30mg', 0, 7000, N'Viên', 75, 'NSX003', 1),
('T057', N'N-acetylcysteine 200mg', 0, 12000, N'Viên', 60, 'NSX004', 1),
('T058', N'Bisolvon', 0, 9500, N'Chai', 65, 'NSX004', 1),
('T059', N'Fluconazole 150mg', 0, 18000, N'Viên', 40, 'NSX005', 1),
('T060', N'Ketoconazole 200mg', 0, 16000, N'Viên', 45, 'NSX005', 1),
('T061', N'Metronidazole 250mg', 0, 5500, N'Viên', 80, 'NSX006', 1),
('T062', N'Albendazole 400mg', 0, 7500, N'Viên', 60, 'NSX006', 1),
('T063', N'Mebendazole 500mg', 0, 8000, N'Viên', 55, 'NSX007', 1),
('T064', N'Domperidone 10mg', 0, 9500, N'Viên', 65, 'NSX007', 1),
('T065', N'Metoclopramide 10mg', 0, 6000, N'Viên', 70, 'NSX008', 1),
('T066', N'Ondansetron 8mg', 0, 14000, N'Viên', 50, 'NSX008', 1),
('T067', N'Dexamethasone 0.5mg', 0, 7500, N'Viên', 45, 'NSX009', 1),
('T068', N'Betamethasone 0.5mg', 0, 8500, N'Viên', 40, 'NSX009', 1),
('T069', N'Hydrocortisone 10mg', 0, 9500, N'Tuýp', 35, 'NSX010', 1),
('T070', N'Triamcinolone 4mg', 0, 12000, N'Viên', 30, 'NSX010', 1),
('T071', N'Furosemide 40mg', 0, 6500, N'Viên', 45, 'NSX001', 1),
('T072', N'Spironolactone 25mg', 0, 9500, N'Viên', 40, 'NSX001', 1),
('T073', N'Indapamide 1.5mg', 0, 11000, N'Viên', 35, 'NSX002', 1),
('T074', N'Hydrochlorothiazide 25mg', 0, 7500, N'Viên', 40, 'NSX002', 1),
('T075', N'Isosorbide dinitrate 10mg', 0, 10500, N'Viên', 35, 'NSX003', 1),
('T076', N'Nitroglycerin 0.5mg', 0, 12500, N'Viên', 30, 'NSX003', 1),
('T077', N'Digoxin 0.25mg', 0, 8500, N'Viên', 25, 'NSX004', 1),
('T078', N'Verapamil 40mg', 0, 11000, N'Viên', 30, 'NSX004', 1),
('T079', N'Diltiazem 60mg', 0, 12500, N'Viên', 28, 'NSX005', 1),
('T080', N'Atenolol 50mg', 0, 9500, N'Viên', 32, 'NSX005', 1),
('T081', N'Propranolol 40mg', 0, 7500, N'Viên', 35, 'NSX006', 1),
('T082', N'Carvedilol 6.25mg', 0, 13500, N'Viên', 30, 'NSX006', 1),
('T083', N'Tamsulosin 0.4mg', 0, 16000, N'Viên', 25, 'NSX007', 1),
('T084', N'Sildenafil 50mg', 0, 25000, N'Viên', 20, 'NSX007', 1),
('T085', N'Tadalafil 10mg', 0, 28000, N'Viên', 18, 'NSX008', 1),
('T086', N'Finasteride 5mg', 0, 18000, N'Viên', 22, 'NSX008', 1),
('T087', N'Ethinylestradiol 35mcg', 0, 12000, N'Viên', 30, 'NSX009', 1),
('T088', N'Medroxyprogesterone 5mg', 0, 14000, N'Viên', 28, 'NSX009', 1),
('T089', N'Clomiphene citrate 50mg', 0, 22000, N'Viên', 22, 'NSX010', 1),
('T090', N'Levothyroxine 50mcg', 0, 9500, N'Viên', 35, 'NSX010', 1),
('T091', N'Levothyroxine 100mcg', 0, 11000, N'Viên', 32, 'NSX001', 1),
('T092', N'Methimazole 5mg', 0, 13500, N'Viên', 28, 'NSX001', 1),
('T093', N'Gliclazide 30mg', 0, 15000, N'Viên', 35, 'NSX002', 1),
('T094', N'Pioglitazone 15mg', 0, 18000, N'Viên', 30, 'NSX002', 1),
('T095', N'Gabapentin 300mg', 0, 16000, N'Viên', 25, 'NSX003', 1),
('T096', N'Pregabalin 75mg', 0, 22000, N'Viên', 22, 'NSX003', 1),
('T097', N'Alprazolam 0.5mg', 0, 8500, N'Viên', 30, 'NSX004', 1),
('T098', N'Diazepam 5mg', 0, 7000, N'Viên', 25, 'NSX004', 1),
('T099', N'Fluoxetine 20mg', 0, 12500, N'Viên', 28, 'NSX005', 1),
('T100', N'Sertraline 50mg', 0, 14000, N'Viên', 26, 'NSX005', 1),
('T101', N'Esomeprazole 20mg', 0, 16500, N'Viên', 32, 'NSX001', 1),
('T102', N'Lansoprazole 30mg', 0, 15500, N'Viên', 29, 'NSX002', 1),
('T103', N'Rabeprazole 20mg', 0, 17500, N'Viên', 27, 'NSX003', 1),
('T104', N'Famotidine 40mg', 0, 9500, N'Viên', 31, 'NSX004', 1),
('T105', N'Ranitidine 150mg', 0, 8500, N'Viên', 35, 'NSX005', 1);
GO

-- Chèn dữ liệu vào bảng ChiTietLoThuoc
INSERT INTO ChiTietLoThuoc (maLo, maThuoc, ngaySanXuat, hanSuDung, soLuong, giaNhap)
VALUES
('LO001', 'T001', '2025-01-10', '2027-01-10', 100, 5000),
('LO001', 'T002', '2025-01-15', '2027-01-15', 120, 4500),
('LO001', 'T051', '2025-01-20', '2027-01-20', 80, 6000),
('LO001', 'T052', '2025-01-25', '2027-01-25', 200, 5200),
('LO001', 'T101', '2025-02-01', '2027-02-01', 150, 5500),

('LO002', 'T003', '2025-01-12', '2027-01-12', 90, 4700),
('LO002', 'T004', '2025-01-17', '2027-01-17', 100, 4900),
('LO002', 'T053', '2025-01-22', '2027-01-22', 110, 5100),
('LO002', 'T054', '2025-01-27', '2027-01-27', 130, 5300),
('LO002', 'T102', '2025-02-02', '2027-02-02', 70, 5000),

('LO003', 'T005', '2025-01-05', '2027-01-05', 80, 4600),
('LO003', 'T006', '2025-01-07', '2027-01-07', 120, 4800),
('LO003', 'T055', '2025-01-09', '2027-01-09', 150, 5100),
('LO003', 'T056', '2025-01-11', '2027-01-11', 130, 4900),
('LO003', 'T103', '2025-01-13', '2027-01-13', 160, 5200),

('LO004', 'T007', '2025-02-01', '2027-02-01', 100, 5000),
('LO004', 'T008', '2025-02-03', '2027-02-03', 120, 4800),
('LO005', 'T009', '2025-02-05', '2027-02-05', 150, 4700),
('LO005', 'T010', '2025-02-07', '2027-02-07', 140, 4600),
('LO006', 'T011', '2025-02-09', '2027-02-09', 130, 4900),
('LO006', 'T012', '2025-02-11', '2027-02-11', 110, 5000),
('LO007', 'T013', '2025-02-13', '2027-02-13', 100, 5200),
('LO007', 'T014', '2025-02-15', '2027-02-15', 120, 5100),
('LO008', 'T015', '2025-02-17', '2027-02-17', 150, 5300),
('LO008', 'T016', '2025-02-19', '2027-02-19', 160, 5400),
('LO009', 'T017', '2025-02-21', '2027-02-21', 140, 5500),
('LO009', 'T018', '2025-02-23', '2027-02-23', 130, 5600),
('LO010', 'T019', '2025-02-25', '2027-02-25', 110, 5800),
('LO010', 'T020', '2025-02-27', '2027-02-27', 90, 6000),

('LO011', 'T021', '2025-03-01', '2027-03-01', 120, 5000),
('LO011', 'T022', '2025-03-05', '2027-03-05', 95, 5200),
('LO012', 'T023', '2025-03-08', '2027-03-08', 80, 5400),
('LO012', 'T024', '2025-03-12', '2027-03-12', 100, 5600),
('LO013', 'T025', '2025-03-15', '2027-03-15', 75, 5800),
('LO013', 'T026', '2025-03-18', '2027-03-18', 85, 6000),
('LO014', 'T027', '2025-03-21', '2027-03-21', 90, 6200),
('LO014', 'T028', '2025-03-24', '2027-03-24', 70, 6400),
('LO015', 'T029', '2025-03-27', '2027-03-27', 60, 6600),
('LO015', 'T030', '2025-03-30', '2027-03-30', 50, 6800),
('LO016', 'T031', '2025-04-01', '2027-04-01', 45, 7000),
('LO016', 'T032', '2025-04-04', '2027-04-04', 40, 7200),
('LO017', 'T033', '2025-04-07', '2027-04-07', 35, 7400),
('LO017', 'T034', '2025-04-10', '2027-04-10', 38, 7600),
('LO018', 'T035', '2025-04-13', '2027-04-13', 42, 7800),
('LO018', 'T036', '2025-04-16', '2027-04-16', 48, 8000),
('LO019', 'T037', '2025-04-19', '2027-04-19', 45, 8200),
('LO019', 'T038', '2025-04-22', '2027-04-22', 40, 8400),
('LO020', 'T039', '2025-04-25', '2027-04-25', 35, 8600),
('LO020', 'T040', '2025-04-28', '2027-04-28', 30, 8800),
('LO021', 'T041', '2025-05-01', '2027-05-01', 48, 9000),
('LO021', 'T042', '2025-05-04', '2027-05-04', 45, 9200),
('LO022', 'T043', '2025-05-07', '2027-05-07', 42, 9400),
('LO022', 'T044', '2025-05-10', '2027-05-10', 40, 9600),
('LO023', 'T045', '2025-05-13', '2027-05-13', 38, 9800),
('LO023', 'T046', '2025-05-16', '2027-05-16', 50, 10000),
('LO024', 'T047', '2025-05-19', '2027-05-19', 55, 10200),
('LO024', 'T048', '2025-05-22', '2027-05-22', 45, 10400),
('LO025', 'T049', '2025-05-25', '2027-05-25', 40, 10600),
('LO025', 'T050', '2025-05-28', '2027-05-28', 50, 10800),

-- Dữ liệu bổ sung cho các thuốc khác
('LO004', 'T057', '2025-06-21', '2027-06-21', 60, 9000),
('LO004', 'T058', '2025-06-24', '2027-06-24', 65, 7500),
('LO005', 'T059', '2025-06-27', '2027-06-27', 40, 15000),
('LO005', 'T060', '2025-06-30', '2027-06-30', 45, 13000),
('LO006', 'T061', '2025-07-01', '2027-07-01', 80, 4000),
('LO006', 'T062', '2025-07-04', '2027-07-04', 60, 6000),
('LO007', 'T063', '2025-07-07', '2027-07-07', 55, 6500),
('LO007', 'T064', '2025-07-10', '2027-07-10', 65, 7000),
('LO008', 'T065', '2025-07-13', '2027-07-13', 70, 4500),
('LO008', 'T066', '2025-07-16', '2027-07-16', 50, 11000),
('LO009', 'T067', '2025-07-19', '2027-07-19', 45, 5500),
('LO009', 'T068', '2025-07-22', '2027-07-22', 40, 6500),
('LO010', 'T069', '2025-07-25', '2027-07-25', 35, 7500),
('LO010', 'T070', '2025-07-28', '2027-07-28', 30, 9000),
('LO011', 'T071', '2025-08-01', '2027-08-01', 45, 5000),
('LO011', 'T072', '2025-08-04', '2027-08-04', 40, 7500),
('LO012', 'T073', '2025-08-07', '2027-08-07', 35, 8500),
('LO012', 'T074', '2025-08-10', '2027-08-10', 40, 6000),
('LO013', 'T075', '2025-08-13', '2027-08-13', 35, 9000),
('LO013', 'T076', '2025-08-16', '2027-08-16', 30, 10500),
('LO014', 'T077', '2025-08-19', '2027-08-19', 25, 6500),
('LO014', 'T078', '2025-08-22', '2027-08-22', 30, 9000),
('LO015', 'T079', '2025-08-25', '2027-08-25', 28, 10000),
('LO015', 'T080', '2025-08-28', '2027-08-28', 32, 7500),
('LO016', 'T081', '2025-09-01', '2027-09-01', 35, 6000),
('LO016', 'T082', '2025-09-04', '2027-09-04', 30, 11000),
('LO017', 'T083', '2025-09-07', '2027-09-07', 25, 12000),
('LO017', 'T084', '2025-09-10', '2027-09-10', 20, 20000),
('LO018', 'T085', '2025-09-13', '2027-09-13', 18, 22000),
('LO018', 'T086', '2025-09-16', '2027-09-16', 22, 14000),
('LO019', 'T087', '2025-09-19', '2027-09-19', 30, 9500),
('LO019', 'T088', '2025-09-22', '2027-09-22', 28, 11000),
('LO020', 'T089', '2025-09-25', '2027-09-25', 22, 18000),
('LO020', 'T090', '2025-09-28', '2027-09-28', 35, 7500),
('LO021', 'T091', '2025-10-01', '2027-10-01', 32, 9000),
('LO021', 'T092', '2025-10-04', '2027-10-04', 28, 10500),
('LO022', 'T093', '2025-10-07', '2027-10-07', 35, 12000),
('LO022', 'T094', '2025-10-10', '2027-10-10', 30, 14000),
('LO023', 'T095', '2025-10-13', '2027-10-13', 25, 13000),
('LO023', 'T096', '2025-10-16', '2027-10-16', 22, 18000),
('LO024', 'T097', '2025-10-19', '2027-10-19', 30, 6500),
('LO024', 'T098', '2025-10-22', '2027-10-22', 25, 5000),
('LO025', 'T099', '2025-10-25', '2027-10-25', 28, 10000),
('LO025', 'T100', '2025-10-28', '2027-10-28', 26, 11500),
('LO004', 'T104', '2025-11-10', '2027-11-10', 31, 7500), 
('LO005', 'T105', '2025-11-13', '2027-11-13', 35, 6500), 

-- DỮ LIỆU BỔ SUNG ĐỂ KIỂM TRA ĐỒNG BỘ TỒN KHO
('LO999', 'T001', '2025-10-15', '2027-10-15', 2000, 4800), -- Thêm 2000 viên Paracetamol
('LO999', 'T007', '2025-10-15', '2027-10-15', 500, 20000),

-- Lô thuốc ĐÃ HẾT HẠN và SẮP HẾT HẠN (Giữ nguyên như cũ)
('LO026', 'T001', '2024-09-19', '2025-09-19', 150, 4500),
('LO026', 'T021', '2024-09-19', '2025-09-19', 100, 7000),
('LO027', 'T003', '2024-09-29', '2025-09-29', 200, 7200),
('LO027', 'T011', '2024-09-29', '2025-09-29', 120, 12500),
('LO027', 'T026', '2024-09-29', '2025-09-29', 80, 6800),
('LO028', 'T006', '2024-10-19', '2025-10-19', 180, 13500),
('LO028', 'T016', '2024-10-19', '2025-10-19', 90, 6200),
('LO028', 'T031', '2024-10-19', '2025-10-19', 60, 7800),
('LO029', 'T002', '2024-11-08', '2025-11-08', 220, 6800),
('LO029', 'T012', '2024-11-08', '2025-11-08', 140, 28000),
('LO029', 'T027', '2024-11-08', '2025-11-08', 95, 7200),
('LO030', 'T007', '2024-11-18', '2025-11-18', 160, 22000),
('LO030', 'T017', '2024-11-18', '2025-11-18', 110, 11200),
('LO030', 'T032', '2024-11-18', '2025-11-18', 75, 9500),
('LO030', 'T041', '2024-11-18', '2025-11-18', 85, 13500),
('LO031', 'T008', '2024-12-18', '2025-12-18', 190, 19800),
('LO031', 'T018', '2024-12-18', '2025-12-18', 130, 9900),
('LO031', 'T033', '2024-12-18', '2025-12-18', 70, 10800),
('LO032', 'T009', '2024-09-19', '2025-09-19', 170, 16200),
('LO032', 'T036', '2024-09-19', '2025-09-19', 140, 6300),
('LO033', 'T010', '2024-09-29', '2025-09-29', 155, 14400),
('LO033', 'T037', '2024-09-29', '2025-09-29', 125, 7650),
('LO034', 'T013', '2024-11-08', '2025-11-08', 135, 25200),
('LO034', 'T038', '2024-11-08', '2025-11-08', 115, 5850),
('LO035', 'T014', '2024-11-18', '2025-11-18', 145, 7650),
('LO035', 'T039', '2024-11-18', '2025-11-18', 105, 11700); 
GO

-- Chèn dữ liệu Hóa Đơn (Giữ nguyên các insert Hóa đơn và ChiTietHoaDon lớn ở đây - do giới hạn ký tự, tôi sẽ giả định phần dữ liệu Hóa Đơn đã có sẵn trong script gốc của bạn. Tôi sẽ tập trung vào phần PhieuDat bạn yêu cầu sửa đổi).
-- (Vui lòng giữ nguyên phần INSERT INTO HoaDon và INSERT INTO ChiTietHoaDon từ script gốc của bạn vào đây)

-- [PHẦN INSERT HOADON VÀ CTHD ĐÃ ĐƯỢC LƯỢC BỎ ĐỂ TẬP TRUNG VÀO YÊU CẦU CHÍNH, HÃY GIỮ NGUYÊN TỪ SCRIPT CŨ]

-- ===============================================
-- DỮ LIỆU PHIẾU ĐẶT (ĐÃ SỬA: isReceived = 1)
-- ===============================================
INSERT INTO PhieuDat (maPhieuDat, maNV, ngayDat, maKH, ghiChu, isReceived) VALUES
('PD001', 'NV004', '2025-09-01', 'KH001', N'Khách hẹn lấy sau 5h chiều', 1),
('PD002', 'NV005', '2025-09-02', 'KH004', N'Gọi điện trước khi giao', 1),
('PD003', 'NV009', '2025-09-03', 'KH007', N'Khách quen, giảm giá nếu có thể', 1),
('PD004', 'NV010', '2025-09-05', 'KH012', NULL, 1),
('PD005', 'NV004', '2025-09-06', 'KH015', N'Đơn thuốc bác sĩ A', 1),
('PD006', 'NV005', '2025-09-08', 'KH019', N'Lấy thuốc cho mẹ', 1),
('PD007', 'NV009', '2025-09-10', 'KH024', NULL, 1),
('PD008', 'NV010', '2025-09-11', 'KH028', N'Giao hàng nhanh', 1),
('PD009', 'NV004', '2025-09-13', 'KH002', NULL, 1),
('PD010', 'NV005', '2025-09-14', 'KH006', N'Kiểm tra lại thuốc dạ dày', 1),
('PD011', 'NV009', '2025-09-16', 'KH010', N'Đơn đặt hàng tháng', 1),
('PD012', 'NV010', '2025-09-17', 'KH014', NULL, 1),
('PD013', 'NV004', '2025-09-19', 'KH018', N'Khách hàng cần tư vấn thêm', 1),
('PD014', 'NV005', '2025-09-20', 'KH022', NULL, 1),
('PD015', 'NV009', '2025-09-22', 'KH026', N'Giao cho người nhà tên B', 1),
('PD016', 'NV010', '2025-09-23', 'KH030', NULL, 1),
('PD017', 'NV004', '2025-09-25', 'KH034', N'Thuốc tiểu đường, kiểm tra kỹ', 1),
('PD018', 'NV005', '2025-09-26', 'KH003', N'Đơn thuốc hen', 1),
('PD019', 'NV009', '2025-09-28', 'KH009', NULL, 1),
('PD020', 'NV010', '2025-09-29', 'KH016', N'Khách hàng VIP', 1);
GO

-- Insert dữ liệu vào bảng ChiTietPhieuDat
INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, maLo, tenThuoc, soLuong) VALUES
('PD001', 'T001', 'LO001', N'Paracetamol 500mg', 5),
('PD001', 'T021', 'LO011', N'Vitamin C 500mg', 3),
('PD001', 'T026', 'LO013', N'Loratadine 10mg', 2),
('PD002', 'T006', 'LO003', N'Amoxicillin 500mg', 4),
('PD002', 'T011', 'LO006', N'Omeprazole 20mg', 3),
('PD002', 'T014', 'LO007', N'Maalox', 2),
('PD003', 'T016', 'LO008', N'Smecta', 6),
('PD003', 'T017', 'LO009', N'Imodium 2mg', 2),
('PD003', 'T019', 'LO010', N'Lacteol Fort', 3),
('PD004', 'T031', 'LO016', N'Amlodipine 5mg', 3),
('PD004', 'T033', 'LO017', N'Bisoprolol 2.5mg', 2),
('PD004', 'T041', 'LO021', N'Atorvastatin 10mg', 2),
('PD005', 'T036', 'LO018', N'Metformin 500mg', 5),
('PD005', 'T038', 'LO019', N'Glibenclamide 5mg', 3),
('PD005', 'T024', 'LO012', N'Canxi D3', 2),
('PD006', 'T027', 'LO014', N'Cetirizine 10mg', 4),
('PD006', 'T029', 'LO015', N'Claritine', 2),
('PD006', 'T022', 'LO011', N'Vitamin E 400UI', 3),
('PD007', 'T007', 'LO004', N'Augmentin 625mg', 3),
('PD007', 'T009', 'LO005', N'Azithromycin 250mg', 2),
('PD007', 'T012', 'LO006', N'Nexium 40mg', 2),
('PD008', 'T055', 'LO003', N'Bromhexine 8mg', 4),
('PD008', 'T056', 'LO003', N'Ambroxol 30mg', 3),
('PD008', 'T051', 'LO001', N'Salbutamol 2mg', 2),
('PD008', 'T052', 'LO001', N'Ventolin inhaler', 1),
('PD009', 'T003', 'LO002', N'Ibuprofen 400mg', 3),
('PD009', 'T046', 'LO023', N'Meloxicam 7.5mg', 2),
('PD009', 'T047', 'LO024', N'Diclofenac 50mg', 3),
('PD010', 'T012', 'LO006', N'Nexium 40mg', 2),
('PD010', 'T013', 'LO007', N'Pantoprazole 40mg', 2),
('PD010', 'T101', 'LO001', N'Esomeprazole 20mg', 3),
('PD011', 'T023', 'LO012', N'Multivitamin', 5),
('PD011', 'T024', 'LO012', N'Canxi D3', 4),
('PD011', 'T025', 'LO013', N'Zinc 20mg', 3),
('PD012', 'T042', 'LO021', N'Atorvastatin 20mg', 3),
('PD012', 'T043', 'LO022', N'Rosuvastatin 10mg', 2),
('PD012', 'T044', 'LO022', N'Simvastatin 20mg', 2),
('PD013', 'T059', 'LO005', N'Fluconazole 150mg', 2),
('PD013', 'T062', 'LO006', N'Albendazole 400mg', 4),
('PD013', 'T063', 'LO007', N'Mebendazole 500mg', 3),
('PD014', 'T001', 'LO001', N'Paracetamol 500mg', 8),
('PD014', 'T002', 'LO001', N'Efferalgan 500mg', 5),
('PD014', 'T004', 'LO002', N'Aspirin 100mg', 3),
('PD015', 'T071', 'LO011', N'Furosemide 40mg', 3),
('PD015', 'T072', 'LO011', N'Spironolactone 25mg', 2),
('PD015', 'T080', 'LO015', N'Atenolol 50mg', 2),
('PD016', 'T064', 'LO007', N'Domperidone 10mg', 4),
('PD016', 'T065', 'LO008', N'Metoclopramide 10mg', 3),
('PD016', 'T066', 'LO008', N'Ondansetron 8mg', 2),
('PD017', 'T037', 'LO019', N'Metformin 850mg', 4),
('PD017', 'T039', 'LO020', N'Acarbose 50mg', 2),
('PD017', 'T093', 'LO022', N'Gliclazide 30mg', 2),
('PD018', 'T052', 'LO001', N'Ventolin inhaler', 2),
('PD018', 'T053', 'LO002', N'Seretide 250mcg', 1),
('PD018', 'T054', 'LO002', N'Combivent', 1),
('PD019', 'T016', 'LO008', N'Smecta', 8),
('PD019', 'T018', 'LO009', N'Bioflora', 4),
('PD019', 'T020', 'LO010', N'Enterogermina', 3),
('PD020', 'T032', 'LO016', N'Amlodipine 10mg', 3),
('PD020', 'T034', 'LO017', N'Enalapril 5mg', 2),
('PD020', 'T035', 'LO018', N'Losartan 50mg', 2),
('PD020', 'T041', 'LO021', N'Atorvastatin 10mg', 2);
GO

-- Insert dữ liệu vào bảng PhieuNhapThuoc (Không thay đổi)
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
INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, maThuoc, soLuong, donGia) VALUES
('PNT001', 'LO001', 'T001', 500, 3500),
('PNT001', 'LO011', 'T021', 600, 5500),
('PNT002', 'LO003', 'T006', 300, 10000),
('PNT002', 'LO004', 'T007', 250, 17000),
('PNT002', 'LO005', 'T009', 200, 12000),
('PNT003', 'LO006', 'T011', 400, 9000),
('PNT003', 'LO007', 'T013', 300, 6000),
('PNT004', 'LO008', 'T016', 500, 5000),
('PNT004', 'LO009', 'T017', 350, 8000),
('PNT004', 'LO010', 'T020', 400, 6500),
('PNT005', 'LO011', 'T022', 700, 5500),
('PNT005', 'LO012', 'T023', 500, 10000),
('PNT005', 'LO013', 'T025', 450, 6500),
('PNT006', 'LO013', 'T026', 400, 5000),
('PNT006', 'LO014', 'T027', 350, 9000),
('PNT006', 'LO015', 'T029', 300, 12000),
('PNT007', 'LO016', 'T031', 300, 6000),
('PNT007', 'LO017', 'T033', 250, 8000),
('PNT007', 'LO018', 'T035', 280, 7500),
('PNT008', 'LO018', 'T036', 320, 5000),
('PNT008', 'LO019', 'T037', 280, 9500),
('PNT008', 'LO020', 'T040', 220, 20000),
('PNT009', 'LO021', 'T041', 300, 11000),
('PNT009', 'LO022', 'T043', 280, 16000),
('PNT009', 'LO023', 'T045', 250, 18000),
('PNT010', 'LO023', 'T046', 350, 8000),
('PNT010', 'LO024', 'T047', 300, 11000),
('PNT010', 'LO025', 'T049', 280, 13000),
('PNT011', 'LO001', 'T002', 800, 3500),
('PNT011', 'LO002', 'T003', 600, 5500),
('PNT012', 'LO003', 'T055', 400, 10000),
('PNT012', 'LO004', 'T057', 300, 17000),
('PNT013', 'LO006', 'T062', 500, 9000),
('PNT013', 'LO007', 'T064', 400, 6000),
('PNT014', 'LO001', 'T051', 400, 3500),
('PNT014', 'LO002', 'T053', 350, 5500),
('PNT014', 'LO003', 'T056', 300, 10000),
('PNT014', 'LO004', 'T058', 200, 17000),
('PNT015', 'LO005', 'T060', 300, 12000),
('PNT015', 'LO010', 'T069', 350, 6500),
('PNT015', 'LO015', 'T080', 250, 12000),
('PNT015', 'LO020', 'T090', 180, 20000);
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
INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo) VALUES
('PDT001', 'T001', 2, 5000, 'LO001', N'Thuốc sắp hết hạn sử dụng'),
('PDT001', 'T021', 1, 8000, 'LO011', N'Thuốc sắp hết hạn sử dụng'),
('PDT002', 'T006', 1, 15000, 'LO003', N'Khách hàng bị dị ứng với thuốc'),
('PDT003', 'T016', 2, 7000, 'LO008', N'Thuốc không có hiệu quả như mong đợi'),
('PDT003', 'T017', 1, 12500, 'LO009', N'Bác sĩ đổi đơn thuốc'),
('PDT004', 'T031', 1, 8500, 'LO016', N'Khách hàng mua nhầm liều lượng'),
('PDT004', 'T041', 1, 15000, 'LO021', N'Bác sĩ kê đơn thuốc khác'),
('PDT005', 'T036', 2, 7000, 'LO018', N'Viên thuốc bị móp méo'),
('PDT006', 'T027', 2, 8000, 'LO014', N'Khách hàng bị buồn ngủ sau khi uống'),
('PDT006', 'T029', 1, 14000, 'LO015', N'Đổi sang thuốc chống dị ứng thế hệ mới'),
('PDT007', 'T007', 1, 25000, 'LO004', N'Khách hàng mua thừa, chưa sử dụng'),
('PDT007', 'T012', 1, 32000, 'LO006', N'Mua trùng với đơn thuốc cũ'),
('PDT008', 'T052', 1, 85000, 'LO001', N'Bác sĩ đổi phác đồ điều trị'),
('PDT008', 'T055', 1, 6500, 'LO003', N'Thuốc không phù hợp với triệu chứng'),
('PDT009', 'T003', 1, 8000, 'LO002', N'Khách hàng bị đau dạ dày sau khi uống'),
('PDT009', 'T046', 1, 11000, 'LO023', N'Gây tác dụng phụ không mong muốn'),
('PDT010', 'T056', 2, 7000, 'LO003', N'Phát hiện thuốc còn hạn dùng ngắn'),
('PDT010', 'T058', 1, 9500, 'LO004', N'Thuốc sắp hết hạn sử dụng'),
('PDT011', 'T023', 2, 15000, 'LO012', N'Khách hàng mua nhầm loại vitamin'),
('PDT011', 'T024', 1, 13500, 'LO012', N'Đổi sang sản phẩm phù hợp hơn'),
('PDT012', 'T042', 1, 19000, 'LO021', N'Khách hàng bị đau cơ sau khi dùng'),
('PDT012', 'T043', 1, 22000, 'LO022', N'Không dung nạp với thuốc'),
('PDT013', 'T037', 2, 8500, 'LO019', N'Bác sĩ điều chỉnh liều lượng'),
('PDT013', 'T039', 1, 13000, 'LO020', N'Thay đổi phác đồ điều trị tiểu đường'),
('PDT014', 'T011', 2, 14000, 'LO006', N'Bao bì thuốc bị rách'),
('PDT014', 'T013', 1, 28000, 'LO007', N'Lọ thuốc bị vỡ seal niêm phong'),
('PDT015', 'T026', 3, 7500, 'LO013', N'Khách hàng khỏi bệnh, còn thừa thuốc'),
('PDT015', 'T022', 1, 12000, 'LO011', N'Mua thừa, chưa mở hộp');
GO

-- ===============================================
-- CẬP NHẬT SỐ LƯỢNG TỒN (THỦ CÔNG)
-- ===============================================
UPDATE T
SET T.soLuongTon = ISNULL(Sub.TotalQuantity, 0)
FROM Thuoc T
LEFT JOIN (
    SELECT 
        maThuoc,
        SUM(soLuong) AS TotalQuantity
    FROM ChiTietLoThuoc
    WHERE isActive = 1
    GROUP BY maThuoc
) AS Sub ON T.maThuoc = Sub.maThuoc;
GO

-- ===============================================
-- TRIGGER & PROCEDURES
-- ===============================================

CREATE TRIGGER tr_CapNhatSoLuongTon
ON ChiTietLoThuoc
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @AffectedMaThuoc TABLE (maThuoc VARCHAR(50));
    
    INSERT INTO @AffectedMaThuoc (maThuoc)
    SELECT DISTINCT maThuoc FROM INSERTED
    UNION
    SELECT DISTINCT maThuoc FROM DELETED;

    UPDATE Thuoc
    SET soLuongTon = ISNULL((
        SELECT SUM(soLuong)
        FROM ChiTietLoThuoc
        WHERE ChiTietLoThuoc.maThuoc = Thuoc.maThuoc 
          AND ChiTietLoThuoc.isActive = 1
    ), 0)
    WHERE maThuoc IN (SELECT maThuoc FROM @AffectedMaThuoc);
END
GO

CREATE TRIGGER tr_CapNhatDiemTichLuy
ON HoaDon
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE KhachHang
    SET diemTichLuy = ISNULL(diemTichLuy, 0) + ISNULL((
        SELECT FLOOR(SUM(ct.soLuong * ct.donGia) / 1000)
        FROM ChiTietHoaDon ct
        WHERE ct.maHoaDon = i.maHoaDon
          AND ct.isActive = 1
    ), 0)
    FROM KhachHang kh
    INNER JOIN INSERTED i ON kh.maKH = i.maKH
    WHERE i.maKH IS NOT NULL;
END
GO

CREATE TRIGGER tr_DongBoAnNhanVien
ON NhanVien
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE TaiKhoan
    SET isActive = 0
    FROM TaiKhoan tk
    INNER JOIN INSERTED i ON tk.maNV = i.maNV
    WHERE i.isActive = 0 AND tk.isActive = 1;
    
    UPDATE TaiKhoan
    SET isActive = 1
    FROM TaiKhoan tk
    INNER JOIN INSERTED i ON tk.maNV = i.maNV
    WHERE i.isActive = 1 AND tk.isActive = 0;
END
GO

-- PROCEDURE: Cập nhật thuốc hết hạn sử dụng
CREATE PROCEDURE sp_CapNhatThuocHetHan
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        UPDATE ChiTietLoThuoc
        SET isActive = 0
        WHERE hanSuDung < GETDATE() AND isActive = 1;
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    END CATCH
END;
GO

-- Các Procedure thống kê khác (Giữ nguyên)
CREATE PROCEDURE sp_ThongKeDoanhThuTheoThang
    @Nam INT
AS
BEGIN
    SELECT 
        MONTH(ngayBan) AS Thang,
        SUM(ct.soLuong * ct.donGia) AS DoanhThu
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
    WHERE YEAR(ngayBan) = @Nam AND hd.isActive = 1 AND ct.isActive = 1
    GROUP BY MONTH(ngayBan)
    ORDER BY Thang;
END
GO

CREATE PROCEDURE sp_ThongKeDoanhThuNhanVien
    @MaNV VARCHAR(50),
    @Nam INT
AS
BEGIN
    DECLARE @Thang TABLE (Thang INT);
    INSERT INTO @Thang VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12);

    SELECT 
        nv.maNV,
        nv.tenNV,
        t.Thang AS 'Tháng',
        ISNULL(COUNT(DISTINCT hd.maHoaDon), 0) AS 'Số hóa đơn',
        ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS 'Doanh thu'
    FROM @Thang t
    LEFT JOIN HoaDon hd ON MONTH(hd.ngayBan) = t.Thang 
                        AND YEAR(hd.ngayBan) = @Nam 
                        AND hd.maNV = @MaNV
                        AND hd.isActive = 1
    LEFT JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon AND cthd.isActive = 1
    CROSS JOIN NhanVien nv WHERE nv.maNV = @MaNV
    GROUP BY nv.maNV, nv.tenNV, t.Thang
    ORDER BY t.Thang;
END
GO

CREATE PROCEDURE sp_ThongKeThueTrongNam
    @Nam INT
AS
BEGIN
    SELECT
        MONTH(hd.ngayBan) AS [Thang],
        SUM(hd.giaTriThue * (cthd.soLuong * cthd.donGia)) AS [giaTriThue]
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    WHERE YEAR(hd.ngayBan) = @Nam AND hd.isActive = 1 AND cthd.isActive = 1
    GROUP BY MONTH(hd.ngayBan)
    ORDER BY [Thang];
END
GO

CREATE PROCEDURE sp_GetSoHoaDonTheoNam
	@Nam int
AS
BEGIN
    SELECT YEAR(ngayBan) AS [Nam], COUNT(maHoaDon) AS [SoHoaDon]
    FROM HoaDon WhERE isActive = 1 and YEAR(ngayBan) = @Nam
    GROUP BY YEAR(ngayBan)
END
GO

CREATE PROCEDURE sp_GetSoHoaDonTheoThang
    @Thang INT, @Nam INT
AS
BEGIN
    SELECT MONTH(ngayBan) AS [Thang], YEAR(ngayBan) AS [Nam], COUNT(maHoaDon) AS [SoHoaDon]
    FROM HoaDon
    WHERE YEAR(ngayBan) = @Nam AND isActive = 1 AND MONTH(ngayBan) = @Thang
    GROUP BY MONTH(ngayBan), YEAR(ngayBan)
    ORDER BY [Thang];
END
GO

CREATE PROCEDURE sp_ThongKeDoanhThuTheoNgay
    @Thang INT, @Nam INT
AS
BEGIN
    SELECT
        DAY(ngayBan) AS [Ngay],
		MONTH(ngayBan) AS [Thang],
        YEAR(ngayBan) AS [Nam],
        SUM(soLuong * donGia) AS [doanhThu]
    FROM HoaDon HD JOIN ChiTietHoaDon CTHD on HD.maHoaDon = CTHD.maHoaDon	
    WHERE YEAR(ngayBan) = @Nam and MONTH(ngayBan) = @Thang AND HD.isActive = 1
    GROUP BY DAY(ngayBan), MONTH(ngayBan), YEAR(ngayBan)
    ORDER BY [Ngay];
END
GO

CREATE PROCEDURE sp_GetDoanhThuCuaNam
    @Nam INT
AS
BEGIN
    SELECT
        @Nam AS [Nam],
        ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS [TongDoanhThu]
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    WHERE YEAR(hd.ngayBan) = @Nam AND hd.isActive = 1 AND cthd.isActive = 1;
END
GO

CREATE PROCEDURE sp_GetDoanhThuCuaThang
    @Thang INT, @Nam INT
AS
BEGIN
    SELECT
        @Thang AS [Thang], @Nam AS [Nam],
        ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS [TongDoanhThu]
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    WHERE YEAR(hd.ngayBan) = @Nam AND MONTH(hd.ngayBan) = @Thang AND hd.isActive = 1 AND cthd.isActive = 1;
END
GO

CREATE PROCEDURE sp_GetSoKhachHangCuaNam
    @Nam INT
AS
BEGIN
    SELECT @Nam AS [Nam], COUNT(DISTINCT hd.maKH) AS [SoKhachHang]
    FROM HoaDon hd
    WHERE YEAR(hd.ngayBan) = @Nam AND hd.isActive = 1 AND hd.maKH IS NOT NULL;
END
GO

CREATE PROCEDURE sp_GetSoKhachHangCuaThang
    @Thang INT, @Nam INT
AS
BEGIN
    SELECT @Thang AS [Thang], @Nam AS [Nam], COUNT(DISTINCT hd.maKH) AS [SoKhachHang]
    FROM HoaDon hd
    WHERE YEAR(hd.ngayBan) = @Nam AND MONTH(hd.ngayBan) = @Thang AND hd.isActive = 1 AND hd.maKH IS NOT NULL;
END
GO

CREATE PROCEDURE sp_GetDoanhThuTrungBinhTheoNgay
    @Thang INT, @Nam INT
AS
BEGIN
    DECLARE @TongDoanhThu FLOAT;
    DECLARE @SoNgayTrongThang INT;
    
    SELECT @TongDoanhThu = ISNULL(SUM(cthd.soLuong * cthd.donGia), 0)
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    WHERE YEAR(hd.ngayBan) = @Nam AND MONTH(hd.ngayBan) = @Thang AND hd.isActive = 1 AND cthd.isActive = 1;
    
    SET @SoNgayTrongThang = DAY(EOMONTH(DATEFROMPARTS(@Nam, @Thang, 1)));
    
    SELECT @Thang AS [Thang], @Nam AS [Nam], @SoNgayTrongThang AS [SoNgayTrongThang], @TongDoanhThu AS [TongDoanhThu],
        CASE WHEN @SoNgayTrongThang > 0 THEN @TongDoanhThu / @SoNgayTrongThang ELSE 0 END AS [DoanhThuTrungBinhMoiNgay];
END
GO

CREATE PROCEDURE sp_GetDoanhThuTrungBinhTheoThang
    @Nam INT
AS
BEGIN
    DECLARE @TongDoanhThu FLOAT;
    DECLARE @SoThangDaQua INT;
    DECLARE @ThangHienTai INT = MONTH(GETDATE());
    DECLARE @NamHienTai INT = YEAR(GETDATE());
    
    IF @Nam < @NamHienTai SET @SoThangDaQua = 12;
    ELSE IF @Nam = @NamHienTai SET @SoThangDaQua = @ThangHienTai;
    ELSE SET @SoThangDaQua = 0;
    
    SELECT @TongDoanhThu = ISNULL(SUM(cthd.soLuong * cthd.donGia), 0)
    FROM HoaDon hd
    INNER JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
    WHERE YEAR(hd.ngayBan) = @Nam AND hd.isActive = 1 AND cthd.isActive = 1;
    
    SELECT @Nam AS [Nam], @SoThangDaQua AS [SoThangDaQua], @TongDoanhThu AS [TongDoanhThu],
        CASE WHEN @SoThangDaQua > 0 THEN @TongDoanhThu / @SoThangDaQua ELSE 0 END AS [DoanhThuTrungBinhMoiThang];
END
GO

CREATE PROCEDURE sp_GetLoThuocDaHetHan
AS
BEGIN
    SELECT ct.maLo, t.maThuoc, t.tenThuoc, ct.ngaySanXuat, ct.hanSuDung, ct.soLuong AS 'soLuongTon', DATEDIFF(DAY, ct.hanSuDung, Getdate()) AS 'soNgayDaHetHan'
    FROM ChiTietLoThuoc ct
    INNER JOIN Thuoc t ON ct.maThuoc = t.maThuoc
    WHERE ct.hanSuDung < getdate() AND ct.isActive = 0
    ORDER BY ct.hanSuDung ASC;
END
GO

CREATE PROCEDURE sp_GetLoThuocSapHetHan
    @SoNgay INT = 30
AS
BEGIN
    DECLARE @NgayHienTai DATE = CAST(GETDATE() AS DATE);
    DECLARE @NgayKiemTra DATE = DATEADD(DAY, @SoNgay, @NgayHienTai);
    
    SELECT ct.maLo, t.maThuoc, t.tenThuoc, ct.ngaySanXuat, ct.hanSuDung, ct.soLuong AS 'soLuongTon', DATEDIFF(DAY, @NgayHienTai, ct.hanSuDung) AS [soNgayConLai]
    FROM ChiTietLoThuoc ct
    INNER JOIN Thuoc t ON ct.maThuoc = t.maThuoc
    WHERE ct.hanSuDung >= @NgayHienTai AND ct.hanSuDung <= @NgayKiemTra AND ct.isActive = 1 AND ct.soLuong > 0 AND t.isActive = 1                            
    ORDER BY ct.hanSuDung ASC, ct.maThuoc;         
END
GO