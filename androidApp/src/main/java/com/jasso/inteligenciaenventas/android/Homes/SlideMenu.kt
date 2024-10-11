package com.jasso.inteligenciaenventas.android.Homes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SlideMenu(isVisible: Boolean, onDismiss: () -> Unit, content: @Composable () -> Unit) {
    val slideOffset = animateFloatAsState(targetValue = if (isVisible) 0f else -300f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .offset(x = slideOffset.value.dp)
            .width(250.dp)
            .background(Color.Gray)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Filled.Close, contentDescription = "Close menu")
            }
            content()
        }
    }
}