package com.app.ichsanulalifwan.barani.benchmark.api

import org.junit.Test

interface NetworkBenchmark {
    @Test
    fun fetchNewsSingleRequest()

    @Test
    fun fetchNewsFiveSequentialRequests()

    @Test
    fun fetchTenNews()

    @Test
    fun fetchTwentyFiveNews()

    @Test
    fun fetchFiftyNews()

    @Test
    fun fetchOneHundredNews()
}