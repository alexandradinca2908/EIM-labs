package ro.pub.systems.eim.lab02.activitylifecyclemonitor.graphicuserinterface

import android.app.ActionBar
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.PopupWindow
import ro.pub.systems.eim.lab02.activitylifecyclemonitor.R
import ro.pub.systems.eim.lab02.activitylifecyclemonitor.general.Constants
import ro.pub.systems.eim.lab02.activitylifecyclemonitor.general.Utilities.allowAccess

class LifecycleMonitorActivity : AppCompatActivity() {
    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val usernameEditText = findViewById(R.id.username_edit_text) as EditText
            val passwordEditText = findViewById(R.id.password_edit_text) as EditText
            if ((view as Button).text.toString() == resources.getString(R.string.ok_button_content)) {
                val layoutInflater =
                    baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                val popupContent: View
                popupContent = if (allowAccess(
                        applicationContext, username, password
                    )
                ) {
                    layoutInflater.inflate(R.layout.popup_window_authentication_success, null)
                } else {
                    layoutInflater.inflate(R.layout.popup_window_authentication_fail, null)
                }
                val popupWindow = PopupWindow(
                    popupContent,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
                val dismissButton = popupContent.findViewById<View>(R.id.dismiss_button) as Button
                dismissButton.setOnClickListener { popupWindow.dismiss() }
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            }
            if (view.text.toString() == resources.getString(R.string.cancel_button_content)) {
                usernameEditText.setText(resources.getText(R.string.empty))
                passwordEditText.setText(resources.getText(R.string.empty))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle_monitor)
        val okButton = findViewById(R.id.ok_button) as Button
        okButton.setOnClickListener(buttonClickListener)
        val cancelButton = findViewById(R.id.cancel_button) as Button
        cancelButton.setOnClickListener(buttonClickListener)
        if (savedInstanceState == null) {
            Log.d(Constants.TAG, "onCreate() method was invoked without a previous state")
        } else {
            Log.d(Constants.TAG, "onCreate() method was invoked with a previous state")
            if (savedInstanceState.containsKey(Constants.USERNAME_EDIT_TEXT)) {
                val usernameEditText = findViewById(R.id.username_edit_text) as EditText
                usernameEditText.setText(savedInstanceState.getString(Constants.USERNAME_EDIT_TEXT))
            }
            if (savedInstanceState.containsKey(Constants.PASSWORD_EDIT_TEXT)) {
                val passwordEditText = findViewById(R.id.password_edit_text) as EditText
                passwordEditText.setText(savedInstanceState.getString(Constants.PASSWORD_EDIT_TEXT))
            }
            if (savedInstanceState.containsKey(Constants.REMEMBER_ME_CHECKBOX)) {
                val rememberMeCheckbox = findViewById(R.id.remember_me_checkbox) as CheckBox
                rememberMeCheckbox.isChecked =
                    savedInstanceState.getBoolean(Constants.REMEMBER_ME_CHECKBOX)
            }
        }
    }

    public override fun onRestart() {
        super.onRestart()
        Log.d(Constants.TAG, "onRestart() method was invoked")
    }

    public override fun onStart() {
        super.onStart()
        Log.d(Constants.TAG, "onStart() method was invoked")
    }

    public override fun onResume() {
        super.onResume()
        Log.d(Constants.TAG, "onResume() method was invoked")
    }

    public override fun onPause() {
        super.onPause()
        Log.d(Constants.TAG, "onPause() method was invoked")
    }

    public override fun onStop() {
        super.onStop()
        Log.d(Constants.TAG, "onStop() method was invoked")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d(Constants.TAG, "onDestroy() method was invoked")
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val usernameEditText = findViewById(R.id.username_edit_text) as EditText
        val passwordEditText = findViewById(R.id.password_edit_text) as EditText
        val rememberMeCheckBox = findViewById(R.id.remember_me_checkbox) as CheckBox
        if (rememberMeCheckBox.isChecked) {
            savedInstanceState.putString(
                Constants.USERNAME_EDIT_TEXT,
                usernameEditText.text.toString()
            )
            savedInstanceState.putString(
                Constants.PASSWORD_EDIT_TEXT,
                passwordEditText.text.toString()
            )
            savedInstanceState.putBoolean(
                Constants.REMEMBER_ME_CHECKBOX,
                rememberMeCheckBox.isChecked
            )
        }
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(Constants.USERNAME_EDIT_TEXT)) {
            val usernameEditText = findViewById(R.id.username_edit_text) as EditText
            usernameEditText.setText(savedInstanceState.getString(Constants.USERNAME_EDIT_TEXT))
        }
        if (savedInstanceState.containsKey(Constants.PASSWORD_EDIT_TEXT)) {
            val passwordEditText = findViewById(R.id.password_edit_text) as EditText
            passwordEditText.setText(savedInstanceState.getString(Constants.PASSWORD_EDIT_TEXT))
        }
        if (savedInstanceState.containsKey(Constants.REMEMBER_ME_CHECKBOX)) {
            val rememberMeCheckBox = findViewById(R.id.remember_me_checkbox) as CheckBox
            rememberMeCheckBox.isChecked =
                savedInstanceState.getBoolean(Constants.REMEMBER_ME_CHECKBOX)
        }
    }
}
