package com.tayyipgunay.cryptocrazy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.tayyipgunay.cryptocrazy.R
import com.tayyipgunay.cryptocrazy.adapter.CryptoAdapter
import com.tayyipgunay.cryptocrazy.databinding.FragmentCryptoListBinding
import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.viewModel.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CryptoList : Fragment() {

    // ViewBinding için nullable bir değişken tanımlıyoruz.
    private var _binding: FragmentCryptoListBinding? = null

    // Binding değişkenine erişim sağlamak için bir getter tanımlıyoruz.
    // Bu, null kontrolü yaparak güvenli bir şekilde binding'e erişmemizi sağlar.
    private val binding get() = _binding!!

    // ViewModel'i lazy bir şekilde tanımlıyoruz.
    // Bu, Fragment'ın yaşam döngüsüne uygun bir şekilde ViewModel'in oluşturulmasını sağlar.
    private val viewModel: CryptoViewModel by viewModels()

    // RecyclerView için bir adapter tanımlıyoruz.
    // Başlangıçta boş bir liste ile başlatıyoruz.
    private val cryptoAdapter = CryptoAdapter(arrayListOf())

    // Yükleme durumunu takip etmek için bir değişken tanımlıyoruz.
    var Loading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding kullanarak layout'u inflate ediyoruz.
        // Bu, findViewById kullanmadan doğrudan view'lara erişim sağlar.
        _binding = FragmentCryptoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView'ın layout manager'ını ayarlıyoruz.
        // LinearLayoutManager, öğeleri dikey bir liste halinde sıralar.
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView'ın adapter'ını ayarlıyoruz.
        // Bu, verilerin RecyclerView'da gösterilmesini sağlar.
        binding.recyclerView2.adapter = cryptoAdapter

        // ViewModel üzerinden kripto verilerini yüklüyoruz.
        viewModel.LoadCrypto()

        // SwipeRefreshLayout için bir dinleyici ekliyoruz.
        // Kullanıcı sayfayı aşağı çektiğinde verileri yenilemek için kullanılır.
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.LoadCrypto() // Verileri yeniden yüklüyoruz.
            binding.swipeRefreshLayout.isRefreshing = false // Yenileme animasyonunu durduruyoruz.
        }

        // SearchView için bir dinleyici ekliyoruz.
        // Kullanıcı arama sorgusu girdiğinde veya değiştirdiğinde tetiklenir.
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Arama sorgusu gönderildiğinde klavyeyi kapatmak için odağı kaldırıyoruz.
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Arama kutusu boşsa, orijinal kripto listesini geri yüklüyoruz.
                    viewModel.cryptoList.value = viewModel.initialCrypto.value
                } else {
                    // Arama kutusu doluysa, arama işlemi için `searchCrypto` fonksiyonunu çağırıyoruz.
                    viewModel.searchCrypto(newText)
                }
                return true
            }
        })

        // LiveData'yı gözlemlemek için observe fonksiyonunu çağırıyoruz.
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bellek sızıntılarını önlemek için binding'i null yapıyoruz.
        // Bu, Fragment'ın view'ı destroy edildiğinde binding'in hala referansını tutmamasını sağlar.
        _binding = null
    }

    fun observe() {


        /*     viewModel.loading.observe(viewLifecycleOwner) { loading ->
            loading?.let { loading ->
                if (loading) {
                    println("true loading observe yani veriler yükleniyor ")
                } else {
                    viewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
                       cryptoList?.let { cryptoList ->
                           cryptoAdapter.updateCryptoList(cryptoList, loading)
                            println("false  loading observe yani veriler yüklendi")

                       }
                    }
                    //    cryptoAdapter.updateCryptoList(viewModel.cryptoList.value, loading)

                    }


                }
            }

    */
        // ViewModel'den gelen `loading` LiveData'sını gözlemliyoruz.
// Bu, verilerin yüklenip yüklenmediğini takip etmek için kullanılır.
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            // Eğer `loading` değeri null değilse, adapter'ı güncelliyoruz.
            loading?.let {
                // Adapter'a yükleme durumunu iletiyoruz.
                // Bu, örneğin bir yükleme animasyonu göstermek için kullanılabilir.
                cryptoAdapter.updateCryptoLoading(loading)
            }
        }

// ViewModel'den gelen `cryptoList` LiveData'sını gözlemliyoruz.
// Bu, kripto para birimlerinin listesini takip etmek için kullanılır.
        viewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            // Eğer `cryptoList` null değilse, adapter'ı güncelliyoruz.
            cryptoList?.let {
                // Adapter'a yeni kripto listesini iletiyoruz.
                // Bu, RecyclerView'ın güncellenmesini sağlar.
                cryptoAdapter.updateCryptoList(cryptoList)
            }
        }
    }
}




