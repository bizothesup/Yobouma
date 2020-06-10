package net.mbs.ybma.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import net.mbs.ybma.R

class CustomDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress_bar, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            return dialog
        }
    }
}
