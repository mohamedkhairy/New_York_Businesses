package com.example.newyorkbusinesses.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import arrow.core.Either
import arrow.core.Try
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newyorkbusinesses.R
import kotlinx.coroutines.Deferred
import java.io.IOException

suspend fun <T> callAPI(f: suspend () -> Deferred<T>): Either<Result.Error, Result.Success<T>> =
    Try { f().await() }
        .fold({ t ->
            t.printStackTrace()
            return@fold when (t) {
                is Errors.NoInternetException -> Either.left(Result.Error(Errors.NoConnectionError))
                else -> Either.left(Result.Error(Errors.UnknownError))
            }
        }, { r: T ->
            return@fold if (r.isNotNull()) Either.right(Result.Success(r))
            else Either.left(Result.Error(Errors.UnknownError))
        })


sealed class Result {
    data class Success<T>(val t: T) : Result()
    data class Error(val error: Errors = Errors.UnknownError) : Result()
}

sealed class Errors {
    object NoConnectionError : Errors()
    object UnknownError : Errors()
    class NoInternetException : IOException() {
        override val message: String = "No internet connection"
    }

}

fun Any?.isNotNull(): Boolean =
    this != null

fun Any?.isNull(): Boolean =
    this == null

fun List<*>?.isNotNullOrEmpty(): Boolean =
    this != null && this.isNotEmpty()


fun ImageView.loadAsyncImage(url: String?) {
    Glide.with(this.context).load(url).apply(RequestOptions().override(width, height))
        .transition(GenericTransitionOptions.with<Drawable>(android.R.anim.fade_in)).into(this)
}

fun AppCompatActivity.addFragment(
    incomingFragment: Lazy<Fragment>,
    tag: String) {
    val ft = supportFragmentManager.beginTransaction()

    ft.add(R.id.activity_main_container, incomingFragment.value, tag).addToBackStack(tag)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.replaceFragment(incomingFragment: Lazy<Fragment>,
                                      tag: String) {
    val fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
    if (fragment != null && fragment.isVisible) return
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(R.id.activity_main_container, incomingFragment.value, tag).commitAllowingStateLoss()
}



fun SearchView.onSearchTextChange(action: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }
        override fun onQueryTextChange(newText: String): Boolean {
            action.invoke(newText)
            return true
        }
    })
}


fun View.showView(){
    this.visibility = View.VISIBLE
}

fun View.hideView(){
    this.visibility = View.GONE

}
