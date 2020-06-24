package com.zk.justcasts.screens.shows.viewModel

import android.util.Log
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.repository.Lce
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

    private fun Observable<Event>.eventToResult(): Observable<Lce<out Result>> {
        return publish { o ->
            Observable.merge(
                o.ofType(Event.ScreenLoad::class.java).onScreenLoad(),
                o.ofType(Event.ItemClicked::class.java).onItemClick(),
                o.ofType(Event.SwipeToRefreshEvent::class.java).onSwipeRefresh()
            )
        }
    }

    private fun Observable<Lce<out Result>>.resultToViewState(): Observable<ViewState> {
        return scan(ViewState()) { state, newValue ->
            when (newValue) {
                is Lce.Content -> {
                    when(newValue.packet) {
                        is GetPodcastsResult -> state.copy(itemList = newValue.packet.podcastsResponse.podcasts)
                        is Result.ScreenLoadResult  -> state.copy()
                        is SubscribeResult -> state.copy()
                        is ItemClickedResult -> state.copy()
                    }
                }
                is Lce.Error ->  state.copy()
                is Lce.Loading -> state.copy()
            }
        }.distinctUntilChanged()
    }

    private fun Observable<Lce<out Result>>.resultToViewEffect(): Observable<ViewEffect> {
            return map { result ->
                when (result) {
                    is Lce.Content -> {
                        when (result.packet) {
                            is SubscribeResult -> ShowVisualResultForAddToFavourites(result.toString())
                            is ItemClickedResult -> itemClickToViewEffect(result.packet)
                            else -> ViewEffect.NoEffect
                        }
                    }
                    is Lce.Error -> ShowVisualResultForAddToFavourites(result.packet.toString())
                    is Lce.Loading -> ViewEffect.NoEffect
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

    private fun Observable<Event.ScreenLoad>.onScreenLoad(): Observable<Lce<GetPodcastsResult>> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.SwipeToRefreshEvent>.onSwipeRefresh(): Observable<Lce<GetPodcastsResult>> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.ItemClicked>.onItemClick(): Observable<Lce<ItemClickedResult>> {
        return switchMap { Observable.just(Lce.Content(ItemClickedResult(it.item, it.SharedElement))) }
    }

    private fun loadFromAPI(): Observable<Lce<GetPodcastsResult>>? {
        return repository.getPodcasts()
            .subscribeOn(Schedulers.io())
            .map { optionalResponse ->
                if (!optionalResponse.errorMessage.isNullOrBlank()) {
                    Lce.Error(GetPodcastsResult(optionalResponse))
                } else {
                    Lce.Content(GetPodcastsResult(optionalResponse))
                }
            } .onErrorReturn {
                Lce.Error(GetPodcastsResult(BestPodcastsResponse(errorMessage = "error")))
            }
            .startWith(Lce.Loading())


    }
}