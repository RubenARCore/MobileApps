package com.ruben.dailymotivator

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class ResetWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val dao = AppDatabase.getDatabase(applicationContext).motivatorDao()
        
        // Reset habits
        val habits: List<Habit> = dao.getAllHabitsSync()
        habits.forEach { habit ->
            dao.updateHabit(habit.copy(isChecked = false))
        }
        
        // Schedule next reset
        schedule(applicationContext)
        
        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(currentDate)) {
                    add(Calendar.HOUR_OF_DAY, 24)
                }
            }

            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

            val dailyWorkRequest = OneTimeWorkRequestBuilder<ResetWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag("midnight_reset")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "midnight_reset",
                ExistingWorkPolicy.REPLACE,
                dailyWorkRequest
            )
        }
    }
}
