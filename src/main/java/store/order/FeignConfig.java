package store.order;

import feign.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${product.service.url:http://localhost:8080}")
    private String productServiceUrl;

}
