package luckyvicky.petharmony.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    /**
     * 주 데이터 소스 프로퍼티 빈 정의
     *
     * @return DataSourceProperties 객체를 반환
     */
    @Primary
    @Bean(name = "petharmonyDataSourceProperties")
    @ConfigurationProperties("petharmony.datasource")
    public DataSourceProperties petharmonyDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 주 데이터 소스 빈 정의
     *
     * @return DataSource 객체를 반환
     */
    @Primary
    @Bean(name = "petharmonyDataSource")
    @ConfigurationProperties("petharmony.datasource")
    public DataSource petharmonyDataSource() {
        return petharmonyDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    /**
     * 주 엔티티 매니저 팩토리 빈 정의
     *
     * @param petharmonyDataSource 주 데이터 소스
     * @return LocalContainerEntityManagerFactoryBean 객체를 반환
     */
    @Primary
    @Bean(name = "petharmonyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean petharmonyEntityManagerFactory(
            @Qualifier("petharmonyDataSource") DataSource petharmonyDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(petharmonyDataSource);
        em.setPackagesToScan("luckyvicky.petharmony.entity"); // 엔티티 클래스 패키지 스캔
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // JPA 속성 설정
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * 주 트랜잭션 매니저 빈 정의
     *
     * @param petharmonyEntityManagerFactory 주 엔티티 매니저 팩토리
     * @return PlatformTransactionManager 객체를 반환
     */
    @Primary
    @Bean(name = "petharmonyTransactionManager")
    public PlatformTransactionManager petharmonyTransactionManager(
            @Qualifier("petharmonyEntityManagerFactory") LocalContainerEntityManagerFactoryBean petharmonyEntityManagerFactory) {
        return new JpaTransactionManager(petharmonyEntityManagerFactory.getObject());
    }

    /**
     * 사용자 데이터 소스 프로퍼티 빈 정의
     *
     * @return DataSourceProperties 객체를 반환
     */
    @Bean(name = "usersDataSourceProperties")
    @ConfigurationProperties("users.datasource")
    public DataSourceProperties usersDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 사용자 데이터 소스 빈 정의
     *
     * @return DataSource 객체를 반환
     */
    @Bean(name = "usersDataSource")
    @ConfigurationProperties("users.datasource")
    public DataSource usersDataSource() {
        return usersDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    /**
     * 사용자 엔티티 매니저 팩토리 빈 정의
     *
     * @param usersDataSource 사용자 데이터 소스
     * @return LocalContainerEntityManagerFactoryBean 객체를 반환
     */
    @Bean(name = "usersEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usersEntityManagerFactory(
            @Qualifier("usersDataSource") DataSource usersDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(usersDataSource);
        em.setPackagesToScan("luckyvicky.petharmony.users.entity"); // 사용자 엔티티 클래스 패키지 스캔
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // JPA 속성 설정
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * 사용자 트랜잭션 매니저 빈 정의
     *
     * @param usersEntityManagerFactory 사용자 엔티티 매니저 팩토리
     * @return PlatformTransactionManager 객체를 반환
     */
    @Bean(name = "usersTransactionManager")
    public PlatformTransactionManager usersTransactionManager(
            @Qualifier("usersEntityManagerFactory") LocalContainerEntityManagerFactoryBean usersEntityManagerFactory) {
        return new JpaTransactionManager(usersEntityManagerFactory.getObject());
    }
}
