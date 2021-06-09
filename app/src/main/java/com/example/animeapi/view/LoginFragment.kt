package com.example.animeapi.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.animeapi.R
import com.example.animeapi.databinding.FragmentAnimeDetailBinding
import com.example.animeapi.databinding.FragmentLoginBinding
import com.example.animeapi.databinding.RegistrationDialogBinding
import com.example.animeapi.model.Anime
import com.example.animeapi.model.db.AppDatabase
import com.example.animeapi.model.db.UserDao
import com.example.animeapi.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    lateinit var anime : Anime
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var _dialogBinding: RegistrationDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!

    var userDao : UserDao? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        userDao = AppDatabase.createDb(requireContext()).userDao()

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _dialogBinding = RegistrationDialogBinding.inflate(inflater, container, false)

        val view = binding.root
        binding.buttonLogin.setOnClickListener{
            val login = binding.editLogin.text.toString()
            val password = binding.editPassword.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    userDao?.deactivateAll()
                    val userList = userDao?.getUserByLogin(login)

                    withContext(Main) {
                        var user : User? = null
                        if (!userList.isNullOrEmpty()){
                            user = userList[0]
                        }

                        if (login == user?.login && password == user?.password) {
                            withContext(Dispatchers.IO) {
                                userDao?.setActive(user.id)
                            }
                            findNavController().navigate(R.id.searchFragment)
                        } else {
                            val builder = AlertDialog.Builder(view.context)
                            builder.setTitle("Ошибка входа")
                            builder.setMessage("Неверный логин и пароль")
                            builder.setPositiveButton("OK") { dialog, id ->
                                // User cancelled the dialog
                            }
                            builder.show()
                        }
                    }
                }
            }
        }

        binding.buttonRegistration.setOnClickListener {
            val dialogView = dialogBinding.root

            val builder = AlertDialog.Builder(dialogView.context)
                .setView(dialogView)
                .setTitle("Форма регистрации")
            val  alertDialog = builder.show()

            dialogBinding.buttonRegConfirm.setOnClickListener {
                alertDialog.dismiss()
//                TODO: пароль по хэшу
                val login = dialogBinding.textLogin.text.toString()
                val email = dialogBinding.textEmail.text.toString()
                val password = dialogBinding.textPassword.text.toString()

                GlobalScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.IO) {
                        userDao!!.insert(User(login, email, password, false))
                    }
                }
            }

            dialogBinding.buttonClose.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _dialogBinding = null
    }
}