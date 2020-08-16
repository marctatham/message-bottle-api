package com.marctatham

// in the long run, we'll work on this
// so it's getting config from different sources
// for now it's just grabbing environment variables
class ConfigProvider {

    fun getOrDefault(key: String, default: String): String {
        return System.getenv().getOrDefault(key, default)
    }

}