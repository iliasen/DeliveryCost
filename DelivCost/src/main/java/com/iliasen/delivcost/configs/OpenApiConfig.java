package com.iliasen.delivcost.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Delivery Cost REST API",
        description = "Пример REST API для онлайн брокера DeliveryCost(Spring)",
        contact = @Contact(
                name = "iliasen",
                email = "iliasen03@mail.ru"
        ),
        license = @License(
                name = "BSD",
                url = "http://www.lemis.com/grog/SCO/cringely.php"
        ),
        version = "0.1"
)
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

}
