package com.ainsigne.interactivelivestreaming.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.interactivelivestreaming.databinding.AdapterChatsBinding
import com.ainsigne.interactivelivestreaming.model.StreamChat


class StreamChatAdapter : ListAdapter<StreamChat, StreamChatAdapter.ViewHolder>(StreamChatDiffCallback()) {

    var streamerName = ""


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val streamchat = getItem(position)

        holder.apply {
            bind(streamchat, streamerName, streamchat.user)
            itemView.tag = streamchat
        }
    }

    fun updateStreamer(streamer : String){
        streamerName = streamer
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterChatsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }


    class ViewHolder(
            private val binding: AdapterChatsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StreamChat, streamer: String, visitor: String) {
            binding.apply {
                streamChat = item
                streamerName = streamer
                visitorName = visitor
                executePendingBindings()

            }
        }
    }
}

private class StreamChatDiffCallback : DiffUtil.ItemCallback<StreamChat>() {

    override fun areItemsTheSame(oldItem: StreamChat, newItem: StreamChat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StreamChat, newItem: StreamChat): Boolean {
        return oldItem == newItem
    }
}