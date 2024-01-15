package com.portfolio.todo_mvvm.firebase

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

object Retrofit {
    private const val BASE_URL = "https://authentication-8a3c5-default-rtdb.firebaseio.com/"

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api = retrofit.create(Service::class.java)
}

interface Service {
    @GET("tasks.json")
    suspend fun getData(s: String): Response<TaskModel>

    @PUT("tasks.json")
    suspend fun createTask(@Body task: TaskModel)
}


/*
*
*
* user -> groupTasks -> task1Id, task2Id
* user1 -> groupTasks -> task1Id
*
* tasks -> task1Id -> taskinfo, task2 task1
*
* */