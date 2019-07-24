package pl.ramalash.moviecatalogservice.resources;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pl.ramalash.moviecatalogservice.model.CatalogItem;
import pl.ramalash.moviecatalogservice.model.Movie;
import pl.ramalash.moviecatalogservice.model.Rating;
import pl.ramalash.moviecatalogservice.model.UserRating;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class MovieCatalogResource {

    private RestTemplate restTemplate;
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/545444" + userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
           Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();

            return new CatalogItem(movie.getName(), "test", 4);
        })
                .collect(Collectors.toList());

    }
}
