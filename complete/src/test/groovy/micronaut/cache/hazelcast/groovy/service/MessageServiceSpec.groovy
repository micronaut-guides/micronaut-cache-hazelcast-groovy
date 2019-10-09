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

    @Inject
    MessageService messageService
    @Inject
    DefaultCacheManager defaultCacheManager

    def setup() {
        messageService.invocationCounter = 0
        SyncCache<DefaultSyncCache> cache = defaultCacheManager.getCache("my-cache")
        cache.invalidateAll()
    }

    def "test method cached after first invocation"() {
        when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'method runs by getting the counter increasing'
        message == "myMessage_FromInsideMethodReturnMessage"
        messageService.invocationCounter == 1

        when: 'called a second time'
        message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'the cache is accessed and the method doesnt run again'
        message == "myMessage_FromInsideMethodReturnMessage"
        messageService.invocationCounter == 1

        when: 'called again with a different param'
        message = messageService.returnMessage(MESSAGE_TITLE + "Again")

        then: 'method is invoked bc cache doesnt have the stored value'
        message == "myMessageAgain_FromInsideMethodReturnMessage"
        messageService.invocationCounter == 2
    }

    def "test method cache invalidate"() {
        when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'method runs by getting the counter increasing'
        message == "myMessage_FromInsideMethodReturnMessage"
        messageService.invocationCounter == 1

        when: 'call a different method with @CacheInvalidate'
        message = messageService.invalidateAndReturnMessage(MESSAGE_TITLE)

        then: 'method is invoked because cache was invalidated and evicted'
        message == "myMessage_FromInsideMethodInvalidateAndReturnMessage"
        messageService.invocationCounter == 2
    }

    def "test method cache put"() {
        when: 'called the first time'
        String message = messageService.returnMessage(MESSAGE_TITLE)

        then: 'method runs by getting the counter increasing'
        message == "myMessage_FromInsideMethodReturnMessage"
        messageService.invocationCounter == 1

        when: 'call a different method with @CachePut'
        message = messageService.putReturnMessage(MESSAGE_TITLE)

        then: 'method is invoked because cache it will put a new entry'
        message == "myMessage_FromInsideMethodPutAndReturnMessage"
        messageService.invocationCounter == 2
    }
}
