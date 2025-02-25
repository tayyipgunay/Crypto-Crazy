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
    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!

    /* @Inject
     lateinit var viewModel: CryptoViewModel*/

    private val viewModel: CryptoViewModel by viewModels()

    // by viewModels() ifadesi, Hilt veya Android'in ViewModel sistemi aracılığıyla
    // CryptoViewModel örneğini otomatik olarak sağlar.
    // - by viewModels() sayesinde, ViewModel yalnızca ihtiyaç duyulduğunda
    // - Android'in yaşam döngüsü ile uyumlu çalıştığından, ViewModel Fragment'ın yeniden
    //   oluşturulması durumunda (örneğin, ekran döndürme) veri kaybını önler.
    // Bu sayede, ViewModel nesnesi her yeniden yüklemede baştan başlatılmak yerine aynı
    // veri ile çalışmaya devam eder.
    private val cryptoAdapter = CryptoAdapter(arrayListOf())
    var Loading = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewBinding kullanarak layout'u inflate ediyoruz
        _binding = FragmentCryptoListBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // viewModel = ViewModelProvider(requireActivity()).get(CryptoViewModel::class.java)
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(requireContext())// RecyclerView'ın layout manager'ını ayarlıyoruz
        binding.recyclerView2.adapter = cryptoAdapter// RecyclerView'ın adapter'ını ayarlıyoruz//
        viewModel.LoadCrypto()
        binding.swipeRefreshLayout.setOnRefreshListener {

            viewModel.LoadCrypto()
            binding.swipeRefreshLayout.isRefreshing = false


        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Arama sorgusu gönderildiğinde klavyeyi kapatmak için odağı kaldırıyoruz
                binding.searchView.clearFocus()
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {

                    viewModel.cryptoList.value = viewModel.initialCrypto.value


                } else {
                    // Arama kutusu doluysa, arama işlemi için `searchCrypto` fonksiyonunu çağırıyoruz
                    viewModel.searchCrypto(newText)
                }
                return true
            }
        })






        observe()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Bellek sızıntılarını önlemek için binding'i null yapıyoruz
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
        viewModel.loading.observe(viewLifecycleOwner){
            loading->
            loading?.let {
                cryptoAdapter.updateCryptoLoading(loading)
            }

        }
        viewModel.cryptoList.observe(viewLifecycleOwner){
            cryptoList->
             cryptoList?.let {
                  cryptoAdapter.updateCryptoList(cryptoList)
             }

        }

        }
    }





