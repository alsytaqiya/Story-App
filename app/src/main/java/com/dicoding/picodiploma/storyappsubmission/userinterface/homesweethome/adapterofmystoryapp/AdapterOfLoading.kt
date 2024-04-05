package com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome.adapterofmystoryapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.storyappsubmission.databinding.ItemForLoadingBinding

class AdapterOfLoading(private val retry: () -> Unit) :
    LoadStateAdapter<AdapterOfLoading.AdapterOfLoadingViewHolder>() {
    class AdapterOfLoadingViewHolder(
        private val binding: ItemForLoadingBinding,
        retry: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(state: LoadState) {
            if (state is LoadState.Error) {

                binding.itemProgressBar.isVisible = state is LoadState.Loading
                binding.retryButton.isVisible = state is LoadState.Error

            }
        }
    }

    override fun onBindViewHolder(holder: AdapterOfLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): AdapterOfLoadingViewHolder {
        val binding =
            ItemForLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterOfLoadingViewHolder(binding, retry)
    }

}
