package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.User
import com.example.data.model.ResourceMaterial
import com.example.data.model.Gig
import com.example.data.model.FeedPost
import com.example.data.repository.CampusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CampusViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = CampusRepository(
        db.userDao(),
        db.resourceDao(),
        db.gigDao(),
        db.postDao()
    )

    // States
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _resources = MutableStateFlow<List<ResourceMaterial>>(emptyList())
    val resources: StateFlow<List<ResourceMaterial>> = _resources.asStateFlow()

    private val _gigs = MutableStateFlow<List<Gig>>(emptyList())
    val gigs: StateFlow<List<Gig>> = _gigs.asStateFlow()

    private val _feedPosts = MutableStateFlow<List<FeedPost>>(emptyList())
    val feedPosts: StateFlow<List<FeedPost>> = _feedPosts.asStateFlow()

    private val _unlockedResourceIds = MutableStateFlow<Set<Int>>(emptySet())
    val unlockedResourceIds: StateFlow<Set<Int>> = _unlockedResourceIds.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()

    init {
        // Observe search query and update resources dynamically
        viewModelScope.launch {
            _searchQuery.collect { query ->
                repository.searchResources(query).collect { list ->
                    _resources.value = list
                }
            }
        }

        // Observe gigs
        viewModelScope.launch {
            repository.getAllGigs().collect { list ->
                _gigs.value = list
            }
        }

        // Check and seed DB if empty
        viewModelScope.launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingResources = repository.getAllResources().first()
        if (existingResources.isEmpty()) {
            // Pre-register some helpful mock accounts
            val alice = User("alice.adams@stateu.edu", "Alice Adams", "State University", "S10405", "Computer Science", 150)
            val bob = User("bob.baker@stateu.edu", "Bob Baker", "State University", "S10982", "Bioengineering", 120)
            val charlie = User("charlie.chen@stateu.edu", "Charlie Chen", "State University", "S11342", "Mechanical Engineering", 80)
            val admin = User("dean.office@stateu.edu", "Dean's Office", "State University", "FAC001", "Administration", 9999)

            repository.insertUser(alice)
            repository.insertUser(bob)
            repository.insertUser(charlie)
            repository.insertUser(admin)

            // Seed resource materials
            repository.insertResource(
                ResourceMaterial(
                    title = "CS 201: Data Structures Exam Review Sheet",
                    courseCode = "CS201",
                    professor = "Dr. Evans",
                    semester = "Spring 2026",
                    description = "Comprehensive 4-page summary of Red-Black Trees, Heaps, and Graph algorithms with practice problems and code snippets.",
                    fileType = "PDF Study Guide",
                    uploaderEmail = "alice.adams@stateu.edu",
                    uploaderName = "Alice Adams",
                    priceInPoints = 15
                )
            )

            repository.insertResource(
                ResourceMaterial(
                    title = "MATH 302: Linear Algebra Final Summary Notes",
                    courseCode = "MATH302",
                    professor = "Prof. Kowalski",
                    semester = "Fall 2025",
                    description = "Extensive review notes covering Eigenvalues, Diagonalization, and Orthogonal projections. Highly detailed hand-drawn diagrams.",
                    fileType = "PDF Lecture Notes",
                    uploaderEmail = "bob.baker@stateu.edu",
                    uploaderName = "Bob Baker",
                    priceInPoints = 25
                )
            )

            repository.insertResource(
                ResourceMaterial(
                    title = "CHEM 101: Organic Chemistry Lab Techniques",
                    courseCode = "CHEM101",
                    professor = "Dr. Al-Hassan",
                    semester = "Spring 2026",
                    description = "Quick reference guide for distillation, extraction, and chromatography. Perfect for pre-lab quizzes.",
                    fileType = "Cheat Sheet",
                    uploaderEmail = "charlie.chen@stateu.edu",
                    uploaderName = "Charlie Chen",
                    priceInPoints = 10
                )
            )

            // Seed Gigs
            repository.insertGig(
                Gig(
                    title = "Need peer-tutoring for Calculus III",
                    category = "Tutoring",
                    description = "Struggling with partial derivatives and Lagrange multipliers before the midterm on Tuesday. Happy to meet at the library.",
                    rewardPoints = 30,
                    requesterEmail = "bob.baker@stateu.edu",
                    requesterName = "Bob Baker",
                    contactInfo = "Library Study Room 4, BobB on Discord",
                    status = "OPEN"
                )
            )

            repository.insertGig(
                Gig(
                    title = "Borrowing Arduino Uno & Breadboard",
                    category = "Lab Equipment",
                    description = "I need to test an embedded sensor layout for class today. Can return it by 5:00 PM tonight. Will credit 15 points!",
                    rewardPoints = 15,
                    requesterEmail = "charlie.chen@stateu.edu",
                    requesterName = "Charlie Chen",
                    contactInfo = "Engineering Block B, Room 204",
                    status = "OPEN"
                )
            )

            repository.insertGig(
                Gig(
                    title = "English 202 Research Paper Proofreading",
                    category = "Study Help",
                    description = "Need a quick read of my 8-page literature analysis on Shakespeare's Tempest. Grammatical and flow check only. Rewarding 20 points.",
                    rewardPoints = 20,
                    requesterEmail = "alice.adams@stateu.edu",
                    requesterName = "Alice Adams",
                    contactInfo = "Text or WhatsApp at 555-0199",
                    status = "OPEN"
                )
            )

            // Seed feed posts
            repository.insertPost(
                FeedPost(
                    collegeName = "State University",
                    authorName = "Dean's Office",
                    authorEmail = "dean.office@stateu.edu",
                    content = "Welcome to ClgMate! This is your localized collegiate board. Reminder: annual Campus Hackathon registration closes tomorrow at 5:00 PM. Team up and register!",
                    likesCount = 24,
                    likedByEmails = "alice.adams@stateu.edu,bob.baker@stateu.edu"
                )
            )

            repository.insertPost(
                FeedPost(
                    collegeName = "State University",
                    authorName = "Alice Adams",
                    authorEmail = "alice.adams@stateu.edu",
                    content = "To whoever left their blue Hydroflask in the Student Union basement study room yesterday—I gave it to the front desk lost-and-found! Hope you find it.",
                    likesCount = 8,
                    likedByEmails = "bob.baker@stateu.edu"
                )
            )

            // Seed initial assignments
            repository.insertResource(
                ResourceMaterial(
                    title = "CS 102: Data Structures Lab 3 - Tree Traversal",
                    courseCode = "CS102",
                    professor = "Dr. Miller",
                    semester = "Spring 2026",
                    description = "Reference solution and explanatory notes for the binary search tree traversal lab. Includes edge case analysis.",
                    fileType = "Assignment",
                    uploaderEmail = "alice.adams@stateu.edu",
                    uploaderName = "Alice Adams",
                    priceInPoints = 20
                )
            )

            repository.insertResource(
                ResourceMaterial(
                    title = "ECON 101: Macroeconomics Problem Set #2",
                    courseCode = "ECON101",
                    professor = "Prof. Smith",
                    semester = "Spring 2026",
                    description = "Detailed breakdown of the IS-LM model problem set. Step-by-step derivation of equilibrium points.",
                    fileType = "Assignment",
                    uploaderEmail = "charlie.chen@stateu.edu",
                    uploaderName = "Charlie Chen",
                    priceInPoints = 15
                )
            )
        }
    }

    fun setRegistering(registering: Boolean) {
        _isRegistering.value = registering
        _loginError.value = null
    }

    // Auth actions
    fun login(email: String) {
        viewModelScope.launch {
            if (!isValidCollegeEmail(email)) {
                _loginError.value = "Email must end with a college-issued domain (e.g., .edu, .ac.uk, or .college.org)"
                return@launch
            }

            val user = repository.getUserByEmailSync(email)
            if (user != null) {
                _currentUser.value = user
                _loginError.value = null
                observePostsForCollege(user.collegeName)
            } else {
                // If user doesn't exist but has valid email, let's auto-create a friendly default profile for easy prototyping!
                val defaultName = email.substringBefore("@").replace(".", " ").capitalizeWords()
                val guessedCollege = guessCollegeName(email)
                val newUser = User(
                    email = email,
                    name = defaultName,
                    collegeName = guessedCollege,
                    studentId = "S${(10000..99999).random()}",
                    major = "General Studies",
                    points = 100
                )
                repository.insertUser(newUser)
                _currentUser.value = newUser
                _loginError.value = null
                observePostsForCollege(guessedCollege)
            }
        }
    }

    fun register(email: String, name: String, collegeName: String, studentId: String, major: String) {
        viewModelScope.launch {
            if (!isValidCollegeEmail(email)) {
                _loginError.value = "Email must end with a college-issued domain (e.g., .edu, .ac.uk, or .college.org)"
                return@launch
            }
            if (name.isBlank() || collegeName.isBlank() || studentId.isBlank() || major.isBlank()) {
                _loginError.value = "All fields are required"
                return@launch
            }

            val existing = repository.getUserByEmailSync(email)
            if (existing != null) {
                _loginError.value = "An account with this email already exists"
                return@launch
            }

            val newUser = User(
                email = email,
                name = name,
                collegeName = collegeName,
                studentId = studentId,
                major = major,
                points = 100 // Welcome points!
            )
            repository.insertUser(newUser)
            _currentUser.value = newUser
            _loginError.value = null
            observePostsForCollege(collegeName)
        }
    }

    fun logout() {
        _currentUser.value = null
        _feedPosts.value = emptyList()
        _unlockedResourceIds.value = emptySet()
    }

    fun updateUserProfile(updatedUser: User) {
        viewModelScope.launch {
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
        }
    }

    private fun observePostsForCollege(collegeName: String) {
        viewModelScope.launch {
            repository.getPostsForCollege(collegeName).collect { posts ->
                _feedPosts.value = posts
            }
        }
    }

    // Marketplace actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addResource(title: String, courseCode: String, professor: String, semester: String, description: String, fileType: String, priceInPoints: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val material = ResourceMaterial(
                title = title,
                courseCode = courseCode.uppercase(),
                professor = professor,
                semester = semester,
                description = description,
                fileType = fileType,
                uploaderEmail = user.email,
                uploaderName = user.name,
                priceInPoints = priceInPoints
            )
            repository.insertResource(material)
        }
    }

    fun purchaseResource(material: ResourceMaterial, onResult: (Boolean, String) -> Unit) {
        val buyer = _currentUser.value ?: return
        viewModelScope.launch {
            if (buyer.email == material.uploaderEmail) {
                // Free for uploader
                _unlockedResourceIds.value = _unlockedResourceIds.value + material.id
                onResult(true, "You already own this document!")
                return@launch
            }

            if (_unlockedResourceIds.value.contains(material.id)) {
                onResult(true, "Document already unlocked!")
                return@launch
            }

            if (buyer.points < material.priceInPoints) {
                onResult(false, "Insufficient balance! You need ${material.priceInPoints} CC (Campus Credits). Completing gigs is a great way to earn points.")
                return@launch
            }

            // Perform transaction securely
            val updatedBuyerPoints = buyer.points - material.priceInPoints
            val updatedBuyer = buyer.copy(points = updatedBuyerPoints)
            repository.insertUser(updatedBuyer)
            _currentUser.value = updatedBuyer

            // Credit seller
            val seller = repository.getUserByEmailSync(material.uploaderEmail)
            if (seller != null) {
                repository.insertUser(seller.copy(points = seller.points + material.priceInPoints))
            }

            // Unlock resource in-memory
            _unlockedResourceIds.value = _unlockedResourceIds.value + material.id
            onResult(true, "Successfully unlocked '${material.title}'! Dedicated download simulator launched.")
        }
    }

    // Gig actions
    fun postGig(title: String, category: String, description: String, rewardPoints: Int, contactInfo: String, onResult: (Boolean, String) -> Unit) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            if (user.points < rewardPoints) {
                onResult(false, "Insufficient balance! Posting this gig requires $rewardPoints CC to be locked in escrow.")
                return@launch
            }

            // Deduct escrow points from user balance
            val updatedUser = user.copy(points = user.points - rewardPoints)
            repository.insertUser(updatedUser)
            _currentUser.value = updatedUser

            val newGig = Gig(
                title = title,
                category = category,
                description = description,
                rewardPoints = rewardPoints,
                requesterEmail = user.email,
                requesterName = user.name,
                contactInfo = contactInfo,
                status = "OPEN"
            )
            repository.insertGig(newGig)
            onResult(true, "Gig posted successfully! $rewardPoints CC locked in escrow.")
        }
    }

    fun acceptGig(gig: Gig) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateGigStatus(gig.id, "ACCEPTED", user.email, user.name)
        }
    }

    fun completeGig(gig: Gig) {
        viewModelScope.launch {
            // Update status
            repository.updateGigStatus(gig.id, "COMPLETED", gig.helperEmail, gig.helperName)

            // Transfer escrowed points to the helper
            val helperEmail = gig.helperEmail ?: return@launch
            val helper = repository.getUserByEmailSync(helperEmail)
            if (helper != null) {
                val updatedHelper = helper.copy(points = helper.points + gig.rewardPoints)
                repository.insertUser(updatedHelper)

                // If currently logged in user is the helper, update current user state
                val currentUserVal = _currentUser.value
                if (currentUserVal != null && currentUserVal.email == helper.email) {
                    _currentUser.value = updatedHelper
                }
            }
        }
    }

    fun cancelGig(gig: Gig) {
        viewModelScope.launch {
            // Delete gig and refund original requester
            repository.deleteGig(gig.id)

            val requester = repository.getUserByEmailSync(gig.requesterEmail)
            if (requester != null) {
                val updatedRequester = requester.copy(points = requester.points + gig.rewardPoints)
                repository.insertUser(updatedRequester)

                val currentUserVal = _currentUser.value
                if (currentUserVal != null && currentUserVal.email == requester.email) {
                    _currentUser.value = updatedRequester
                }
            }
        }
    }

    // Feed actions
    fun createPost(content: String) {
        val user = _currentUser.value ?: return
        if (content.isBlank()) return

        viewModelScope.launch {
            val newPost = FeedPost(
                collegeName = user.collegeName,
                authorName = user.name,
                authorEmail = user.email,
                content = content
            )
            repository.insertPost(newPost)
        }
    }

    fun toggleLikePost(post: FeedPost) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val likedEmailsList = post.likedByEmails.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
            val newLikesCount: Int
            val newLikedByEmails: String

            if (likedEmailsList.contains(user.email)) {
                likedEmailsList.remove(user.email)
                newLikesCount = (post.likesCount - 1).coerceAtLeast(0)
            } else {
                likedEmailsList.add(user.email)
                newLikesCount = post.likesCount + 1
            }
            newLikedByEmails = likedEmailsList.joinToString(",")

            repository.updatePostLikes(post.id, newLikesCount, newLikedByEmails)

            // Refresh current feed to reflect immediately
            observePostsForCollege(user.collegeName)
        }
    }

    // Utilities
    private fun isValidCollegeEmail(email: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        return cleanEmail.contains("@") && (
            cleanEmail.endsWith(".edu") ||
            cleanEmail.endsWith(".ac.uk") ||
            cleanEmail.endsWith(".college.org") ||
            cleanEmail.endsWith(".edu.in") ||
            cleanEmail.contains(".edu.")
        )
    }

    private fun guessCollegeName(email: String): String {
        val domain = email.substringAfter("@").lowercase()
        val parts = domain.substringBeforeLast(".edu").split(".")
        val namePart = parts.lastOrNull() ?: "State University"
        return namePart.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } + " University"
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
