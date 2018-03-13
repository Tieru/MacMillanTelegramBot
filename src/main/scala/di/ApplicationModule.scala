package di

import com.google.inject.AbstractModule
import dictionary.{Api, ApiClient, ClientWrapper, DictionaryClient}
import net.codingwell.scalaguice.ScalaModule
import repository.entry.EntryRepository
import repository.entry.impl.{EntryCache, EntryCacheImpl, EntryRepositoryImpl}
import repository.search.SearchRepository
import repository.search.impl.SearchRepositoryImpl
import response.entry.{EntryResponseHandler, EntryResponseHandlerImpl}
import response.search.{SearchResponseHandler, SearchResponseHandlerImpl}

import scala.concurrent.ExecutionContext

class ApplicationModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

    //probably shouldn't be here
    bind[ExecutionContext].toInstance(ExecutionContext.Implicits.global)

    bind[ApiClient].toInstance(DictionaryClient)
    bind[Api].to[ClientWrapper].asEagerSingleton()

    bind[SearchRepository].to[SearchRepositoryImpl].asEagerSingleton()
    bind[SearchResponseHandler].to[SearchResponseHandlerImpl].asEagerSingleton()

    bind[EntryCache].to[EntryCacheImpl].asEagerSingleton()
    bind[EntryRepository].to[EntryRepositoryImpl].asEagerSingleton()
    bind[EntryResponseHandler].to[EntryResponseHandlerImpl].asEagerSingleton()
  }

}
