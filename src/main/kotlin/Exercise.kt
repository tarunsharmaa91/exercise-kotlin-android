import model.Comments
import model.Posts
import model.TopUsers
import model.User
import resources.Data
import kotlin.concurrent.thread

/**

# Fueled Kotlin Exercise

A blogging platform stores the following information that is available through separate API endpoints:
+ user accounts
+ blog posts for each user
+ comments for each blog post

### Objective
The organization needs to identify the 3 most engaging bloggers on the platform. Using only Kotlin and the Kotlin standard library, output the top 3 users with the highest average number of comments per post in the following format:

`[name]` - `[id]`, Score: `[average_comments]`

Instead of connecting to a remote API, we are providing this data in form of JSON files, which have been made accessible through a custom Resource enum with a `data` method that provides the contents of the file.

### What we're looking to evaluate
1. How you choose to model your data
2. How you transform the provided JSON data to your data model
3. How you use your models to calculate this average value
4. How you use this data point to sort the users

 */

// 1. First, start by modeling the data objects that will be used.

fun main(vararg args: String) {

    // 2. Next, decode the JSON source using `[Data.getUsers()]`

    thread {
        val users = Data.getUsers<Array<User>>()
        val posts = Data.getPosts<Array<Posts>>()
        val comments = Data.getComments<Array<Comments>>()
        findTopUsers(users, posts, comments)
    }
    // 3. Finally, calculate the average number of comments per user and use it
    //    to find the 3 most engaging bloggers and output the result.
}

fun findTopUsers(users: Array<User>, posts: Array<Posts>, comments: Array<Comments>) {
    val topUsers = mutableListOf<TopUsers>()
    for (user in users) {
        val totalPostByUser = posts.filter {
            it.userId == user.id
        }.listIterator()

        val totalPosts = posts.filter {
            it.userId == user.id
        }.size
        val engagementList = arrayListOf<Double>()
        var sum = 0.0
        for (post in totalPostByUser) {
            val totalNumberOfCommentsForPost = comments.filter {
                it.postId == post.id
            }.size
            //Engagement = total comments each post / total post * 100
            val engagementPerPost = totalNumberOfCommentsForPost.toDouble() / totalPosts.toDouble() * 100
            engagementList.add(engagementPerPost)
        }
        for (num in engagementList) {
            sum += num
        }
        //Find average
        val average = sum / engagementList.size
        topUsers.add(TopUsers(user.name, user.id, average))
    }
    //Sort users by avg
    topUsers.sortByDescending { it.average_comments }
    printTopThreeUsers(topUsers)
}

fun printTopThreeUsers(topUsers: MutableList<TopUsers>) {
    for ((index, value) in topUsers.withIndex()) {
        //The format of the output should be as follows:     [name] - [id], Score: [average_comments]
        println("${value.name} - ${value.id}, Score: ${value.average_comments}")
        if (index == 2) break
    }
}
