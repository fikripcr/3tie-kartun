package com.example.a3tie_kartun.Home.pertemuan_10

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TenthTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Jumlah total tab yang ada
    override fun getItemCount(): Int = 3

    // Menentukan Fragment mana yang akan ditampilkan berdasarkan posisi tab
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabAFragment()
            1 -> TabBFragment()
            2 -> TabCFragment()
            else -> throw IllegalStateException("Posisi tidak valid")
        }
    }
}