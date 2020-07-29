package com.zk.justcasts.screens.show.viewModel

import android.os.Build
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.zk.justcasts.R
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.repository.database.ShowsDatabase
import com.zk.justcasts.repository.database.podcast.PodcastDAO
import com.zk.justcasts.repository.database.podcast.PodcastEntity
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.Result
import com.zk.justcasts.screens.show.model.ViewEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class ShowViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var showViewModel: ShowViewModel

    //Mock parameters
    private val mockPodcastDTO = PodcastDTO(id = "1", title = "mock", image = "mockUrl")
    private val mockEpisodeDTO = EpisodeDTO(id = "1")
    private val mockPodcastEntity = mockPodcastDTO.entity()
    private val mockResults = listOf(mockPodcastDTO, mockPodcastDTO)
    private val mockSearchText = "search"
    private val mockSearchResults = listOf(mockPodcastDTO)
    private val mockPodcastResponse = BestPodcastsResponse(id = 1, podcasts = mockResults)
    private val mockSharedElementView = View(ApplicationProvider.getApplicationContext())
    private val mockListOfEpisodes: List<EpisodeDTO> = listOf(mockEpisodeDTO, mockEpisodeDTO)
    private val mockListOfPodcastEntities: List<PodcastEntity> = listOf(mockPodcastDTO.entity(), mockPodcastDTO.entity())

    //Mock objects
    private val repository: Repository by lazy { Mockito.mock(Repository::class.java) }
    private val dataBase: ShowsDatabase by lazy { Mockito.mock(ShowsDatabase::class.java) }
    private val podcastDao: PodcastDAO by lazy { Mockito.mock(PodcastDAO::class.java) }

    @Before
    fun setUp() {
        podcastDao.apply {
            runBlocking<Unit> {
                launch(Dispatchers.Main) {
                    Mockito.`when`(getAll()).thenReturn(mockListOfPodcastEntities)
                    Mockito.`when`(insert(mockPodcastEntity)).thenReturn(1)
                }
            }
        }
        dataBase.apply {
                runBlocking<Unit> {
                    launch(Dispatchers.Main) {
                        whenever(podcastDao()).thenReturn(podcastDao)
                    }
                }
            }
        repository.apply {
            runBlocking<Unit> {
                launch(Dispatchers.Main) {
                    whenever(getPodcastsASync()).thenReturn(mockPodcastResponse)
                    whenever(getEpisodesASync("1")).thenReturn(mockListOfEpisodes)
                    whenever(search(mockSearchText)).thenReturn(mockSearchResults)
                }
            }
        }
        showViewModel = ShowViewModel(dataBase, repository)
        mockSharedElementView.transitionName = "transitionName"
    }

    @Test
    fun eventToResult_eventIsScreeLoad_verifyLoadingAndEpisodesListState() {

        //Given
        val spiedViewModel = Mockito.spy(showViewModel)

        //When
        spiedViewModel.onEvent(Event.ScreenLoad(mockPodcastDTO))

        //Then
        verify(spiedViewModel, times(1)).resultToViewState(Lce.Loading())
        verify(spiedViewModel, times(1)).resultToViewState(
            Lce.Content(Result.LoadEpisodes(mockListOfEpisodes))
        )
    }

    @Test
    fun eventToResult_eventIsAddToMyShows_verifyShowAddedState() {

        //Given
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.AddToMyShows(mockPodcastDTO))

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewState(Lce.Loading())
        verify(spiedSearchViewModel, times(1)).resultToViewState(
            Lce.Content(Result.ShowAddToFavConfirmation(mockPodcastDTO)
            )
        )
    }

    @Test
    fun eventToResult_eventIsAddToMyShows_verifyShowAddedEffect() {

        //Given
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.AddToMyShows(mockPodcastDTO))

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewEffect(
            Lce.Content(Result.ShowAddToFavConfirmation(mockPodcastDTO)
            )
        )
    }

    @Test
    fun eventToResult_eventIsItemClicked_verifyResultToViewEffectCalled() {

        //Given
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.ListItemClicked(mockEpisodeDTO, mockSharedElementView))

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewEffect(
            Lce.Content(Result.ItemClickedResult(mockEpisodeDTO, mockSharedElementView)))
    }

    @Test
    fun resultToViewState_resultIsLoading_stateIsLoading() {
        //Given
        val testObserverState = showViewModel.viewState.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.resultToViewState(Lce.Loading())

        //Then
        testObserverState
            .assertHistorySize(1)
            .value().apply {
                assertThat(episodes).isEqualTo(null)
                assertThat(loadingStateVisibility).isEqualTo(View.VISIBLE)
            }
    }

    @Test
    fun resultToViewState_resultIsError_stateIsError() {
        //Given
        val testObserverState = showViewModel.viewState.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        val message = "mockMessage"
        spiedSearchViewModel.resultToViewState(Lce.Error(Result.Error(message)))

        //Then
        testObserverState
            .assertHistorySize(1)
            .value().apply {
                assertThat(episodes).isEqualTo(null)
                assertThat(loadingStateVisibility).isEqualTo(View.GONE)
                assertThat(errorMessage).isEqualTo(message)
            }
    }

    @Test
    fun resultToViewState_resultIsErrorLceNotError_stateIsError() {
        //Given
        val testObserverState = showViewModel.viewState.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        val message = R.string.unexpectedError
        spiedSearchViewModel.resultToViewState(Lce.Error(Result.ScreenLoad))

        //Then
        testObserverState
            .assertHistorySize(1)
            .value().apply {
                assertThat(episodes).isEqualTo(null)
                assertThat(loadingStateVisibility).isEqualTo(View.GONE)
                assertThat(errorMessageResource).isEqualTo(message)
            }
    }

    @Test
    fun resultToViewEffect_resultIsItemClickedResult_effectIsTransitionToScreenWithElement() {
        //Given
        val testObserverState = showViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(
            Lce.Content(Result.ItemClickedResult(mockEpisodeDTO, mockSharedElementView)))

        //Then
        testObserverState
            .value()
            .apply {
                assertThat(this is ViewEffect.TransitionToScreenWithElement).isTrue()
            }
    }

    @Test
    fun resultToViewEffect_resultIsScreeLoad_effectIsNoEffect() {
        //Given
        val testObserverState = showViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(Lce.Content(Result.ScreenLoad))

        //Then
        testObserverState.assertValue { it is ViewEffect.NoEffect }
    }

    @Test
    fun resultToViewEffect_resultIsShowAddToFavConfirmation_effectIsShowAddToFavConfirmation() {
        //Given
        val testObserverState = showViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(showViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(Lce.Content(Result.ShowAddToFavConfirmation(mockPodcastDTO)))

        //Then
        testObserverState.assertValue { it is ViewEffect.ShowAddToFavConfirmation }
    }
}


