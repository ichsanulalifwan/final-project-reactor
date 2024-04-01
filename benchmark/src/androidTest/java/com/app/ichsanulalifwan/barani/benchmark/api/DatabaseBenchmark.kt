package com.app.ichsanulalifwan.barani.benchmark.api

import org.junit.Test

interface DatabaseBenchmark {
    @Test
    fun queryOneNews()

    @Test
    fun queryFiveNews()

    @Test
    fun queryTenNews()

    @Test
    fun queryTwentyFiveNews()

    @Test
    fun queryFiftyNews()

    @Test
    fun queryOneHundredNews()
}