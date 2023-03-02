package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.squarecross.photoalbum.dto.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    AlbumService albumService;

//    @Test
//    void getAlbum() {
//        Album album = new Album();
//        album.setAlbumName("테스트");
//        Album saveAlbum = albumRepository.save(album);
//
////        AlbumDto resAlbum = albumService.getAlbum(saveAlbum.getAlbumName());
//        assertEquals("테스트", resAlbum.getAlbumName());
//    }

    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album saveAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(saveAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(saveAlbum);
        photoRepository.save(photo2);

        AlbumDto resAlbum = albumService.getAlbum(saveAlbum.getAlbumId());

        assertEquals(2, resAlbum.getCount());
    }

    @Test
    void testCreateAlbum() throws IOException {
        AlbumDto albumDto1 = new AlbumDto();
        albumDto1.setAlbumName("새 앨범 1");
        AlbumDto savedAlbumDto1 = albumService.createAlbum(albumDto1);

        File originalFile = new File(String.format(Constant.PATH_PREFIX + "/photos/original/" + savedAlbumDto1.getAlbumId()));
        File thumbFile = new File(String.format(Constant.PATH_PREFIX + "/photos/thumb/" + savedAlbumDto1.getAlbumId()));

        assertTrue(originalFile.exists()&&thumbFile.exists());

        albumService.deleteAlbum(savedAlbumDto1.getAlbumId());

        assertTrue(!(originalFile.exists()&&thumbFile.exists()));
    }

    @Test
    void testAlbumSort() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaab");
        album2.setAlbumName("aaaa");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertEquals("aaaa", resDate.get(0).getAlbumName());
        assertEquals("aaab", resDate.get(1).getAlbumName());

        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertEquals("aaaa", resDate.get(0).getAlbumName());
        assertEquals("aaab", resDate.get(1).getAlbumName());

    }

    @Test
    void testChangeName() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경전");
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);

        AlbumDto updateAlbumDto = new AlbumDto();
        updateAlbumDto.setAlbumName("변경후");
        Long albumId = savedAlbumDto.getAlbumId();
        albumService.changeName(albumId, updateAlbumDto);

        AlbumDto updatedAlbumDto = albumService.getAlbum(albumId);

        assertEquals("변경후", updatedAlbumDto.getAlbumName());

    }

}