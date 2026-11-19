package com.example.pokeapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentBuscaBinding
import com.example.pokeapp.databinding.FragmentCadastroBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class CadastroFragment : Fragment() {
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleCadastrar()
    }

    private fun handleCadastrar() {

        binding.btnCadastrar.setOnClickListener({
            val name = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val passwd1 = binding.editTextPasswd1.text.toString()
            val passwd2 = binding.editTextPasswd2.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha seu nome", Toast.LENGTH_SHORT)
                Toast.makeText(requireContext(), "Preencha seu nome", Toast.LENGTH_SHORT)
            } else if (lastName.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha seu sobrenome", Toast.LENGTH_SHORT)
            } else if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha seu email", Toast.LENGTH_SHORT)
            } else if (passwd1.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha a senha 1", Toast.LENGTH_SHORT)
            } else if (passwd2.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha a senha 2", Toast.LENGTH_SHORT)
            } else if (passwd1 != passwd2) {
                Toast.makeText(requireContext(), "Senha 1 não pode ser diferente da 2", Toast.LENGTH_SHORT)
            } else {
                auth.createUserWithEmailAndPassword(email, passwd1).addOnCompleteListener({
                    if (it.isSuccessful) {
                        saveUserData(name, lastName)
                        Toast.makeText(requireContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()

                        findNavController().navigate(R.id.action_cadastroFragment_to_loginFragment)

                        //finish()
                    }
                }).addOnFailureListener({
                    var msg = "Houve algum problema no cadastro"
                    if (it is FirebaseAuthInvalidCredentialsException) {
                        msg = "Digite um email válido"
                    } else if (it is FirebaseAuthWeakPasswordException) {
                        msg = "Digite uma senha com no mínimo 6 caracteres"
                    } else if (it is FirebaseAuthUserCollisionException) {
                        msg = "Este e-mail já foi cadastrado"
                    } else if (it is FirebaseNetworkException) {
                        msg = "Falha ao conectar com a internet"
                    }
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    private fun saveUserData(name: String, lastName: String){
        val userMap = hashMapOf(
            "name" to name,
            "lastName" to lastName
        )
        val currUserId = auth.currentUser?.uid
        if (currUserId != null) {
            db.collection("usuarios").document(currUserId)
                .set(userMap)
                .addOnSuccessListener {
                    Log.d("Firebase", "Salvo com sucesso")
                }
                .addOnFailureListener{
                    Log.d("Firebase", "Problema ao salvar: ", it)
                }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}