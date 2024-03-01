package org.example.board.domain.image.repository;

import org.example.board.domain.image.entity.Image;
import org.example.board.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findBySiteUser(SiteUser siteUser);

    void deleteBySiteUserId(Long id);
}
