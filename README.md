# Popular Movies

*Popular Movies* is an Android app that shows you the the latest
popular movies from [The Movie DB](https://www.themoviedb.org/) website.

The application is part of Udacity's [Google Associate Android Developer Fast Track](https://www.udacity.com/course/associate-android-developer-fast-track--nd818).
It's purpose is to teach the students how to build a modern Android app
(e.g. which patterns to use, material design, how to be energy
efficient, etc).

You can find my version of the *Popular Movies* here. It is work in
progress, and it is built for education purposes only. If you need a
more sophisticated, finalized solution, I recommend [Dmitry Malkovich](https://github.com/DmitryMalkovich/popular-movies-app)'s
or [Emin Yahyayev](https://github.com/ewintory/udacity-popular-movies)'s version of the app.

![screen](../master/art/app-screens.png)

## Application secrets

In order to use The Movie DB service you need to register your own API
key (see [the documentation](https://www.themoviedb.org/documentation/api)).
To use your own API key in *Popular Movies* you will need to add it to
your local gradle configuration file. You can do that by adding the
following line to your `%USERPROFILE%\.gradle\gradle.properties` file:

    MyTheMovieDbApiKey="copy your api key here"

Here is an example:

    MyTheMovieDbApiKey="ajjs983i3JPEOIJD00384jdo203ks9732322jK"

## Coding style

There is no perfect coding guidelines except the one that everybody in
your team follows. I follow [Ribot's Android Guidelines](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md)
among with [CodePath's recommended directory structure](https://guides.codepath.com/android/Organizing-your-Source-Files)
and keeping [Futurice's Android Best Practices](https://github.com/futurice/android-best-practices)
in my mind.

## How to contribute

Thank you, but please do not contribute. :) The app is part of the
course evaluation process thus it should contain my work only.
Nevertheless, you can open an issue if you think there are things which
needs to be improved. I appreciate if you teach me.
