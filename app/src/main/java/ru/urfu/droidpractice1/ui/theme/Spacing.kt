package ru.urfu.droidpractice1.ui.theme

import androidx.compose.ui.unit.dp

/** Шкала отступов экрана статьи, чтобы не разбрасывать «магические» dp по разметке. */
object Spacing {
    /** 4.dp — зазор между строкой и подписью к ней. */
    val tiny = 4.dp

    /** 8.dp — отступ внутри блока. */
    val small = 8.dp

    /** 12.dp — расстояние между соседними блоками статьи. */
    val medium = 12.dp

    /** 16.dp — поля экрана и внутренние поля карточек. */
    val large = 16.dp
}
