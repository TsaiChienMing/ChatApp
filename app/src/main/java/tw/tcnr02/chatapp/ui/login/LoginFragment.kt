package tw.tcnr02.chatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import tw.tcnr02.chatapp.R
import tw.tcnr02.chatapp.databinding.FragmentLoginBinding
import tw.tcnr02.chatapp.ui.BindingFragment
import tw.tcnr02.chatapp.util.Constants
import tw.tcnr02.chatapp.util.navigateSafely

//be able to Inject dependencies into android components using dagger hilt
@AndroidEntryPoint
class LoginFragment: BindingFragment<FragmentLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    private val viewModel:LoginViewModel by viewModels()

    //viewBinding must use (view: View, savedInstanceState: Bundle?)
    //not (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            setupConnectingUiState()
            viewModel.connectUser(binding.etUsername.text.toString())
        }


        binding.etUsername.addTextChangedListener{
            binding.etUsername.error = null
        }

        subscribeToEvent()
    }

    private fun subscribeToEvent(){
        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect {event ->
                when(event){
                    is LoginViewModel.LoginEvent.ErrorInputTooShort ->{
                        setupIdleUiState()
                        binding.etUsername.error = getString(R.string.error_username_too_short,Constants.MIN_USERNAME_LENGTH)
                    }
                    is LoginViewModel.LoginEvent.ErrorLogin ->{
                        setupIdleUiState()
                        Toast.makeText(
                            requireContext(),
                            event.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is LoginViewModel.LoginEvent.Success ->{
                        setupIdleUiState()
                        //  it will crash
//                        findNavController().navigate(
//                            R.id.action_loginFragment_to_channelFragment
//                        )
                        // it will work
                        findNavController().navigateSafely(
                            R.id.action_loginFragment_to_channelFragment
                        )
                    }
                }
            }
        }
    }

    private fun setupConnectingUiState(){
        binding.progressBar.isVisible = true
        binding.btnConfirm.isEnabled = false
    }
    private fun setupIdleUiState(){
        binding.progressBar.isVisible = false
        binding.btnConfirm.isEnabled = true
    }
}