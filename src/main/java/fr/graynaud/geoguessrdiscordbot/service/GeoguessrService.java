package fr.graynaud.geoguessrdiscordbot.service;

import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import fr.graynaud.geoguessrdiscordbot.service.objects.SearchMapResult;

import java.util.List;

public interface GeoguessrService {

    GeoguessrMap getMap(String slug);

    String getGameToken(GeoguessrMap geoguessrMap, Integer duration);

    List<SearchMapResult> searchMaps(String query);
}
