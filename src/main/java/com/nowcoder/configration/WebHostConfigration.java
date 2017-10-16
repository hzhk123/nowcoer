package com.nowcoder.configration;

import com.nowcoder.interceptor.HostholderInterceptor;
import com.nowcoder.interceptor.PassportIntercetpor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebHostConfigration extends WebMvcConfigurerAdapter{

    @Autowired
    PassportIntercetpor passportIntercetpor ;

    @Autowired
    HostholderInterceptor hostholderInterceptor ;
    public void  addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(passportIntercetpor) ;
        registry.addInterceptor(hostholderInterceptor).addPathPatterns("/setting*") ;
        super.addInterceptors(registry);
    }
}
