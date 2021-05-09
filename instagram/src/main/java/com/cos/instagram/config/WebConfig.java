package com.cos.instagram.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final String uploadImagesPath;

  public WebConfig(@Value("${custom.path.upload-images}") String uploadImagesPath) {
    this.uploadImagesPath = uploadImagesPath;
  }

  @Override 
  public void addResourceHandlers(ResourceHandlerRegistry registry) { 
	  registry.addResourceHandler("/js/**")
	  .addResourceLocations("classpath:/static/js/"); 
	  registry.addResourceHandler("/css/**") 
	  .addResourceLocations("classpath:/static/css/"); 
	  registry.addResourceHandler("/img/**") 
	  .addResourceLocations("classpath:/static/img/"); 
	  registry.addResourceHandler("/fonts/**") 
	  .addResourceLocations("classpath:/static/fonts/"); 
	  registry.addResourceHandler("/data/**") 
	  .addResourceLocations("classpath:/static/data/"); 
//	  registry.addResourceHandler("/") 
//	  .addResourceLocations("classpath:/static/index.html"); 
	  
	  // 업로드 이미지용 외부 폴더 추가 
	  registry.addResourceHandler("/upload/**") 
	  .addResourceLocations("file:///"+uploadImagesPath) // 웹에서 이미지 호출시 'file:///' 설정됨 
	  .setCachePeriod(3600) 
	  	.resourceChain(true) 
	  .addResolver(new PathResourceResolver()); 
  }

  
}


