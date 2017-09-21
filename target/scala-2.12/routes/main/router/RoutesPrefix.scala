
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/playframework/screenbuddy/conf/routes
// @DATE:Thu Sep 21 11:39:51 MDT 2017


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
