package com.YouRL.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "long_to_sequence_id")
@Entity
@Data
@NoArgsConstructor
public class LongToSequenceId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "sequence_id")
    private long sequenceId;
    @Column(name = "long_url")
    private String longUrl;

    public LongToSequenceId(long sequenceId, String longUrl) {
        this.sequenceId = sequenceId;
        this.longUrl = longUrl;
    }
}
