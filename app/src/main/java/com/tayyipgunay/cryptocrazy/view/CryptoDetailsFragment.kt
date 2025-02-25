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
    private var _binding: FragmentCryptoDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewBinding kullanarak layout'u inflate ediyoruz
        _binding = FragmentCryptoDetailsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val asset_id_base = CryptoDetailsFragmentArgs.fromBundle(it).assetname
            println("detail fragmnetten gelen asset_id_base: " + asset_id_base)





            if (asset_id_base != null) {
                viewModel.loadCrypto(asset_id_base)//asset_id_base
            }


            observe()


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bellek sızıntılarını önlemek için binding'i null yapıyoruz
        _binding = null


    }

    fun observe() {

        viewModel.cryptoLiveData.observe(viewLifecycleOwner) { crypto ->
            crypto?.let { crypto ->
                binding.assetIdBase.text = crypto[0].symbol.uppercase()

                binding.rate.text = crypto[0].currentPrice.toString()
                Glide.with(this)
                    .load(crypto[0].image)
                    .fitCenter()
                    .into(binding.imageView)


            }
        }
    }
}

