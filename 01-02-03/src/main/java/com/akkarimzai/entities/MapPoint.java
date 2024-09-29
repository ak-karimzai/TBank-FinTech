package com.akkarimzai.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapPoint {
    private String slug;
    private Coordinate coords;
}
