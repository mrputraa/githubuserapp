package com.example.githubuserappv03.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.githubuserappv03.R
import com.example.githubuserappv03.alarm.AlarmReceiver
import com.loopj.android.http.AsyncHttpClient.log

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var reminder: String
    private lateinit var language: String

    private lateinit var alarmReceiver: AlarmReceiver

    private lateinit var langPreference: Preference
    private lateinit var reminderPreference: SwitchPreference


    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.settings)
        init()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        language = resources.getString(R.string.key_language)
        alarmReceiver = AlarmReceiver()

        langPreference = findPreference<Preference>(language) as Preference
        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
    }


    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

    }


    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == language) {
            val sIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(sIntent)
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences, key: String?) {
        if (key == reminder) {
            reminderPreference.isChecked = preference.getBoolean(reminder, false)
            if (reminderPreference.isChecked) {
                context?.let { alarmReceiver.setRepeatingAlarm(it) }
                Toast.makeText(context, getString(R.string.reminder_on), Toast.LENGTH_SHORT).show()
            } else {
                context?.let { alarmReceiver.cancelRepeatingAlarm(it) }
                Toast.makeText(context, getString(R.string.reminder_off), Toast.LENGTH_SHORT).show()
            }
        }
    }


}