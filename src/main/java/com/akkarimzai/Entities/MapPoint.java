package com.akkarimzai.Entities;

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
