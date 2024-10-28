package org.app.softunigamestore.config;

import org.app.softunigamestore.utils.ValidationUtil;
import org.app.softunigamestore.utils.ValidationUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }
}