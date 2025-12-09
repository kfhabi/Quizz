# Database Runbook — Quizz

Mục đích: hướng dẫn DBA / operator cách xử lý hai vấn đề chính đã gặp:
- MySQL authentication: lỗi `Public Key Retrieval is not allowed` (khi dùng `caching_sha2_password`).
- Role enum mismatch giữa dữ liệu (lowercase) và mã Java (enum names uppercase).

LUẬT AN TOÀN: Thực hiện trên môi trường staging trước khi chạy trên production. Backup database trước khi thay đổi: `mysqldump -u root -p quizz_db > quizz_db.bak.sql`.

=== 1) Kiểm tra hiện trạng ===

- Kiểm tra các giá trị role hiện có:

```sql
SELECT DISTINCT role FROM users;
```

- Kiểm tra user MySQL và plugin auth đang dùng (ví dụ cho tài khoản ứng dụng `appuser`):

```sql
SELECT user, host, plugin FROM mysql.user WHERE user = 'appuser';
```

- Kiểm tra phiên bản MySQL và cấu hình ssl/rsa (trên host):

```sql
SHOW VARIABLES LIKE 'version';
SHOW VARIABLES LIKE 'have_openssl';
SHOW VARIABLES LIKE 'ssl%';
```

=== 2) Fix nhanh (dev / staging) — chuyển user sang `mysql_native_password` (không khuyến nghị cho production lâu dài) ===

Chạy trên server MySQL (với quyền đủ cao):

```sql
ALTER USER 'appuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'ReplaceWithStrongPasswordHere!';
FLUSH PRIVILEGES;
```

- Thay `appuser` và `localhost` theo user/host thực tế.
- Sau lệnh này, connector không cần public key retrieval.

=== 3) Giải pháp an toàn (production) — cấu hình server để hỗ trợ `caching_sha2_password` an toàn ===

Hai cách an toàn:
- (A) Kích hoạt TLS và cấu hình server RSA public key để client có thể lấy public key an toàn, hoặc
- (B) Tạo user được cấu hình để dùng `mysql_native_password` nếu policy cho phép (ít an toàn hơn).

Tài liệu tham khảo MySQL:
- `mysql_ssl_rsa_setup` để sinh key nếu cần.
- Trong `mysqld` config: `caching_sha2_password_auto_generate_rsa_keys=1` hoặc cung cấp `--ssl-ca`/`--ssl-cert`/`--ssl-key`.

=== 4) Normalize role values (an toàn) ===

Vấn đề: file seed `src/main/resources/data/qizz.sql` khai báo enum (`'student','admin','teacher'`) — giá trị lowercase. Ứng dụng Java dùng enum `Role` với tên viết hoa (ví dụ `ADMIN`), gây lỗi mapping nếu không có converter.

Hai cách xử lý an toàn:

A) Giữ enum DB lowercase và đảm bảo ứng dụng convert (đã áp dụng `RoleConverter` trong mã). Không cần thay đổi DB.

B) Nếu muốn chuẩn hóa DB sang UPPERCASE (thống nhất với mã): sử dụng cột tạm để tránh lỗi enum khi đổi giá trị. Ví dụ an toàn:

```sql
-- 1) Backup trước
-- 2) Thêm cột tạm
ALTER TABLE users ADD COLUMN role_tmp VARCHAR(20) AFTER role;

-- 3) Sao chép giá trị đã chuẩn hoá
UPDATE users SET role_tmp = UPPER(role);

-- 4) Loại bỏ cột enum cũ và tạo lại dưới dạng ENUM uppercase
ALTER TABLE users DROP COLUMN role;
ALTER TABLE users ADD COLUMN role ENUM('STUDENT','ADMIN','TEACHER') NOT NULL DEFAULT 'STUDENT';

-- 5) Sao chép lại giá trị
UPDATE users SET role = role_tmp;

-- 6) Xoá cột tạm
ALTER TABLE users DROP COLUMN role_tmp;
```

Lưu ý: Thao tác này an toàn nếu không có ràng buộc khác phụ thuộc vào giá trị enum. Thử trên staging trước.

=== 5) Kiểm tra sau sửa ===

- Kiểm tra distinct role:
```sql
SELECT DISTINCT role FROM users;
```
- Kiểm tra kết nối từ ứng dụng (sau khi đổi plugin hoặc bật TLS): chạy thử `./mvnw test` hoặc khởi động ứng dụng.

=== 6) Gợi ý về cấu hình ứng dụng / CI ===

- Đừng bật `allowPublicKeyRetrieval=true` trong `application.properties` cho production.
- Nếu CI/dev cần kết nối nhanh: dùng H2 hoặc bật workaround chỉ cho `src/test/resources/application.properties` (chỉ dev). Tốt hơn là tạo một container MySQL cấu hình đúng cho CI.
- Xoay JWT secret vào biến môi trường hoặc secret manager.

=== 7) Các lệnh bổ sung hữu ích ===

- Liệt kê users trong bảng `users` (xem sample admin):
```sql
SELECT user_id, username, email, role, created_at FROM users ORDER BY user_id LIMIT 50;
```

- Nếu cần tạo user mới (ví dụ admin) với `mysql_native_password` (DB account, không phải ứng dụng user):
```sql
CREATE USER 'ci_admin'@'%' IDENTIFIED WITH mysql_native_password BY 'StrongPass!123';
GRANT ALL PRIVILEGES ON quizz_db.* TO 'ci_admin'@'%';
FLUSH PRIVILEGES;
```

=== 8) Kế tiếp (recommended) ===
- Nếu bạn muốn tôi tạo patch để dùng H2 cho test (loại bỏ dependency vào MySQL trên CI/dev), tôi có thể làm.
- Nếu muốn, tôi có thể commit `docs/DB_RUNBOOK.md` vào repo (đã tạo ở `docs/DB_RUNBOOK.md`).

