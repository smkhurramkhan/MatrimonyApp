package com.prathamesh.matrimonyapp.model

class ChatUser {
    var imageUrl: String? = null
    var name: String? = null
    var userId: String? = null
    private var mORr: String? = null
    fun getmORr(): String? {
        return mORr
    }

    fun setmORr(mORr: String?) {
        this.mORr = mORr
    }
}