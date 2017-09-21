
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/playframework/screenbuddy/conf/routes
// @DATE:Thu Sep 21 11:39:51 MDT 2017

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:8
  class ReverseSecondary(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def signUp(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "signup")
    }
  
    // @LINE:9
    def search(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "search")
    }
  
  }

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:12
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:12
    def versioned(file:Asset): Call = {
    
      (file: @unchecked) match {
      
        // @LINE:12
        case (file)  =>
          implicit val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public")))
          Call("GET", _prefix + { _defaultPrefix } + "public/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
      
      }
    
    }
  
  }


}
