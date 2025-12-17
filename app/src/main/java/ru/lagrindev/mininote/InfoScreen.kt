package ru.lagrindev.mininote

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun InfoScreen() {

    val versionName = BuildConfig.VERSION_NAME
    val versionCode = BuildConfig.VERSION_CODE
    val buildType = if (BuildConfig.DEBUG) "DEBUG" else "RELEASE"

    val currentYear = java.util.Calendar
        .getInstance()
        .get(java.util.Calendar.YEAR)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Мини Заметки",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Простое и быстрое приложение для заметок",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Версия $versionName ($versionCode)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        AssistChip(
            onClick = {},
            label = { Text(buildType) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (BuildConfig.DEBUG)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.tertiaryContainer,
                labelColor = if (BuildConfig.DEBUG)
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onTertiaryContainer
            )
        )

        InfoLinkCard(
            title = "GitHub",
            subtitle = "Исходный код проекта",
            url = "https://github.com/lagrindev"
        )

        InfoLinkCard(
            title = "Telegram",
            subtitle = "Мой Telegram-канал",
            url = "https://t.me/devlagrin"
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.6f),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = "© LagrinDev $currentYear",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun InfoLinkCard(
    title: String,
    subtitle: String,
    url: String
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, url.toUri())
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
