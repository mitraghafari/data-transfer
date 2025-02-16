package com.np.ifcp.repair.runner;


import org.bardframework.commons.config.ReloadableConfig;
import org.bardframework.commons.spring.boot.SpringBootRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, ValidationAutoConfiguration.class})
@ComponentScan(basePackages = {"com.np.ifcp.repair"}, excludeFilters = {@ComponentScan.Filter(value = {Configuration.class})})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ImportResource("classpath*:spring-configuration/**/**.xml")
@EnableTransactionManagement
@EnableScheduling
public class Application implements SpringBootRunner {

    public static void main(String[] args) {
        SpringBootRunner.run(Application.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(30);
        messageSource.setConcurrentRefresh(true);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }


    @Bean("reloadableConfig")
    public ReloadableConfig reloadableConfig(@Value("${reloadableConfig.path}") List<String> reloadableConfigPath) throws IOException {
        return new ReloadableConfig(reloadableConfigPath);
    }
}
