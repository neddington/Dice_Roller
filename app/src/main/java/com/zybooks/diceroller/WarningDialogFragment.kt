package com.zybooks.diceroller

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class WarningDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.disk_full)
            .setMessage(R.string.make_room)
            .setPositiveButton(R.string.yes, { dialog, id ->
                // User clicked Yes
            })
            .setNegativeButton(R.string.no, { dialog, id ->
                // User clicked No
            })
            .setNeutralButton(R.string.later, { dialog, id ->
                // User clicked Maybe later
            })
            .create()
    }
}