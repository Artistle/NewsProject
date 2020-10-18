package com.example.foodcourt.ViewModel.FireBaseConfigure

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

//класс воркер для планирования асинхронной работы с FB
class FirebaseWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.d("TAG", "Performing long running task in scheduled job")

        return ListenableWorker.Result.success()
    }
}