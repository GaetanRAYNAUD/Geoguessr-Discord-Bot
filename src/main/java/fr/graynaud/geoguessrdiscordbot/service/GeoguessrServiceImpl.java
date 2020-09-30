package fr.graynaud.geoguessrdiscordbot.service;

import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.exception.GeoguessrDiscordBotException;
import fr.graynaud.geoguessrdiscordbot.common.utils.GeoguessrUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.objects.CreateChallengeRequest;
import fr.graynaud.geoguessrdiscordbot.service.objects.CreateChallengeResponse;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import fr.graynaud.geoguessrdiscordbot.service.objects.SearchMapResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoguessrServiceImpl implements GeoguessrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoguessrServiceImpl.class);

    private final RestTemplate restTemplate;

    private final HttpHeaders httpHeaders;

    private final UriComponentsBuilder searchUriBuilder;

    private final MapsCache mapsCache;

    public GeoguessrServiceImpl(ApplicationProperties applicationProperties, MapsCache mapsCache) {
        this.mapsCache = mapsCache;
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        this.httpHeaders.add(HttpHeaders.COOKIE, Constants._NCFA + "=" + applicationProperties.getGeoguessrNcfa());
        this.searchUriBuilder = UriComponentsBuilder.fromUriString(Constants.API_SEARCH_MAPS_URL)
                                                    .queryParam("page", 0)
                                                    .queryParam("count", 5);
    }

    @Override
    public GeoguessrMap getMap(String slug) {
        GeoguessrMap geoguessrMap = this.mapsCache.getBySlug(slug);

        if (geoguessrMap == null) {
            geoguessrMap = this.mapsCache.getByName(slug);

            if (geoguessrMap == null) {
                try {
                    ResponseEntity<GeoguessrMap> response = this.restTemplate.getForEntity(Constants.API_MAPS_URL + GeoguessrUtils.cleanToUrl(slug),
                                                                                      GeoguessrMap.class);

                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        this.mapsCache.registerMap(response.getBody());
                        return response.getBody();
                    }

                    return null;
                } catch (HttpClientErrorException.NotFound e) {
                    return null;
                } catch (Exception e) {
                    LOGGER.error("An error occurred while getting map from Geoguessr: {} !", e.getMessage(), e);
                    return null;
                }
            }
        }

        return geoguessrMap;
    }

    @Override
    public String getGameToken(GeoguessrMap geoguessrMap, Integer duration) {
        return getGameToken(geoguessrMap, duration, false, false);
    }

    @Override
    public String getGameToken(GeoguessrMap geoguessrMap, Integer duration, boolean isCountryStreak, boolean forbidMoving) {
        CreateChallengeRequest createChallengeBody = new CreateChallengeRequest(geoguessrMap.getSlug(), duration, isCountryStreak, forbidMoving);

        HttpEntity<CreateChallengeRequest> httpEntity = new HttpEntity<>(createChallengeBody, this.httpHeaders);

        ResponseEntity<CreateChallengeResponse> response = this.restTemplate.exchange(Constants.GENERATE_TOKEN_URL, HttpMethod.POST, httpEntity,
                                                                                      CreateChallengeResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getToken();
        } else {
            throw new GeoguessrDiscordBotException();
        }
    }

    @Override
    public List<SearchMapResult> searchMaps(String query) {
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<>(null, this.httpHeaders);
            ResponseEntity<List<SearchMapResult>> response = this.restTemplate.exchange(this.searchUriBuilder.cloneBuilder().queryParam("q", query).build().toUri(),
                                                                                   HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                response.getBody().forEach(searchMapResult -> this.getMap(searchMapResult.id));
                return response.getBody();
            }

            return new ArrayList<>();
        } catch (HttpClientErrorException.NotFound e) {
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.error("An error occurred while searching maps from Geoguessr (query: {}): {} !", query, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
