# PlaceHolderPlugin
占位式插件化
### Activity
思路：
   1. 在宿主中定义一个ProxyActivity，用于代理插件中Activity的相关事宜
   2. 定义一个协议标准用于宿主与插件之间的通信
   3. 首先是加载插件，就是初始化一个DexClassLoader和Resource,其中DexClassLoader的加载路径就是插件在SdCard中的路径
   4. 通过反射执行AssetsManager中的添加资源路径的方法，把插件中的资源加载进去完成Resource的初始化
   5. 通过插件路径找到对应的首个Activity,以参数的形式把Activity的全类名传递到ProxyActivity
   6. 在ProxyActivity中通过反射获取到插件中Activity的对象实例，然后执行对应的方法，完成交互
   7. 插件中的跳转 同样也是借助于ProxyActivity
 注意点：
 > 在android9.0之后用getPackageArchiveInfo通过SdCard路径是找不到对应的PackageInfo信息的，所以在代码中使用copy
 > 在使用权限上 需要添加 android:requestLegacyExternalStorage="true"这在华为手机会出现，其他手机没有测试
 > 插件中的其他Activity不需要在清单中注册，其实都是跳转到ProxyActivity,从ProxyActivity中执行相应的方法，因此ProxyActivity的启动模式只能是标准模式
 
### Service
  Service 与Activity一样
### 插件中静态注册广播
  1. 首先拿到宿主中Manifest中的信息，比如说静态广播的全类名，Action过滤器等
  2. 通过初始化的DexClassLoader把广播的全类名加载进来 然后转化为BroadCastReceiver
  3. 在宿主中动态注册
  
