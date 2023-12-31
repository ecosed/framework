/**
 * Copyright EcosedKit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ecosed.engine

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AlertDialogLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import io.ecosed.client.EcosedClient
import io.ecosed.common.FlutterPluginProxy
import io.ecosed.common.MethodCallProxy
import io.ecosed.common.ResultProxy
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedMethodCall
import io.ecosed.plugin.EcosedResult
import io.ecosed.plugin.PluginBinding

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件引擎
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
open class EcosedEngine : EcosedPlugin(), FlutterPluginProxy, LifecycleOwner,
    DefaultLifecycleObserver, SensorEventListener {

    override val channel: String
        get() = "ecosed_engine"

    /** 应用程序全局类. */
   // private lateinit var mApp: Application

    /** 基础上下文. */
   // protected lateinit var mBase: Context

    /** 客户端组件. */
    //private lateinit var mHost: EcosedHost

    /** 应用程序全局上下文, 非UI上下文. */
 //   protected lateinit var mContext: Context

    /**  */
    private lateinit var mClient: EcosedClient

    /** 插件绑定器. */
    private var mBinding: PluginBinding? = null

    /** 插件列表. */
    private var mPluginList: ArrayList<EcosedPlugin>? = null

    private val isBaseDebug: Boolean = AppUtils.isAppDebug()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    private var execResult: Any? = null
    override fun getActivity(activity: Activity) {
        mActivity = activity
    }

    private lateinit var mLifecycle: Lifecycle
    private lateinit var mActivity: Activity

    override fun getLifecycle(lifecycle: Lifecycle) {
        mLifecycle = lifecycle
    }

    override val lifecycle: Lifecycle
        get() = mLifecycle

    override fun onEcosedAdded(binding: PluginBinding) {
        super.onEcosedAdded(binding)
    }

    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
        super.onEcosedMethodCall(call, result)

    }

    override fun onMethodCall(call: MethodCallProxy, result: ResultProxy) {
//        try {
//            val bundle = Bundle()
//            bundle.putString(
//                call.argument<String>("key"),
//                call.argument<String>("value")
//            )
//            execResult = execMethodCall<Any>(
//                channel = EcosedClient.mChannelName,
//                method = call.method,
//                bundle = bundle
//            )
//            if (execResult != null) {
//                result.success(execResult)
//            } else {
//                result.notImplemented()
//            }
//        } catch (e: Exception) {
//            result.error(tag, "", e)
//        }
    }



    /**
     * 将引擎附加到应用.
     */
    override fun attach() {

        when {
            (mPluginList == null) or (mBinding == null) -> apply {
                // 引擎附加基本上下文
                attachBaseContext(
                    base = mActivity
                )
                lifecycle.addObserver(this@EcosedEngine)
                // 初始化客户端组件
                mClient = EcosedClient.build()
                // 初始化插件绑定器.
                mBinding = PluginBinding(
                    context = this@EcosedEngine, debug = isBaseDebug
                )
                // 初始化插件列表.
                mPluginList = arrayListOf()
                // 加载框架
                mClient.let { ecosed ->
                    mBinding?.let { binding ->
                        ecosed.apply {
                            try {
                                onEcosedAdded(binding = binding)
                                if (isBaseDebug) {
                                    Log.d(tag, "框架已加载")
                                }
                            } catch (e: Exception) {
                                if (isBaseDebug) {
                                    Log.e(tag, "框架加载失败!", e)
                                }
                            }
                        }
                    }.run {
                        mPluginList?.add(element = ecosed)
                        if (isBaseDebug) {
                            Log.d(tag, "框架已添加到插件列表")
                        }
                    }
                }
//                // 添加所有插件.
//                mHost.getPluginList()?.let { plugins ->
//                    mBinding?.let { binding ->
//                        plugins.forEach { plugin ->
//                            plugin.apply {
//                                try {
//                                    onEcosedAdded(binding = binding)
//                                    if (isDebug) {
//                                        Log.d(tag, "插件${plugin.javaClass.name}已加载")
//                                    }
//                                } catch (e: Exception) {
//                                    if (isDebug) {
//                                        Log.e(tag, "插件添加失败!", e)
//                                    }
//                                }
//                            }
//                        }
//                    }.run {
//                        plugins.forEach { plugin ->
//                            mPluginList?.add(element = plugin)
//                            if (isDebug) {
//                                Log.d(tag, "插件${plugin.javaClass.name}已添加到插件列表")
//                            }
//                        }
//                    }
//                }
            }

            else -> if (isBaseDebug) {
                Log.e(tag, "请勿重复执行attach!")
            }
        }
    }

    // Flutter插件Activity生命周期
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        var dialog: AlertDialog

        AlertDialog.Builder(this@EcosedEngine).apply {
            setTitle("Flutter Ecosed Dev Menu")
            setMessage("Running ")

            setPositiveButton("确定") { dialog, which ->

            }
            setNegativeButton("取消") { dialog, which ->

            }
            dialog = create()
        }


        if (!dialog.isShowing) dialog.show()


    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    /**
     * 调用插件代码的方法.
     * @param channel 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值,类型为Any?.
     */
    internal fun <T> execMethodCall(
        channel: String,
        method: String,
        bundle: Bundle?,
    ): T? {
        var result: T? = null
        try {
            mPluginList?.forEach { plugin ->
                plugin.getPluginChannel.let { pluginChannel ->
                    when (pluginChannel.getChannel()) {
                        channel -> {
                            result = pluginChannel.execMethodCall<T>(
                                name = channel, method = method, bundle = bundle
                            )
                            if (isBaseDebug) {
                                Log.d(
                                    tag,
                                    "插件代码调用成功!\n通道名称:${channel}.\n方法名称:${method}.\n返回结果:${result}."
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (isBaseDebug) {
                Log.e(tag, "插件代码调用失败!", e)
            }
        }
        return result
    }

    companion object {

        /** 用于打印日志的标签. */
        private const val tag: String = "PluginEngine"

        fun build(): EcosedEngine {
            return EcosedEngine()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}