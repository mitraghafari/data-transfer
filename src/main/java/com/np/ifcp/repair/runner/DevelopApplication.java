package com.np.ifcp.repair.runner;

import org.bardframework.commons.spring.boot.SpringBootRunner;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@ImportResource("classpath*:develop-configuration/**/**.xml")
@Import(Application.class)
@Component
public class DevelopApplication    {



    public static void main(String[] args) {
        SpringBootRunner.run(DevelopApplication.class, args);

    }
}
