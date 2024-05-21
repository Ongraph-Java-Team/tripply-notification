package com.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configurable
public class TripplyBookingConfig {

    @Value("${application.notification.base-url}")
    private String url;

    @Value("${spring.application.version}")
    private String version;

    public OpenAPI defineOpenApi(){
        Server server = new Server();
        server.setUrl(url);
        server.setDescription("Development");

        Contact contact = new Contact();
        contact.setName("Tripply");
        contact.setEmail("tripply@ongraph.com");

        Info information = new Info()
                .title("Tripply - Hotel Booking System")
                .version(version)
                .description("This API exposes endpoints for notification service")
                .contact(contact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
