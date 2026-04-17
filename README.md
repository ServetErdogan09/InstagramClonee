# Instagram Clone (Kotlin & Firebase) 📸

Instagram'ın temel özelliklerini barındıran, Firebase ekosistemi (Authentication, Cloud Firestore, Cloud Storage) üzerine inşa edilmiş modern bir Android sosyal medya uygulamasıdır. 

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blue?logo=kotlin)](#)
[![Firebase](https://img.shields.io/badge/Firebase-Auth%20|%20Firestore%20|%20Storage-orange?logo=firebase)](#)

## 📱 Ekran Görüntüsü (Gelecek)
*(Kullanıcı arayüzü ve akışlar eklenecektir)*
<!-- <img src="assets/feed_screen.jpeg" width="300"/> -->

## 🚀 Proje Odak Noktaları ve Özellikler
Bu proje, modern Android uygulamalarında sıkça karşılaştığımız "Yetkilendirme (Auth), Veritabanı ve Medya Yönetimi" süreçlerini bir arada profesyonelce sunmayı hedefler.

- **Kullanıcı Yetkilendirme (Firebase Auth):** Güvenli bir şekilde kullanıcı kayıt olma (Register) ve giriş yapma (Login) işlemleri. Oturum yönetimi sayesinde çıkış yapana kadar sistemde kalınır.
- **Gerçek Zamanlı Akış (Cloud Firestore):** Paylaşılan tüm gönderiler (fotoğraf, açıklama, kullanıcı maili ve paylaşım zamanı) anlık olarak Firestore NoSQL veritabanına kaydedilir ve Feed (Akış) sayfasında RecyclerView ile listelenir.
- **Medya Yönetimi (Cloud Storage):** Kullanıcıların galeriden seçtiği veya yeni çektiği fotoğraflar Cloud Storage'a yüklenir, URL'leri Firestore'da referans olarak tutulur.
- **Görsel Yükleme Optimizasyonu (Picasso):** Akışta (Feed) yer alan yüksek çözünürlüklü fotoğraflar Picasso kütüphanesi yardımıyla asenkron olarak ve önbelleklenerek (cache) yüklenir.

## 🛠 Kullanılan Teknolojiler & Mimari Yapı

- **Dil:** Kotlin
- **Kullanıcı Arayüzü (UI):** XML & ViewBinding
- **Bağımlılık (Görsel İçerik):** [Picasso](https://square.github.io/picasso/)
- **Backend (BaaS):** 
  - **Firebase Authentication**
  - **Firebase Cloud Firestore**
  - **Firebase Cloud Storage**

## 🔧 Kurulum ve Başlangıç

Bu projeyi kendi bilgisayarınızda çalıştırmak isterseniz kendi Firebase ortamınızı bağlamanız gerekmektedir:

1. Bu depoyu klonlayın:
   ```bash
   git clone https://github.com/ServetErdogan09/InstagramClonee.git
   ```
2. Android Studio ile açın.
3. [Firebase Console](https://console.firebase.google.com/) üzerinden yeni bir proje oluşturun.
4. `google-services.json` dosyanızı projedeki `app/` dizininin içine atın.
5. Firebase konsolundan **Authentication (Email/Password)**, **Firestore Database** ve **Storage** servislerini aktifleştirin.
6. Projeyi derleyip çalıştırın.

---
**Geliştirici:** Servet Erdoğan
