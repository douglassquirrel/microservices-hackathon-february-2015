package combo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import static java.net.URI.create;
import static java.util.Collections.singletonList;

public final class ComboFactory {

    public static Combo httpCombo(final URI baseUrl) {
        return new HttpCombo(restTemplate(baseUrl));
    }

    private ComboFactory() {
    }

    private static RestTemplate restTemplate(final URI baseUrl) {
        final RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(new GsonHttpMessageConverter()));
        restTemplate.getInterceptors().add(new BaseUrlInterceptor(baseUrl));
        return restTemplate;
    }

    private static final class BaseUrlInterceptor implements ClientHttpRequestInterceptor {

        private final URI baseUri;

        private BaseUrlInterceptor(final URI baseUri) {
            this.baseUri = baseUri;
        }

        @Override
        public ClientHttpResponse intercept(final HttpRequest request,
                                            final byte[] body,
                                            final ClientHttpRequestExecution execution) throws IOException {
            return execution.execute(new BaseUriRequestDecorator(request, baseUri), body);
        }

        private static final class BaseUriRequestDecorator implements HttpRequest {

            private final HttpRequest request;
            private final URI baseUri;

            private BaseUriRequestDecorator(final HttpRequest request, final URI baseUri) {
                this.request = request;
                this.baseUri = baseUri;
            }

            @Override public HttpMethod getMethod() {
                return request.getMethod();
            }

            @Override public URI getURI() {
                return create(baseUri + request.getURI().toString());
            }

            @Override public HttpHeaders getHeaders() {
                return request.getHeaders();
            }
        }
    }
}
