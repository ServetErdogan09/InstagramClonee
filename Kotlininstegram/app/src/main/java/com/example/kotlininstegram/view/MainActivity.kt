package com.example.kotlininstegram.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlininstegram.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        //Firebase  Initializ et
        auth = Firebase.auth

        val currentUser = auth.currentUser // nullable veriyor kullanıcı giriş yapmamış olablir ila kullanıcı olacak diye birşey yok

        // kullanıcı daha önceden giriş yapmışsa bir daha giriş için email ve şifre isteme
        if (currentUser != null){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun signUpClicked(view: View) {

        email = mainBinding.EmaileditText.text.toString()
        password = mainBinding.passwordeditText.text.toString()

        if (email.equals("") || password == "") {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_LONG).show()
        } else {
            // internete istek atacağı için firebase'ten cevap gelmeyince kadar diğer kodlar çalışmasın onun için firebase dinleyeceğiz
            auth.createUserWithEmailAndPassword(email!!, password!!).addOnSuccessListener {
                // kullanıcı bağarıyla oluşturulmuşsa
                Toast.makeText(this, "User registration created", Toast.LENGTH_LONG).show()
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                 finish()
            }.addOnFailureListener {
                // kullanıcı başarıyla oluşturulmadıysa hata oluşmuşsa
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }

    }

    fun signInClicked(view: View) {
        email = mainBinding.EmaileditText.text.toString()
        password = mainBinding.passwordeditText.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_LONG).show()

        } else {
            auth.signInWithEmailAndPassword(email!!, password!!)
                // tam dinleyici hem başarılı hem hatalı dinlemeleri yapacak
                .addOnCompleteListener(this) { task ->
                    // kimlik doğrulama başarılı
                    if (task.isSuccessful){
                    val intent = Intent(this, FeedActivity::class.java)
                     startActivity(intent)
                      finish()
                    }else{
                        Toast.makeText(this,"Authentication failed",Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}