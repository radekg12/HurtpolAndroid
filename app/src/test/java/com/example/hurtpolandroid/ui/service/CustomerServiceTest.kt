import com.example.hurtpolandroid.ui.model.CustomerDTO
import com.example.hurtpolandroid.ui.service.CustomerService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class CustomerServiceTest {

    private lateinit var service: CustomerService

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory( RxJava2CallAdapterFactory.create())
            .build()
            .create(CustomerService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getCustomer() {
        mockWebServer.enqueue(MockResponse().setBody("{\"email\": \"email@email.com\", " +
                "\"lastName\" : \"kowalski\", \"firstName\": \"jan\"}"))
        assertThat(CustomerDTO(email="email@email.com", lastName = "kowalski", firstName = "jan"),
            equalTo(service.getUser().execute().body()))
    }
}