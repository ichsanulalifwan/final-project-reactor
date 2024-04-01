package com.app.ichsanulalifwan.barani.benchmark.reactor.database

import android.annotation.SuppressLint
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.ichsanulalifwan.barani.benchmark.api.DatabaseBenchmark
import com.app.ichsanulalifwan.barani.benchmark.mock.getMockNewsEntity
import com.app.ichsanulalifwan.barani.benchmark.reactor.ReactorBenchmark
import org.junit.Test
import org.junit.runner.RunWith

@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
class ReactorDatabaseBenchmark : ReactorBenchmark(), DatabaseBenchmark {

    @Test
    override fun queryOneNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 1)
        }
        repository.news.blockFirst()
    }

    @Test
    override fun queryFiveNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 5)
        }
        repository.news.blockFirst()
    }

    @Test
    override fun queryTenNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 10)
        }
        repository.news.blockFirst()
    }

    @Test
    override fun queryTwentyFiveNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 25)
        }
        repository.news.blockFirst()
    }

    @Test
    override fun queryFiftyNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 50)
        }
        repository.news.blockFirst()
    }

    @Test
    override fun queryOneHundredNews() = benchmarkRule.measureRepeated {
        runWithTimingDisabled {
            clearAndInsertMovies(size = 100)
        }
        repository.news.blockFirst()
    }

    private fun clearAndInsertMovies(size: Int) {
        val listToInsert = List(size) {
            getMockNewsEntity(it)
        }
        repository.insertNewsToDatabase(listToInsert).block()
    }
}