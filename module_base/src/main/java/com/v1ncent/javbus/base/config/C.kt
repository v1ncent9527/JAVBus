package com.v1ncent.javbus.base.config

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/8
 * 描    述：Constant
 * 修订历史：
 * ================================================
 */
interface C {

    object Common {
    }

    /**
     * 规则 :  /(module后缀)/(所在类名)
     * 路由 A_ : Activity
     *     F_ : Fragment
     */
    interface RouterPath {
        object Common {
            private const val Common = "/common"
            const val A_H5 = "${com.v1ncent.javbus.base.config.C.RouterPath.Common.Common}/H5Activity"
        }

        object Main {
            private const val MAIN = "/main"
            const val A_MAIN = "${com.v1ncent.javbus.base.config.C.RouterPath.Main.MAIN}/MainActivity"
        }
    }

    object BusTAG {
        const val LOGIN_STATUE = "LOGIN_STATUE"
    }


}