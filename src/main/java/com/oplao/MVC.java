package com.oplao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.oplao")
public class MVC extends WebMvcConfigurerAdapter {

    private static final int BROWSER_CACHE_CONTROL = 604800;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("/images/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("/img/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/assets/js/**")
                .addResourceLocations("/assets/js/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/assets/plugins/**")
                .addResourceLocations("/assets/plugins/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/scss/**")
                .addResourceLocations("/scss/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/svg/**")
                .addResourceLocations("/svg/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/css/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);

        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("/js/")
                .setCachePeriod(BROWSER_CACHE_CONTROL);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:messages");
        resource.setDefaultEncoding("UTF-8");
        return resource;
    }
}