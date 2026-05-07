package my.com.syazli.outflow.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import my.com.syazli.outflow.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun LoadingDialog(isLoading: Boolean, onDismiss:() -> Unit) {

    if (!isLoading) return

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.money_icon))

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )

    ) {

        Box(Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.dark_grey)),
            contentAlignment = Alignment.Center

        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}