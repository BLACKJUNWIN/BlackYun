package com.black.blackyun.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class blackFile implements Serializable {
    private String mainPath;
    private String path;
    private String fileName;
    private String type;
    private long size;}
