package com.example.kotlininstegram.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.kotlininstegram.R
import com.example.kotlininstegram.adapter.FeedRecyclerAdapter
import com.example.kotlininstegram.databinding.ActivityFeedBinding
import com.example.kotlininstegram.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var feedBinding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var feedRecyclerAdapter: FeedRecyclerAdapter

    private lateinit var postList: ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        feedBinding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(feedBinding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        postList = ArrayList<Post>()
        getData()

        feedRecyclerAdapter = FeedRecyclerAdapter(postList)
        feedBinding.RecyclerView.layoutManager = LinearLayoutManager(this)
        feedBinding.RecyclerView.adapter = feedRecyclerAdapter


    }


    private fun getData() {
        db.collection(("Post")).orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error -> // burada bütün postu değilde esqllite gibi where kullanıp filtreleme yapabiliriz  orderBy da kullanabilriiz


                if (error != null) {
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                } else {

                    // liste halinde döküman verecek  documents artık bir liste
                    val documents = value?.documents

                    if (documents != null) {
                        postList.clear() // verileri almadan listeyi temizle

                        for (document in documents) {
                            // casting
                            val comment = document.get("comment") as String // string sana gelecek onları stringe çevir
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            println(downloadUrl)

                            val post = Post(userEmail, comment, downloadUrl)
                            postList.add(post)

                        }
                    }
                    feedRecyclerAdapter.notifyDataSetChanged()

                }
            }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.add_post == item.itemId){
            // uploadactivity
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
          //  finish() //kullanıcı resim yüklemek istemeyebilir geri gelebilir

        } else if (item.itemId == R.id.signout) {
            auth.signOut() // Kullanıcının oturumunu kapatır
            val intent = Intent(this, MainActivity::class.java) // Kullanıcıyı MainActivity'ye yönlendirir
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}