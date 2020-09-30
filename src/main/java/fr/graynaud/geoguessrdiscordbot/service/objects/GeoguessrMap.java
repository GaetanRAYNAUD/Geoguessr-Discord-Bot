
package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GeoguessrMap {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url")
    private String url;

    @JsonProperty("playUrl")
    private String playUrl;

    @JsonProperty("published")
    private Boolean published;

    @JsonProperty("banned")
    private Boolean banned;

    @JsonProperty("tags")
    private List<Object> tags = new ArrayList<>();

    @JsonProperty("images")
    private Images images;

    @JsonProperty("bounds")
    private Bounds bounds;

    @JsonProperty("customCoordinates")
    private Object customCoordinates;

    @JsonProperty("coordinateCount")
    private String coordinateCount;

    @JsonProperty("regions")
    private Object regions;

    @JsonProperty("creator")
    private Creator creator;

    @JsonProperty("created")
    private String created;

    @JsonProperty("updated")
    private String updated;

    @JsonProperty("numFinishedGames")
    private Integer numFinishedGames;

    @JsonProperty("likes")
    private Integer likes;

    @JsonProperty("likedByUser")
    private Boolean likedByUser;

    @JsonProperty("averageScore")
    private Integer averageScore;

    @JsonProperty("avatar")
    private Avatar avatar;

    @JsonProperty("difficulty")
    private String difficulty;

    @JsonProperty("difficultyLevel")
    private Integer difficultyLevel;

    @JsonProperty("highscore")
    private Object highscore;

    @JsonProperty("isUserMap")
    private Boolean isUserMap;

    @JsonProperty("highlighted")
    private Boolean highlighted;

    @JsonProperty("free")
    private Boolean free;

    @JsonProperty("inExplorerMode")
    private Boolean inExplorerMode;

    public GeoguessrMap() {
    }

    public GeoguessrMap(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Object getCustomCoordinates() {
        return customCoordinates;
    }

    public void setCustomCoordinates(Object customCoordinates) {
        this.customCoordinates = customCoordinates;
    }

    public String getCoordinateCount() {
        return coordinateCount;
    }

    public void setCoordinateCount(String coordinateCount) {
        this.coordinateCount = coordinateCount;
    }

    public Object getRegions() {
        return regions;
    }

    public void setRegions(Object regions) {
        this.regions = regions;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getNumFinishedGames() {
        return numFinishedGames;
    }

    public void setNumFinishedGames(Integer numFinishedGames) {
        this.numFinishedGames = numFinishedGames;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Object getHighscore() {
        return highscore;
    }

    public void setHighscore(Object highscore) {
        this.highscore = highscore;
    }

    public Boolean getIsUserMap() {
        return isUserMap;
    }

    public void setIsUserMap(Boolean isUserMap) {
        this.isUserMap = isUserMap;
    }

    public Boolean getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean getInExplorerMode() {
        return inExplorerMode;
    }

    public void setInExplorerMode(Boolean inExplorerMode) {
        this.inExplorerMode = inExplorerMode;
    }
}
