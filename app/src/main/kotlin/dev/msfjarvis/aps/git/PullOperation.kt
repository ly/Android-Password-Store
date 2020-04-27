/*
 * Copyright © 2014-2020 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
package dev.msfjarvis.aps.git

import android.app.Activity
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.msfjarvis.aps.R
import dev.msfjarvis.aps.ng.git.PullOperation
import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand

/**
 * Creates a new git operation
 *
 * @param fileDir the git working tree directory
 * @param callingActivity the calling activity
 */
class PullOperation(fileDir: File, callingActivity: Activity) : GitOperation(fileDir, callingActivity) {

    /**
     * Sets the command
     *
     * @return the current object
     */
    fun setCommand(): PullOperation {
        this.command = Git(repository)
                .pull()
                .setRebase(true)
                .setRemote("origin")
        return this
    }

    override fun execute() {
        (this.command as? PullCommand)?.setCredentialsProvider(this.provider)
        GitAsyncTask(callingActivity, false, this, Intent()).execute(this.command)
    }

    override fun onError(errorMessage: String) {
        super.onError(errorMessage)
        MaterialAlertDialogBuilder(callingActivity)
                .setTitle(callingActivity.resources.getString(R.string.jgit_error_dialog_title))
                .setMessage("Error occured during the pull operation, " +
                        callingActivity.resources.getString(R.string.jgit_error_dialog_text) +
                        errorMessage +
                        "\nPlease check the FAQ for possible reasons why this error might occur.")
                .setPositiveButton(callingActivity.resources.getString(R.string.dialog_ok)) { _, _ -> callingActivity.finish() }
                .show()
    }
}
