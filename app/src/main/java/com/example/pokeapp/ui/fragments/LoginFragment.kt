package com.example.pokeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentCadastroBinding
import com.example.pokeapp.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textRegister.setOnClickListener({
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        })

        handleLogin()
    }

    private fun handleLogin(){

        binding.buttonLogin.setOnClickListener({
            val email = binding.editTextEmail.text.toString()
            val passwd = binding.editTextPasswd.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha seu email", Toast.LENGTH_SHORT).show()
            } else if (passwd.isEmpty()){
                Toast.makeText(requireContext(), "Preencha sua senha", Toast.LENGTH_SHORT).show()
            } else {

                auth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener({
                    if (it.isSuccessful){
                        findNavController().navigate(R.id.action_loginFragment_to_buscaFragment)
                        //finish()
                    }
                }).addOnFailureListener({
                    var msg = "Houve algum problema no login, tente novamente"
                    if (it is FirebaseAuthInvalidUserException) {
                        msg = "Este usuário não existe"
                    } else if (it is FirebaseAuthInvalidCredentialsException) {
                        msg = "O usuário existe mas a senha está incorreta"
                    } else if (it is FirebaseNetworkException) {
                        msg = "Falha ao conectar com a internet"
                    }
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_buscaFragment)
            //finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}