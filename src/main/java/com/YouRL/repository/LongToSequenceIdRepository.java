package com.YouRL.repository;

import com.YouRL.entity.LongToSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LongToSequenceIdRepository extends JpaRepository<LongToSequenceId, Long> {
    LongToSequenceId findTopByOrderByIdDesc();
}
