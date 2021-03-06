package micronaut.example;

public class MessageService {

    private final String message;

    public MessageService(String message) {
        this.message = message;
    }

    public String sayHello(String name, long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (Exception e) {

        }
        return message + " " + name;
    }
}
