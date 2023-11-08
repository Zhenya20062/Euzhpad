package com.euzhene.euzhpad.presentation

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

@BindingAdapter("toolbar_title")
fun bindToolbarTitle(toolbar:View, title:String) {
    (toolbar as Toolbar).title = title
}