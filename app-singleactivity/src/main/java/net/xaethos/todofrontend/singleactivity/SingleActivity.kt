package net.xaethos.todofrontend.singleactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import net.xaethos.todofrontend.singleactivity.util.bindView

class SingleActivity : AppCompatActivity() {

    private val container: ViewGroup by bindView(R.id.controller_container)

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ToDoListController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }
}
