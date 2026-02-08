# MOS - 飲食店注文管理システム (バックエンド)

Spring Boot + React ベースの焼き鳥屋向け注文管理システムのバックエンドAPI

## 技術スタック

- **Java**: 21
- **Spring Boot**: 3.2.2
- **Database**: MariaDB 10.x
- **Build Tool**: Maven 3.9
- **Server**: Apache Tomcat (組み込み)
- **ORM**: JPA/Hibernate
- **認証**: JWT (JSON Web Token)
- **ファイルアップロード**: MultipartFile

## 主要な機能

### 1. 認証機能
- 従業員ログイン（ログインID/パスワード）
- JWTベースの認証
- BCryptによるパスワードハッシュ化
- ロール管理（管理者/スタッフ）

### 2. 注文機能
- テーブル番号による注文開始（テーブル検証機能付き）
- メニュー選択・数量指定
- カート機能
- 注文送信
- 注文履歴確認

### 3. 提供機能
- 注文一覧の確認（テーブル別グルーピング）
- 提供状態管理（PENDING → PREPARING → SERVED）
- 個別商品の提供完了チェック
- リアルタイム状態更新

### 4. 商品管理
- カテゴリ別商品管理（焼き鳥、ごはんもの、スピード、ドリンク、デザート）
- 商品の追加・編集・削除
- 価格変更
- 売り切れ設定
- **画像アップロード機能**（URLまたはローカルファイル）
- 商品オプション管理

### 5. テーブル管理
- テーブル状態管理（AVAILABLE/OCCUPIED/CLEANING）
- テーブル番号検証API
- フロア別管理
- 座席数管理

### 6. 従業員管理
- 従業員の追加・編集・削除
- ロール設定（管理者: 1、スタッフ: 2）
- アクティブ状態管理

### 7. 呼び出し機能
- 顧客からの従業員呼び出し
- 呼び出しタイプ管理（従業員呼び出し、お会計、その他）
- 呼び出し対応管理

### 8. カテゴリ管理
- カテゴリの追加・編集・削除
- 表示順序管理
- アクティブ状態管理

## プロジェクト構造

```
src/main/java/com/example/mos/
├── Models/           # エンティティクラス
│   ├── Store.java
│   ├── Employee.java
│   ├── Table.java
│   ├── Category.java
│   ├── Product.java
│   ├── ProductOption.java
│   ├── Order.java
│   ├── OrderDetail.java
│   ├── CallRequest.java
│   └── CallType.java
├── Repositories/     # データアクセス層（JpaRepository）
│   ├── StoreRepository.java
│   ├── EmployeeRepository.java
│   ├── TableRepository.java
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   └── ...
├── Services/         # ビジネスロジック層
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── EmployeeService.java
│   └── ...
├── Controls/         # REST APIコントローラー
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   ├── EmployeeController.java
│   ├── CategoryController.java
│   └── TableController.java
├── dto/             # データ転送オブジェクト
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── OrderCreateRequest.java
│   ├── ProductCreateRequest.java
│   ├── EmployeeDTO.java
│   └── ApiResponse.java
├── config/          # 設定クラス
│   ├── WebConfig.java        # CORS設定、静的リソース設定
│   └── SecurityConfig.java   # セキュリティ設定
├── util/            # ユーティリティクラス
│   └── JwtUtil.java          # JWT生成・検証
└── exception/       # 例外ハンドリング
    └── GlobalExceptionHandler.java

src/main/resources/
├── application.properties           # ローカル環境設定
├── application-docker.properties    # Docker環境設定
├── application-test.properties      # テスト環境設定
└── database_init.sql               # 初期スキーマ・データ

uploads/
└── products/       # アップロードされた商品画像（実行時作成）
```

## セットアップ

### 前提条件
- Java 21以上
- MariaDB 15.x
- Maven 3.6以上

### データベース設定

1. MariaDBにデータベースを作成:
```sql
CREATE DATABASE mosdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 初期データ投入:
```bash
# database_init.sqlを使用してスキーマとサンプルデータを投入
mysql -u root -p mosdb < database_init.sql
```

3. `src/main/resources/application.properties`を設定:
```properties
# データベース接続設定
spring.datasource.url=jdbc:mysql://localhost:3306/mosdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA設定
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# ファイルアップロード設定
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# 静的リソース設定（画像アップロード用）
upload.directory=uploads/products
```

### Docker環境での設定

Docker Compose環境では`application-docker.properties`が使用されます:
```properties
spring.datasource.url=jdbc:mysql://db:3306/mosdb
spring.datasource.username=mosuser
spring.datasource.password=mospassword
```

### ビルド＆実行

#### ローカル環境
```bash
# 依存関係のインストール＆ビルド
mvn clean install

