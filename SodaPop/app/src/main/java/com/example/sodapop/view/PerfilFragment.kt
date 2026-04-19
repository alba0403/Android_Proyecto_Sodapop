package com.example.sodapop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.sodapop.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val switchVeu = view.findViewById<SwitchCompat>(R.id.switchVeu)

        // carreguem el valor guardat
        switchVeu.isChecked = prefs.getBoolean("veu_activada", false)

        // guardem quan l'usuari canvia el valor al activar la veu
        switchVeu.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("veu_activada", isChecked).apply()
            // Avisar al HomeActivity
            (activity as? VeuListener)?.onVeuChanged(isChecked)
        }
    }
}