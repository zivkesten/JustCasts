package com.zk.justcasts.screens.search.viewModel

import android.os.Build
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.view.ViewCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertThat
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.Lce
import com.zk.justcasts.repository.Repository
import com.zk.justcasts.screens.search.model.Event
import com.zk.justcasts.screens.search.model.Result
import com.zk.justcasts.screens.search.model.ViewEffect
import com.zk.justcasts.screens.search.views.SearchFragmentDirections
import com.zk.justcasts.screens.shows.views.MyShowsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class SearchViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var searchViewModel: SearchViewModel

    //Mock parameters
    private val mockPodcastDTO = PodcastDTO(id = "1")
    private val mockResults = listOf(mockPodcastDTO, mockPodcastDTO)
    private val mockSearchText = "search"
    private val mockSearchResults = listOf(mockPodcastDTO)
    private val mockPodcastResponse = BestPodcastsResponse(id = 1, podcasts = mockResults)
    private val mockSharedElementView = View(ApplicationProvider.getApplicationContext())
    private val mockEmptyListOfPodcasts: List<PodcastDTO> = emptyList()

    //Mock Repository
    private val repository: Repository by lazy {
        Mockito.mock(Repository::class.java).apply {
            runBlocking<Unit> {
                launch(Dispatchers.Main) {
                    whenever(getPodcastsASync()).thenReturn(mockPodcastResponse)
                    whenever(getEpisodesASync("1")).thenReturn(listOf(EpisodeDTO()))
                    whenever(search(mockSearchText)).thenReturn(mockSearchResults)
                }
            }
        }
    }

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(repository)
        mockSharedElementView.transitionName = "transitionName"
    }

    @Test
    fun eventToResult_eventIsScreeLoad_verifyLoadingAndResultsState() {

        //Given
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.ScreenLoad)

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewState(Lce.Loading())
        verify(spiedSearchViewModel, times(1)).resultToViewState(
            Lce.Content(Result.SearchResults(mockResults))
        )
    }

    @Test
    fun eventToResult_eventIsSearchTextInput_verifyLoadingAndResultsState() {

        //Given
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.SearchTextInput(mockSearchText))

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewState(Lce.Loading())
        verify(spiedSearchViewModel, times(1)).resultToViewState(
            Lce.Content(Result.SearchResults(mockSearchResults)
            )
        )
    }

    @Test
    fun eventToResult_eventIsItemClicked_verifyResultToViewEffectCalled() {

        //Given
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.onEvent(Event.ItemClicked(mockPodcastDTO, mockSharedElementView))

        //Then
        verify(spiedSearchViewModel, times(1)).resultToViewEffect(
            Lce.Content(Result.ItemClickedResult(mockPodcastDTO, mockSharedElementView)))
    }

    @Test
    fun resultToViewState_resultIsLoading_stateIsLoading() {
        //Given
        val testObserverState = searchViewModel.viewState.test()
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.resultToViewState(Lce.Loading())

        //Then
        testObserverState
            .assertHistorySize(1)
            .value().apply {
                assertThat(searchResultList).isEqualTo(mockEmptyListOfPodcasts)
                assertThat(searchText).isEqualTo(null)
                assertThat(loadingStateVisibility).isEqualTo(View.VISIBLE)// prevents search box from reset
            }
    }

    @Test
    fun resultToViewEffect_resultIsItemClickedResult_effectIsTransitionToScreenWithElement() {
        //Given
        val testObserverState = searchViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(
            Lce.Content(Result.ItemClickedResult(mockPodcastDTO, mockSharedElementView)))

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
        val testObserverState = searchViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(Lce.Content(Result.ScreeLoad))

        //Then
        testObserverState.assertValue { it is ViewEffect.NoEffect }
    }
    @Test
    fun resultToViewEffect_resultIsSearchResults_effectIsNoEffect() {
        //Given
        val testObserverState = searchViewModel.viewEffects.test()
        val spiedSearchViewModel = Mockito.spy(searchViewModel)

        //When
        spiedSearchViewModel.resultToViewEffect(Lce.Content(Result.SearchResults(mockEmptyListOfPodcasts)))

        //Then
        testObserverState.assertValue { it is ViewEffect.NoEffect }
    }
}


