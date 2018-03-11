package di

import com.google.inject.AbstractModule
import dictionary.{Api, ApiClient, ClientWrapper, DictionaryClient}
import net.codingwell.scalaguice.ScalaModule
import repository.entry.EntryRepository
import repository.entry.impl.EntryRepositoryImpl
import response.entry.{EntryResponseHandler, EntryResponseHandlerImpl}

import scala.concurrent.ExecutionContext

class ApplicationModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

    //probably shouldn't be here
    bind[ExecutionContext].toInstance(ExecutionContext.Implicits.global)

    bind[ApiClient].toInstance(DictionaryClient)
    bind[Api].to[ClientWrapper].asEagerSingleton()
    bind[EntryRepository].to[EntryRepositoryImpl].asEagerSingleton()
    bind[EntryResponseHandler].to[EntryResponseHandlerImpl].asEagerSingleton()
  }

}
