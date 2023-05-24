package com.prathamesh.matrimonyapp.model

class User {
    var userID: String? = null
    var email: String? = null
    var password: String? = null
    var imageUrl: String? = null
    var fullName: String? = null
    var profession: String? = null
    var adress: String? = null
    var birthDate: String? = null
    var age = 0
    var gender: String? = null
    var number: String? = null
    var isMarried = false
    var bio: String? = null
    var hobbies: List<Int>? = null
    var imagesUser: List<String>? = null

    constructor() {}
    constructor(
        email: String?,
        password: String?,
        imageUrl: String?,
        fullName: String?,
        profession: String?,
        adress: String?,
        birthDate: String?,
        age: Int,
        gender: String?,
        number: String?,
        isMarried: Boolean
    ) {
        this.email = email
        this.password = password
        this.imageUrl = imageUrl
        this.fullName = fullName
        this.profession = profession
        this.adress = adress
        this.birthDate = birthDate
        this.age = age
        this.gender = gender
        this.number = number
        this.isMarried = isMarried
    }

    constructor(
        email: String?,
        password: String?,
        imageUrl: String?,
        fullName: String?,
        profession: String?,
        adress: String?,
        birthDate: String?,
        age: Int,
        gender: String?,
        number: String?,
        isMarried: Boolean,
        image1: String?,
        image2: String?,
        image3: String?,
        image4: String?
    ) {
        this.email = email
        this.password = password
        this.imageUrl = imageUrl
        this.fullName = fullName
        this.profession = profession
        this.adress = adress
        this.birthDate = birthDate
        this.age = age
        this.gender = gender
        this.number = number
        this.isMarried = isMarried
    }
}