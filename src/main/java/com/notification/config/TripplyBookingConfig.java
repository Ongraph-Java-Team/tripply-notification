package com.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

@Configurable
public class TripplyBookingConfig {

    public OpenAPI defineOpenApi(){
        Server server = new Server();
        server.setUrl("http://localhost:8087");
        server.setDescription("Development");

        Contact contact = new Contact();
        contact.setName("Tripply");
        contact.setEmail("tripply@ongraph.com");

        Info information = new Info()
                .title("Tripply - Hotel Booking System")
                .version("1.0")
                .description("This API exposes endpoints for notification service")
                .contact(contact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
