package com.spacece.milestonetracker.data.remote

//base url
const val BASE_URL = "https://hustle-7c68d043.mileswebhosting.com/"
const val CONSULT_AGORA_BASE_URL = "http://spacefoundation.in/test/SpacECE-PHP/"

//end points
const val LOGIN = "spacece-main/spacece_auth/login_action.php"
const val SIGNUP = "spacece-main/spacece_auth/register_action.php"
const val UPDATE_PASSWORD = "spacece-main/spacece_auth/api_updatePassword.php"
const val ADD_NEW_CHILD = "spacece/api/AddNewChild_MilesStone.php"
const val UPDATE_CHILD_PROGRESS = "spacece/api/Update_Childprogress_MilesStone.php"

// Home Fragment end points
const val UPDATE_CHILD_GROWTH = "spacece/api/updateChildGrowth_MilesStone.php"
const val GET_ALL_CHILD = "spacece/api/getAllChild.php"
const val GET_CHILD_DETAILS = "spacece/api/getChildDetails.php"

//request keys
const val NAME = "name"
const val PHONE = "phone"
const val EMAIL = "email"
const val PASSWORD = "password"
const val IMAGE = "image"
const val TYPE = "type"
const val IS_API = "isAPI"
const val U_MOB = "u_mob"
const val C_CATEGORIES = "c_categories"
const val C_OFFICE = "c_office"
const val C_FROM_TIME = "c_from_time"
const val C_TO_TIME = "c_to_time"
const val C_LANGUAGE = "c_language"
const val C_FEE = "c_fee"
const val SELECTED_ITEM = "selectedItem"
const val C_QUALIFICATION = "c_qualification"
const val USER_ID = "user_id"
const val CENTER = "center"
const val CHILD_NAME = "childName"
const val DOB = "dob"
const val GENDER = "gender"

//response keys
const val STATUS_SUCCESS = "success"
const val STATUS_FAILURE = "error"

//status codes
const val UNKNOWN_ERROR = "Unknown Error"
const val STATUS_CODE_SUCCESS = 200
const val STATUS_CODE_FAILURE = 400

//other values
const val TYPE_TEXT = "text/plain"
const val TYPE_IMAGE = "image/*"
