package com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome.adapterofmystoryapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp
import com.dicoding.picodiploma.storyappsubmission.databinding.MyStoryAppItemListBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.detailofuserstory.DetailOfMyPastStory

class HomeSweetHomeOfAdapter :
    PagingDataAdapter<MyStoryApp, HomeSweetHomeOfAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyStoryAppItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStory = getItem(position)
        if (dataStory != null) {
            holder.bind(dataStory)
        }
    }

    inner class ViewHolder(private var binding: MyStoryAppItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: MyStoryApp) {
            with(binding) {
                storyPhotoOfUser.load(story.photoUrl)
                nameOfUser.text = story.name
                storyDescription.text = story.description


                storyPhotoOfUser.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(storyPhotoOfUser, "image"),
                            Pair(nameOfUser, "name"),
                            Pair(storyDescription, "description")
                        )

                    val intent = Intent(it.context, DetailOfMyPastStory::class.java)
                    intent.putExtra(DetailOfMyPastStory.STORY, story)
                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyStoryApp>() {
            override fun areItemsTheSame(
                oldItem: MyStoryApp,
                newItem: MyStoryApp
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MyStoryApp,
                newItem: MyStoryApp
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}