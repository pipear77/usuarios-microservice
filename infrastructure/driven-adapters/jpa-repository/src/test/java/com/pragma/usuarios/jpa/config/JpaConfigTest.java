package com.pragma.usuarios.jpa.config;

import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class JpaConfigTest {

    @Test
    void entityManagerFactoryTest() {
        JpaConfig jpaConfig = new JpaConfig();
        DataSource dataSource = mock(DataSource.class);

        LocalContainerEntityManagerFactoryBean factoryBean = jpaConfig.entityManagerFactory(
                dataSource,
                "org.hibernate.dialect.PostgreSQLDialect"
        );

        assertNotNull(factoryBean);
    }
}