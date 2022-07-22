package com.YouRL.repository;

import com.YouRL.entity.ShortToLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortToLongRepository extends JpaRepository<ShortToLong,Long> {
    ShortToLong getByShortUrl(String shortUrl);
}
