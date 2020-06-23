package com.zk.justcasts.screens.show.viewModel

import android.util.Log
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.Result
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ShowViewModel(private val repository: Repository) : ViewModel() {
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
                o.ofType(Event.ListItemClicked::class.java).onItemClick()
            )
        }
    }

    private fun Observable<out Result>.resultToViewState(): Observable<ViewState> {
        return scan(ViewState()) { state, newValue ->
            when (newValue) {
                is Result.TransitionToScreenWithElement -> state.copy()
                else -> state.copy()
            }
        }
    }

    private fun Observable<out Result>.resultToViewEffect(): Observable<ViewEffect> {
        return map { ViewEffect.TransitionToScreenWithElement }
    }

    private fun Observable<Event.ScreenLoad>.onScreenLoad(): Observable<out Result.GetPodcastsResult?> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.ListItemClicked>.onItemClick(): Observable<out Result.GetPodcastsResult?> {
        return switchMap { loadFromAPI() }
    }

    private fun loadFromAPI(): Observable<Result.GetPodcastsResult?>? {
        return repository.getPodcasts()
            .subscribeOn(Schedulers.io())
            .map { optionalResponse ->
                optionalResponse.podcasts?.let { Result.GetPodcastsResult(it) }
            }
    }

}