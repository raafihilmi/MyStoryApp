package com.bumantra.mystoryapp.ui.story.list

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumantra.mystoryapp.data.local.entity.StoryEntity
import com.bumantra.mystoryapp.databinding.ItemStoryBinding
import com.bumantra.mystoryapp.ui.story.detail.DetailStoryActivity
import com.bumptech.glide.Glide

class  StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryEntity) {
            binding.apply {
                ivItemPhoto.loadImage(data.photoUrl)
                tvItemName.text = data.name
                tvItemDesc.text = data.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "image"),
                    Pair(holder.binding.tvItemName, "name"),
                    Pair(holder.binding.tvItemDesc, "description"),
                )
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra("id", story?.id)
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    companion object {
        private fun ImageView.loadImage(url: String?) {
            Glide.with(this)
                .load(url)
                .into(this)
        }

        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(
                    oldItem: StoryEntity,
                    newItem: StoryEntity
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: StoryEntity,
                    newItem: StoryEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }


}

