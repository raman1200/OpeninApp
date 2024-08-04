package com.raman.project.openinapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.raman.project.openinapp.databinding.ItemViewBinding
import com.raman.project.openinapp.models.Link

class LinksAdapter(private val context: Context): RecyclerView.Adapter<LinksAdapter.LinkViewHolder>() {

    private val links: ArrayList<Link> = ArrayList()

    inner class LinkViewHolder(itemView: View):ViewHolder(itemView){
        val binding = ItemViewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        return LinkViewHolder(ItemViewBinding.inflate(LayoutInflater.from(context),parent,false).root)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val data = links[position]
        holder.binding.linkName.text = data.title
        holder.binding.date.text = data.created_at
        holder.binding.click.text = data.total_clicks.toString()
        holder.binding.url.text = data.smart_link
        Glide.with(context).load(data.original_image).into(holder.binding.img)
    }
    fun updateList(list:List<Link>){
        links.clear()
        links.addAll(list)
    }


}