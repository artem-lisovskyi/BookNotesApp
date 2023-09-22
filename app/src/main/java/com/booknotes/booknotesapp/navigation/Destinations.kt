package com.booknotes.booknotesapp.navigation

import com.booknotesapp.booknotesapp.R


sealed class Destinations(
    var title: String,
    var icon: Int = 0,
    var route: String
) {
    object Home : Destinations("Home", R.drawable.ic_home, "home")
    object Favorites : Destinations("Favorites", R.drawable.ic_favorite, "favorites")
    object Recommendations : Destinations("For you", R.drawable.ic_recommendations, "For you")
    object Profile : Destinations("Profile", R.drawable.ic_profile, "profile")
    object Information : Destinations(title = "Information", route = "info") {
        const val argBookId = "bookId"
    }

    object Onboarding : Destinations(title = "Onboarding", route = "onboarding")
}