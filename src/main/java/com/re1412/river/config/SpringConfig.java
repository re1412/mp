package com.re1412.river.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.List;

@Configuration
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    @Value("${tomcat.ajp.protocol}")
    String ajpProtocol;
    @Value("${tomcat.ajp.port}")
    int ajpPort;

    @Bean
    public ServletWebServerFactory servletContainer(){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createAjpConnector());

        return tomcat;
    }

    private Connector createAjpConnector(){
        Connector ajpConnector = new Connector(ajpProtocol);
        ajpConnector.setPort(ajpPort);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
        ((AbstractAjpProtocol<?>)ajpConnector.getProtocolHandler()).setSecretRequired(false);
        return ajpConnector;
    }

    /* templates 정적 페이지 경로 추가 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/templates/views/", "file:src/main/resources/static/","classpath:/static/");
        registry.addResourceHandler("/painting/**").
                addResourceLocations("file:///" + Paths.get(System.getProperty("user.home")).resolve("images/painting") + "/");
    }
}
