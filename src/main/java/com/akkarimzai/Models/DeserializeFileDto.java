package com.akkarimzai.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DeserializeFileDto {
    private String srcPath;
    private String dstPath;
}
