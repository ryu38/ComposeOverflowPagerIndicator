package com.example.composeoverflowpagerindicator.ui.old

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun OverflowPagerIndicator2(numPages: Int, currentPage: Int) {
    val visibleIndicator = rememberSaveable { VisibleIndicator(numPages, 0) }

    visibleIndicator.adjustVisiblePages(currentPage)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (page in 0 until numPages) {
            AnimatedVisibility(
                visible = visibleIndicator.isPageVisible(page)
            ) {
                IndicatorCircle(
                    checked = page == currentPage,
                    circleScale = CircleScale.create(
                        visibleIndicator, visibleIndicator.getPositionByPage(page)
                    )
                )
            }
        }
    }
}

@Composable
private fun IndicatorCircle(checked: Boolean, circleScale: CircleScale) {
    val color = if (checked) Color.Cyan else Color.Gray
    val size = CIRCLE_SIZE * circleScale.scale
    Box(modifier = Modifier
        .padding(horizontal = 4.dp)
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
    private val pageAtEndIndicator: Int
        get() = pageAtStartIndicator + NUM_INDICATORS - 1

    val isFirstPageVisible: Boolean
        get() = pageAtStartIndicator == 0

    val isLastPageVisible: Boolean
        get() = pageAtEndIndicator == numAllPages - 1

    fun isPageVisible(page: Int) = page in pageAtStartIndicator..pageAtEndIndicator

    fun getPositionByPage(page: Int) = page - pageAtStartIndicator

    fun adjustVisiblePages(currentPage: Int) {
        when(Position.createOrNull(
            getPositionByPage(currentPage)
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
    SMALL(1/3f);

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
                if (position in 0 until NUM_INDICATORS) LARGE else SMALL
        }
    }
}

private typealias IPosition = VisibleIndicator.Position