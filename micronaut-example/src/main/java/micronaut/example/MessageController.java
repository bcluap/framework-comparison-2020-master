package micronaut.example;


import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

import javax.validation.constraints.NotBlank;

@Controller("/")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    
    @Get(value = "/hello/{name}", produces = MediaType.TEXT_PLAIN)
    String hello(@NotBlank String name, @QueryValue(value="sleep", defaultValue="0") long sleep) {
        return messageService.sayHello(name, sleep);
    }
}
