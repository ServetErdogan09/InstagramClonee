package com.example.kotlininstegram.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlininstegram.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var uploadBinding: ActivityUploadBinding
    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectImageUri: Uri? = null
    private  lateinit var auth : FirebaseAuth // hangi kullanıcı postu yapcak onu bilmemiz lazım
    private  lateinit var fireStore : FirebaseFirestore
    private  lateinit var stroage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(uploadBinding.root)

        registerLauncher()

        auth = Firebase.auth
        fireStore = Firebase.firestore
        stroage = Firebase.storage

    }

    fun upload(view: View) {
        // Upload işlemi burada yapılabilir

        // rasgele uuıd ve string alacağız
        val uuid = UUID.randomUUID() // ÖNCEDEN KAYDETİĞİMİZ İSMİN ÜZERİNE FARKLI BİR RESİM YAZMASIN DİYE FARKLI BİR İSİM ALTINDA STROAGE KOYACAĞIZ

        val imageName = "$uuid.jpg"

        val referance = stroage.reference // Firebasede'ki stroage arayüzünün referansını veriyor

       // val imageReference = referance.child("images/image.jpg") // stroage içine images diye klasör oluştur ve image.jpg dosyasını koy

        val imageReference = referance.child("images").child(imageName) // böyle ayrı ayrı da yazabiliriz
        //selectImageUri?.let { referance.putFile(it) }  // buraya koyduğumuz uri stroage kaydedecek

        // resim seçmeden uploade basarsa kabull etme
        if (selectImageUri != null){

            imageReference.putFile(selectImageUri!!).addOnSuccessListener {  // bu listener upload edilip edilmediğini verecek

                // download url -> firestore
                val uploadPictureReference = stroage.reference.child("images").child(imageName) // stroage deki images klasöründeki görselerin isimlerine ulaş
                uploadPictureReference.downloadUrl.addOnSuccessListener {it ->  // bize url verecek

                    val downloadUrl = it.toString() // uri 'yi Stringe çevirdik veri tabanaına yazacağız firestore



                    // fireStore ye yükleyeceğim bütün değerlerin key ve valuelerini aldım
                    val postMap = hashMapOf<String,Any>() // anahtarlarım string olacak ama değerlerim herşey olabilir any
                    postMap["downloadUrl"] = downloadUrl
                    postMap["userEmail"] = auth.currentUser!!.email!!
                    postMap["comment"] = uploadBinding.CommentText.text.toString()
                    postMap["date"] = Timestamp.now() // güncel zaman neyse onu verecek

                    fireStore.collection("Post")
                        .add(postMap)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Yüklendi",Toast.LENGTH_LONG).show()
                        finish()
                     }.addOnFailureListener{
                      Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                }

            }.addOnFailureListener{ // UPLOAD EDERKEN HATA OLUŞURSA ONU VERECEK

                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }

    fun selectImage(view: View) {
        // İzinleri kontrol et, izin yoksa izin iste
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission") {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else {
                // Direkt izin iste, açıklama göstermeden
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            // Galeriye git
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imageLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            // İzin verilmişse
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imageLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(this, "Permission needed", Toast.LENGTH_LONG).show()
            }
        }

        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                intentFromResult?.data?.let {
                    selectImageUri = it
                    uploadBinding.imageView.setImageURI(it)
                }
            }
        }
    }
}
