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

class CryptoAdapter(val cryptoList: ArrayList<CryptoModel>?): RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    private val colors: Array<String> = arrayOf(
        "#31d5c8", "#1330bf", "#d7b8ee", "#fd9d65", "#fef65b", "#fa1e4e", "#6dffdf", "#ff9d1c"
    )
   var isLoading = true // Yükleme durumu


    class CryptoViewHolder(val binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CryptoViewHolder(binding)




    }

    override fun getItemCount(): Int {
        //return cryptoList?.count() ?: 8
        //return if (isLoading) 10 else cryptoList?.size ?: 0 // Örneğin, 10 satır boyunca ProgressBar göster
         if(isLoading){
             return 10
         }else{
             return cryptoList!!.size
         }




    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))

        if (isLoading) {
            holder.binding.name.visibility = View.INVISIBLE
            holder.binding.price.visibility = View.INVISIBLE
            holder.binding.progressBar.visibility = View.VISIBLE
            println("adaptera veriler daha gelmedi ve yüklenmedi")
        } else {
            val crypto = cryptoList?.get(position)

            println("adaptera veriler daha yeni geldi ve yüklendi ")

            holder.binding.progressBar.visibility = View.GONE


            holder.binding.name.visibility = View.VISIBLE

            holder.binding.price.visibility = View.VISIBLE

            holder.binding.name.text = crypto?.symbol?.uppercase()
            holder.binding.price.text = crypto?.currentPrice.toString()


            holder.itemView.setOnClickListener {


                val asset_id_base = cryptoList?.get(position)?.id// tıklanan crypto
                val action = CryptoListDirections.actionCryptoListToCryptoDetailsFragment(asset_id_base)// tıklanan crypto
                Navigation.findNavController(it).navigate(action)//
            }


        }
    }


        fun updateCryptoList(newCryptoList: List<CryptoModel>?) {
            cryptoList?.clear()
           // isLoading = loading
            cryptoList?.addAll(newCryptoList ?: emptyList()) // Eğer liste null ise boş liste ekle
            notifyDataSetChanged()



        }
    fun updateCryptoLoading(newLoading: Boolean) {
        isLoading = newLoading
        notifyDataSetChanged()
    }





    }
