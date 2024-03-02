package `in`.technowolf.ipscanner.utils

/*
 * Copyright (C) 2014 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process

/**
 * Process Phoenix facilitates restarting your application process. This should only be used for
 * things like fundamental state changes in your debug builds (e.g., changing from staging to
 * production).
 *
 *
 * Trigger process recreation by calling [.triggerRebirth] with a [Context] instance.
 */
class ProcessPhoenix : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Process.killProcess(
            intent.getIntExtra(
                KEY_MAIN_PROCESS_PID,
                -1
            )
        ) // Kill original main process
        val intents = intent.getParcelableArrayListExtra<Intent>(KEY_RESTART_INTENTS)
        startActivities(intents!!.toTypedArray())
        finish()
        Runtime.getRuntime().exit(0) // Kill kill kill!
    }

    companion object {
        private const val KEY_RESTART_INTENTS = "phoenix_restart_intents"
        private const val KEY_MAIN_PROCESS_PID = "phoenix_main_process_pid"

        /**
         * Call to restart the application process using the [default][Intent.CATEGORY_DEFAULT]
         * activity as an intent.
         *
         *
         * Behavior of the current process after invoking this method is undefined.
         */
        fun triggerRebirth(context: Context) {
            triggerRebirth(context, getRestartIntent(context))
        }

        /**
         * Call to restart the application process using the specified intents.
         *
         *
         * Behavior of the current process after invoking this method is undefined.
         */
        fun triggerRebirth(
            context: Context,
            vararg nextIntents: Intent
        ) {
            if (nextIntents.isEmpty()) {
                throw IllegalArgumentException("intents cannot be empty")
            }
            // create a new task for the first activity.
            nextIntents[0].addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            val intent = Intent(context, ProcessPhoenix::class.java)
            // In case we are called with non-Activity context.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putParcelableArrayListExtra(
                KEY_RESTART_INTENTS,
                ArrayList(listOf(*nextIntents))
            )
            intent.putExtra(KEY_MAIN_PROCESS_PID, Process.myPid())
            context.startActivity(intent)
        }

        private fun getRestartIntent(context: Context): Intent {
            val packageName = context.packageName
            val defaultIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (defaultIntent != null) {
                return defaultIntent
            }
            throw IllegalStateException(
                "Unable to determine default activity for " +
                    packageName +
                    ". Does an activity specify the DEFAULT category in its intent filter?"
            )
        }

        /**
         * Checks if the current process is a temporary Phoenix Process.
         * This can be used to avoid initialisation of unused resources or to prevent running code that
         * is not multi-process ready.
         *
         * @return true if the current process is a temporary Phoenix Process
         */
        fun isPhoenixProcess(context: Context): Boolean {
            val currentPid = Process.myPid()
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val runningProcesses = manager.runningAppProcesses
            if (runningProcesses != null) {
                for (processInfo: RunningAppProcessInfo in runningProcesses) {
                    if (processInfo.pid == currentPid &&
                        processInfo.processName.endsWith(":phoenix")
                    ) {
                        return true
                    }
                }
            }
            return false
        }
    }
}
