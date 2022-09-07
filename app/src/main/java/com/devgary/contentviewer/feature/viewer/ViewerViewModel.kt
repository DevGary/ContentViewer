package com.devgary.contentviewer.feature.viewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devgary.contentcore.model.content.Content
import com.devgary.contentlinkapi.content.ContentLinkHandler
import com.devgary.contentviewer.util.cancel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel @Inject constructor(
    private val contentLinkHandler: ContentLinkHandler,
) : ViewModel() {

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