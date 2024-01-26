package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.AgentAffecter.class.getName());
            createCache(cm, com.mycompany.myapp.domain.AgentStructure.class.getName());
            createCache(cm, com.mycompany.myapp.domain.AgentStructure.class.getName() + ".strutures");
            createCache(cm, com.mycompany.myapp.domain.AgentStructure.class.getName() + ".agents");
            createCache(cm, com.mycompany.myapp.domain.Categorie.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Gestion.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Immobilisation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Immobilisation.class.getName() + ".categories");
            createCache(cm, com.mycompany.myapp.domain.Immobilisation.class.getName() + ".gestions");
            createCache(cm, com.mycompany.myapp.domain.Localite.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Maintenance.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Maintenance.class.getName() + ".immobilisations");
            createCache(cm, com.mycompany.myapp.domain.Structure.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Structure.class.getName() + ".localites");
            createCache(cm, com.mycompany.myapp.domain.Transfert.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Transfert.class.getName() + ".immobilisations");
            createCache(cm, com.mycompany.myapp.domain.Transfert.class.getName() + ".strutures");
            createCache(cm, com.mycompany.myapp.domain.Agent.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
