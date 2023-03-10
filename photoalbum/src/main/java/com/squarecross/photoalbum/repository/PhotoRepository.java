package com.squarecross.photoalbum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.squarecross.photoalbum.domain.*;

import java.util.List;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);

    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId);

}
