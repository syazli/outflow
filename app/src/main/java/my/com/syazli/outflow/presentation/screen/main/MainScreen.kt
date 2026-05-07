package my.com.syazli.outflow.presentation.screen.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import my.com.syazli.outflow.R
import kotlin.math.absoluteValue

@Composable
fun MainScreen(onGetStartedClick: () -> Unit) {

    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(900))
    }

    Box(Modifier.fillMaxSize().background(colorResource(R.color.dark))) {
        Box(Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.top_glow_height))
            .background(Brush.verticalGradient(listOf(colorResource(R.color.green).copy(0.08f), Color.Transparent))))

        Column(Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(dimensionResource(R.dimen.spacing_xlarge))
            .alpha(alpha.value),
            Arrangement.Center,
            Alignment.Start) {
                Text(stringResource(R.string.main_label), color = colorResource(R.color.green), fontSize = dimensionResource(R.dimen.text_label).value.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)

                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

                Text(stringResource(R.string.app_name), color = colorResource(R.color.white), fontSize = dimensionResource(R.dimen.text_title).value.sp, fontWeight = FontWeight.Black, lineHeight = dimensionResource(R.dimen.text_title).value.sp, letterSpacing = (-2).sp)
                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

                Box(Modifier
                    .height(dimensionResource(R.dimen.accent_line_height))
                    .fillMaxWidth(0.12f)
                    .background(colorResource(R.color.green)))

                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))
                Text(stringResource(R.string.main_desc), color = colorResource(R.color.light_grey), fontSize = dimensionResource(R.dimen.text_body).value.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal)

                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_xxlarge)))

                Button(onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.green),
                            contentColor = colorResource(R.color.dark)
                        ),
                        shape =  RoundedCornerShape(dimensionResource(R.dimen.button_radius))) {
                            Text(
                                stringResource(R.string.main_get_started),
                                fontSize = dimensionResource(R.dimen.text_button).value.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                        Text(
                            stringResource(R.string.main_privacy),
                            color = colorResource(R.color.light_grey).copy(0.5f),
                            fontSize = dimensionResource(R.dimen.text_caption).value.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
            }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(onGetStartedClick = {})
}