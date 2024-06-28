package banseok.concurrency.config

import lombok.RequiredArgsConstructor
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
class RedisConfig {
    private val redisProperties: RedisProperties? = null

    @Value("\${spring.data.redis.host}")
    private val redisHost: String? = null

    @Value("\${spring.data.redis.port}")
    private val redisPort: String? = null

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.hostName = redisHost!!
        redisStandaloneConfiguration.port = redisPort!!.toInt()
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        return redisTemplate
    }

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://$redisHost:$redisPort")
        return Redisson.create(config)
    }
}