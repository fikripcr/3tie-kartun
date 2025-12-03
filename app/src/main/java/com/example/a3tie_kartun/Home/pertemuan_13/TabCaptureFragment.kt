package com.example.a3tie_kartun.Home.pertemuan_13

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import com.example.a3tie_kartun.R
import com.example.a3tie_kartun.databinding.FragmentTabCaptureBinding

class TabCaptureFragment : Fragment() {
    private var _binding: FragmentTabCaptureBinding? = null
    private val binding get() = _binding!!

    private var currentPhotoUri: Uri? = null  //Penting !!!

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoUri?.let { uri ->
                binding.ivCapturedImage.setImageURI(uri)
                // Refresh galeri
                context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            }
        }
    }

    // Launcher untuk meminta izin. Ini adalah cara modern untuk menangani hasil permintaan izin.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Cek apakah semua izin yang diminta telah diberikan
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                // Izin diberikan. Lanjutkan dengan logika yang memerlukan izin,
                // misalnya membuka kamera atau menyimpan gambar.
                Toast.makeText(requireContext(), "Izin penyimpanan diberikan", Toast.LENGTH_SHORT).show()
                openCamera() // Panggil fungsi untuk capture di sini
            } else {
                // Pengguna menolak izin. Beri tahu pengguna bahwa fitur tidak dapat berjalan.
                Toast.makeText(requireContext(), "Izin penyimpanan ditolak. Fitur tidak dapat digunakan.", Toast.LENGTH_LONG).show()
            }
        }

//    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//        if (isGranted) {
//            openCamera()
//        } else {
//            Toast.makeText(context, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTabCaptureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCapture.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                //permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Hapus binding saat view dihancurkan untuk mencegah memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireContext().packageManager) != null) {

            //generate alamat tempat penyimpanan dan nama foto
            currentPhotoUri = createGalleryPhotoUri()

            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            cameraLauncher.launch(intent)
        }
    }

    private fun createGalleryPhotoUri(): Uri {
        val folderName = "TestCaptures"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${folderName}")
        }
        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw RuntimeException("Gagal membuat URI MediaStore")
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Untuk Android 10 (API 29) ke bawah, WRITE_EXTERNAL_STORAGE diperlukan.
        // Untuk Android 11 (API 30) ke atas, aplikasi tidak bisa lagi meminta izin ini secara luas
        // karena Scoped Storage, tetapi requestLegacyExternalStorage="true" membantu di API 29.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // Android 9 (Pie) dan di bawahnya
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        // READ_EXTERNAL_STORAGE tetap relevan di banyak versi
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // Jika ada izin yang perlu diminta
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // Semua izin sudah diberikan, langsung jalankan logika Anda
            Toast.makeText(requireContext(), "Izin sudah ada. Melakukan capture...", Toast.LENGTH_SHORT).show()
            createGalleryPhotoUri()
        }
    }
}