# Space Library 操作手册(繁星计划之太空探索)

  代码是一种艺术

## 基础配置：
1.ARouter 实例化配置
```android
//如果是debug环境，isDebug()必须返回true

if (isDebug()) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
    ARouter.openLog();     // Print log
    ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
}
ARouter.init(mApplication); // As early as possible, it is recommended to initialize in the Application
```
注意：
> 1. ARouter 不只是Group设置组，path这个字段里最前面的两个『/』中间的部分是路由表中『组』的标识，后面的内容是具体表示，不同的Module不能使用相同的组，否则会报找不到Path的错误  
> 2. 每一个module在使用到ARouter的时候，都要使用注解，所以都需要添加以下代码：
```android
android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ moduleName : project.getName() ]
            }
        }
    }
}

dependencies {
    annotationProcessor "com.alibaba:arouter-compiler:${AROUTER_COMPILER}"
}
```



## 1.获取权限
  方法一：
```android     
      PermissionCheckActivity. checkPermission(Activity activity, String... permissions);    
```
  方法二：

```android         
      PermissionCheckActivity.checkPermission(Activity activity, int requestCode, String... permissions)
```

获取权限处理结果：
```android
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionCheckActivity.REQUEST_CODE){
            if (resultCode == PermissionCheckActivity.RESULT_CODE_SUCCESS){
                Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
            }else  if (resultCode == PermissionCheckActivity.RESULT_CODE_FAIL){
                Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
```
## 2.BackgroundLibrary
我在项目的基础上进行适配了4.0版本  

[项目地址]((https://github.com/JavaNoober/BackgroundLibrary))  

Android Studio 代码提示功能：

> windows: 进入目录C:\Users\XXX.AndroidStudio3.2\config\templates  
> 如果templates不存在，手动创建文件夹即可； 在templates下面加入文件BackgroundLibrary.xml 重启as即可。  
> BackgroundLibrary.xml 存放在项目的OtherResource