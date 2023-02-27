package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.mapper.*;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    private final PhotoRepository photoRepository;

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

    public AlbumDto getAlbum(String albumName) {
        Optional<Album> res = albumRepository.findByAlbumName(albumName);
        if (res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(res.get().getAlbumId()));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범명 %s로 조회되지 않았습니다.", albumName));
        }
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.albumRepository.save(album);
        this.createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(Constant.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constant.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    //추후 앨범 데이터 삭제 추가
    public AlbumDto deletAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.deleteAlbumDirectories(album);
        return null;
    }

    private void deleteAlbumDirectories(Album album) throws IOException {
        File originalFile = new File(String.format(Constant.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        File thumbFile = new File(String.format(Constant.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
        if(originalFile.exists()&&thumbFile.exists()) {
            originalFile.delete();
            thumbFile.delete();
        } else {
            throw new IOException(String.format("%d 앨범이 존재하지 않습니다.", album.getAlbumId()));
        }
    }

    public List<AlbumDto> getAlbumList(String keyword, String sort, String orderBy) {
        List<Album> albums;
        if("byDate".equals(sort)) {
            if("asc".equals(orderBy))   { albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtAsc(keyword); }
            else if("desc".equals(orderBy)) { albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword); }
            else { throw new IllegalArgumentException("알 수 없는 정렬 기준입니다."); }
        } else if("byName".equals(sort)) {
                if("asc".equals(orderBy))   { albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtAsc(keyword); }
                else if("desc".equals(orderBy)) { albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword); }
                else { throw new IllegalArgumentException("알 수 없는 정렬 기준입니다."); }
        } else { throw new IllegalArgumentException("알 수 없는 정렬 기준입니다."); }
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);

        for(AlbumDto albumDto : albumDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream().map(Photo::getThumbUrl).map(c -> Constant.PATH_PREFIX + c).collect(Collectors.toList()));
        }

        return albumDtos;
    }


}
