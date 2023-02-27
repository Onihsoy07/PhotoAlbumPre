package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.mapper.*;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public AlbumDto getAlbum(Long albumId) {
        Optional<Album> res = albumRepository.findById(albumId);
        if (res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다.", albumId));
        }
    }

//    public AlbumDto getAlbum(String albumName) {
//        Optional<Album> res = albumRepository.findByAlbumName(albumName);
//        if (res.isPresent()) {
//            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
//            albumDto.setCount(photoRepository.countByAlbum_AlbumId(res.get().getAlbumId()));
//            return albumDto;
//        } else {
//            throw new EntityNotFoundException(String.format("앨범명 %s로 조회되지 않았습니다.", albumName));
//        }
//    }


}
