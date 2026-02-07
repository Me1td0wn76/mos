-- MOSシステム - データベース初期化スクリプト
-- MariaDB / MySQL

-- データベース作成（既に作成済みの場合はスキップ）
-- CREATE DATABASE IF NOT EXISTS mosdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE mosdb;

-- ===================================
-- テーブル定義 (DDL)
-- ===================================

-- 1. 店舗テーブル
CREATE TABLE IF NOT EXISTS stores (
    store_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 従業員テーブル
CREATE TABLE IF NOT EXISTS employees (
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. カテゴリテーブル
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    display_order INT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 商品テーブル
CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    is_sold_out BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. 商品カテゴリ紐付けテーブル
CREATE TABLE IF NOT EXISTS product_categories (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. テーブルテーブル（座席）
CREATE TABLE IF NOT EXISTS tables (
    table_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    table_number VARCHAR(20) NOT NULL,
    floor INT,
    capacity INT,
    qr_code VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES stores(store_id),
    UNIQUE KEY unique_store_table (store_id, table_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. 呼び出し種類マスタ
CREATE TABLE IF NOT EXISTS call_types (
    call_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. 注文テーブル
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    plan_type VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    plan_duration INT,
    timer_start_time TIMESTAMP NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(table_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. 注文明細テーブル
CREATE TABLE IF NOT EXISTS order_details (
    order_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    remarks VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. 呼び出しテーブル
CREATE TABLE IF NOT EXISTS calls (
    call_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    call_type_id BIGINT NOT NULL,
    remarks VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    responded_by BIGINT,
    responded_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(table_id),
    FOREIGN KEY (call_type_id) REFERENCES call_types(call_type_id),
    FOREIGN KEY (responded_by) REFERENCES employees(employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===================================
-- サンプルデータ投入 (DML)
-- ===================================

-- 1. 店舗データ
INSERT IGNORE INTO stores (store_id, store_name, address, phone_number, is_active, created_at, updated_at) VALUES
(1, '焼き鳥屋MOSテスト店', '東京都渋谷区1-2-3', '03-1234-5678', true, NOW(), NOW());

-- 2. 従業員データ（BCryptでハッシュ化されたパスワード）
-- 平文パスワード: password123
-- ハッシュ化されたパスワード: $2a$10$trDuiHHQGz9rS3cTi8abDesoxJij99wIxnQqWLhTiIE5sIqeAt21G
INSERT IGNORE INTO employees (store_id, employee_name, login_id, password, role_id, is_active, created_at, updated_at) VALUES
(1, '山田太郎', 'yamada', '$2a$10$trDuiHHQGz9rS3cTi8abDesoxJij99wIxnQqWLhTiIE5sIqeAt21G', 1, true, NOW(), NOW()),
(1, '佐藤花子', 'sato', '$2a$10$trDuiHHQGz9rS3cTi8abDesoxJij99wIxnQqWLhTiIE5sIqeAt21G', 2, true, NOW(), NOW());

-- 3. カテゴリデータ
INSERT IGNORE INTO categories (category_id, store_id, category_name, display_order, is_active, created_at, updated_at) VALUES
(1, 1, '焼き鳥', 1, true, NOW(), NOW()),
(2, 1, 'ごはんもの', 2, true, NOW(), NOW()),
(3, 1, 'スピード', 3, true, NOW(), NOW()),
(4, 1, 'ドリンク', 4, true, NOW(), NOW()),
(5, 1, 'デザート', 5, true, NOW(), NOW()),
(6, 1, '無料備品', 6, true, NOW(), NOW()),
(7, 1, '季節限定', 7, true, NOW(), NOW());

-- 4. 商品データ
INSERT IGNORE INTO products (product_id, store_id, product_name, description, price, image_url, is_available, is_sold_out, created_at, updated_at) VALUES
(1, 1, 'ねぎま', '定番の焼き鳥', 180.00, '/images/negima.jpg', true, false, NOW(), NOW()),
(2, 1, 'つくね', '特製つくね', 200.00, '/images/tsukune.jpg', true, false, NOW(), NOW()),
(3, 1, 'もも', 'ジューシーなもも', 180.00, '/images/momo.jpg', true, false, NOW(), NOW()),
(4, 1, '親子丼', '大人気の親子丼', 680.00, '/images/oyakodon.jpg', true, false, NOW(), NOW()),
(5, 1, '枝豆', 'スピード提供', 380.00, '/images/edamame.jpg', true, false, NOW(), NOW()),
(6, 1, '生ビール', '冷えたビール', 580.00, '/images/beer.jpg', true, false, NOW(), NOW()),
(7, 1, 'ウーロン茶', 'さっぱりウーロン茶', 280.00, '/images/oolong.jpg', true, false, NOW(), NOW()),
(8, 1, 'アイスクリーム', 'バニラアイス', 380.00, '/images/ice.jpg', true, false, NOW(), NOW()),
(9, 1, 'お手拭き', '無料', 0.00, '/images/towel.jpg', true, false, NOW(), NOW()),
(10, 1, 'お取り皿', '無料', 0.00, '/images/plate.jpg', true, false, NOW(), NOW());

-- 5. 商品とカテゴリの紐付け
INSERT IGNORE INTO product_categories (product_id, category_id) VALUES
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
INSERT IGNORE INTO tables (store_id, table_number, floor, capacity, qr_code, status, created_at, updated_at) VALUES
(1, 'A01', 1, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'A02', 1, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'A03', 1, 2, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'B01', 1, 6, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'B02', 1, 8, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'C01', 2, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'C02', 2, 4, NULL, 'AVAILABLE', NOW(), NOW()),
(1, 'D01', 2, 10, NULL, 'AVAILABLE', NOW(), NOW());

-- 7. 呼び出し種類マスタ
INSERT IGNORE INTO call_types (type_name, description, is_active) VALUES
('従業員呼び出し', 'スタッフを呼ぶ', true),
('お会計', '会計をお願いする', true),
('その他', 'その他の用件', true);

-- ===================================
-- 初期データ投入完了
-- ===================================
-- 注意: 
-- - パスワードはBCryptでハッシュ化済み (password123)
-- - 本番環境では必ず強力なパスワードに変更してください
