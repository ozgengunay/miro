package com.ozgen.miro;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ozgen.miro.controllers.ratelimiting.MiroRateLimiter;
import com.ozgen.miro.controllers.ratelimiting.RateLimiter;
import com.ozgen.miro.domain.WidgetEntity;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiroApplication.class, args);
    }

    @Bean
    public Map<String, WidgetEntity> getInMemoryDatabaseEngine() {
        return new ConcurrentHashMap<String, WidgetEntity>();
    }

    @Bean
    public RateLimiter getWidgetRateLimiter(@Value("${rateLimiter.getWidget.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.getWidget.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("getWidget", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }

    @Bean(name = "createWidgetRateLimiter")
    public RateLimiter createWidgetRateLimiter(
            @Value("${rateLimiter.createWidget.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.createWidget.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("createWidget", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }

    @Bean(name = "updateWidgetRateLimiter")
    public RateLimiter updateWidgetRateLimiter(
            @Value("${rateLimiter.updateWidget.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.updateWidget.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("updateWidget", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }

    @Bean(name = "deleteWidgetRateLimiter")
    public RateLimiter deleteWidgetRateLimiter(
            @Value("${rateLimiter.deleteWidget.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.deleteWidget.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("deleteWidget", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }

    @Bean(name = "getAllWidgetsRateLimiter")
    public RateLimiter getAllWidgetsRateLimiter(
            @Value("${rateLimiter.getAllWidgets.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.getAllWidgets.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("getAllWidgets", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }

    @Bean(name = "globalWidgetRateLimiter")
    public RateLimiter globalWidgetRateLimiter(
            @Value("${rateLimiter.globalWidget.periodInSeconds}") String periodInSeconds,
            @Value("${rateLimiter.globalWidget.maxRequestsInPeriod}") String maxRequestsInPeriod) {
        return new MiroRateLimiter("globalWidget", Integer.parseInt(periodInSeconds),
                Integer.parseInt(maxRequestsInPeriod));
    }
}
