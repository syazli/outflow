package my.com.syazli.outflow.utils

import my.com.syazli.outflow.OutFlowApplication

object AuthHelper {

    fun getAuthState(): Boolean {
        return OutFlowApplication.getAppInstance().getPreference(OutFlowApplication.PREFERENCE_APP_AUTH, false)
    }

    fun setAuthState() {
        OutFlowApplication.getAppInstance().setPreference(OutFlowApplication.PREFERENCE_APP_AUTH, true)
    }

}