package micronaut.cache.hazelcast.groovy.service

import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable

import javax.inject.Singleton

@Singleton
class MessageService {

    int invocationCounter = 0

    @Cacheable("my-cache")
    String returnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodReturnMessage"
    }

    @CacheInvalidate(value="my-cache", all=true)
    String invalidateAndReturnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodInvalidateAndReturnMessage"
    }

    @CachePut("my-cache")
    String putReturnMessage(String message) {
        ++invocationCounter
        message+"_FromInsideMethodPutAndReturnMessage"
    }
}