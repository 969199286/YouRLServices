package com.YouRL.repository;

import com.YouRL.Entity.LongToShort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LongToShortRepository extends JpaRepository<LongToShort,Long> {
    LongToShort getByLongUrl(String longUrl);
}
