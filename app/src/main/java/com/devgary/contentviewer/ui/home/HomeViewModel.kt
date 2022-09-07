package com.devgary.contentviewer.ui.home

import androidx.lifecycle.*
import com.devgary.contentcore.model.content.Content
import com.devgary.contentlinkapi.content.BaseContentLinkHandler
import com.devgary.contentlinkapi.content.CompositeContentLinkHandler
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentlinkapi.handlers.FallthroughContentLinkHandler
import com.devgary.contentlinkapi.handlers.gfycat.GfycatContentLinkHandler
import com.devgary.contentlinkapi.handlers.imgur.ImgurContentLinkHandler
import com.devgary.contentlinkapi.handlers.streamable.StreamableContentLinkHandler
import com.devgary.contentviewer.BuildConfig
import com.devgary.contentviewer.util.cancel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val contentLinkHandler: CompositeContentLinkHandler by lazy {
        object : BaseContentLinkHandler() {
            override fun provideContentHandlers(): List<ContentLinkHandler> {
                return listOf(
                    GfycatContentLinkHandler(
                        clientId = BuildConfig.GFYCAT_CLIENT_ID,
                        clientSecret = BuildConfig.GFYCAT_CLIENT_SECRET
                    ),
                    ImgurContentLinkHandler(
                        authorizationHeader = BuildConfig.IMGUR_AUTHORIZATION_HEADER,
                        mashapeKey = BuildConfig.IMGUR_MASHAPE_KEY
                    ),
                    StreamableContentLinkHandler(),
                    FallthroughContentLinkHandler()
                )
            }
        }
    }

    private var getContentJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.postValue(throwable.message)
    }

    private val _content = MutableLiveData<Content>()
    val content: LiveData<Content> = _content
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    init {
        // TODO: Remove testing code
        if (content.value == null) {
            loadContent("https://imgur.com/gallery/EH7seB9")
        }
    }
    
    fun loadContent(url: String) {
        getContentJob.cancel()
        getContentJob = viewModelScope.launch(coroutineExceptionHandler) {
            contentLinkHandler.getContent(url)?.let { it ->
                _content.postValue(it)
            }
        }
    }
}