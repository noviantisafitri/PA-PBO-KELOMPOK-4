# PA-PBO-KELOMPOK-4

# Sistem Informasi Peminjaman Ruang

# Deskripsi Project

"Sistem Informasi Peminjaman Ruang" merupakan aplikasi Java yang dibangun menggunakan paradigma Pemrograman Berorientasi Objek (PBO). Aplikasi ini dirancang untuk memudahkan proses peminjaman ruang dengan menyediakan antarmuka yang interaktif dan mudah digunakan. Aplikasi ini mengikuti arsitektur Model-View-Controller (MVC), yang memisahkan logika aplikasi menjadi tiga komponen utama untuk meningkatkan modularity dan memfasilitasi perawatan kode yang lebih mudah.

Fitur Utama

- Manajemen ruang: Mengizinkan pengguna untuk menambah, mengubah, dan menghapus informasi ruang yang tersedia untuk peminjaman.
- Manajemen Peminjaman: Memungkinkan pengguna untuk membuat, memodifikasi, dan mengelola peminjaman ruang.
- Autentikasi Pengguna: Fitur login untuk membedakan antara berbagai jenis pengguna seperti staff dan mahasiswa.

Teknologi

- Java: Bahasa pemrograman utama yang digunakan untuk membangun aplikasi.
- NetBeans IDE: Lingkungan pengembangan terpadu yang digunakan untuk mengembangkan aplikasi.
- MySQL: Sistem manajemen basis data yang digunakan untuk menyimpan informasi ruang dan peminjaman.

Struktur Proyek

- src: Direktori yang berisi source code Java, termasuk paket Controller, Database, Model, dan View.
- dist: Berisi file JAR yang dapat dijalankan dan library pihak ketiga yang diperlukan untuk menjalankan aplikasi.
- nbproject: Berisi file konfigurasi yang spesifik untuk proyek NetBeans.

Proyek ini juga mencakup file peminjaman_ruang.sql yang menyediakan skrip SQL untuk membuat struktur basis data yang diperlukan oleh aplikasi. Ini mencakup tabel dan relasi yang diperlukan untuk menyimpan data peminjaman ruang.

Cara Menjalankan

- Untuk menjalankan aplikasi ini, Anda dapat menggunakan file JAR yang disediakan di dalam direktori dist. Pastikan bahwa semua library yang terdapat di dist/lib sudah termasuk dalam classpath aplikasi.

# Flowchart

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/454cfc23-8e7f-411e-bf23-f79a03bdbdfe)

# ERD

![Logical](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/4bb8dbab-c531-4280-b389-d3267c7237fc)
![Relational_1](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/9001fe4d-111f-4e8f-9d79-7edb36fdde32)

# Hirarki Class

# SourceCode dan Penjelasan

Model Classes

- GedungModel: Bertanggung jawab atas representasi data gedung, termasuk ID, nama, dan kapasitas. Class ini menyediakan metode untuk CRUD operasi yang memungkinkan aplikasi untuk berinteraksi dengan database gedung, seperti menambahkan gedung baru, mengambil daftar gedung, mengupdate informasi gedung, dan menghapus gedung dari sistem.
- PeminjamanModel: Mengelola data peminjaman gedung, seperti informasi peminjam, tanggal peminjaman, dan keperluan peminjaman. Class ini berinteraksi dengan database untuk menyimpan dan mengambil peminjaman, serta mengelola status dan detail peminjaman gedung.
- UserModel: Mengatur informasi pengguna aplikasi. Ini termasuk pengelolaan kredensial pengguna, fungsi login, dan mempertahankan status sesi pengguna. Class ini juga dapat menangani peran pengguna (misalnya, mahasiswa atau staff) dan membatasi akses ke fitur tertentu berdasarkan peran tersebut.

Controller Classes

- GedungController: Sebagai penghubung antara GedungModel dan tampilan yang berkaitan dengan gedung, class ini mengelola logika bisnis untuk operasi gedung. Ini termasuk mengambil input pengguna, memvalidasi data, dan memanggil metode yang relevan pada GedungModel untuk menjalankan operasi database.

View Classes

- Login: Tampilan untuk autentikasi pengguna. Memiliki elemen UI seperti kotak teks untuk nama pengguna dan kata sandi, serta tombol untuk masuk. Class ini berkomunikasi dengan UserController untuk memverifikasi kredensial pengguna dan memberikan akses ke sistem.

