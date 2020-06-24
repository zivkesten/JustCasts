package com.zk.justcasts.screens.show.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.repository.database.ShowsDatabase
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.Result
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ShowViewModel(private val database: ShowsDatabase, private val repository: Repository) : ViewModel() {
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
                    .doOnNext { Log.d("Zivi", "resultToViewState before -> $it") }
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
                o.ofType(Event.ListItemClicked::class.java).onItemClick(),
                o.ofType(Event.AddToMyShows::class.java).onTapFab()
            )
        }
    }

    private fun Observable<Lce<out Result>>.resultToViewState(): Observable<ViewState> {
        return scan(ViewState()) { state, newValue ->
            when (newValue) {
                is Lce.Content -> state.copy()
                is Lce.Error -> state.copy()
                is Lce.Loading -> state.copy()
            }
        }
    }

    private fun Observable<Lce<out Result>>.resultToViewEffect(): Observable<ViewEffect> {
        return map {
            when(it) {
                is Lce.Content -> {
                    when (it.packet) {
                        is Result.ShowAddToFavConfirmation -> ViewEffect.ShowAddToFavConfirmation(it.packet.podcastAdded)
                        is Result.TransitionToScreenWithElement -> ViewEffect.TransitionToScreenWithElement
                        else -> ViewEffect.NoEffect
                    }
                }
                is Lce.Error -> ViewEffect.NoEffect
                is Lce.Loading -> ViewEffect.NoEffect
            }
        }
    }

    private fun Observable<Event.ScreenLoad>.onScreenLoad(): Observable<Lce<out Result.GetPodcastsResult>> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.ListItemClicked>.onItemClick(): Observable<Lce<out Result.GetPodcastsResult>> {
        return switchMap { loadFromAPI() }
    }

    private fun Observable<Event.AddToMyShows>.onTapFab(): Observable<Lce<out Result.ShowAddToFavConfirmation>> {
        return  switchMap { addToFavourites(it.item) }
    }

    private fun loadFromAPI(): Observable<Lce<Result.GetPodcastsResult>>? {
        return repository.getPodcasts()
            .subscribeOn(Schedulers.io())
            .map { optionalResponse ->
                if (!optionalResponse.errorMessage.isNullOrBlank()) {
                    Lce.Error(Result.GetPodcastsResult(ArrayList<PodcastDTO>()))
                } else {
                    Lce.Content(Result.GetPodcastsResult(ArrayList<PodcastDTO>()))
                }
            }.onErrorReturn { Lce.Error(Result.GetPodcastsResult(ArrayList<PodcastDTO>())) }
            .startWith(Lce.Loading())
    }

    private fun addToFavourites(item: PodcastDTO): Observable<Lce<Result.ShowAddToFavConfirmation>> {
        return database.podcastDao().insert(item.entity())
            .toObservable()
            .doOnNext { Log.d("Zivi", "insert result $it") }
            .subscribeOn(Schedulers.io())
            .map {
                if (it > 0) {
                    Lce.Content(Result.ShowAddToFavConfirmation(item))
                } else {
                    Lce.Error(Result.ShowAddToFavConfirmation(item))
                }
            }
            .onErrorReturn {
                Lce.Error(Result.ShowAddToFavConfirmation(item))
            }
            .startWith(Lce.Loading())
    }

}