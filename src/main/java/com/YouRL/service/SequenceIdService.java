package com.YouRL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class SequenceIdService {
    private static final String SEQUENCE_ID = "Sequence_ID";
    private static final String GLOBAL_SEQUENCE_ID = "Global_Sequence_ID";



    private RedisAtomicLong entityIdCounter;
    private final RedisTemplate<String, Long> sequenceIDRedisTemplate;
    @Autowired
    public SequenceIdService(@Qualifier("sequenceIdTemplate") RedisTemplate<String, Long> sequenceIDRedisTemplate) {
        this.sequenceIDRedisTemplate = sequenceIDRedisTemplate;
    }

    @PostConstruct
    public void setUp() {
        entityIdCounter = new RedisAtomicLong(SEQUENCE_ID, sequenceIDRedisTemplate.getConnectionFactory());
    }
    public long getNextSequenceIdByAtomic() {
        long increment = entityIdCounter.getAndIncrement();
        sequenceIDRedisTemplate.getConnectionFactory().getConnection().bgSave();
        return increment;
    }
}

