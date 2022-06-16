package com.example.composeoverflowpagerindicator.ui

import android.os.Parcelable
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

private const val CIRCLE_SIZE = 6
private const val NUM_INDICATORS = 9

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverflowPagerIndicator(numPages: Int, currentPage: Int) {
    val visibleIndicator = rememberSaveable { VisibleIndicator(numPages, 0) }

    visibleIndicator.adjustVisiblePages(currentPage)

    LazyRow(
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(1 + NUM_INDICATORS + 1, key = {
            val position = it - 1
            visibleIndicator.getPage(position)
        }) {
            val position = it - 1
            AnimatedVisibility(
                visible = position in 0 until NUM_INDICATORS,
                enter = fadeIn() + expandHorizontally { width -> width },
                exit = fadeOut() + shrinkHorizontally { width -> -width }
            ) {
                IndicatorCircle(
                    checked = position == visibleIndicator.getPosition(currentPage),
                    circleScale = CircleScale.create(visibleIndicator, position),
                    modifier = Modifier.animateItemPlacement().animateContentSize()
                )
            }
        }
    }
}

@Composable
private fun IndicatorCircle(
    checked: Boolean, circleScale: CircleScale, modifier: Modifier = Modifier) {
    val color = if (checked) Color.Cyan else Color.Gray
    val size = CIRCLE_SIZE * circleScale.scale
    Box(modifier = modifier
        .padding(
            horizontal = 4.dp,
            vertical = ((CIRCLE_SIZE * CircleScale.LARGE.scale - size) / 2).dp)
        .size(size.dp)
        .clip(CircleShape)
        .background(color)
    )
}

@Parcelize
private class VisibleIndicator(
    private val numAllPages: Int,
    private var pageAtStartIndicator: Int
): Parcelable {
    val pageAtEndIndicator: Int
        get() = pageAtStartIndicator + NUM_INDICATORS - 1

    val isFirstPageVisible: Boolean
        get() = pageAtStartIndicator == 0

    val isLastPageVisible: Boolean
        get() = pageAtEndIndicator == numAllPages - 1

    fun getPosition(page: Int) = page - pageAtStartIndicator

    fun getPage(position: Int) = pageAtStartIndicator + position

    fun adjustVisiblePages(currentPage: Int) {
        when(Position.createOrNull(
            getPosition(currentPage)
        )) {
            Position.SECOND_START -> back()
            Position.SECOND_END -> forward()
            else -> {}
        }
    }

    fun back() {
        if (!isFirstPageVisible) {
            pageAtStartIndicator--
        }
    }

    fun forward() {
        if (!isLastPageVisible) {
            pageAtStartIndicator++
        }
    }

    enum class Position(val value: Int) {
        START(0),
        SECOND_START(1),
        SECOND_END(NUM_INDICATORS - 2),
        END(NUM_INDICATORS - 1);

        companion object {
            fun createOrNull(position: Int) = values().firstOrNull { it.value == position }
        }
    }
}

private enum class CircleScale(val scale: Float) {
    LARGE(1f),
    MEDIUM(2/3f),
    SMALL(1/3f),
    NONE(0f);

    companion object {
        fun create(visibleIndicator: VisibleIndicator, position: Int): CircleScale =
        when(IPosition.createOrNull(position)) {
            IPosition.START ->
                if (visibleIndicator.isFirstPageVisible) LARGE else SMALL
            IPosition.SECOND_START ->
                if (visibleIndicator.isFirstPageVisible) LARGE else MEDIUM
            IPosition.SECOND_END ->
                if (visibleIndicator.isLastPageVisible) LARGE else MEDIUM
            IPosition.END ->
                if (visibleIndicator.isLastPageVisible) LARGE else SMALL
            null ->
                if (position in 0 until NUM_INDICATORS) LARGE else NONE
        }
    }
}

private typealias IPosition = VisibleIndicator.Position