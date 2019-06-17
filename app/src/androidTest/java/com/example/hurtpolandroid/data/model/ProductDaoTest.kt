package com.example.hurtpolandroid.data.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.example.hurtpolandroid.data.AppDatabase
import com.example.hurtpolandroid.data.ProductDao
import com.example.hurtpolandroid.data.model.LiveDataTestUtil.getValue
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataTestUtil {
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {
    private lateinit var productDao: ProductDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        productDao = db.productDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun whenGetById_thenReturnProduct() {
        val product = Product(
            id = 0,
            name = "cheese",
            description = "super cheese",
            company = "cheese company sp z.o.o",
            quantityInStock = 5,
            unitPrice = 100,
            imageUrl = "http://www.chesse.img",
            specificationPositions = emptyList()
        )
        productDao.insert(product)
        val loaded = getValue(productDao.getByID(0))
        assertThat(loaded, equalTo(product))
    }
}
