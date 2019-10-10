package micronaut.cache.hazelcast.groovy.service

import io.micronaut.cache.hazelcast.HazelcastClientConfiguration
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener

import javax.inject.Singleton

@Singleton
class HazelcastAdditionalSettings implements BeanCreatedEventListener<HazelcastClientConfiguration> {

    @Override
    HazelcastClientConfiguration onCreated(BeanCreatedEvent<HazelcastClientConfiguration> event) {
        HazelcastClientConfiguration hazelcastClientConfiguration = event.getBean()
        hazelcastClientConfiguration.getNetworkConfig().setConnectionTimeout(8888)
        hazelcastClientConfiguration
    }
}
