package com.tibame.peterparker.dto;

import java.util.List;

public class FilterRequest {
    private List<String> types;
    private Double distance;

    // Getters and setters
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
