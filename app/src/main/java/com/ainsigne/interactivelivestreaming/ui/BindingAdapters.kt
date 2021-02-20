package com.ainsigne.interactivelivestreaming.ui

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ainsigne.mobilesocialblogapp.utils.timeFromNow


/**
 * Data Binding adapters specific to the app.
 */


@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}



@BindingAdapter("streamer", "visitor")
fun bindStreamer(view: View, streamerName: String, visitorName: String) {
    if(view is TextView) {
        view.setTextColor(if (streamerName == visitorName) Color.RED else Color.WHITE)
    }
}

@BindingAdapter("fromNow")
fun bindFromNow(view: View, fromNowText: String) {
    if(view is TextView)
    view.setText(fromNowText.timeFromNow())
}

@BindingAdapter("streamUser")
fun bindStreamUser(view: View, streamUser: String) {
    if(view is TextView)
        view.setText(streamUser.replace("[-+.^:,\"]".toRegex(), ""))
}


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}
