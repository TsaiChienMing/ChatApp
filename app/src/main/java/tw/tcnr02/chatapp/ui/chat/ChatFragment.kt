package tw.tcnr02.chatapp.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import tw.tcnr02.chatapp.databinding.FragmentChatBinding
import tw.tcnr02.chatapp.ui.BindingFragment

@AndroidEntryPoint
class ChatFragment : BindingFragment<FragmentChatBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChatBinding::inflate

    private val args: ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MessageListViewModelFactory(args.channelId)
        val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel: MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        messageListHeaderViewModel.bindView(binding.messageListHeaderView, viewLifecycleOwner)
        messageListViewModel.bindView(binding.messageListView, viewLifecycleOwner)
        messageInputViewModel.bindView(binding.messageInputView, viewLifecycleOwner)

        //  There have two different mode in chat
        messageListViewModel.mode.observe(viewLifecycleOwner) { mode ->
            when (mode) {
                is MessageListViewModel.Mode.Thread -> {
                    messageListHeaderViewModel.setActiveThread(mode.parentMessage)
                    messageInputViewModel.setActiveThread(mode.parentMessage)
                }
                is MessageListViewModel.Mode.Normal -> {
                    messageListHeaderViewModel.resetThread()
                    messageInputViewModel.resetThread()
                }
            }

        }
        //  The option to be able to navigate back
        //  We need to override the back press handler for activity
        messageListViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state is MessageListViewModel.State.NavigateUp) {
                findNavController().navigateUp()
            }
        }
        //   back handler is just a current number fun
        //  in that fun we want to send an event
        //  and here we the back button press event
        //  we click on that back arrow then we send an event to our messageListViewModel
        val backHandler = {
            messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
        }
        binding.messageListHeaderView.setBackButtonClickListener(backHandler)
        //   import androidx.activity.addCallback
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backHandler()
        }
    }
}