
package views.html.partials

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import play.data._
import play.core.j.PlayFormsMagicForJava._

object _head extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.17*/(""";

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>"""),_display_(/*5.9*/title),format.raw/*5.14*/("""</title>

    <!-- Bootstrap core CSS -->
<link rel="stylesheet" media="screen" href="/assets/stylesheets/bootstrap.min.css">
<link rel="shortcut icon" type="image/png" href="/assets/images/favicon.ico">

    <!-- Custom styles for this template -->
<link rel="stylesheet" media="screen" href="/assets/stylesheets/jumbotron.css">"""))
      }
    }
  }

  def render(title:String): play.twirl.api.HtmlFormat.Appendable = apply(title)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (title) => apply(title)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Sat Sep 16 16:23:40 MDT 2017
                  SOURCE: C:/workspace/screenbuddy/app/views/partials/_head.scala.html
                  HASH: 73964e4b5e8cb4d2a63c721b9eadd2b2f6088ef4
                  MATRIX: 957->1|1067->16|1217->141|1242->146
                  LINES: 28->1|33->1|37->5|37->5
                  -- GENERATED --
              */
          