# アプリケーション起動
mvn spring-boot:run

# 特定のプロファイルで起動
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

サーバーは `http://localhost:8080` で起動します。

#### Docker環境
```bash
# Dockerコンテナでビルド＆起動
docker-compose up -d --build

# バックエンドのログ確認
docker-compose logs -f backend

# コンテナの状態確認
docker-compose ps

# 停止
docker-compose down

# データベースとアップロード画像も削除する場合
docker-compose down -v
```

### アップロード画像の配信
アップロードされた画像は以下のURLでアクセス可能です:
```
http://localhost:8080/uploads/products/{ファイル名}
```

例: `http://localhost:8080/uploads/products/abc123-def456.jpg`

## API レスポンスフォーマット

すべてのAPIエンドポイントは統一された`ApiResponse<T>`形式でレスポンスを返します:

### 成功時
```json
{
  "success": true,
  "message": "操作が成功しました",
  "data": {
    // 実際のデータ
  }
}
```

### エラー時
```json
{
  "success": false,
  "message": "エラーメッセージ",
  "data": null
}
```

### 使用例

#### 商品一覧取得
```bash
curl http://localhost:8080/api/products/store/1
```
```json
{
  "success": true,
  "message": "商品一覧を取得しました",
  "data": [
    {
      "productId": 1,
      "productName": "ねぎま",
      "description": "定番の焼き鳥",
      "price": 180.00,
      "imageUrl": "/images/negima.jpg",
      "categoryNames": ["焼き鳥"],
      "isAvailable": true,
      "isSoldOut": false
    }
  ]
}
```

#### 画像アップロード
```bash
curl -X POST http://localhost:8080/api/products/upload-image \
  -F "file=@/path/to/image.jpg"
```
```json
{
  "success": true,
  "message": "画像がアップロードされました",
  "data": {
    "imageUrl": "/uploads/products/9a7b3c5d-e2f1-4a6b-8c9d-0e1f2a3b4c5d.jpg"
  }
}
```

## API エンドポイント

### 認証
- `POST /api/auth/login` - ログイン
  - Request: `{ "loginId": "yamada", "password": "password123" }`
  - Response: `{ "token": "jwt_token", "employeeId": 1, "employeeName": "山田太郎", "roleId": 1 }`

### 商品
- `GET /api/products` - 全商品一覧
- `GET /api/products/store/{storeId}` - 店舗別商品一覧
- `GET /api/products/category/{categoryId}` - カテゴリ別商品一覧
- `GET /api/products/{productId}` - 商品詳細
- `POST /api/products` - 商品作成
- `PUT /api/products/{productId}` - 商品更新
- `DELETE /api/products/{productId}` - 商品削除
- `PATCH /api/products/{productId}/sold-out` - 売り切れ設定
- `POST /api/products/upload-image` - **商品画像アップロード**
  - Content-Type: `multipart/form-data`
  - Parameter: `file` (最大5MB、画像ファイルのみ)
  - Response: `{ "imageUrl": "/uploads/products/{uuid}.{ext}" }`

### 注文
- `POST /api/orders` - 注文作成
  - Request: `{ "storeId": 1, "tableNumber": "A01", "orderDetails": [...] }`
- `GET /api/orders/store/{storeId}` - 店舗別注文一覧（テーブルごとにグループ化）
- `GET /api/orders/{orderId}` - 注文詳細
- `PATCH /api/orders/details/{orderDetailId}/status` - 提供状態更新
  - Request: `{ "status": "SERVED" }` (PENDING/PREPARING/SERVED)

### テーブル管理
- `GET /api/tables/store/{storeId}` - 店舗別テーブル一覧
- `GET /api/tables/store/{storeId}/floor/{floor}` - フロア別テーブル一覧
- `GET /api/tables/{tableId}` - テーブル詳細
- `POST /api/tables/verify` - **テーブル番号検証**
  - Request: `{ "storeId": 1, "tableNumber": "A01" }`
  - Response: `{ "exists": true, "tableId": 1 }`
- `POST /api/tables` - テーブル作成
- `PUT /api/tables/{tableId}` - テーブル更新
- `PATCH /api/tables/{tableId}/status` - テーブル状態更新
- `DELETE /api/tables/{tableId}` - テーブル削除

