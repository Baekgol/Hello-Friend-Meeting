package com.web.curation;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.web.curation.config.JwtInterceptor;
import com.web.curation.config.SSHConnection;

@SpringBootApplication
public class WebCurationApplication implements WebMvcConfigurer{
	
	@Autowired
	private JwtInterceptor jwtInterceptor;

	private static SSHConnection sshConnection;

	public static void main(String[] args) {
		
		sshConnection = new SSHConnection();
		SpringApplication.run(WebCurationApplication.class, args);
	}
	

////	JWTInterceptor를 설치한다.
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**") // 기본 적용 경로
//                .excludePathPatterns(Arrays.asList("/login", "/join", "/error"));// 적용 제외 경로
//    }

//  Interceptor를 이용해서 처리하므로 전역의 Cross Origin 처리를 해준다.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("auth-token");
    }

}
