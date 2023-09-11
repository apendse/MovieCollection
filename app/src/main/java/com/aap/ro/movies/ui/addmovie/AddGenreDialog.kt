package com.aap.ro.movies.ui.addmovie

import android.content.Context
import android.os.Bundle
import com.aap.ro.movies.data.Genre
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddGenreDialog(context: Context): BottomSheetDialogFragment() {

    companion object {
        fun newInstance(context: Context, selected: List<Genre>, available: List<Genre> ): AddGenreDialog {
            val bundle = Bundle()

            return AddGenreDialog(context).apply {
                arguments = bundle
            }
        }
    }
}