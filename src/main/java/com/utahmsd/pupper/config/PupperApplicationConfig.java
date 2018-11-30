package com.utahmsd.pupper.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(lazyInit = true)
@EnableJpaRepositories("com.utahmsd.pupper.dao")
@EnableTransactionManagement
public class PupperApplicationConfig {

    @Value("${spring.datasource.driver-class-name:}")
    private String dbDriver;

    @Value("${spring.datasource.url:}")
    private String dbUrl;

    @Value("${spring.datasource.username:}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${zipCode.api.key}")
    private String zipCodeApiKey;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("pupper")));
//
//        return cacheManager;
//    }

//    @Bean
//    public DataSource dataSource() {
////        final HikariConfig hikariConfig = new HikariConfig();
//        return DataSourceBuilder.create().driverClassName(dbDriver).url(dbUrl).username(dbUsername).password(dbPassword).build();
////        hikariConfig.setDriverClassName(dbDriver);
////        hikariConfig.setJdbcUrl(dbUrl);
////        hikariConfig.setUsername(dbUsername);
////        hikariConfig.setPassword(dbPassword);
////        hikariConfig.setMaximumPoolSize(10);
////
////        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean
//    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactoryBean.setPackagesToScan("com.utahmsd.pupper");
//        entityManagerFactoryBean.setDataSource(dataSource());
//        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
////        entityManagerFactoryBean.afterPropertiesSet();
//
//        return entityManagerFactoryBean;
//    }
//
//    @Bean
//    PlatformTransactionManager transactionManager() {
//        return new JpaTransactionManager(entityManagerFactory().getObject());
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClients.createDefault();
    }
}
