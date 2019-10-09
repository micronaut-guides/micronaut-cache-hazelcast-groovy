package micronaut.cache.hazelcast.groovy.service

import io.micronaut.cache.DefaultCacheManager
import io.micronaut.cache.DefaultSyncCache
import io.micronaut.cache.SyncCache
import io.micronaut.test.annotation.MicronautTest
import micronaut.cache.hazelcast.groovy.Application
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest(application = Application)
class MessageServiceSpec extends Specification {

    static final String MESSAGE_TITLE = "myMessage"

    @Inject MessageService messageService
    @Inject DefaultCacheManager defaultCacheManager

    def setup(){
        messageService.invocationCounter = 0
    }

    def "test method cached after first invocation"() {
        when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'method runs by getting the gorm object'
        message == "myMessageFromInsideMethod"
        messageService.invocationCounter == 1

        when: 'called a second time'
        message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'the cache is accessed and the method doesnt run again'
        message == "myMessageFromInsideMethod"
        messageService.invocationCounter == 1

        when: 'called again with a different param'
        message = messageService.returnMessage(MESSAGE_TITLE+"Again")

        then: 'method is invoked bc cache doesnt have the stored value'
        message == "myMessageAgainFromInsideMethod"
        messageService.invocationCounter == 2
    }

    def "inspect the default cache manager"() {
        when: 'use the manager to find the caches'
        Set<String> cacheNames = defaultCacheManager.getCacheNames()

        then:
        cacheNames.size() == 1
        cacheNames.asList()[0] == "my-cache"

        when: 'get the cache'
        SyncCache<DefaultSyncCache> cache = defaultCacheManager.getCache("my-cache")
        String message = (String) cache.get("myMessage", String).get()

        then:
        cache.getName() == "my-cache"
        message == "myMessageFromInsideMethod"

        when: 'cache is invalidated'
        cache.invalidateAll()
        Optional<String> result = cache.get("myMessage", String)

        then:
        !result.isPresent()
    }
}
