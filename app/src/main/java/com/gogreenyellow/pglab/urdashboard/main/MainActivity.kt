package com.gogreenyellow.pglab.urdashboard.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest
import com.gogreenyellow.pglab.urdashboard.util.DateUtil
import kotlinx.android.synthetic.main.sheet_request_settings.*
import kotlinx.android.synthetic.main.sheet_slots.*

class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter

    companion object {
        const val UDACITY_DASHBOARD_URL = "https://mentor-dashboard.udacity.com/reviews/overview";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mm_open_udacity_website) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UDACITY_DASHBOARD_URL))
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun displaySubmissionRequests(submissionRequests: List<SubmissionRequest>) {
        if (submissionRequests.isNotEmpty()) {
            val first = submissionRequests.get(0);
            srs_queue_left_time.text = DateUtil.getTimeLeft(first.closedAt, this)
            srs_queue_end_time.text = DateUtil.getTime(first.closedAt)
        }
    }

    override fun displayAssignedSubmissions(assignedSubmissions: List<AssignedSubmission>) {

    }

}
