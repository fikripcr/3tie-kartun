package com.example.a3tie_kartun.Home.pertemuan_13

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a3tie_kartun.Home.pertemuan_10.TabAFragment
import com.example.a3tie_kartun.Home.pertemuan_10.TabBFragment
import com.example.a3tie_kartun.Home.pertemuan_10.TabCFragment

class ThirteenthTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Jumlah total tab yang ada
    override fun getItemCount(): Int = 3

    // Menentukan Fragment mana yang akan ditampilkan berdasarkan posisi tab
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabCaptureFragment()
            1 -> TabQrcodeFragment()
            2 -> TabScanFragment()
            else -> throw IllegalStateException("Posisi tidak valid")
        }
    }
}