package com.sena.sistemainventario.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de DataSource para Render.com
 * 
 * Render genera URIs tipo: postgres://user:password@host:port/dbname
 * Pero Spring Boot necesita:  jdbc:postgresql://host:port/dbname
 * 
 * Esta clase convierte el formato automáticamente.
 */
@Configuration
@ConditionalOnProperty(name = "DATABASE_DRIVER", havingValue = "org.postgresql.Driver")
public class RenderDataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:}")
    private String renderUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:}")
    private String password;

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = renderUrl;

        // Convertir postgres://user:pass@host:port/db → jdbc:postgresql://host:port/db
        if (jdbcUrl.startsWith("postgres://") || jdbcUrl.startsWith("postgresql://")) {
            jdbcUrl = jdbcUrl.replaceFirst("postgres(ql)?://", "jdbc:postgresql://");

            // Extraer usuario y contraseña de la URI si están embebidos
            if (jdbcUrl.contains("@")) {
                String afterProtocol = jdbcUrl.substring("jdbc:postgresql://".length());
                String credentials = afterProtocol.substring(0, afterProtocol.indexOf("@"));
                String hostAndDb = afterProtocol.substring(afterProtocol.indexOf("@") + 1);
                jdbcUrl = "jdbc:postgresql://" + hostAndDb;

                if (credentials.contains(":")) {
                    String[] parts = credentials.split(":", 2);
                    // Solo sobreescribir si no vienen de otra variable
                    if (username == null || username.isEmpty()) {
                        username = parts[0];
                    }
                    if (password == null || password.isEmpty()) {
                        password = parts[1];
                    }
                }
            }
        }

        System.out.println("🔗 Conectando a PostgreSQL (Render)...");
        System.out.println("   URL: " + jdbcUrl.replaceAll(":[^@/]+@", ":****@"));

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
