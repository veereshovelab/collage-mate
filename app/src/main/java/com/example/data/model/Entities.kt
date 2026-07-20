package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val collegeName: String,
    val studentId: String,
    val major: String,
    val points: Int = 100, // Starting balance for localized token collaboration
    val bio: String = "",
    val profilePictureUri: String? = null,
    val appearance: String = "System",
    val collegeCourse: String = "",
    val phoneNumber: String = "",
    val socialLink: String = ""
)

@Entity(tableName = "resource_materials")
data class ResourceMaterial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val courseCode: String, // CS101, MATH201
    val professor: String,
    val semester: String, // Fall 2026, Spring 2026
    val description: String,
    val fileType: String, // PDF, Study Guide, Exam Prep
    val uploaderEmail: String,
    val uploaderName: String,
    val collegeName: String,
    val priceInPoints: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "gigs")
data class Gig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // Tutoring, Lab Equipment, Study Group, Help, Other
    val description: String,
    val rewardPoints: Int,
    val requesterEmail: String,
    val requesterName: String,
    val contactInfo: String,
    val status: String = "OPEN", // OPEN, ACCEPTED, COMPLETED
    val helperEmail: String? = null,
    val helperName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "feed_posts")
data class FeedPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val collegeName: String,
    val authorName: String,
    val authorEmail: String,
    val content: String,
    val likesCount: Int = 0,
    val likedByEmails: String = "", // Comma-separated list of emails who liked
    val timestamp: Long = System.currentTimeMillis()
)
