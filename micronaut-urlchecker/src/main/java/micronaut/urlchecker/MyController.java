package micronaut.urlchecker;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/")
public class MyController {

    @Get("/check{?url}")
    public String check(String url) {
        return "Hello world " + url;
    }
}
