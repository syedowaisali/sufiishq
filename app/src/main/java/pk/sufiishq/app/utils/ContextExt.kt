package pk.sufiishq.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.widget.Toast

fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasAnyOneTransport(TRANSPORT_CELLULAR, TRANSPORT_WIFI, TRANSPORT_ETHERNET)
        ?: false
}

fun NetworkCapabilities.hasAnyOneTransport(vararg transport: Int): Boolean {
    var value = false
    run lit@{
        transport.forEach {
            if (hasTransport(it)) {
                value = true
                return@lit
            }
        }
    }
    return value
}

fun Context.dpToPx(dp: Float) = dp * resources.displayMetrics.density

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
