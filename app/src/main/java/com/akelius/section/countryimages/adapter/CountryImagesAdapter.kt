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
import com.akelius.service.model.countryimagesmodel.FileCheck
import com.akelius.service.model.countryimagesmodel.ImageDataClass
import com.akelius.service.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.imageitem.view.*
import java.nio.file.Files

import java.util.*
import kotlin.collections.HashMap

class CountryImagesAdapter(private val context: Context) :
    RecyclerView.Adapter<CountryImagesAdapter.TodayViewHolder>() {

    // data
    var countryimglist = TreeMap<Int, FileCheck>()

    var count=0

    var b=true
    inner class TodayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val atime: TextView = itemView.text
        val checktext: TextView = itemView.checktext
        val img: ImageView = itemView.images
        fun bind(position: Int) {

            atime.text = countryimglist.get(position)?.file?.stats?.atime
                checktext.text=countryimglist.get(position)?.status
            Glide.with(context)
                .load(countryimglist.get(position)?.file?.path)
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
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return countryimglist.size
    }


    // reloadData()
    fun update(users: TreeMap<Int, FileCheck>) {
        this.countryimglist.clear()
        countryimglist = users
        notifyDataSetChanged()
    }
}