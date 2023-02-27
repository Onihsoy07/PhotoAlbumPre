package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable(value = "albumId") final Long albumId) {
        AlbumDto albumDto = albumService.getAlbum(albumId);
        return new ResponseEntity<>(albumDto, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<AlbumDto> getAlbumByQuery(@RequestParam final Long albumId) {
        AlbumDto albumDto = albumService.getAlbum(albumId);
        return new ResponseEntity<>(albumDto, HttpStatus.OK);
    }

    @PostMapping("/json_body")
    public ResponseEntity<AlbumDto> getAlbumByJson(@RequestBody final AlbumDto albumDto) {
        AlbumDto savedAlbumDto = albumService.getAlbum(albumDto.getAlbumId());
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }

//    @PostMapping("")
//    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
//        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
//        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
//    }

    @PostMapping("")
    public ResponseEntity<List<AlbumDto>>
    getAlbumList(@RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
                 @RequestParam(value = "sort", required = false, defaultValue = "byDate") final String sort,
                 @RequestParam(value = "orderBy", required = false, defaultValue = "asc") final String orderBy) {
        List<AlbumDto> albumDtos = albumService.getAlbumList(keyword, sort, orderBy);
        return new ResponseEntity<>(albumDtos, HttpStatus.OK);
    }

}
