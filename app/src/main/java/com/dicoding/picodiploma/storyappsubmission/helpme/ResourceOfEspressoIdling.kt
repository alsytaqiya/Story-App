package com.dicoding.picodiploma.storyappsubmission.helpme

import androidx.test.espresso.idling.CountingIdlingResource

object ResourceOfEspressoIdling {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val resourceOfCountIdling = CountingIdlingResource(RESOURCE)

    fun incrementplus() {
        resourceOfCountIdling.increment()
    }

    fun decrementminus() {
        if (!resourceOfCountIdling.isIdleNow) {
            resourceOfCountIdling.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {

    ResourceOfEspressoIdling.incrementplus()
    return try {
        function()
    } finally {
        ResourceOfEspressoIdling.decrementminus()
    }
}