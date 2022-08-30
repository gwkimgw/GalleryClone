package com.example.endterm

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val context: Context, private val dataList: ArrayList<ImageElement>)
    : RecyclerView.Adapter<MyAdapter.ItemViewHolder>() {

    fun addItem(imageElement: ImageElement) {
        dataList.add(imageElement)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val userPhoto = itemView.findViewById<ImageView>(R.id.userImg)

        fun bind(imageElement: ImageElement, context: Context) {
            if(imageElement.uri != "") {
                userPhoto.setImageURI(imageElement.uri?.toUri())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EnlargeActivity::class.java)
            intent.putExtra("image_uri", dataList[position].uri)
            context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener{ view ->
            dataList.removeAt(position)
            this.notifyDataSetChanged()
            Toast.makeText(view.context, "Photo removed", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}