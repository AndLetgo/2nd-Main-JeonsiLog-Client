package com.example.jeonsilog.view.photocalendar




interface CommunicationListener {
    fun onDialogButtonClick(data: String)
    fun onRecyclerViewItemClick(position: Int)
}