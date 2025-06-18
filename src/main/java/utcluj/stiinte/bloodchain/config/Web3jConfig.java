package utcluj.stiinte.bloodchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {
    
    @Bean
    public Web3j web3j(@Value("${ethereum.url}") String url) {
        return Web3j.build(new HttpService(url));
    }
    
    @Bean
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "local")
    public Credentials adminCredentialsLocal(@Value("${ethereum.admin.privateKey}") String privateKey) {
        return Credentials.create(privateKey);
    }
    
    @Bean
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "default")
    public Credentials adminCredentials() {
        // TODO get private key from Secret Vault
        return null;
    }
}
