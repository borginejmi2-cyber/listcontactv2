package com.example.listcontactv2

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ContactAdapter(
    context: Context,
    private val contacts: List<Contact>,
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = contacts.size

    override fun getItem(position: Int): Any = contacts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_contact, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.imgContact),
                view.findViewById(R.id.tvNomPrenom),
                view.findViewById(R.id.tvTelephone)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val contact = contacts[position]
        holder.tvNomPrenom.text = "${contact.nom} ${contact.prenom}"
        holder.tvTelephone.text = contact.telephone

        // Affichage de la photo (ou image par défaut si aucune photo n'est définie)
        if (!contact.photoUri.isNullOrEmpty()) {
            try {
                holder.imgContact.setImageURI(Uri.parse(contact.photoUri))
            } catch (_: Exception) {
                holder.imgContact.setImageResource(R.mipmap.ic_launcher_round)
            }
        } else {
            holder.imgContact.setImageResource(R.mipmap.ic_launcher_round)
        }

        return view
    }

    private class ViewHolder(
        val imgContact: ImageView,
        val tvNomPrenom: TextView,
        val tvTelephone: TextView
    )
}