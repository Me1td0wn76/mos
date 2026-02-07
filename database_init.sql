-- MOSシステム - データベース初期化スクリプト
-- MariaDB / MySQL

-- データベース作成（既に作成済みの場合はスキップ）
-- CREATE DATABASE IF NOT EXISTS mosdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE mosdb;

-- サンプルデータ投入

-- 1. 店舗データ
INSERT INTO stores (store_name, address, phone_number, is_active, created_at, updated_at) VALUES
('焼き鳥屋MOSテスト店', '東京都渋谷区1-2-3', '03-1234-5678', true, NOW(), NOW());

-- 2. 従業員データ（BCryptでハッシュ化されたパスワード）
-- 平文パスワード: password123
-- ハッシュ化されたパスワード: $2a$10$N9qo8uLOickgx2ZMRZoMye1J8BcJjdQpNCOqYJRdAOiKEMg8I8jSG
INSERT INTO employees (store_id, employee_name, login_id, password, role_id, is_active, created_at, updated_at) VALUES
(1, '山田太郎', 'yamada', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8BcJjdQpNCOqYJRdAOiKEMg8I8jSG', 1, true, NOW(), NOW()),
(1, '佐藤花子', 'sato', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8BcJjdQpNCOqYJRdAOiKEMg8I8jSG', 2, true, NOW(), NOW());

-- 3. カテゴリデータ
INSERT INTO categories (store_id, category_name, display_order, is_active, created_at, updated_at) VALUES
(1, '焼き鳥', 1, true, NOW(), NOW()),
(1, 'ごはんもの', 2, true, NOW(), NOW()),
(1, 'スピード', 3, true, NOW(), NOW()),
(1, 'ドリンク', 4, true, NOW(), NOW()),
(1, 'デザート', 5, true, NOW(), NOW()),
(1, '無料備品', 6, true, NOW(), NOW()),
(1, '季節限定', 7, true, NOW(), NOW());

-- 4. 商品データ
INSERT INTO products (store_id, product_name, description, price, image_url, is_available, is_sold_out, created_at, updated_at) VALUES
(1, 'ねぎま', '定番の焼き鳥', 180.00, '/images/negima.jpg', true, false, NOW(), NOW()),
(1, 'つくね', '特製つくね', 200.00, '/images/tsukune.jpg', true, false, NOW(), NOW()),
(1, 'もも', 'ジューシーなもも', 180.00, '/images/momo.jpg', true, false, NOW(), NOW()),
(1, '親子丼', '大人気の親子丼', 680.00, '/images/oyakodon.jpg', true, false, NOW(), NOW()),
(1, '枝豆', 'スピード提供', 380.00, '/images/edamame.jpg', true, false, NOW(), NOW()),
(1, '生ビール', '冷えたビール', 580.00, '/images/beer.jpg', true, false, NOW(), NOW()),
(1, 'ウーロン茶', 'さっぱりウーロン茶', 280.00, '/images/oolong.jpg', true, false, NOW(), NOW()),
(1, 'アイスクリーム', 'バニラアイス', 380.00, '/images/ice.jpg', true, false, NOW(), NOW()),
(1, 'お手拭き', '無料', 0.00, '/images/towel.jpg', true, false, NOW(), NOW()),
(1, 'お取り皿', '無料', 0.00, '/images/plate.jpg', true, false, NOW(), NOW());

-- 5. 商品とカテゴリの紐付け
INSERT INTO product_categories (product_id, category_id) VALUES
(1, 1), -- ねぎま - 焼き鳥
(2, 1), -- つくね - 焼き鳥
(3, 1), -- もも - 焼き鳥
(4, 2), -- 親子丼 - ごはんもの
(5, 3), -- 枝豆 - スピード
(6, 4), -- 生ビール - ドリンク
(7, 4), -- ウーロン茶 - ドリンク
(8, 5), -- アイスクリーム - デザート
(9, 6), -- お手拭き - 無料備品
(10, 6); -- お取り皿 - 無料備品

-- 6. テーブルデータ
INSERT INTO tables (store_id, table_number, floor, capacity, qr_code, status, created_at, updated_at) VALUES
(1, 'A01', 1, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'A02', 1, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'A03', 1, 2, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'B01', 1, 6, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'B02', 1, 8, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'C01', 2, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'C02', 2, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'D01', 2, 10, NULL, 'AVAILABLE', NOW(), NOW());

-- 7. 呼び出し種類マスタ
INSERT INTO call_types (type_name, description, is_active) VALUES
('従業員呼び出し', 'スタッフを呼ぶ', true),
('お会計', '会計をお願いする', true),
('その他', 'その他の用件', true);

-- 初期データ投入完了
-- 注意: パスワードはハッシュ化していません。本番環境では必ずBCryptなどでハッシュ化してください。
