import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hurtpolandroid.ui.model.Product
import com.example.hurtpolandroid.ui.model.ProductRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

public class ProductDetailDBViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int
) : ViewModel() {
    val product: LiveData<Product>

    init {
        product = productRepository.getProduct(productId)
    }

    fun addProduct() {
        productRepository.addProduct(product.value!!)
    }

}