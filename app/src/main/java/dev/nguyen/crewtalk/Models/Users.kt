package dev.nguyen.crewtalk.Models

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var profile: String = ""
    private var cover: String = ""
    private var status: String = ""
    private var search: String = ""
    private var facebook: String = ""
    private var instagram: String = ""
    private var website: String = ""

    constructor()
    constructor(
        uid: String,
        username: String,
        profile: String,
        cover: String,
        status: String,
        search: String,
        facebook: String,
        instagram: String,
        website: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
    }

    fun getUid(): String? = this.uid
    fun setUid(uid: String) {this.uid = uid}

    fun getUserName(): String? = this.username
    fun setUserName(userName: String) {this.username = username}

    fun getProfile(): String? = this.profile
    fun setProfile(profile: String) {this.profile = profile}

    fun getCover(): String? = this.cover
    fun setCover(cover: String) {this.cover = cover}

    fun getStatus(): String? = this.status
    fun setStatus(status: String) {this.status = status}

    fun getSearch(): String? = this.search
    fun setSearch(search: String) {this.search = search}

    fun getFacebook(): String? = this.facebook
    fun setFacebook(facebook: String) {this.facebook = facebook}

    fun getInstagram(): String? = this.instagram
    fun setInstagram(instagram: String) {this.instagram = instagram}

    fun getWebsite(): String? = this.website
    fun setWebsite(website: String) {this.website = website}

}