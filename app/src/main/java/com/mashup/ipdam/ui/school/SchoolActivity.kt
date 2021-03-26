package com.mashup.ipdam.ui.school

import com.mashup.base.BaseActivity
import com.mashup.ipdam.R
import com.mashup.ipdam.databinding.ActivitySchoolBinding

class SchoolActivity : BaseActivity<ActivitySchoolBinding>(R.layout.activity_school) {

    override var logTag: String = "SchoolActivity"

    override fun initLayout() {
        binding.schoolBackButton.setOnClickListener {
            finish()
        }
    }
}