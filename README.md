# MOS - 飲食店注文管理システム

Spring Boot + React ベースの飲食店向け注文管理システムのバックエンド

## 技術スタック

- **Java**: 21
- **Spring Boot**: 4.0.2
- **Database**: MariaDB 15.x
- **Build Tool**: Maven
- **Server**: Apache Tomcat (組み込み)

## 主要な機能

### 1. 認証機能
- 従業員ログイン（ID/パスワード）
- JWTベースの認証

### 2. 注文機能
- QRコードからの注文開始
- メニュー選択・数量指定
- カート機能
- 注文送信

### 3. 提供機能
- 注文一覧の確認（卓別グルーピング）
- 提供完了チェック
- 備考記載

### 4. 席管理
- テーブル状態管理（空席/使用中/会計済/停止中）
- QRコード発行（1-5枚）
- フロア別管理

### 5. 時間管理
- 飲み放題タイマー機能
- 残り時間表示

### 6. 商品管理
- カテゴリ別商品管理
- 価格変更
- 売り切れ設定

### 7. 呼び出し機能
- 従業員呼び出し
- 呼び出し対応管理

## プロジェクト構造

```
src/main/java/com/example/mos/
├── Models/           # エンティティクラス（16テーブル）
├── Repositories/     # データアクセス層
├── Services/         # ビジネスロジック層
├── Controls/         # REST APIコントローラー
├── dto/             # データ転送オブジェクト
├── util/            # ユーティリティクラス
├── config/          # 設定クラス
└── exception/       # 例外ハンドリング
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

2. `src/main/resources/application.properties`を編集:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mosdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### ビルド＆実行

```bash
# 依存関係のインストール
mvn clean install

# アプリケーション起動
mvn spring-boot:run
```

サーバーは `http://localhost:8080` で起動します。

## API エンドポイント

### 認証
- `POST /api/auth/login` - ログイン

### 商品
- `GET /api/products/store/{storeId}` - 店舗別商品一覧
- `GET /api/products/category/{categoryId}` - カテゴリ別商品一覧
- `GET /api/products/{productId}` - 商品詳細
- `PATCH /api/products/{productId}/sold-out` - 売り切れ設定

### 注文
- `POST /api/orders` - 注文作成
- `GET /api/orders/store/{storeId}` - 店舗別注文一覧
- `GET /api/orders/{orderId}` - 注文詳細
- `PATCH /api/orders/details/{orderDetailId}/status` - 提供状態更新

### テーブル管理
- `GET /api/tables/store/{storeId}` - 店舗別テーブル一覧
- `GET /api/tables/store/{storeId}/floor/{floor}` - フロア別テーブル一覧
- `PATCH /api/tables/{tableId}/status` - テーブル状態更新
- `POST /api/tables/qr-codes` - QRコード生成

### 呼び出し
- `POST /api/calls` - 呼び出し作成
- `GET /api/calls/store/{storeId}/pending` - 未対応呼び出し一覧
- `POST /api/calls/{callId}/respond` - 呼び出し対応

## データベーステーブル

1. **stores** (T10) - 店舗
2. **employees** (T09) - 店員
3. **customers** (T16) - 顧客
4. **tables** (T11) - 卓
5. **table_status_history** (T12) - 卓状態履歴
6. **categories** (T07) - カテゴリ
7. **products** (T05) - 商品
8. **product_options** (T06) - 商品オプション
9. **orders** (T01) - 注文
10. **order_details** (T03) - 注文詳細
11. **order_detail_options** (T04) - 注文詳細オプション
12. **order_responses** (T02) - 注文対応
13. **payments** (T08) - 会計
14. **call_requests** (T13) - 呼出し
15. **call_responses** (T14) - 呼出し対応
16. **call_types** (T15) - 呼出し種類マスタ

## セキュリティ

- JWT認証
- HTTPS通信（本番環境）
- パスワードハッシュ化（実装推奨）
- CORS設定（React連携用）

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

### TODO
- [x] パスワードのハッシュ化実装（BCryptPasswordEncoder）
- [ ] 会計連携（POS API）実装
- [ ] データエクスポート機能（CSV）
- [ ] 画像アップロード機能
- [x] WebSocket実装（リアルタイム通知）

### 注意事項
- 現在、パスワードは平文で保存されています。本番環境では必ずハッシュ化してください。
- `spring.jpa.hibernate.ddl-auto=update` は開発環境用です。本番では `validate` または `none` に変更してください。
## 作成者
Me1td0wn76
