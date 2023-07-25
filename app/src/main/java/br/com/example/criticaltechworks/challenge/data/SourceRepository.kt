package br.com.example.criticaltechworks.challenge.data

import br.com.example.criticaltechworks.challenge.data.entity.SourceEntity
import br.com.example.criticaltechworks.challenge.data.local.SourceDao
import br.com.example.criticaltechworks.challenge.data.remote.RemoteDataSource
import br.com.example.criticaltechworks.challenge.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SourceRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sourceDao: SourceDao
) {
    suspend fun fetchSources(): Flow<Result<List<SourceEntity>>?> {
        return flow {
            val cachedData = fetchSourceCached()
            emit(Result.loading())
            emit(cachedData)

            val result = remoteDataSource.fetchSources()
            //Cache to database if response is successful
            if (result.status == Result.Status.SUCCESS) {
                result.data?.sources?.let { newSources ->
                    sourceDao.deleteAll(cachedData?.data!!)
                    sourceDao.insertAll(newSources)
                    emit(Result.success(newSources))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun fetchSourceCached(): Result<List<SourceEntity>>? =
        sourceDao.getAll()?.let { source->
            Result.success(source)
        }

}