package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R

/**
 * Reusable App Logo component with optimized scaling.
 */
@Composable
fun CampusLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = "Campusdeck Logo"
) {
    Image(
        painter = painterResource(id = R.drawable.app_logo),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}
