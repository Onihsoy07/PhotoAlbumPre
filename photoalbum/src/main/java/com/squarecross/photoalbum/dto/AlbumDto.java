package com.squarecross.photoalbum.dto;

import com.squarecross.photoalbum.domain.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class AlbumDto {
    private Long albumId;
    private String albumName;
    private Date createdAt;
    private int count;

    private List<String> thumbUrls;

}
