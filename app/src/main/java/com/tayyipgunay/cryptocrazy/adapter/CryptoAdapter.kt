package com.tayyipgunay.cryptocrazy.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tayyipgunay.cryptocrazy.databinding.ListRowBinding
import com.tayyipgunay.cryptocrazy.model.CryptoModel
import com.tayyipgunay.cryptocrazy.view.CryptoListDirections
import kotlinx.coroutines.delay

class CryptoAdapter(val cryptoList: ArrayList<CryptoModel>?) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    // Renkler için önceden tanımlanmış bir dizi (Her satır için farklı renkler)
    private val colors: Array<String> = arrayOf(
        "#31d5c8", "#1330bf", "#d7b8ee", "#fd9d65", "#fef65b", "#fa1e4e", "#6dffdf", "#ff9d1c"
    )

    var isLoading = true // Yükleme durumu, veriler henüz yükleniyorsa true olur

    // ViewHolder sınıfı, RecyclerView öğeleri için görünüm referanslarını tutar
    class CryptoViewHolder(val binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {}

    // onCreateViewHolder: Yeni bir ViewHolder oluşturulurken çağrılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoViewHolder(binding)
    }

    // getItemCount: Verilen liste boyutunu döndürür
    override fun getItemCount(): Int {
        // Eğer yükleniyorsa, geçici olarak 10 öğe döndürür (bu sırada progress bar gösterilir)
        if (isLoading) {
            return 10
        } else {
            return cryptoList!!.size // Gerçek veriyi döndürür
        }
    }

    // onBindViewHolder: Her öğe için veriyi bağlamak için çağrılır
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        // Her öğeye farklı bir arka plan rengi uygular
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))

        // Eğer veriler yükleniyorsa, ProgressBar'ı gösterir
        if (isLoading) {
            holder.binding.name.visibility = View.INVISIBLE
            holder.binding.price.visibility = View.INVISIBLE
            holder.binding.progressBar.visibility = View.VISIBLE
            println("adaptera veriler daha gelmedi ve yüklenmedi")
        } else {
            // Veriler geldiğinde, öğe görünür hale gelir
            val crypto = cryptoList?.get(position)

            println("adaptera veriler daha yeni geldi ve yüklendi")

            holder.binding.progressBar.visibility = View.GONE
            holder.binding.name.visibility = View.VISIBLE
            holder.binding.price.visibility = View.VISIBLE

            // Kripto para sembolü ve fiyatını bağlar
            holder.binding.name.text = crypto?.symbol?.uppercase()
            holder.binding.price.text = crypto?.currentPrice.toString()

            // Öğeye tıklandığında, CryptoDetailsFragment'e yönlendirir
            holder.itemView.setOnClickListener {
                val asset_id_base = cryptoList?.get(position)?.id // Tıklanan kripto para ID'si
                val action = CryptoListDirections.actionCryptoListToCryptoDetailsFragment(asset_id_base) // Yönlendirme işlemi
                Navigation.findNavController(it).navigate(action) // Yönlendir
            }
        }
    }

    // Yeni bir kripto listesi alır ve RecyclerView'ı günceller
    fun updateCryptoList(newCryptoList: List<CryptoModel>?) {
        cryptoList?.clear() // Önceki listeyi temizler
        cryptoList?.addAll(newCryptoList ?: emptyList()) // Yeni listeyi ekler
        notifyDataSetChanged() // RecyclerView'a değişiklik bildirir
    }

    // Yükleme durumu güncelleme fonksiyonu
    fun updateCryptoLoading(newLoading: Boolean) {
        isLoading = newLoading
        notifyDataSetChanged() // Yükleme durumu değiştiğinde RecyclerView'ı yeniden günceller
    }
}
