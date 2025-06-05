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

package com.fairphone.spring.launcher.ui.component.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fairphone.spring.launcher.ui.FP6Preview
import com.fairphone.spring.launcher.ui.component.SearchBar
import com.fairphone.spring.launcher.ui.component.selector.SelectableItem
import com.fairphone.spring.launcher.ui.theme.SpringLauncherTheme
import com.fairphone.spring.launcher.util.fakeApp

@Composable
fun <T : SelectableItem> ItemSwitcherLayout(
    itemList: List<T>,
    selectedItems: List<T>,
    onItemClick: (T, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var filter: String by remember { mutableStateOf("") }

    val filteredItemList = if (filter.isEmpty()) {
        itemList
    } else {
        itemList.filter { it.name.contains(filter, ignoreCase = true) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(bottom = 20.dp)
    ) {
        SearchBar(
            query = filter,
            onQueryChange = { filter = it },
            modifier = Modifier.padding(horizontal = 20.dp)
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
                val isChecked = item.id in selectedItems.map { it.id }

                SwitcherListItem(
                    item = item,
                    isChecked = isChecked,
                    onClick = { value -> onItemClick(item, value) }
                )
            }
        }
    }


}

@Composable
@FP6Preview
fun ItemSwitcherLayout_Preview() {
    val context = LocalContext.current
    SpringLauncherTheme {
        ItemSwitcherLayout(
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
            onItemClick = {app, value ->},
        )
    }
}