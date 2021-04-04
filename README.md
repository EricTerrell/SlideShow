# SlideShow

SlideShow turns a laptop computer into a high-capacity photo frame. Runs on Windows 10. SlideShow can run for months with no issues.

# Screenshot

![`Vault 3 Screenshot`](https://ericbt.com/uploaded_images/photoframe.jpg "SlideShow")

# Copyright

SlideShow &#169; Copyright 2021, [`Eric Bergman-Terrell`](https://www.ericbt.com)

# Links

* [`website`](https://ericbt.com/)
* [`GitHub repo`](https://github.com/EricTerrell/SlideShow)

# Building for Windows 10

1. [`Download`](https://archive.eclipse.org/eclipse/downloads/drops4/R-4.18-202012021800/) the appropriate version of the swt.jar file.
1. Update the ant build script to point to the swt.jar file that you downloaded.
1. Run the ant build script (build.xml). It writes a SlideShow.jar file to C:/Temp.

# How to Run

```
java -jar {path to SlideShow.jar file} {path to folder containing image files} {seconds to display each image} {image file types}
```

For example:

```
java -jar C:/Temp/SlideShow.jar C:\Temp\Vault3Photos.001\ 1 ".jpg" ".jpeg" ".png" 10
```

# Building for Linux or OSX

1. [`Download`](https://archive.eclipse.org/eclipse/downloads/drops4/R-4.18-202012021800/) the appropriate version of the swt.jar file.
1. Update the ant build script to point to the swt.jar file.
1. Run the updated ant build script.

# License

[`GPL3`](https://www.gnu.org/licenses/gpl-3.0.en.html)

# Known Issues

1. When SlideShow is run from a command line, pressing Esc will stop the program. When SlideShow is run automatically at startup, pressing Esc has no effect.

# Feedback

Please submit your feedback to [SlideShow@EricBT.com](mailto:SlideShow@EricBT.com).