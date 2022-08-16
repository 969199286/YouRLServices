package com.YouRL.ratelimit;

import com.YouRL.ratelimit.payload.RateVariables;

public interface RateLimiter {
    boolean isRateLimited(String key, RateVariables rateVariables);
}
