package backend.backend;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class BackendApplication {
	//aws cloudfront를 사용할 때 privateKey를 RSA형식으로 변경할 때 사용하는 라이브러리 aws권장 라이브러리이다.
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
