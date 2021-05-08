package in.rcardin.reactor.emitter;

import in.rcardin.reactor.emitter.FooController.Foo;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FooControllerTest {

  @Autowired private WebTestClient webClient;

  @Test
  @Timeout(value = 6)
  void emitFooEverySecondShouldEmitASequenceOfFoo() {
    final FluxExchangeResult<Foo> fluxResult =
        webClient
            .get()
            .uri("/foos")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
            .returnResult(Foo.class);
    StepVerifier.create(fluxResult.getResponseBody())
        .expectNextCount(5)
        .thenCancel()
        .verify();
  }
}
