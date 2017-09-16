
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/workspace/screenbuddy/conf/routes
// @DATE:Sat Sep 16 16:00:12 MDT 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
