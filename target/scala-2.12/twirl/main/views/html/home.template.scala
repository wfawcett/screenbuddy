
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

object home extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

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
        """),_display_(/*8.10*/_head(title)),format.raw/*8.22*/("""
    """),format.raw/*9.5*/("""</head>

    <body>

    """),_display_(/*13.6*/_navigation()),format.raw/*13.19*/("""

            """),format.raw/*15.13*/("""<!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Welcome to Screenbuddy</h1>
                <p>This classes leading movie reminder service.</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more &raquo;</a></p>
            </div>
        </div>

        <div class="container">
                <!-- Example row of columns -->
            <div class="row">
                <div class="col-md-4">
                    <h2>Sign Up</h2>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                    <p><a class="btn btn-secondary" href="#" role="button">View details &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2>Select your Services</h2>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                    <p><a class="btn btn-secondary" href="#" role="button">View details &raquo;</a></p>
                </div>
                <div class="col-md-4">
                    <h2>Pick your movies</h2>
                    <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
                    <p><a class="btn btn-secondary" href="#" role="button">View details &raquo;</a></p>
                </div>
            </div>

            <hr>

            <footer>
                <p>&copy; Company 2017</p>
            </footer>
        </div> <!-- /container -->


        <!-- Bootstrap core JavaScript
    ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="/assets/javascripts/jquery-3.2.1.slim.min.js"></script>
        <script src=""""),_display_(/*56.23*/routes/*56.29*/.Assets.versioned("javascripts/vendor/popper.min.js")),format.raw/*56.82*/("""" type="text/javascript"></script>
        <script src=""""),_display_(/*57.23*/routes/*57.29*/.Assets.versioned("javascripts/bootstrap.min.js")),format.raw/*57.78*/("""" type="text/javascript"></script>
            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src=""""),_display_(/*59.23*/routes/*59.29*/.Assets.versioned("javascripts/ie10-viewport-bug-workaround.js")),format.raw/*59.93*/("""" type="text/javascript"></script>
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
                  DATE: Sat Sep 16 15:50:54 MDT 2017
                  SOURCE: C:/workspace/screenbuddy/app/views/home.scala.html
                  HASH: 8c8c9c2938b0f14e7ec69543fe28e2dd00055769
                  MATRIX: 952->1|1055->33|1095->67|1163->31|1190->106|1217->107|1296->160|1328->172|1359->177|1411->203|1445->216|1487->230|3894->2610|3909->2616|3983->2669|4067->2726|4082->2732|4152->2781|4310->2912|4325->2918|4410->2982
                  LINES: 28->1|31->2|32->3|35->1|36->4|37->5|40->8|40->8|41->9|45->13|45->13|47->15|88->56|88->56|88->56|89->57|89->57|89->57|91->59|91->59|91->59
                  -- GENERATED --
              */
          