/*
 * Copyright © 2014-2020 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.zeapo.pwdstore.git

import android.app.Activity
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zeapo.pwdstore.R
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import java.io.File

/**
 * Creates a new git operation
 *
 * @param fileDir the git working tree directory
 * @param callingActivity the calling activity
 */
class PullOperation(fileDir: File, callingActivity: Activity) : GitOperation(fileDir, callingActivity) {

    /**
     * Sets the commands required to do a git pull on the repository. The [rebase] parameter decides
     * whether the pull will be of type `git pull origin --rebase` or `git pull origin --no-rebase`.
     *
     * @return An instance of [PullOperation]
     */
    fun setCommand(rebase: Boolean): PullOperation {
        this.command = Git(repository)
            .pull()
            .setRebase(rebase)
            .setRemote("origin")
        return this
    }

    override fun execute() {
        (this.command as? PullCommand)?.setCredentialsProvider(this.provider)
        GitAsyncTask(callingActivity, false, this, Intent()).execute(this.command)
    }

    override fun onError(err: Exception) {
        super.onError(err)
        MaterialAlertDialogBuilder(callingActivity)
            .setTitle(callingActivity.resources.getString(R.string.jgit_error_dialog_title))
            .setMessage("Error occurred during the pull operation, " +
                callingActivity.resources.getString(R.string.jgit_error_dialog_text) +
                err.message +
                "\nPlease check the FAQ for possible reasons why this error might occur.")
            .setPositiveButton(callingActivity.resources.getString(R.string.dialog_ok)) { _, _ -> callingActivity.finish() }
            .show()
    }
}
