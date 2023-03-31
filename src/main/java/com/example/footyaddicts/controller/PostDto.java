package com.example.footyaddicts.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@AllArgsConstructor
public class PostDto  {
    private String title;
    private String content;
    private String coverImage;


    public PostDto() {

    }
}
