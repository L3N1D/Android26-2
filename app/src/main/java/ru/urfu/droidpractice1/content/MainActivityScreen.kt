@file:OptIn(ExperimentalMaterial3Api::class)

package ru.urfu.droidpractice1.content

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.SecondActivity
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme
import ru.urfu.droidpractice1.ui.theme.Spacing

private val ReadColor = Color(0xFF2E7D32)
private val PlaceholderPainter = ColorPainter(Color(0xFFE0E0E0))

@Composable
fun MainActivityScreen() {
    val context = LocalContext.current
    // rememberSaveable переживает и поворот экрана, и уход на другую активити
    var likes by rememberSaveable { mutableIntStateOf(0) }
    var dislikes by rememberSaveable { mutableIntStateOf(0) }
    var isNextArticleRead by rememberSaveable { mutableStateOf(false) }

    val nextArticleLauncher = rememberLauncherForActivityResult(SecondActivity.OpenArticle()) { isRead ->
        if (isRead != null) isNextArticleRead = isRead
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val shareText = articleShareText(context)

    DroidPractice1Theme(dynamicColor = false) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.article_title),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    actions = {
                        IconButton(onClick = { shareArticle(context, shareText) }) {
                            Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.share))
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.large, vertical = Spacing.small),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium),
            ) {
                ArticleBody()

                HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.small))

                Reactions(
                    likes = likes,
                    dislikes = dislikes,
                    onLike = { likes++ },
                    onDislike = { dislikes++ },
                )

                NextArticleCard(
                    isRead = isNextArticleRead,
                    onClick = { nextArticleLauncher.launch(isNextArticleRead) },
                )
                Button(
                    onClick = { nextArticleLauncher.launch(isNextArticleRead) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        stringResource(
                            if (isNextArticleRead) R.string.reread_next_article else R.string.go_to_next_article,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(Spacing.large))
            }
        }
    }
}

@Composable
private fun ArticleBody() {
    AsyncImage(
        model = stringResource(R.string.first_image_url),
        contentDescription = stringResource(R.string.first_image_caption),
        contentScale = ContentScale.Crop,
        placeholder = PlaceholderPainter,
        error = PlaceholderPainter,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp)),
    )
    Text(
        text = stringResource(R.string.first_image_caption),
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
        modifier = Modifier.fillMaxWidth(),
    )

    Text(
        text = stringResource(R.string.first_category),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 2.sp,
    )
    Text(
        text = stringResource(R.string.first_title),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
    Text(
        text = stringResource(R.string.first_meta),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
        text = stringResource(R.string.first_lead),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )

    SectionHeading(stringResource(R.string.first_heading_1))
    Paragraph(stringResource(R.string.first_paragraph_1))
    Paragraph(stringResource(R.string.first_paragraph_2))

    Quote(
        text = stringResource(R.string.first_quote),
        author = stringResource(R.string.first_quote_author),
    )

    SectionHeading(stringResource(R.string.first_heading_2))
    val paragraph = stringResource(R.string.first_paragraph_3)
    Text(
        text = buildAnnotatedString {
            val highlights = listOf("6,5 метра", "18 шестиугольных сегментов", "золота")
            var cursor = 0
            highlights.forEach { word ->
                val start = paragraph.indexOf(word, cursor)
                if (start < 0) return@forEach
                append(paragraph.substring(cursor, start))
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))) { append(word) }
                cursor = start + word.length
            }
            append(paragraph.substring(cursor))
        },
        style = MaterialTheme.typography.bodyLarge,
    )

    FactsCard()
    Paragraph(stringResource(R.string.first_paragraph_4))
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = Spacing.small),
    )
}

@Composable
private fun Paragraph(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun Quote(text: String, author: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = Spacing.tiny),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp)),
        )
        Column(modifier = Modifier.padding(start = Spacing.medium)) {
            Text(
                text = text,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            )
            Text(
                text = author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.tiny),
            )
        }
    }
}

@Composable
private fun FactsCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(Spacing.large), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            Text(
                text = stringResource(R.string.first_facts_title).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp,
            )
            listOf(
                R.string.first_fact_1,
                R.string.first_fact_2,
                R.string.first_fact_3,
                R.string.first_fact_4,
            ).forEach { fact ->
                Text(
                    text = "•  " + stringResource(fact),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun Reactions(likes: Int, dislikes: Int, onLike: () -> Unit, onDislike: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.reactions_question),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.large),
            modifier = Modifier.padding(top = Spacing.small),
        ) {
            ReactionButton(R.drawable.ic_thumb_up, R.string.like, likes, Color(0xFF2E7D32), onLike)
            ReactionButton(R.drawable.ic_thumb_down, R.string.dislike, dislikes, Color(0xFFC62828), onDislike)
        }
    }
}

@Composable
private fun ReactionButton(@DrawableRes icon: Int, description: Int, count: Int, tint: Color, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(description),
            tint = tint,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(Spacing.small))
        Text(text = count.toString(), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun NextArticleCard(isRead: Boolean, onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.next_article_label),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
        ),
    ) {
        Row(modifier = Modifier.padding(Spacing.medium), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = stringResource(R.string.second_image_url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = PlaceholderPainter,
                error = PlaceholderPainter,
                alpha = if (isRead) 0.5f else 1f,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )
            Column(modifier = Modifier.padding(start = Spacing.medium)) {
                Text(
                    text = stringResource(R.string.second_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(Spacing.small))
                if (isRead) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = ReadColor,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = stringResource(R.string.next_article_read),
                            color = ReadColor,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = Spacing.tiny),
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.next_article_unread),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

private fun articleShareText(context: Context): String = listOf(
    R.string.first_title,
    R.string.first_lead,
    R.string.first_heading_1,
    R.string.first_paragraph_1,
    R.string.first_paragraph_2,
    R.string.first_heading_2,
    R.string.first_paragraph_3,
    R.string.first_paragraph_4,
).joinToString(separator = "\n\n") { context.getString(it) }

private fun shareArticle(context: Context, text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.first_title))
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_chooser_title)))
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}
