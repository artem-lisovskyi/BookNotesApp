package com.booknotes.booknotesapp.navigation

import com.booknotesapp.booknotesapp.R


sealed class DestinationsBottom(
    var title: String,
    var icon: Int = 0,
    var route: String
) {
    object Home : DestinationsBottom("Home",  R.drawable.ic_home, "home")
    object Favorites : DestinationsBottom("Favorites", R.drawable.ic_favorite, "favorites")
    object Recommendations : DestinationsBottom("For you", R.drawable.ic_recommendations, "For you")
    object Profile : DestinationsBottom("Profile", R.drawable.ic_profile, "profile")
    object Information : DestinationsBottom(title = "Information", route = "info") {
        const val argBookId = "bookId"
    }
    object Onboarding : DestinationsBottom(title = "Onboarding", route = "onboarding")
}