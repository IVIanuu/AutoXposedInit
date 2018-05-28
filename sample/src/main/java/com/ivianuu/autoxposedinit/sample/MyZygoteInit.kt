package com.ivianuu.autoxposedinit.sample

import com.ivianuu.autoxposedinit.AutoXposedInit
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge

@AutoXposedInit
class MyZygoteInit : IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        XposedBridge.log("hello from my zygote init")
    }

}