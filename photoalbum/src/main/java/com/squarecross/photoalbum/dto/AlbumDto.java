package com.squarecross.photoalbum.dto;

import com.squarecross.photoalbum.domain.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class AlbumDto {
    Long albumId;
    String albumName;
    Date createdAt;
    int count;

    private List<String> thumbUrls;

}
