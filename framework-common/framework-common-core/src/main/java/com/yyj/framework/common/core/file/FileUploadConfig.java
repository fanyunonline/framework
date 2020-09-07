package com.yyj.framework.common.core.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;

/**
 * Created by yangyijun on 2018/5/11.
 */
@Configuration
@Component
public class FileUploadConfig {

    @Value("${fileUpload.singleMaxSize:102400}")
    private String singleMaxSize;

    @Value("${fileUpload.totalMaxSize:10240000}")
    private String totalMaxSize;

    @Value("${fileUpload.rootPath:}")
    private String uploadRootPath;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(singleMaxSize); //KB,MB
        factory.setMaxRequestSize(totalMaxSize);
        return factory.createMultipartConfig();
    }

    public String getUploadRootPath() {
        return this.uploadRootPath;
    }
}
