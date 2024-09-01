package com.akkarimzai.entities;

import lombok.*;

@Data
@AllArgsConstructor
public class MapPoint {
    private String slug;
    private Coordinate coords;
}
