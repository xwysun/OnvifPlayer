package com.xwysun.onvifplayer.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.xwysun.onvifplayer.base.BaseActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xwysun.onvifplayer.R
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : BaseActivity() {

    private val fragments= arrayListOf<Fragment>()

    private val mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        QMUIStatusBarHelper.translucent(this)
        initTabs()
        initPager()
    }
    private fun initTabs(){
        val normalColor = QMUIResHelper.getAttrColor(this, R.attr.qmui_config_color_gray_6)
        val selectColor = QMUIResHelper.getAttrColor(this, R.attr.qmui_config_color_blue)
        tabs.setDefaultNormalColor(normalColor)
        tabs.setDefaultSelectedColor(selectColor)
        val component = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_component_selected),
                "扫描", false
        )

        val util = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_util),
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_util_selected),
                "保存", false
        )
        val lab = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_lab),
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_lab_selected),
                "设置", false
        )
        tabs.addTab(component)
                .addTab(util)
                .addTab(lab)
    }
    fun initPager(){
        fragments.add(ScanFragment.newInstance())
        fragments.add(SavedFragment.newInstance())
        fragments.add(SettingFragment.newInstance())
        pager.adapter=mPagerAdapter
        tabs.setupWithViewPager(pager,false)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }



}