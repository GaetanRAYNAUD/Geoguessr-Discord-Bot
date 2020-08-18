package fr.graynaud.geoguessrdiscordbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.GeoguessrUtils;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    private final RestTemplate restTemplate = new RestTemplate();

    private final List<GeoguessrMap> geoguessrMaps = new ArrayList<>();

    private final Map<String, GeoguessrMap> geoguessrMapsBySlug = new HashMap<>();

    private final Map<String, GeoguessrMap> geoguessrMapsByName = new HashMap<>();

    private final SortedSet<GeoguessrMap> popularMaps = new TreeSet<>(Comparator.comparing(GeoguessrMap::getLikes).reversed());

    public MapsCache() {
        try {
            new ObjectMapper().readValue(Constants.POPULAR_FAKE_DATA, new TypeReference<List<GeoguessrMap>>() {}).forEach(this::registerMap);
            new ObjectMapper().readValue(Constants.POPULAR_FAKE_DATA, new TypeReference<List<GeoguessrMap>>() {}).forEach(this::registerPopularMap);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
/*        try {
            URI uri = UriComponentsBuilder.fromUriString(Constants.API_POPULAR_MAPS_URL)
                                          .queryParam("page", 0)
                                          .queryParam("count", 10)
                                          .build()
                                          .toUri();

            ResponseEntity<List<GeoguessrMap>> responsePopulars = this.restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            if (responsePopulars.getStatusCode().is2xxSuccessful() && responsePopulars.getBody() != null) {
                responsePopulars.getBody().forEach(this::registerMap);
            } else {
                LOGGER.error("An error occurred while getting popular maps from Geoguessr: {} !", responsePopulars.toString());
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while initialization: {} !", e.getMessage(), e);
        }*/
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
        this.geoguessrMaps.add(geoguessrMap);
        this.geoguessrMapsBySlug.put(geoguessrMap.getSlug(), geoguessrMap);
        this.geoguessrMapsByName.put(GeoguessrUtils.cleanName(geoguessrMap.getName()), geoguessrMap);
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
