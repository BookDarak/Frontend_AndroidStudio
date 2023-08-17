package com.cookandroid.bookdarak_1.ui.viewmodel

//관심사 분리해서 뷰모델을 여러개 만든 이후로 얘는 필요 없어짐

/*
@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
    private val workManager: WorkManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //책 검색 결과 Livedata로 다루기
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> get() = _searchResult

    //책 검색 결과 가져오는 함수
    fun searchBooks(query: String) = viewModelScope.launch{
        val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
        if (response.isSuccessful){
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }

    // Room 이용
    fun saveBook(book: Book) = viewModelScope.launch {
        bookSearchRepository.insertBooks(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        bookSearchRepository.deleteBooks(book)
    }

    //라이브 데이터
    //val favoriteBooks: LiveData<List<Book>> = bookSearchRepository.getFavoriteBooks()

    //Flow
    //val favoriteBooks: Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()

    //StateFlow
    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())


    //SavedState
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object{
        private const val SAVE_STATE_KEY = "query"
        private const val WORKER_KEY = "cache_worker"
    }

    // DataStore
    fun saveSortMode(value: String) = viewModelScope.launch {
        bookSearchRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(Dispatchers.IO){
        bookSearchRepository.getSortMode().first()
    }

    fun saveCacheDeleteMode(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.saveCacheDeleteMode(value)
    }

    suspend fun getCacheDeleteMode() = withContext(Dispatchers.IO) {
        bookSearchRepository.getCacheDeleteMode().first()
    }

    // Paging

    //룸 페이징
    val favoritePagingBooks: StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())


    //레트로핏 페이징
    private val _searchPagingResult = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Book>> = _searchPagingResult.asStateFlow()

    fun searchBooksPaging(query: String) {
        viewModelScope.launch {
            bookSearchRepository.searchBooksPaging(query, getSortMode())
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }

    // WorkManager
    fun setWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()

        //주기적으로 일해라
        val workRequest = PeriodicWorkRequestBuilder<CacheDeleteWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        //동일한 일을 반복하지 않도록
        workManager.enqueueUniquePeriodicWork(
            WORKER_KEY, ExistingPeriodicWorkPolicy.REPLACE, workRequest
        )
    }

    fun deleteWork() = workManager.cancelUniqueWork(WORKER_KEY)

    fun getWorkStatus(): LiveData<MutableList<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData(WORKER_KEY)
}*/