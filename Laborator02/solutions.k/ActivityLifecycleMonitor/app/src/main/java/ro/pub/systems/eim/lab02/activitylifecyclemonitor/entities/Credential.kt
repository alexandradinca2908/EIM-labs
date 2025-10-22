package ro.pub.systems.eim.lab02.activitylifecyclemonitor.entities

class Credential {
    var username: String
    var password: String

    constructor() {
        username = ""
        password = ""
    }

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}
