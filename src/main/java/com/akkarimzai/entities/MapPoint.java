package com.akkarimzai.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MapPoint {
    private String Slug;
    private Coordinate Coordinate;
}
