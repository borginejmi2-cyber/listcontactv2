package com.example.listcontactv2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat

class DetailActivity : AppCompatActivity() {

    private lateinit var imgPhoto: ImageView
    private lateinit var etNom: EditText
    private lateinit var etPrenom: EditText
    private lateinit var etTelephone: EditText
    private lateinit var etEmail: EditText

    private var currentPhotoUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgPhoto = findViewById(R.id.imgDetailPhoto)
        etNom = findViewById(R.id.etDetailNom)
        etPrenom = findViewById(R.id.etDetailPrenom)
        etTelephone = findViewById(R.id.etDetailTelephone)
        etEmail = findViewById(R.id.etDetailEmail)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnCall = findViewById<Button>(R.id.btnCall)
        val btnEmail = findViewById<Button>(R.id.btnEmail)

        // Récupérer le contact envoyé par la MainActivity
        val contact = IntentCompat.getSerializableExtra(intent, "contact", Contact::class.java)
        contact?.let {
            etNom.setText(it.nom)
            etPrenom.setText(it.prenom)
            etTelephone.setText(it.telephone)
            etEmail.setText(it.email)
            currentPhotoUri = it.photoUri
            if (!currentPhotoUri.isNullOrEmpty()) {
                try {
                    imgPhoto.setImageURI(Uri.parse(currentPhotoUri))
                } catch (_: Exception) {
                    imgPhoto.setImageResource(R.mipmap.ic_launcher_round)
                }
            }
        }

        // Bouton Enregistrer : renvoie les données modifiées
        btnSave.setOnClickListener {
            val nom = etNom.text.toString().trim()
            val prenom = etPrenom.text.toString().trim()
            val telephone = etTelephone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedContact = Contact(nom, prenom, telephone, email, currentPhotoUri)
            val resultIntent = Intent().apply {
                putExtra("contact", updatedContact)
                putExtra("position", intent.getIntExtra("position", -1))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Bouton Annuler
        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // Bouton Appeler (Intent implicite)
        btnCall.setOnClickListener {
            val tel = etTelephone.text.toString().trim()
            if (tel.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))
                startActivity(intent)
            }
        }

        // Bouton Email (Intent implicite)
        btnEmail.setOnClickListener {
            val mail = etEmail.text.toString().trim()
            if (mail.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$mail"))
                startActivity(intent)
            }
        }
    }
}