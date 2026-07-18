package com.example.data.repository

import com.example.data.db.UserDao
import com.example.data.db.ResourceDao
import com.example.data.db.GigDao
import com.example.data.db.PostDao
import com.example.data.model.User
import com.example.data.model.ResourceMaterial
import com.example.data.model.Gig
import com.example.data.model.FeedPost
import kotlinx.coroutines.flow.Flow

class CampusRepository(
    private val userDao: UserDao,
    private val resourceDao: ResourceDao,
    private val gigDao: GigDao,
    private val postDao: PostDao
) {
    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    suspend fun getUserByEmailSync(email: String): User? = userDao.getUserByEmailSync(email)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun updateUserPoints(email: String, points: Int) = userDao.updateUserPoints(email, points)

    fun getAllResources(): Flow<List<ResourceMaterial>> = resourceDao.getAllResources()

    fun searchResources(query: String): Flow<List<ResourceMaterial>> {
        return if (query.isBlank()) {
            resourceDao.getAllResources()
        } else {
            resourceDao.searchResources("%$query%")
        }
    }

    suspend fun insertResource(material: ResourceMaterial) = resourceDao.insertResource(material)

    suspend fun deleteResource(id: Int) = resourceDao.deleteResource(id)

    fun getAllGigs(): Flow<List<Gig>> = gigDao.getAllGigs()

    suspend fun insertGig(gig: Gig) = gigDao.insertGig(gig)

    suspend fun updateGigStatus(id: Int, status: String, helperEmail: String?, helperName: String?) {
        gigDao.updateGigStatus(id, status, helperEmail, helperName)
    }

    suspend fun deleteGig(id: Int) = gigDao.deleteGig(id)

    fun getPostsForCollege(collegeName: String): Flow<List<FeedPost>> = postDao.getPostsForCollege(collegeName)

    suspend fun insertPost(post: FeedPost) = postDao.insertPost(post)

    suspend fun updatePostLikes(id: Int, likesCount: Int, likedByEmails: String) {
        postDao.updatePostLikes(id, likesCount, likedByEmails)
    }
}
