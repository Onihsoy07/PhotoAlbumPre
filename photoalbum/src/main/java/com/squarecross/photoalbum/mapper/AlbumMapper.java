package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.domain.*;

public class AlbumMapper {
    public static AlbumDto convertToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(album.getAlbumId());
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setCreatedAt(album.getCreatedAt());
        return albumDto;
    }

}
