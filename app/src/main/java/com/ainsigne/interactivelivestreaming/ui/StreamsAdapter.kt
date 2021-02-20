package com.ainsigne.interactivelivestreaming.ui

import com.ainsigne.interactivelivestreaming.model.Streams


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.ainsigne.interactivelivestreaming.R
import com.ainsigne.interactivelivestreaming.ui.StreamDashboardFragmentDirections
import com.ainsigne.interactivelivestreaming.databinding.AdapterStreamsBinding



class StreamsAdapter : ListAdapter<Streams, StreamsAdapter.ViewHolder>(StreamsDiffCallback()) {
    var visitor : String? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = getItem(position)

        holder.apply {
            bind(createOnClickListener(stream.channel, stream.user.userName, stream.userId.toString(), stream.status), stream)
            itemView.tag = stream
        }
    }

    fun setupVisitor(visitorName : String){
        visitor = visitorName
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterStreamsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(channelId: String, user : String, userId : String, status : Boolean): View.OnClickListener {
        return View.OnClickListener {
            visitor?.let { visitor ->
                val direction = StreamDashboardFragmentDirections.actionStreamDashboardToStreamDetail(channelId =
                channelId, visitor, user, userId, status)
                it.findNavController().navigate(direction)
            }
        }

//        return View.OnClickListener { v ->
//            var direction = if (isVisitor) {
//
//            } else{
//                StreamDashboardFragmentDirections.actionStreamDashboardToStreamDetail(channelId =
//                channelId, user = user, userId)
//            }
//            v.findNavController().navigate(direction)
//        }
    }

    class ViewHolder(
            private val binding: AdapterStreamsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Streams) {
            binding.apply {
                clickListener = listener
                stream = item
                item.imageString?.let { bitmapString ->
                    binding.ivStreamPic.load(Streams.Base64ToBitmap(bitmapString)) {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_foreground)
                        transformations(CircleCropTransformation())
                    }
                }
                executePendingBindings()

            }
        }
    }
}

private class StreamsDiffCallback : DiffUtil.ItemCallback<Streams>() {

    override fun areItemsTheSame(oldItem: Streams, newItem: Streams): Boolean {
        return oldItem.channel == newItem.channel
    }

    override fun areContentsTheSame(oldItem: Streams, newItem: Streams): Boolean {
        return oldItem == newItem
    }
}