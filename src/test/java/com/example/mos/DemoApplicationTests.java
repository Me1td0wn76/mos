package com.example.mos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * アプリケーション起動テスト
 */
@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

	@Test
	@DisplayName("アプリケーションコンテキストのロード")
	void contextLoads() {
		// アプリケーションが正常に起動することを確認
	}

}
