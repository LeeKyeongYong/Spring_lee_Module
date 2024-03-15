package com.surl.studyurl.domain.home.controller;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageGeneratorController {


    @Autowired
    private ImageClient openAiImageClient;

    @GetMapping("/image")
    public Image getImage(@RequestParam String imagePrompt){
        ImageResponse response = openAiImageClient.call(
                new ImagePrompt(imagePrompt,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withN(4)
                                .withHeight(1024)
                                .withWidth(1024).build())

        );
        return response.getResult().getOutput();
    }
}