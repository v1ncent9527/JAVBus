package com.v1ncent.javbus.base

import android.app.Activity
import android.app.Application
import android.util.Log
import android.util.Log.INFO
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.didichuxing.doraemonkit.DoraemonKit
import com.drake.net.NetConfig
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetErrorHandler
import com.drake.net.okhttp.*
import com.drake.net.request.BaseRequest
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.tencent.bugly.crashreport.CrashReport
import com.v1ncent.javbus.base.net.Host
import com.v1ncent.javbus.base.net.converter.GsonConverter
import com.v1ncent.javbus.base.net.cookie.CookieManger
import com.v1ncent.javbus.base.net.error.toast
import com.v1ncent.javbus.base.utils.MMKVUtils
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.design.app.SkinMaterialViewInflater
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/8/5
 * 描    述：App
 * 修订历史：
 * ================================================
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Bugly
        CrashReport.initCrashReport(this, "666018d4a4", true)
        //工具基类初始化
        Utils.init(this)
        //ARouter初始化
        ARouter.init(this)
        //mmkv初始化
        MMKVUtils.init(this)
        //Log初始化
        initLog()
        //Skin初始化
        initSkin()
        //初始化Net
        initNet()
        //初始化BRV
        initBRV()

        //Debug模式下开启
        if (AppUtils.isAppDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
            CrashUtils.init()
            DoraemonKit.install(this)
        }


    }

    //初始化日志工具类
    private fun initLog() {
        val config = LogUtils.getConfig()
            .setLogSwitch(AppUtils.isAppDebug()) // 设置 log 总开关，包括输出到控制台和文件，默认开
            .setConsoleSwitch(AppUtils.isAppDebug()) // 设置是否输出到控制台开关，默认开
            .setGlobalTag(null) // 设置 log 全局标签，默认为空
            // 当全局标签不为空时，我们输出的 log 全部为该 tag，
            // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
            .setLogHeadSwitch(true) // 设置 log 头信息开关，默认为开
            .setLog2FileSwitch(true) // 打印 log 时是否存到文件的开关，默认关
            .setDir("") // 当自定义路径为空时，写入应用的/cache/log/目录中
            .setFilePrefix("") // 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd$fileExtension"
            .setFileExtension(".log") // 设置日志文件后缀
            .setBorderSwitch(true) // 输出日志是否带边框开关，默认开
            .setSingleTagSwitch(true) // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
            .setConsoleFilter(LogUtils.V) // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
            .setFileFilter(LogUtils.E) // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
            .setStackDeep(1) // log 栈深度，默认为 1
            .setStackOffset(0) // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
            .setSaveDays(1) // 设置日志可保留天数，默认为 -1 表示无限时长
            // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
            .addFormatter(object : LogUtils.IFormatter<ArrayList<*>>() {
                override fun format(arrayList: ArrayList<*>): String {
                    return "LogUtils Formatter ArrayList { $arrayList }"
                }
            })
        LogUtils.i(config.toString())
    }

    //初始化皮肤切换
    private fun initSkin() {
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater())
            .addInflater(SkinCardViewInflater())
            .addInflater(SkinMaterialViewInflater())
            .setSkinStatusBarColorEnable(false) // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin()
    }

    //初始化Net
    private fun initNet() {
        NetConfig.init(host = Host.BASE_HOST) {
            // 超时设置
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)

            //添加日志拦截器
            addInterceptor(
                LoggingInterceptor.Builder()
                    .setLevel(if (AppUtils.isAppDebug()) Level.BASIC else Level.NONE)
                    .log(INFO)
                    .build()
            )
            //信任所有证书
            trustSSLCertificate()
        }

        NetConfig.init(host = Host.BASE_HOST) {
            // 超时设置
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)

            //添加解析器
            setConverter(GsonConverter())
            //作用域发生异常是否打印
            setLog(AppUtils.isAppDebug())
            //添加日志拦截器
            addInterceptor(
                LoggingInterceptor.Builder()
                    .setLevel(if (AppUtils.isAppDebug()) Level.BASIC else Level.NONE)
                    .log(Log.INFO)
                    .build()
            )
            //添加Cas401拦截器
//            addInterceptor(Cas401Interceptor())
            //添加请求拦截器
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                }
            })
            //添加Cookie
            cookieJar(CookieManger(applicationContext))
            //信任所有证书
            trustSSLCertificate()
            //全局错误处理器
            setErrorHandler(object : NetErrorHandler {
                override fun onError(e: Throwable) {
                    e.toast()
                }
            })
        }
    }

    //初始化BindingAdapter
    private fun initBRV() {
//        BRV.modelId = BR.rvModel
//
//        StateConfig.apply {
//            emptyLayout = R.layout.layout_empty
//            errorLayout = R.layout.layout_error
//            loadingLayout = R.layout.layout_loading
//
//            setRetryIds(R.id.msg, R.id.iv)
//        }
    }

    //App前后端切换监听
    fun registerAppStatusChangedListener() {
        AppUtils.registerAppStatusChangedListener(object : Utils.OnAppStatusChangedListener {
            override fun onForeground(activity: Activity) {
            }

            override fun onBackground(activity: Activity) {
            }
        })
    }
}