/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.ui.component.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.R
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.component.PrimaryButton
import com.fairphone.spring.launcher.ui.component.SearchBar
import com.fairphone.spring.launcher.ui.theme.FairphoneTypography
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun <T : SelectableItem> ItemSelectorLayout(
    itemList: List<T>,
    selectedItems: List<T>,
    maxItemCount: Int,
    showConfirmButton: Boolean,
    showItemCounter: Boolean,
    showEmptyItemSelectedError: Boolean,
    showMaxItemSelectedError: Boolean,
    confirmButtonTextResource: Int,
    onItemClick: (T) -> Unit,
    onItemDeselected: (T) -> Unit,
    onConfirmItemSelection: () -> Unit,
    modifier: Modifier = Modifier,
    maxItemCountErrorText: String,
    emptyItemSelectedErrorText: String,
) {
    var filter: String by remember { mutableStateOf("") }

    val filteredItemList = if (filter.isEmpty()) {
        itemList
    } else {
        itemList.filter { it.name.contains(filter, ignoreCase = true) }
    }


    Box(modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            SearchBar(
                query = filter,
                onQueryChange = { filter = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            SelectedItemsRow(
                selectedItems = selectedItems,
                onDeletedClick = onItemDeselected,
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .clip(RoundedCornerShape(size = 12.dp))
            ) {
                items(
                    count = filteredItemList.size,
                    contentType = { index -> filteredItemList[index] },
                ) { index ->
                    val item = filteredItemList[index]
                    val isSelected = item in selectedItems

                    SelectableListItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onItemClick(item) },
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
        ) {
            when {
                showMaxItemSelectedError -> {
                    ErrorView(errorText = maxItemCountErrorText)
                }

                showEmptyItemSelectedError -> {
                    ErrorView(errorText = emptyItemSelectedErrorText)
                }

                showItemCounter -> {
                    ItemCounter(
                        selectedItemCount = selectedItems.size,
                        maxItemCount = 5,
                    )
                }
            }

            PrimaryButton(
                text = stringResource(confirmButtonTextResource),
                onClick = onConfirmItemSelection,
                visible = showConfirmButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun ErrorView(
    errorText: String,
    modifier: Modifier = Modifier,
) {
    val bgGradientColors = remember {
        listOf(
            Color(0xFFFF4E4E),
            Color(0xFFFF1E1E),
        )
    }
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .background(
                brush = Brush.horizontalGradient(bgGradientColors),
                shape = RoundedCornerShape(size = 100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = errorText,
            style = FairphoneTypography.LabelMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun <T : SelectableItem> SelectedItemsRow(
    selectedItems: List<T>,
    onDeletedClick: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedItems.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .width(332.dp)
                .height(93.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                )
        ) {
            items(selectedItems.size) { index ->
                val item = selectedItems[index]

                SelectedListItem(
                    item = item,
                    onDeleteClick = { onDeletedClick(item) },
                )
            }
        }
    }
}

@Composable
fun ItemCounter(
    selectedItemCount: Int,
    maxItemCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.visible_apps_count, selectedItemCount, maxItemCount),
            style = FairphoneTypography.LabelMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@FP6Preview
fun ItemSelectorLayout_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        ItemSelectorLayout(
            itemList = listOf(
                context.fakeApp("test"),
                context.fakeApp("test1"),
                context.fakeApp("test2"),
                context.fakeApp("test3"),
                context.fakeApp("test4"),
                context.fakeApp("test5"),
            ),
            selectedItems = listOf(
                context.fakeApp("test"),
                context.fakeApp("test4"),
            ),
            maxItemCount = 5,
            showConfirmButton = true,
            showItemCounter = false,
            showEmptyItemSelectedError = false,
            showMaxItemSelectedError = true,
            confirmButtonTextResource = R.string.bt_confirm,
            onItemClick = {},
            onItemDeselected = {},
            onConfirmItemSelection = {},
            maxItemCountErrorText = "Max item count error",
            emptyItemSelectedErrorText = "Empty item selected error",
        )
    }
}