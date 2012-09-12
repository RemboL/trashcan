The purpose of this project is to create a basic RCP Application compatible with Maven.

Eclipse RCP (and PDE in general) provides quite nice framework for creating executable applications. For those of you who are not familiar with this technology I'd like to point you toward Lars Vogel's tutorial at http://www.vogella.com/articles/EclipseRCP/article.html . Basically it's a framework for creating GUI applications (based on SWT library) with executable entry points - in form of exe or sh files (depending on your OS). It pretty much creates a mini-Eclipse instance with almost everything cut out.

The major disadvantage of this framework is its lack of direct compatibility with build tools such as Ant or Maven. Whole process of exporting such executable package is quite complicated. Add to it fact that RCP/PDE Applications are based on OSGi bundles which are something little different from maven artifacts (mandatory manifest.mf file). And this is where tycho plugin comes in.

Tycho is a maven plugin which compensates for this disadvantage providing complex support for PDE applications, plugins, features, etc. The one capability that I'm looking for here is exporting executable package.


Structure of this project is as follows:

pom.xml - main POM script. Notice tycho plugin and repository configuration. This repo contains basic PDE plugins in maven/tycho-friendly form. Neat :)
rcp-test-plugin/ - basic RCP "Hello World" plugin
 |-	src - source code of this plugin. Everything here is automatically generated with eclipse, nothing interesting.
 |-	plugin.xml - plugin definition. Also nothing interesting.
 \-	pom.xml - POM script for plugin definition. Only interesting thing here is packaging type of eclipse-plugin
rcp-test-application - now here is where magic happens...
 |-	app02-rcp-test.product - basic product definition. Only non-standard things here are os/ws/arch definitions in swt plugins (required by tycho multi-platform build, should correspond with definitions in main pom.xml) and custom plugin configurations - without those core.runtime won't start and your exported application will crash on starting.
 \-	pom.xml - the main result of my research. This pom has a packaging type of eclipse-repository since this one supposedly is going to supersede previous two previous types of eclipse-application and eclipse-update-site. Basically it creates an update-site for products with option of materializing them into executables.

All you need to do is run 'mvn install' on parent pom. Inside rcp-test-application/target/products you should find exported packages (zip-files) and unzipped folder with contents of those. Voila!


Contents of this project are based on following materials:
 * http://wiki.eclipse.org/Tycho/Packaging_Types - wiki page containing POM packaging types of tycho-based projects
 * http://wiki.eclipse.org/Tycho/Reference_Card - list of snippets from POM scripts.
 * http://www.vogella.com/articles/EclipseTycho/article.html
 * http://www.vogella.com/articles/Eclipse3RCP/article.html - two tutorials from Lars Vogel - nicely detailed and all.

