package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.User
import com.example.data.model.ResourceMaterial
import com.example.data.model.Gig
import com.example.data.model.FeedPost
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<User?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmailSync(email: String): User?

    @Query("UPDATE users SET points = :points WHERE email = :email")
    suspend fun updateUserPoints(email: String, points: Int)
}

@Dao
interface ResourceDao {
    @Query("SELECT * FROM resource_materials ORDER BY timestamp DESC")
    fun getAllResources(): Flow<List<ResourceMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(material: ResourceMaterial)

    @Query("SELECT * FROM resource_materials WHERE title LIKE :query OR courseCode LIKE :query OR professor LIKE :query ORDER BY timestamp DESC")
    fun searchResources(query: String): Flow<List<ResourceMaterial>>

    @Query("SELECT * FROM resource_materials WHERE collegeName = :collegeName ORDER BY timestamp DESC")
    fun getResourcesByCollege(collegeName: String): Flow<List<ResourceMaterial>>

    @Query("SELECT DISTINCT collegeName FROM resource_materials UNION SELECT DISTINCT collegeName FROM users")
    fun getUniqueColleges(): Flow<List<String>>

    @Query("DELETE FROM resource_materials WHERE id = :id")
    suspend fun deleteResource(id: Int)
}

@Dao
interface GigDao {
    @Query("SELECT * FROM gigs ORDER BY timestamp DESC")
    fun getAllGigs(): Flow<List<Gig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGig(gig: Gig)

    @Query("UPDATE gigs SET status = :status, helperEmail = :helperEmail, helperName = :helperName WHERE id = :id")
    suspend fun updateGigStatus(id: Int, status: String, helperEmail: String?, helperName: String?)

    @Query("DELETE FROM gigs WHERE id = :id")
    suspend fun deleteGig(id: Int)
}

@Dao
interface PostDao {
    @Query("SELECT * FROM feed_posts WHERE collegeName = :collegeName ORDER BY timestamp DESC")
    fun getPostsForCollege(collegeName: String): Flow<List<FeedPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: FeedPost)

    @Query("UPDATE feed_posts SET likesCount = :likesCount, likedByEmails = :likedByEmails WHERE id = :id")
    suspend fun updatePostLikes(id: Int, likesCount: Int, likedByEmails: String)
}
