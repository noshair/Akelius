package com.akelius.section.countryimages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akelius.R
import com.akelius.service.model.countryimagesmodel.File
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.imageitem.view.*
import java.nio.file.Files

import java.util.*

class CountryImagesAdapter(private val context: Context) :
    RecyclerView.Adapter<CountryImagesAdapter.TodayViewHolder>() {

    // data
    var countryimglist = mutableListOf<File>()



    inner class TodayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaztiming: TextView = itemView.fajar
        val img: ImageView = itemView.text_view_id

        fun bind(position: Int) {
            namaztiming.text = countryimglist.get(position)?.stats?.atime
            Glide
                .with(context)
                .load(countryimglist.get(position).path)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(img);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.imageitem, parent, false)
        return TodayViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        val item = countryimglist[position]
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return countryimglist.size
    }


    // reloadData()
    fun update(users: List<File>?) {
        this.countryimglist.clear()
        countryimglist = users as MutableList<File>
        notifyDataSetChanged()
    }
}