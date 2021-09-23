package tw.tcnr02.chatapp.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

//That fun it won't crash it will normally navigate
fun NavController.navigateSafely(
    //Id resource
    @IdRes idRes: Int,
    arg: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
){
    //   first we want to get a reference to the actual action
    val action = currentDestination?.getAction(idRes) ?: graph.getAction(idRes)
    //  Check okay do we navigate to a fragment that is not the current Fragment
    //  if we navigate back from our ChannelFragment
    //  the navigation component for some reason doesn't really register (not sure)
    //  if we don't do this it seems like it doesn't register that we actually navigate it back
    //  it still thinks we are inside of the ChannelFragment but we are actually in the LoginFragment
    //  if we then try to navigate from the LoginFragment to the ChannelFragment
    //  the navigation component still think we're in the ChannelFragment
    if(action != null && currentDestination?.id != action.destinationId){
        navigate(idRes,arg,navOptions,navExtras)
    }
}