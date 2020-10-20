package com.example.githubuserappv03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserappv03.fragment.SettingFragment

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.setting_menu)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.setting_preferences, SettingFragment())
            .commit()
    }


}
