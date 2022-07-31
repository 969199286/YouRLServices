package com.YouRL.repository;

import com.YouRL.entity.LongToShort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LongToShortRepository extends JpaRepository<LongToShort,Long> {
    LongToShort getByLongUrl(String longUrl);
    LongToShort getByShortUrl(String shortUrl);
}
