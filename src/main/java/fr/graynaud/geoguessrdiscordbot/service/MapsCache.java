package fr.graynaud.geoguessrdiscordbot.service;

import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.GeoguessrUtils;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class MapsCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapsCache.class);

    private static final int POPULAR_SIZE = 10;

    private final Map<String, GeoguessrMap> geoguessrMapsBySlug = new HashMap<>();

    private final Map<String, GeoguessrMap> geoguessrMapsByName = new HashMap<>();

    private final SortedSet<GeoguessrMap> popularMaps = new TreeSet<>(Comparator.comparing(GeoguessrMap::getLikes).reversed());

    public MapsCache() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(Constants.API_POPULAR_MAPS_URL)
                                          .queryParam("page", 0)
                                          .queryParam("count", 10)
                                          .build()
                                          .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<GeoguessrMap>> responsePopulars = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            if (responsePopulars.getStatusCode().is2xxSuccessful() && responsePopulars.getBody() != null) {
                responsePopulars.getBody().forEach(this::registerMap);
                responsePopulars.getBody().forEach(this::registerPopularMap);
            } else {
                LOGGER.error("An error occurred while getting popular maps from Geoguessr: {} !", responsePopulars.toString());
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while initialization: {} !", e.getMessage(), e);
        }
    }

    public SortedSet<GeoguessrMap> getPopularMaps() {
        return popularMaps;
    }

    public GeoguessrMap getBySlug(String slug) {
        return this.geoguessrMapsBySlug.get(slug);
    }

    public GeoguessrMap getByName(String name) {
        return this.geoguessrMapsByName.get(GeoguessrUtils.cleanName(name));
    }

    public void registerMap(GeoguessrMap geoguessrMap) {
        this.geoguessrMapsBySlug.put(geoguessrMap.getSlug(), geoguessrMap);
        this.geoguessrMapsByName.putIfAbsent(GeoguessrUtils.cleanName(geoguessrMap.getName()), geoguessrMap);
    }

    private void registerPopularMap(GeoguessrMap geoguessrMap) {
        if (this.popularMaps.size() < POPULAR_SIZE) {
            this.popularMaps.add(geoguessrMap);
        } else if (this.popularMaps.last().getLikes() < geoguessrMap.getLikes()) {
            this.popularMaps.add(geoguessrMap);
            this.popularMaps.remove(this.popularMaps.last());
        }
    }
}
