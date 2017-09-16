
package views.html

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

object secondary extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {
/*2.2*/import views.html.partials._head
/*3.2*/import views.html.partials._navigation


Seq[Any](format.raw/*1.32*/("""
"""),format.raw/*4.1*/("""
"""),format.raw/*5.1*/("""<!DOCTYPE html>
<html lang="en">
    <head>
    """),_display_(/*8.6*/_head(title)),format.raw/*8.18*/("""
    """),format.raw/*9.5*/("""</head>

    <body>

        """),_display_(/*13.10*/_navigation()),format.raw/*13.23*/("""

        """),format.raw/*15.9*/("""<div class="container">

            """),_display_(/*17.14*/content),format.raw/*17.21*/("""

            """),format.raw/*19.13*/("""<footer>
                <p>&copy; Company 2017</p>
            </footer>
        </div> <!-- /container -->


        <!-- Bootstrap core JavaScript
    ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="/assets/javascripts/jquery-3.2.1.slim.min.js"></script>
        <script src=""""),_display_(/*29.23*/routes/*29.29*/.Assets.versioned("javascripts/vendor/popper.min.js")),format.raw/*29.82*/("""" type="text/javascript"></script>
        <script src=""""),_display_(/*30.23*/routes/*30.29*/.Assets.versioned("javascripts/bootstrap.min.js")),format.raw/*30.78*/("""" type="text/javascript"></script>
            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src=""""),_display_(/*32.23*/routes/*32.29*/.Assets.versioned("javascripts/ie10-viewport-bug-workaround.js")),format.raw/*32.93*/("""" type="text/javascript"></script>
    </body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Sat Sep 16 16:00:12 MDT 2017
                  SOURCE: C:/workspace/screenbuddy/app/views/secondary.scala.html
                  HASH: 148b349888ee72611b1c4a3830bab7401dfbe378
                  MATRIX: 957->1|1060->34|1100->69|1168->31|1196->109|1224->111|1301->163|1333->175|1365->181|1426->215|1460->228|1499->240|1566->280|1594->287|1638->303|2059->697|2074->703|2148->756|2233->814|2248->820|2318->869|2478->1002|2493->1008|2578->1072
                  LINES: 28->1|31->2|32->3|35->1|36->4|37->5|40->8|40->8|41->9|45->13|45->13|47->15|49->17|49->17|51->19|61->29|61->29|61->29|62->30|62->30|62->30|64->32|64->32|64->32
                  -- GENERATED --
              */
          