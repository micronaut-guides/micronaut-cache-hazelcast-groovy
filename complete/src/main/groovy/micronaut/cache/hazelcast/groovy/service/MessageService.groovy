package micronaut.cache.hazelcast.groovy.service

import io.micronaut.cache.annotation.Cacheable

import javax.inject.Singleton

@Singleton
class MessageService {

    int invocationCounter = 0

    @Cacheable("my-cache")
    String returnMessage(String message) {
        ++invocationCounter
        message+"FromInsideMethod"
    }
}