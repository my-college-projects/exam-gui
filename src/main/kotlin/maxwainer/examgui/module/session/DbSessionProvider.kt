package maxwainer.examgui.module.session

import org.hibernate.Session
import org.hibernate.Transaction
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import java.net.URL

fun DbSessionProvider(config: URL): DbSessionProvider = DbSessionProviderImpl(config)

interface DbSessionProvider {

  fun <T> openSession(commit: Boolean = true, consumer: (Session) -> T): T
}

private class DbSessionProviderImpl(hibernateConfig: URL) : DbSessionProvider {

  private val config by lazy {
    Configuration().configure(hibernateConfig)
  }

//  private val serviceRegistry = StandardServiceRegistryBuilder()
//    .applySettings(config.properties) // add settings
//    .build()

  // create session factory
  private val sessionFactory = config.buildSessionFactory().apply {
    this.properties.putAll(config.properties.mapKeys { it.key.toString() })
  }

  override fun <T> openSession(commit: Boolean, consumer: (Session) -> T): T {
    sessionFactory.openSession().use {
      val transaction = it.beginTransaction()

      try {
        // use it
        val result = consumer(it)

        // if commit re
        if (commit)
          transaction.commit()

        return result
      } catch (exception: Exception) {
        // if exception appears, rollback transaction
        transaction.rollback()

        // rethrow
        throw exception
      }
    }
  }

}