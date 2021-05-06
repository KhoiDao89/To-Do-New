package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentCategoryBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.common.BaseFragment

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val viewModel by sharedViewModel<CategoryViewModel>()
    // val mesage: String? = getArguments()?.getString("category")
    override val layoutId: Int
        get() = R.layout.fragment_category

    //private lateinit var viewModel: CategoryViewModel

    private val taskAdapter by lazy {
        TaskAdapter(
            importantCallback = {
                // Xu ly cap nhat database truong important cho task model
                viewModel.updateImportant(it)
            },
            finishedCallback = {
                viewModel.updateComplete(it)
            }
        )
    }

    override fun init() {
        super.init()
        //       viewModel.insertTask()
//    viewModel.insertTask()
//    viewModel.insertTask()
//        viewModel.insertCategory()
        viewModel.loadCategory()
        viewModel.loadData()
        binding.event = EventAddTask()
    }

    override fun initUi() {
        super.initUi()
//    cat.apply {
//      text = mesage
//    }
        recycleView1.apply {
            adapter = taskAdapter
        }
    }

    override fun registerLiveDataListener() {
        super.registerLiveDataListener()

        viewModel.itemList.observe(
            this,
            {
                taskAdapter.setItems(it)
            }
        )

//        viewModel.itemTask.observe(
//            this,
//            {
//                taskAdapter.setItem(it)
//            }
//        )
        viewModel.updateImportantCompleted.observe(
            this,
            {
                viewModel.loadData()
            }
        )

        viewModel.itemCategory.observe(
            this,
            {
                safeActivity.supportActionBar?.title = it.name
            }
        )

//    viewModel.updateFinishedCompleted.observe(this, {
//      viewModel.loadData()
//    })
        viewModel.addTaskCompleted.observe(
            this@CategoryFragment,
            Observer {
                if (!it) return@Observer
                viewModel.loadData()
            }
        )
    }
    inner class EventMenu() {
        override fun
    }
    inner class EventAddTask() {
        fun onNewTask() {
            MaterialDialog(safeContext).show {
                input(
                    hint = resources.getString(R.string.new_task_hint),
                ) { _, title ->
                    val model = TaskModel(
                        name = title.toString(),
                        catId = viewModel.categoryId?.toInt(),
                        createdAt = LocalDateTime.now()
                    )
                    viewModel.addNewTask(model)
                }
                positiveButton(R.string.button_create_task)
                negativeButton(R.string.button_delete)
            }
        }
    }

    /**
     * Static definition
     */
    companion object {

        /**
         * Create MainFragment instance pattern
         */
        fun newInstance() = CategoryFragment()
    }
}