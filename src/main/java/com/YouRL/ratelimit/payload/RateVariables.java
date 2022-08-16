package com.YouRL.ratelimit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RateVariables {

    private String key;

    @EqualsAndHashCode.Exclude
    private double permitsPerSecond;

    @EqualsAndHashCode.Exclude
    private int permits;

    @EqualsAndHashCode.Exclude
    private int period;
}