MenuMahasiswa dan MenuStaff: Ini adalah tampilan yang berbeda untuk mahasiswa dan staff, menyediakan antarmuka pengguna yang disesuaikan dengan peran mereka di dalam sistem. Menu ini mungkin menampilkan opsi yang berbeda, seperti pemesanan gedung bagi mahasiswa dan manajemen gedung untuk staff.

Database dan Konfigurasi

- Koneksi: Class penting yang menyediakan koneksi ke database. Ini adalah bagian kunci dari paket Database dan digunakan oleh semua model untuk melakukan transaksi database.

Konfigurasi NetBeans: File seperti project.xml dan build-impl.xml mengonfigurasi cara proyek dibangun dan dijalankan di dalam NetBeans IDE. Ini mencakup pengaturan path, library yang digunakan, dan konfigurasi khusus proyek.

Ekstra dan Library

- Library Eksternal: Aplikasi ini menggunakan beberapa library Java seperti jcalendar untuk memilih tanggal, dan mysql-connector-java untuk koneksi database. Ini memperluas fungsionalitas dasar Java dan menyediakan komponen UI yang lebih canggih dan pengelolaan database yang efisien.

File Eksekusi: File JAR dalam direktori dist adalah versi yang dapat dijalankan dari aplikasi, yang telah dikompilasi dan siap untuk didistribusikan atau dijalankan oleh pengguna akhir.

Aplikasi ini dirancang dengan pemisahan tanggung jawab yang jelas, memungkinkan perawatan dan pengembangan yang teratur. Dengan menggunakan pola MVC, aplikasi ini memastikan bahwa antarmuka pengguna, logika bisnis, dan manipulasi data terpisah, yang memudahkan pengujian dan pengembangan lebih lanjut.

Setiap komponen bekerja sama untuk menyediakan pengalaman pengguna yang lancar, dari login ke sistem, pengelolaan gedung, hingga proses peminjaman. Arsitektur aplikasi ini mendukung skalabilitas, memungkinkan penambahan fitur baru atau penyesuaian dengan relatif mudah.

# Output

Tampilan Login
Sebagaimana dijelaskan sebelumnya, gambar ini menunjukkan tampilan login yang meminta pengguna untuk memasukkan nama pengguna dan kata sandi mereka. Hal ini untuk memastikan bahwa hanya pengguna terotorisasi yang dapat mengakses fitur aplikasi.

Dashboard Utama
merupakan tampilan setelah pengguna berhasil masuk, menampilkan dashboard atau menu utama. Ini bisa menunjukkan berbagai opsi yang tersedia bagi pengguna, seperti melihat daftar gedung yang dapat dipinjam, melakukan pemesanan gedung, atau mengakses riwayat peminjaman.

Formulir Peminjaman Gedung
Gambar ini menampilkan formulir yang harus diisi saat pengguna ingin meminjam gedung. Formulir ini meminta informasi seperti nama gedung, tanggal peminjaman, durasi, dan tujuan peminjaman.

Kalender Peminjaman
Menunjukkan kalender yang memungkinan pengguna untuk melihat tanggal yang tersedia untuk peminjaman gedung. Pengguna dapat memilih tanggal dan melihat gedung yang tersedia atau sudah dipesan pada tanggal tersebut.

Konfirmasi Peminjaman
Gambar ini menampilkan ringkasan atau konfirmasi detail peminjaman sebelum pengguna menyelesaikan proses peminjaman. Ini memastikan bahwa semua informasi yang dimasukkan benar dan memungkinkan pengguna untuk melakukan perubahan jika diperlukan.

Manajemen Gedung
Bagi pengguna dengan peran administratif, seperti staff, gambar ini menunjukkan antarmuka untuk mengelola informasi gedung, termasuk menambahkan gedung baru, mengedit informasi gedung, atau menghapus gedung dari sistem.

Laporan dan Statistik
Gambar ini menunjukkan laporan atau statistik tentang peminjaman gedung. Ini bisa termasuk data seperti jumlah peminjaman per periode waktu, gedung yang paling sering dipinjam, atau tren lain yang berguna untuk analisis administratif.

Pengaturan Akun Pengguna
Merupakan tampilan pengaturan akun pengguna, memungkinkan pengguna untuk mengubah informasi pribadi mereka, kata sandi, atau preferensi lain yang terkait dengan akun mereka di aplikasi.
