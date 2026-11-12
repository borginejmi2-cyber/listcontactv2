package com.example.listcontactv2

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat

class MainActivity : AppCompatActivity() {

    private lateinit var lvContacts: ListView
    private lateinit var adapter: ContactAdapter
    private val contactsList = mutableListOf<Contact>()

    // Contrat pour récupérer le résultat envoyé par DetailActivity
    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val updatedContact = data?.let { IntentCompat.getSerializableExtra(it, "contact", Contact::class.java) }
            val position = data?.getIntExtra("position", -1) ?: -1

            if (updatedContact != null && position != -1) {
                // Mettre à jour le contact dans la liste et rafraîchir l'adaptateur
                contactsList[position] = updatedContact
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvContacts = findViewById(R.id.lvContacts)

        // Initialisation de quelques contacts de test (avec les champs demandés)
        contactsList.add(Contact("Dupont", "Claire", "06 12 34 56 78", "claire.dupont@example.com"))
        contactsList.add(Contact("Martin", "Thomas", "07 23 45 67 89", "thomas.martin@example.com"))
        contactsList.add(Contact("Bernard", "Sophie", "06 98 76 54 32", "sophie.bernard@example.com"))
        contactsList.add(Contact("Petit", "Lucas", "07 11 22 33 44", "lucas.petit@example.com"))
        contactsList.add(Contact("Moreau", "Julien", "06 55 44 33 22", "julien.moreau@example.com"))

        // Lier l'adaptateur personnalisé à la ListView
        adapter = ContactAdapter(this, contactsList)
        lvContacts.adapter = adapter

        // Clic sur un contact de la liste : ouvre DetailActivity en lui envoyant l'objet Contact et sa position
        lvContacts.setOnItemClickListener { _, _, position, _ ->
            val contact = contactsList[position]
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("contact", contact)
                putExtra("position", position)
            }
            detailLauncher.launch(intent)
        }
    }
}