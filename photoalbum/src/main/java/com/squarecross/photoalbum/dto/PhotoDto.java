package com.squarecross.photoalbum.dto;

import com.squarecross.photoalbum.domain.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PhotoDto {
    private Long photoId;
    private String fileName;
    private Integer fileSize;
    private String originalUrl;
    private String thumbUrl;
    private Date uploadedAt;
    private Long albumId;

}
