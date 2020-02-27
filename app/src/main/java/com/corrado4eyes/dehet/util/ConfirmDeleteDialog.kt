package com.corrado4eyes.dehet.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.adapters.HistoryEntryEvent

class ConfirmDeleteDialog(private val context: Context,
                          private val onDeleteEntry: HistoryEntryEvent) {

    fun build(title: String,
              message: String,
              entry: HistoryEntry): AlertDialog {
        return AlertDialog.Builder(context)
            .setIcon(R.drawable.delete_icon)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, i ->
                onDeleteEntry.onDeleteButtonClicked(entry)
            }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
            .create()
    }

}