package cc.uman.generator;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 31644
 */
@EnableSwagger2Doc
@SpringBootApplication
public class FrameworkGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkGeneratorApplication.class, args);
    }
}
