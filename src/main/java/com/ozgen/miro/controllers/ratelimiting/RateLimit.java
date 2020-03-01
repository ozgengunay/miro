package com.ozgen.miro.controllers.ratelimiting;

/**
 * RateLimit POJO for {@link RateLimiter}
 *
 */
public class RateLimit {
    private final Integer maxRequestsInPeriod;
    private final Integer periodInSeconds;
    private final String limiterId;
    private final Integer remainingRequests;
    private final Long nextResetInMilliSeconds;

    private RateLimit() {
        this.maxRequestsInPeriod = null;
        this.periodInSeconds = null;
        this.limiterId = null;
        this.remainingRequests = null;
        this.nextResetInMilliSeconds = null;
    }

    private RateLimit(Builder builder) {
        this.maxRequestsInPeriod = builder.maxRequestsInPeriod;
        this.periodInSeconds = builder.periodInSeconds;
        this.limiterId = builder.limiterId;
        this.remainingRequests = builder.remainingRequests;
        this.nextResetInMilliSeconds = builder.nextResetInMilliSeconds;
    }

    /**
     * @return maxRequestsInPeriod Maximum number of requests in a given period
     */
    public Integer getMaxRequestsInPeriod() {
        return maxRequestsInPeriod;
    }

    /**
     * @return periodInSeconds Period in seconds in which number of request will be limited
     */
    public Integer getPeriodInSeconds() {
        return periodInSeconds;
    }

    /**
     * @return limiterId Unique id of the rate limiter
     */
    public String getLimiterId() {
        return limiterId;
    }

    /**
     * @return remainingRequests Remaining permittable requests in the period
     */
    public Integer getRemainingRequests() {
        return remainingRequests;
    }

    /**
     * @return nextResetInMilliSeconds Milliseconds left for a new period for limits
     */
    public Long getNextResetInMilliSeconds() {
        return nextResetInMilliSeconds;
    }

    /**
     * @return Converts this instance to a builder instance
     */
    public Builder toBuilder() {
        return new RateLimit.Builder(this);
    }

    public static class Builder {
        private Integer maxRequestsInPeriod;
        private Integer periodInSeconds;
        private String limiterId;
        private Integer remainingRequests;
        private Long nextResetInMilliSeconds;

        /**
         * Constructs Builder with given {@link RateLimit} object
         * 
         * @param rateLimit {@link RateLimit} object to copy its parameters
         */
        public Builder(RateLimit rateLimit) {
            this.maxRequestsInPeriod = rateLimit.maxRequestsInPeriod;
            this.periodInSeconds = rateLimit.periodInSeconds;
            this.limiterId = rateLimit.limiterId;
            this.remainingRequests = rateLimit.remainingRequests;
            this.nextResetInMilliSeconds = rateLimit.nextResetInMilliSeconds;
        }

        /**
         * Default Constructor of {@link Builder} class
         */
        public Builder() {
        }

        /**
         * 
         * Sets maxRequestsInPeriod
         * 
         * @param maxRequestsInPeriod Maximum number of requests in a given period
         * @return {@link Builder}
         */
        public Builder withMaxRequestsInPeriod(Integer maxRequestsInPeriod) {
            this.maxRequestsInPeriod = maxRequestsInPeriod;
            return this;
        }

        /**
         * 
         * Sets periodInSeconds
         * 
         * @param periodInSeconds period in seconds
         * @return {@link Builder}
         */
        public Builder withPeriodInSeconds(Integer periodInSeconds) {
            this.periodInSeconds = periodInSeconds;
            return this;
        }

        /**
         * 
         * Sets limiterId
         * 
         * @param limiterId Unique id of the rate limiter
         * @return {@link Builder}
         */
        public Builder withLimiterId(String limiterId) {
            this.limiterId = limiterId;
            return this;
        }

        /**
         * 
         * Sets remainingRequests
         * 
         * @param remainingRequests Remaining permittable requests in the period
         * @return {@link Builder}
         */
        public Builder withRemainingRequests(Integer remainingRequests) {
            this.remainingRequests = remainingRequests;
            return this;
        }

        /**
         * 
         * Sets nextResetInMilliSeconds
         * 
         * @param nextResetInMilliSeconds Milliseconds left for a new period for limits
         * @return {@link Builder}
         */
        public Builder withNextResetInMilliSeconds(Long nextResetInMilliSeconds) {
            this.nextResetInMilliSeconds = nextResetInMilliSeconds;
            return this;
        }

        /**
         * Builds new instance of <code>RateLimit</code> object
         * 
         * @return new instance of {@link RateLimit} object
         */
        public RateLimit build() {
            return new RateLimit(this);
        }
    }
}
