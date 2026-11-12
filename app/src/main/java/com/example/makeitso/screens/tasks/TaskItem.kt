/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.makeitso.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.makeitso.R.drawable as AppIcon
import com.example.makeitso.common.composable.DropdownContextMenu
import com.example.makeitso.common.ext.contextMenu
import com.example.makeitso.common.ext.hasDueDate
import com.example.makeitso.common.ext.hasDueTime
import com.example.makeitso.model.Priority
import com.example.makeitso.model.Task
import java.lang.StringBuilder

@Composable
@ExperimentalMaterialApi
fun TaskItem(
  task: Task,
  options: List<String>,
  onCheckChange: () -> Unit,
  onActionClick: (String) -> Unit
) {
  Card(
    backgroundColor = MaterialTheme.colors.surface,
    elevation = 3.dp,
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth().padding(12.dp),
    ) {
      Checkbox(
        checked = task.completed,
        onCheckedChange = { onCheckChange() }
      )

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.subtitle1.copy(
            textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
            color = if (task.completed) MaterialTheme.colors.onSurface.copy(alpha = 0.6f) else MaterialTheme.colors.onSurface
          )
        )
        val priorityEnum = Priority.getByName(task.priority)
        if (priorityEnum != Priority.None) {
          Spacer(modifier = Modifier.height(2.dp))
          PriorityBadge(priority = priorityEnum)
        }
        Spacer(modifier = Modifier.height(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
          Text(text = getDueDateAndTime(task), fontSize = 12.sp)
        }
      }

      if (task.flag) {
        Icon(
          painter = painterResource(AppIcon.ic_flag),
          tint = MaterialTheme.colors.secondary,
          contentDescription = "Flag",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
      }

      DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
    }
  }
}

@Composable
private fun PriorityBadge(priority: Priority) {
  val (color, text) = when (priority) {
    Priority.High -> Color(0xFFE53935) to "High"
    Priority.Medium -> Color(0xFFFB8C00) to "Medium"
    Priority.Low -> Color(0xFF43A047) to "Low"
    else -> Color.Transparent to ""
  }

  Surface(
    color = color.copy(alpha = 0.15f),
    shape = RoundedCornerShape(4.dp),
    modifier = Modifier.padding(top = 2.dp)
  ) {
    Text(
      text = text,
      color = color,
      style = MaterialTheme.typography.caption.copy(fontSize = 10.sp),
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
    )
  }
}

private fun getDueDateAndTime(task: Task): String {
  val stringBuilder = StringBuilder("")

  if (task.hasDueDate()) {
    stringBuilder.append(task.dueDate)
    stringBuilder.append(" ")
  }

  if (task.hasDueTime()) {
    stringBuilder.append("at ")
    stringBuilder.append(task.dueTime)
  }

  return stringBuilder.toString()
}
