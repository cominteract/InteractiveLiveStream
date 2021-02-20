package com.ainsigne.interactivelivestreaming

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.ainsigne.interactivelivestreaming.fake.*
import com.ainsigne.interactivelivestreaming.model.StreamUser
import com.ainsigne.interactivelivestreaming.model.Streams
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.util.*
import java.util.stream.Stream

class StreamingUnitTest {

    inline fun <reified T> lambdaMock(): T = Mockito.mock(T::class.java)

    lateinit var repository: FakeStreamItemRepository
    lateinit var detailsRepo : FakeStreamItemDetailRepository
    lateinit var viewmodel: FakeStreamItemViewModel
    lateinit var detailsViewmodel: FakeStreamItemDetailViewModel
    lateinit var userRepo: FakeStreamUserRepository
    lateinit var userViewModel: FakeStreamUserViewModel
    lateinit var streamId : String
    @Before
    fun setup(){
        streamId = UUID.randomUUID().toString()
        repository = FakeStreamItemRepository()
        detailsRepo = FakeStreamItemDetailRepository()
        userRepo = FakeStreamUserRepository()
        userViewModel = FakeStreamUserViewModel(userRepo)
        viewmodel = FakeStreamItemViewModel(repository)
        detailsViewmodel = FakeStreamItemDetailViewModel(detailsRepo)
    }

    fun observeStreamChanges(lifecycle: Lifecycle, observer: (List<Streams>) -> Unit) {
        viewmodel.streams.observe( { lifecycle } ) { streams ->
            streams?.let(observer)
        }
    }

    fun observeStreamUpdateChanges(channel : String, lifecycle: Lifecycle, observer: (Streams) -> Unit) {
        viewmodel.streams.observe( { lifecycle } ) { streams ->
            streams?.filter { it.channel == channel }?.first()?.let(observer)
//            streams?.let(observer)
        }
    }


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun testStreamItems() {
        val observer = lambdaMock<(List<Streams>) -> Unit>()
        val observerStream = lambdaMock<(Streams) -> Unit>()
        val lifecycle = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        observeStreamChanges(lifecycle,observer)
        var stream = Streams(channel = UUID.randomUUID().toString(), StreamUser("user7",
                Date().toStringFormat()), Date().toStringFormat(), true, "data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA\n" +
                "    AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO\n" +
                "        9TXL0Y4OHwAAAABJRU5ErkJggg=="
                , (7 * 11111)
        )
        repository.createStream(stream)
        // verify that it is added and it is the same list that is being updated meaning the repository works!
        viewmodel.streams.value?.let {
            assert(it.size == 6)
            Mockito.verify(observer).invoke(it)
        }


        stream.status = false
        repository.updateStream(stream)
        observeStreamUpdateChanges(stream.channel, lifecycle,observerStream)
        viewmodel.streams.value?.let {
            assert(!it.last().status)
            Mockito.verify(observerStream).invoke(it.last())
        }
        repository.updateStream(stream)
    }

}