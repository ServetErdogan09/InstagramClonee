package com.example.kotlininstegram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlininstegram.databinding.RecyclerRowBinding
import com.example.kotlininstegram.model.Post
import com.google.firebase.firestore.persistentCacheSettings
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(val feedList : ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.FeedHolder>(){

    class FeedHolder(val binding : RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
       val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FeedHolder(binding)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {

        holder.binding.recyclerEmailText.text = feedList[position].email
        holder.binding.recyclerCommentText.text = feedList[position].comment
        Picasso.get().load(feedList[position].downloadURL).into(holder.binding.recyclerimage)

    }
}