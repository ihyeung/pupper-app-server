//package com.utahmsd.pupper.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

//
//@Configuration
//@EnableJpaRepositories(basePackages = "com.utahmsd.pupper.dao")
//@EnableTransactionManagement
//public class DatabaseConfig {
//
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public DataSource dataSource() {
//        return new HikariDataSource(hikariConfig());
//    }
//
//    private HikariConfig hikariConfig() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(env.getProperty("db.url"));
//        config.setUsername(env.getProperty("db.username"));
//        config.setPassword(env.getProperty("db.password"));
//        config.setMaximumPoolSize(10);
//
//        return config;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setShowSql(true);
//        vendorAdapter.setGenerateDdl(false);
//        vendorAdapter.setDatabasePlatform(env.getProperty("db.hibernate.dialect"));
//
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setJpaVendorAdapter(vendorAdapter);
//        factoryBean.setPackagesToScan("ccom.utahmsd.pupper.dao");
//        factoryBean.setDataSource(dataSource());
//        factoryBean.setPersistenceUnitName("pupper-persistence-unit");
//
//        return factoryBean;
//    }
//
//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager() throws SQLException
//    {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return txManager;
//    }
//
//    @Bean
//    @Primary
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
//    {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//}
