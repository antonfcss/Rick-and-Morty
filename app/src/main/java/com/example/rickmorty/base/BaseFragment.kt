package com.example.rickmorty.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<State>, State> :
    Fragment() {

    protected lateinit var viewModel: VM
    private val viewModelClass: Class<VM>
        get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("ViewBinding is null")

    private val bindingClass: Class<VB>
        get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>

    private fun <T : ViewBinding> bindView(
        bindingClass: Class<T>,
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): T {
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, inflater, container, false) as T
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(bindingClass, layoutInflater, container)
        viewModel = ViewModelProvider(this)[viewModelClass]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEmptyDataLiveData().observe(viewLifecycleOwner, ::showEmptyDataDialog)
        viewModel.viewModelState.observe(viewLifecycleOwner, ::renderState)
    }

    private fun showEmptyDataDialog(error: String?) {
        error?.let { noDataError ->
            viewModel.onPostEmptyDataDialogShown()
            AlertDialog.Builder(requireContext()).setTitle("No data were received")
                .setMessage(noDataError)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun renderState(viewState: ViewState<State>) {
        when (viewState) {
            is ViewState.Success<State> -> renderSuccessState(viewState)
            is ViewState.Error -> renderErrorState(viewState)
            is ViewState.Loading -> renderLoadingState(viewState)
        }
    }

    abstract fun renderSuccessState(viewState: ViewState.Success<State>)

    open fun renderErrorState(viewState: ViewState.Error) {}

    open fun renderLoadingState(viewState: ViewState.Loading) {}

}