### 従業員管理
- `GET /api/employees` - 全従業員一覧
- `GET /api/employees/store/{storeId}` - 店舗別従業員一覧
- `GET /api/employees/{employeeId}` - 従業員詳細
- `POST /api/employees` - 従業員作成
- `PUT /api/employees/{employeeId}` - 従業員更新
- `DELETE /api/employees/{employeeId}` - 従業員削除

### カテゴリ管理
- `GET /api/categories` - 全カテゴリ一覧
- `GET /api/categories/store/{storeId}` - 店舗別カテゴリ一覧
- `GET /api/categories/{categoryId}` - カテゴリ詳細
- `POST /api/categories` - カテゴリ作成
- `PUT /api/categories/{categoryId}` - カテゴリ更新
- `DELETE /api/categories/{categoryId}` - カテゴリ削除

### 呼び出し
- `POST /api/calls` - 呼び出し作成
- `GET /api/calls/store/{storeId}/pending` - 未対応呼び出し一覧
- `POST /api/calls/{callId}/respond` - 呼び出し対応

### 呼び出しタイプ
- `GET /api/call-types` - 呼び出しタイプ一覧

## データベーステーブル

### 主要テーブル
1. **stores** - 店舗
   - 店舗ID、店舗名、住所、電話番号、アクティブフラグ
   
2. **employees** - 従業員
   - 従業員ID、店舗ID、従業員名、ログインID、パスワード（BCryptハッシュ化）、ロールID
   
3. **tables** - テーブル（座席）
   - テーブルID、店舗ID、テーブル番号、階数、座席数、状態、QRコード
   
4. **categories** - カテゴリ
   - カテゴリID、店舗ID、カテゴリ名、表示順序、アクティブフラグ
   - 例: 焼き鳥、ごはんもの、スピード、ドリンク、デザート、無料備品、季節限定
   
5. **products** - 商品
   - 商品ID、店舗ID、商品名、説明、価格、画像URL、在庫状況、売り切れフラグ
   
6. **product_categories** - 商品-カテゴリ紐付け
   - 商品ID、カテゴリID（複合主キー）
   
7. **product_options** - 商品オプション
   - オプションID、商品ID、オプション名、オプション値、追加価格
   
8. **orders** - 注文
   - 注文ID、店舗ID、テーブルID、注文日時、合計金額、状態
   
9. **order_details** - 注文詳細
   - 注文詳細ID、注文ID、商品ID、数量、単価、小計、提供状態（PENDING/PREPARING/SERVED）
   
10. **order_detail_options** - 注文詳細オプション
    - 注文詳細ID、オプションID、オプション値、追加価格
    
11. **call_requests** - 呼び出しリクエスト
    - 呼び出しID、テーブルID、呼び出しタイプID、備考、状態、作成日時
    
12. **call_types** - 呼び出しタイプマスタ
    - タイプID、タイプ名（従業員呼び出し、お会計、その他）、説明

### ER関係
- stores 1:N employees, tables, categories, products, orders
- tables 1:N orders, call_requests
- products N:M categories (through product_categories)
- products 1:N product_options
- orders 1:N order_details
- order_details N:M product_options (through order_detail_options)
- call_types 1:N call_requests

## セキュリティ

- **JWT認証**: トークンベースの認証システム
- **BCryptパスワードハッシュ化**: 従業員パスワードはBCryptで暗号化して保存
- **CORS設定**: React フロントエンド（ポート3000, 3001）との通信を許可
- **静的リソースアクセス制御**: アップロードされた画像ファイルへのアクセス管理
- **入力検証**: ファイルアップロード時のサイズ制限（最大5MB）および形式チェック

### セキュリティ設定（WebConfig.java）
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静的リソース（画像）の提供
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## テスト

### テスト構成

プロジェクトには以下のテストが含まれています：

#### ユニットテスト
- **Services**
  - `AuthServiceTest` - 認証サービスのテスト
  - `OrderServiceTest` - 注文サービスのテスト
  - `ProductServiceTest` - 商品サービスのテスト

- **Utilities**
  - `JwtUtilTest` - JWT生成・検証のテスト

#### 統合テスト
- **Controllers**
  - `AuthControllerTest` - 認証APIのテスト
  - `ProductControllerTest` - 商品APIのテスト

- **Repositories**
  - `StoreRepositoryTest` - 店舗リポジトリのテスト
  - `EmployeeRepositoryTest` - 従業員リポジトリのテスト

