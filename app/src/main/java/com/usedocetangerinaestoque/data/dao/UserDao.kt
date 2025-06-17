package com.usedocetangerinaestoque.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usedocetangerinaestoque.data.entities.User

@Dao
interface UserDao {
    //TODO provavelmente apenas para teste
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE ID = :id")
    suspend fun getById(id: Long): User?

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email)")
    suspend fun existEmail(email: String): Boolean

    @Insert
    suspend fun add(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT EXISTS(SELECT email FROM user WHERE email = :email AND id != :excludeId)")
    suspend fun existOtherEmail(email: String, excludeId: Long): Boolean
}