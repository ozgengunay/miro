package com.ozgen.miro.controllers.ratelimiting;

/**
 * A rate limiter. Conceptually, a rate limiter distributes permits at a configurable rate.
 *
 */
public interface RateLimiter {
    /**
     * Method to gracefully shut down the rate limiter.
     */
    void shutdown();

    /**
     * 
     * Acquires a single permit from the {@link RateLimiter}, doesn't block and throws
     * {@link RateLimitExceededException} if request can not be granted.
     *
     * @return RateLimit
     * @throws RateLimitExceededException If rate limit is exceeded
     */
    RateLimit acquire() throws RateLimitExceededException;
}
