package com.dream.dreamview.module.secret

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dream.dreamview.R
import com.dream.dreamview.base.NavBaseActivity
import com.dream.dreamview.module.rxbus.RxBus
import com.dream.dreamview.secret.AESCryptService

/**
 * Created on 2017/8/27
 */
class SecretActivity : NavBaseActivity(), View.OnClickListener {
    private lateinit var inputText: EditText
    private lateinit var encryptText: TextView
    private lateinit var decryptText: TextView

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SecretActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getContentView(): Int {
        return R.layout.secret_secret_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputText = findViewById(R.id.input_text)
        encryptText = findViewById(R.id.encrypt_text)
        decryptText = findViewById(R.id.decrypt_text)
        val encryptBtn: Button = findViewById(R.id.encrypt_btn)
        val decryptBtn: Button = findViewById(R.id.decrypt_btn)
        encryptBtn.setOnClickListener(this)
        decryptBtn.setOnClickListener(this)
        val rxBus = RxBus.getInstance()
        if (rxBus.hasObservers()) {
            rxBus.post("ssss")
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.encrypt_btn -> encrypt()
            R.id.decrypt_btn -> decrypt()
        }
    }

    private fun encrypt() {
        val inputText = inputText.text.toString()
        if (!TextUtils.isEmpty(inputText)) {
            encryptText.text = AESCryptService.getInstance().encrypt(inputText)
        }
    }

    private fun decrypt() {
        val encryptText = encryptText.text.toString()
        if (!TextUtils.isEmpty(encryptText)) {
            decryptText.text = AESCryptService.getInstance().decrypt(encryptText)
        }
    }
}