package comp.example.ahsimapc.smack

import android.app.Application

class App:Application() {


    companion object {
        lateinit var prefs:SharedPrefs
    }


    override fun onCreate() {
        super.onCreate()

        prefs=SharedPrefs(applicationContext)

    }
}