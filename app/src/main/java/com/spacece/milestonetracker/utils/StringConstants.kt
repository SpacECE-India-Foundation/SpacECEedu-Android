package com.spacece.milestonetracker.utils

import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.VolunteerOption

//Intent Keys
const val WEB_VIEW_TITLE = "web_view_title"
const val WEB_VIEW_URL = "web_view_url"

//App Details Links
const val INSTAGRAM_URL = "https://www.instagram.com/spac.ece/"
const val YOUTUBE_URL = "https://www.youtube.com/@SpacECE"
const val FACEBOOK_URL = "https://www.facebook.com/SpacECE/"
const val LINKEDIN_URL = "https://www.linkedin.com/company/spacecein/"
const val TWITTER_URL = "https://x.com/ece_spac"
const val ABOUT_US_URL = "https://www.spacece.in/about-us"
const val TERMS_AND_CONDITIONS_URL = "https://www.spacece.co/terms-and-conditions"
const val PRIVACY_POLICY_URL = "https://www.spacece.co/privacy-policy"
const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.spacece.milestonetracker"
const val MAP_LOCATION = "SpacECE INDIA FOUNDATION, CHANDRALOK NAGARI, C602, opp. Muktai Garden, Ganesh Nagar, Dhayari, Pune, Maharashtra 411041"

//UI State Enums
enum class UIState {
    DataView, NoDataView, NoMoreData, Loading, LoadingMore,
    ConnectionError, InternetError, ServerDown,
    CommonError, MultiLogin, GuestUserError
}

//User Type Enums
enum class UserType(val value: String) {
    PARENT("customer"),
    ADMIN("consultant");
}

//Chat Type Enums
enum class ChatSender(val value: Int) {
    SELF(0),
    OTHER(1);
}

//Other Values
const val STRING_TOKEN_C1 = "#c1"
const val STRING_TOKEN_C2 = "#c2"
