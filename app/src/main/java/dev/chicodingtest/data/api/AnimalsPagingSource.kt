package dev.chicodingtest.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.chicodingtest.data.mappers.toAnimalModel
import dev.chicodingtest.domain.model.Animal
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AnimalsPagingSource @Inject constructor(private val animalsRemoteDataSource: AnimalsRemoteDataSource) : PagingSource<Int, Animal>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        return try {
            val nextPageNumber = params.key?.plus(1) ?: 1
            val response = animalsRemoteDataSource.fetchAnimalImages().toAnimalModel()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Animal>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}