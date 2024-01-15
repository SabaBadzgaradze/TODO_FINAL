package com.portfolio.todo_mvvm.firebase

import android.util.Log
import retrofit2.Response

data class TaskModel(
    val id: Long,
    val title: String,
    val note: String,
    val date: Long,
    val isCompleted: Boolean
)

data class UserModel(
    val userId: String,
    val taskIds: List<String>
)

class FirebaseRepository {
    suspend fun getTasks(): Response<TaskModel> {
        return try {
            val response = Retrofit.api.getData("/tasks/") // Specify the appropriate endpoint URL
            if (response.isSuccessful) {
                // Data retrieval was successful
                Response.success(response.body())
            } else {
                // Handle unsuccessful response
                Response.error(response.errorBody(), response.raw())
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
            Response.error(null, null)
        }
    }

    suspend fun createTask(task: TaskModel){
//        Log.e("Saba", "createTask function")

//        return try {
//            val response = Retrofit.api.createTask(task)
////            if (response.isSuccessful) {
////                // Task creation was successful
////                Log.d("Body", response.body()!!.toString())
////                Response.success(response.body())
////            } else {
////                // Handle unsuccessful response
////                Response.error(response.errorBody(), response.raw())
////            }
//        } catch (e: Exception) {
//            Log.e("Saba", e.localizedMessage)
//            // Handle network or other exceptions
//            Response.error(null, null)
//        }

   }
}
