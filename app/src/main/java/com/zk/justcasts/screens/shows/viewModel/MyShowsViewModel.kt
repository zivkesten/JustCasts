package com.zk.justcasts.screens.shows.viewModel

import android.util.Log
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.Result
import com.zk.justcasts.screens.shows.model.Result.SubscribeResult
import com.zk.justcasts.screens.shows.model.ViewEffect.TransitionToScreenWithElement
import com.zk.justcasts.screens.shows.model.ViewEffect.ShowVisualResultForAddToFavourites
import com.zk.justcasts.screens.shows.model.Result.ItemClickedResult
import com.zk.justcasts.screens.shows.model.Result.GetPodcastsResult
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.views.MyShowsFragmentDirections
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MyShowsViewModel(private val repository: Repository): ViewModel() {

    private val eventEmitter: PublishSubject<Event> = PublishSubject.create()

    private lateinit var disposable: Disposable

    val viewState: Observable<ViewState>
    val viewEffects: Observable<ViewEffect>

    init {
        eventEmitter
            .doOnNext { Log.d("Zivi", "eventEmitter -> $it") }
            .eventToResult()
            .doOnNext { Log.d("Zivi", "eventToResult -> $it") }
            .share()
            .doOnError { Log.e("Zivi", "doOnError " + it.localizedMessage) }

            .also { result ->
                viewState = result
                    .resultToViewState()
                    .doOnNext { Log.d("Zivi", "resultToViewState -> $it") }
                    .replay(1)
                    .autoConnect(1) { disposable = it }

                viewEffects = result
                    .resultToViewEffect()
                    .doOnNext { Log.d("Zivi", "resultToViewEffect -> $it") }
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun processInput(event: Event) {
        Log.d("Zivi", "processInput: $event")
        eventEmitter.onNext(event)
    }
    // -----------------------------------------------------------------------------------
    // Internal helpers

    private fun Observable<Event>.eventToResult(): Observable<out Result?> {
        return publish { o ->
            Observable.merge(
                o.ofType(Event.ScreenLoad::class.java).onScreenLoad(),
                o.ofType(Event.ItemClicked::class.java).onItemClick(),
                o.ofType(Event.SwipeToRefreshEvent::class.java).onSwipeRefresh()
            )
        }
    }

    private fun Observable<out Result>.resultToViewState(): Observable<ViewState> {
        return scan(ViewState()) { state, newValue ->
            when (newValue) {
                is GetPodcastsResult -> state.copy(itemList = newValue.podcats)
                is Result.ScreenLoadResult  -> state.copy()
                is SubscribeResult -> state.copy()
                else -> state.copy()
            }
        }
    }

    private fun Observable<out Result>.resultToViewEffect(): Observable<ViewEffect> {
            return map {
                when(it) {
                    is SubscribeResult -> ShowVisualResultForAddToFavourites(it.toString())
                    is ItemClickedResult -> itemClickToViewEffect(it)
                    is GetPodcastsResult, Result.ScreenLoadResult -> ViewEffect.NoEffect
                }
            }
    }

    private fun itemClickToViewEffect(it: ItemClickedResult): TransitionToScreenWithElement? {
        var directions: TransitionToScreenWithElement? = null
        val sharedElement = it.sharedElement
        val item = it.item
        ViewCompat.getTransitionName(sharedElement)?.let { transitionName ->
            val extras = FragmentNavigatorExtras(sharedElement to transitionName)
            val direction = MyShowsFragmentDirections.selectShow(item, transitionName)
            directions = TransitionToScreenWithElement(extras, direction)
        }
        return directions
    }

    private fun Observable<Event.ScreenLoad>.onScreenLoad(): Observable<out GetPodcastsResult?> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.SwipeToRefreshEvent>.onSwipeRefresh(): Observable<out GetPodcastsResult?> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.ItemClicked>.onItemClick(): Observable<out ItemClickedResult> {
        return switchMap { Observable.just(ItemClickedResult(it.item, it.SharedElement)) }
    }

    private fun loadFromAPI(): Observable<GetPodcastsResult?>? {
        return repository.getPodcasts()
            .subscribeOn(Schedulers.io())
            .map { optionalResponse ->
                optionalResponse.podcasts?.let { GetPodcastsResult(it) }
            }
    }
}