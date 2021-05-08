package in.rcardin.reactor.emitter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/foos")
public class FooController {
  
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  Flux<Foo> emitFooEverySecond() {
    // Eventually, we can consider the creation of Foo objects as business logic,
    // moving it into a dedicated FooService.
    return Flux.interval(Duration.ofSeconds(1L))
        .map(i -> new Foo(UUID.randomUUID().toString(), String.format("Foo-%s", i)));
  }
  
  public static class Foo {
    private final String id;
    private final String name;
  
    @JsonCreator
    public Foo(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name) {
      this.id = id;
      this.name = name;
    }
  
    public String getId() {
      return id;
    }
  
    public String getName() {
      return name;
    }
  }
}
