package com.example.hydrationapp2.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hydrationapp2.databinding.RecordListItemBinding

class RecordListAdapter : ListAdapter<FullRecord, RecordViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(
            RecordListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FullRecord>() {
            override fun areItemsTheSame(oldItem: FullRecord, newItem: FullRecord): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FullRecord, newItem: FullRecord): Boolean {
                return oldItem.date == newItem.date
            }
        }
    }

}