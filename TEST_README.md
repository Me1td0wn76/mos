# MOSシステム - テスト概要

## テスト構成

このプロジェクトには、以下のテストファイルが準備されています：

### 1. ユニットテスト (Services)

#### AuthServiceTest
- `testLogin_Success()` - 正常なログイン
- `testLogin_InvalidLoginId()` - 存在しないログインID
- `testLogin_InvalidPassword()` - パスワード不一致
- `testLogin_InactiveEmployee()` - 無効化されたアカウント

#### OrderServiceTest
- `testCreateOrder_NormalPlan()` - 通常プラン注文作成
- `testCreateOrder_InvalidQRCode()` - 無効なQRコード
- `testCreateOrder_ProductSoldOut()` - 売り切れ商品
- `testUpdateOrderDetailStatus()` - 注文詳細ステータス更新

#### ProductServiceTest
- `testGetProductsByStore()` - 店舗別商品一覧取得
- `testGetProductsByStore_StoreNotFound()` - 存在しない店舗
- `testGetProductById()` - 商品詳細取得
- `testUpdateProductSoldOutStatus()` - 売り切れステータス更新

### 2. 統合テスト (Controllers)

#### AuthControllerTest
- `testLogin_Success()` - ログインAPI成功
- `testLogin_InvalidCredentials()` - ログインAPI失敗

#### ProductControllerTest
- `testGetProductsByStore()` - 商品一覧API
- `testGetProductById()` - 商品詳細API
- `testUpdateSoldOutStatus()` - 売り切れ設定API

### 3. リポジトリテスト

#### StoreRepositoryTest
- `testSaveAndFind()` - 店舗保存と取得
- `testFindByIsActive()` - アクティブ店舗検索
- `testUpdate()` - 店舗更新
- `testDelete()` - 店舗削除

#### EmployeeRepositoryTest
- `testSaveAndFind()` - 従業員保存と取得
- `testFindByLoginId()` - ログインID検索
- `testFindByStore()` - 店舗別従業員取得
- `testFindByStoreAndIsActive()` - アクティブ従業員検索

### 4. ユーティリティテスト

#### JwtUtilTest
- `testGenerateToken()` - JWTトークン生成
- `testExtractLoginId()` - ログインID抽出
- `testExtractEmployeeId()` - 従業員ID抽出
- `testExtractStoreId()` - 店舗ID抽出
- `testExtractRoleId()` - 役職ID抽出
- `testValidateToken()` - トークン検証
- `testIsTokenNotExpired()` - トークン有効期限確認

## テスト実行方法

### 現在の状態

⚠️ **注意**: Lombokのアノテーション処理に問題があり、現在テストは実行できません。

### 問題の解決方法

1. **IDEで Lombok プラグインをインストール**
   - IntelliJ IDEA: Settings > Plugins > "Lombok" で検索してインストール
   - VS Code: Extensions > "Lombok Annotations Support" をインストール

2. **Lombokアノテーションプロセッサーの有効化**
   - IntelliJ IDEA: Settings > Build > Compiler > Annotation Processors > "Enable annotation processing" をチェック

3. **プロジェクトの再ビルド**
   ```bash
   mvn clean install
   ```

4. **テスト実行**
   ```bash
   # すべてのテストを実行
   mvn test
   
   # 特定のテストクラスを実行
   mvn test -Dtest=DemoApplicationTests
   ```

## テスト環境

- **データベース**: H2インメモリデータベース
- **設定ファイル**: `src/test/resources/application-test.properties`
- **テストフレームワーク**: JUnit 5 + Mockito
- **Spring Boot バージョン**: 3.2.2

## 次のステップ

1. Lombok設定の修正
2. テスト実行の成功確認
3. カバレッジレポートの生成
4. 継続的インテグレーション（CI）の設定
