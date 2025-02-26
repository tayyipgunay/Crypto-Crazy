package com.tayyipgunay.cryptocrazy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tayyipgunay.cryptocrazy.R
import com.tayyipgunay.cryptocrazy.adapter.CryptoAdapter
import com.tayyipgunay.cryptocrazy.databinding.FragmentCryptoDetailsBinding
import com.tayyipgunay.cryptocrazy.databinding.FragmentCryptoListBinding
import com.tayyipgunay.cryptocrazy.viewModel.CryptoViewModel
import com.tayyipgunay.cryptocrazy.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CryptoDetailsFragment : Fragment() {

    // ViewBinding için nullable bir değişken tanımlıyoruz.
    private var _binding: FragmentCryptoDetailsBinding? = null

    // Binding değişkenine erişim sağlamak için bir getter tanımlıyoruz.
    // Bu, null kontrolü yaparak güvenli bir şekilde binding'e erişmemizi sağlar.
    private val binding get() = _binding!!

    // ViewModel'i lazy bir şekilde tanımlıyoruz.
    // Bu, Fragment'ın yaşam döngüsüne uygun bir şekilde ViewModel'in oluşturulmasını sağlar.
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding kullanarak layout'u inflate ediyoruz.
        // Bu, findViewById kullanmadan doğrudan view'lara erişim sağlar.
        _binding = FragmentCryptoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment'a iletilen argümanları alıyoruz.
        arguments?.let {
            // Safe Args plugin'i kullanarak argümanları güvenli bir şekilde alıyoruz.
            val asset_id_base = CryptoDetailsFragmentArgs.fromBundle(it).assetname
            println("Detail Fragment'ten gelen asset_id_base: " + asset_id_base)

            // Eğer asset_id_base null değilse, ViewModel üzerinden kripto verilerini yüklüyoruz.
            if (asset_id_base != null) {
                viewModel.loadCrypto(asset_id_base)
            }

            // LiveData'yı gözlemlemek için observe fonksiyonunu çağırıyoruz.
            observe()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bellek sızıntılarını önlemek için binding'i null yapıyoruz.
        // Bu, Fragment'ın view'ı destroy edildiğinde binding'in hala referansını tutmamasını sağlar.
        _binding = null
    }

    // ViewModel'den gelen LiveData'yı gözlemlemek için bir fonksiyon tanımlıyoruz.
    fun observe() {
        viewModel.cryptoLiveData.observe(viewLifecycleOwner) { crypto ->
            // Eğer crypto null değilse, UI'ı güncelliyoruz.
            crypto?.let { crypto ->
                // Kripto para biriminin sembolünü büyük harflerle gösteriyoruz.
                binding.assetIdBase.text = crypto[0].symbol.uppercase()

                // Kripto para biriminin mevcut fiyatını gösteriyoruz.
                binding.rate.text = crypto[0].currentPrice.toString()

                // Glide kütüphanesi kullanarak kripto para biriminin resmini yüklüyoruz.
                Glide.with(this)
                    .load(crypto[0].image)
                    .fitCenter()
                    .into(binding.imageView)
            }
        }
    }
}