### テスト実行

```bash
# すべてのテストを実行
mvn test

# 特定のテストクラスを実行
mvn test -Dtest=AuthServiceTest

# カバレッジレポート付きで実行
mvn clean test jacoco:report
```

### テストデータベース

テスト環境ではH2インメモリデータベースを使用します。テスト設定は `src/test/resources/application-test.properties` で管理されています。

## 開発メモ

### 実装済み機能
- [x] パスワードのハッシュ化実装（BCryptPasswordEncoder）
- [x] テーブル番号検証機能
- [x] 商品画像アップロード機能（MultipartFile）
- [x] 静的リソース提供（画像配信）
- [x] 提供状態管理（3段階: PENDING/PREPARING/SERVED）
- [x] カテゴリ別商品フィルタリング
- [x] 従業員・カテゴリ・商品のCRUD操作

### TODO
- [ ] WebSocket実装（リアルタイム通知）
- [ ] 会計連携（POS API）実装
- [ ] データエクスポート機能（CSV/Excel）
- [ ] 売上統計・レポート機能
- [ ] 予約管理機能
- [ ] メール通知機能
- [ ] ログ記録・監査機能

### カテゴリ一覧（サンプルデータ）
1. 焼き鳥 (ID: 1) - ねぎま、つくね、もも
2. ごはんもの (ID: 2) - 親子丼
3. スピード (ID: 3) - 枝豆
4. ドリンク (ID: 4) - 生ビール、ウーロン茶
5. デザート (ID: 5) - アイスクリーム
6. 無料備品 (ID: 6) - お手拭き、お取り皿
7. 季節限定 (ID: 7) - 季節の商品

### 注意事項
- `spring.jpa.hibernate.ddl-auto=update` は開発環境用です。本番では `validate` または `none` に変更してください。
- アップロードされた画像ファイルは `uploads/products/` ディレクトリに保存されます。Docker環境ではボリュームマウントで永続化されます。
- JWT トークンの有効期限は適切に設定してください（現在のデフォルト設定を確認）。
- ファイルアップロードサイズの上限は5MBに設定されています。必要に応じて `application.properties` で変更可能です。

### デフォルトログイン情報（開発環境）
- **管理者**
  - ログインID: `yamada`
  - パスワード: `password123`
  - ロール: 管理者 (roleId: 1)
  
- **スタッフ**
  - ログインID: `sato`
  - パスワード: `password123`
  - ロール: スタッフ (roleId: 2)

**⚠️ 本番環境では必ずパスワードを変更してください！**

## トラブルシューティング

### よくある問題

#### 1. データベース接続エラー
```
Communications link failure
```
**解決方法**: 
- MariaDBが起動しているか確認: `docker-compose ps` または `systemctl status mariadb`
- `application.properties` の接続情報（URL、ユーザー名、パスワード）を確認

#### 2. ファイルアップロードエラー
```
Maximum upload size exceeded
```
**解決方法**:
- `application.properties` でサイズ制限を変更:
  ```properties
  spring.servlet.multipart.max-file-size=10MB
  spring.servlet.multipart.max-request-size=10MB
  ```

#### 3. CORS エラー
```
Access-Control-Allow-Origin header is missing
```
**解決方法**:
- `WebConfig.java` でフロントエンドのURLを確認・追加
- ブラウザのキャッシュをクリア（Ctrl+Shift+Delete）

#### 4. 画像が表示されない
**解決方法**:
- `uploads/products/` ディレクトリが存在するか確認
- Dockerの場合、ボリュームマウントが正しいか確認: `docker-compose ps`
- 画像URLが正しいか確認: `/uploads/products/{filename}`

#### 5. JWT認証エラー
```
Invalid JWT token
```
**解決方法**:
- トークンの有効期限を確認
- ログアウトして再ログイン
- フロントエンドのlocalStorageをクリア

## 作成者

Me1td0wn76

---

## 更新履歴

- **2026-02-08**: 
  - 画像アップロード機能の実装
  - カテゴリ名の統一（焼き鳥、スピード）
  - テーブル検証機能の追加
  - READMEの全面更新
  
- **2026-02-07**:
  - 提供状態管理の3段階化（PENDING/PREPARING/SERVED）
  - 注文管理UIの改善
  - データ型の修正（Array.isArray対応）
  
- **初版**: 
  - 基本機能の実装
  - データベーススキーマの設計
  - JWT認証の実装
