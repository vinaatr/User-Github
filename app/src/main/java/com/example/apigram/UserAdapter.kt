package com.example.apigram

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apigram.databinding.ItemRowBinding
import coil.transform.CircleCropTransformation
import coil.load

class UserAdapter(private val data: MutableList<NoteUserGithub.Item> = mutableListOf(),
                  private val listener: (NoteUserGithub.Item) -> Unit) :

    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<NoteUserGithub.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

    inner class UserViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteUserGithub.Item) {
            binding.imageUser.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }
            binding.username.text = item.login
        }
    }
}





