package com.zk.justcasts.screens.shows.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.Podcast
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.Result
import com.zk.justcasts.screens.shows.model.Result.SubscribeResult
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MyShowsViewModel(private val repository: Repository): ViewModel() {

    private val eventEmitter: PublishSubject<Event> = PublishSubject.create()

    private lateinit var disposable: Disposable

    lateinit var viewState: Observable<ViewState>
    lateinit var viewEffects: Observable<ViewEffect>

    init {
        eventEmitter
            .doOnNext { Log.d("Zivi", "--1-- result $it") }
            .eventToResult()
            .doOnNext { Log.d("Zivi", "--2-- result $it") }
            .share()
            .doOnError { Log.e("Zivi", "Throwable " + it.localizedMessage) }
            .also { result ->
                viewState = result.scan(ViewState()) { state, newValue ->
                    when (newValue) {
                        is Result.GetPodcastsResult -> state.copy(itemList = newValue.podcats)
                        is Result.ScreenLoadResult -> state.copy()
                        is Result.SubscribeResult -> state.copy()
                    }
                }
            }
    }

    private fun Observable<Event>.eventToResult(): Observable<out Result> {
        return publish { o ->
            Observable.merge(
                o.ofType(Event.ScreenLoad::class.java).onScreenLoad(),
                o.ofType(Event.SwipeToRefreshEvent::class.java).onSwipeRefresh(),
                o.ofType(Event.SubscribeToPodcast::class.java).onSubscribeToPodcast()
            )
        }
    }

    private fun Observable<Event.ScreenLoad>.onScreenLoad(): Observable<out Result.GetPodcastsResult> {
        return loadPodcastsFromAPI()
    }

    private fun Observable<Event.SwipeToRefreshEvent>.onSwipeRefresh(): Observable<out Result.GetPodcastsResult> {
        return loadPodcastsFromAPI()
    }

    private fun Observable<Event.SubscribeToPodcast>.onSubscribeToPodcast(): Observable<out Result.SubscribeResult> {
        //Update Room
        return Observable.just(SubscribeResult(true))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun processInput(event: Event) {
        Log.d("Zivi", "processInput: $event")
        eventEmitter.onNext(event)
    }

    private fun loadPodcastsFromAPI(): Observable<out Result.GetPodcastsResult?> {
        return repository.getPodcasts()
            .subscribeOn(Schedulers.io())
            .map { optionalResponse ->
                optionalResponse.podcasts?.let { Result.GetPodcastsResult(it) }
            }
    }

// -----------------------------------------------------------------------------------
// Internal helpers
}