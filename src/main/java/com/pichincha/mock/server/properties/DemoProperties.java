package com.pichincha.mock.server.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("bs.account")
public class DemoProperties {

    @Value("${mock.server.baseUrl}")
    private String baseUrl;
}
