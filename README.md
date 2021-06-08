# Sammy
#### _Accessibility Media Player_

Sammy is a smart mediaplayer, that enables understanding of videos for the visually-disabled and quicker navigation for everyone

- ✨ Sammy is a multimedia player that can help you find, in a video, anything from a simple phrase that was spoken to a scene that you can describe in words.
- ✨ We combined OCR (Optical Character Recognition), ASR (Automatic Speech Recognition) and Scene Understanding (SDD) to describe the scene taking place in the video and to make this information searchable.

###### Problem
## WHAT ARE WE SOLVING?

Imagine a world where even specially-enabled people are able to understand and navigate through video content and where nobody has to spend ages searching for a single line in hour-long videos.

Today, we don’t have a system that can make the content inside videos searchable and describable. Videos/movies for the optically-disabled have to be professionally voiced through an expensive process which makes them inaccessible. This lack of a system is also a problem when one is trying to pick up a small piece of information in a long video.
## Features

- Can search for a particular phrase through your voice
- It can Describe the video with voice and text
- User can upload video of their choice

## Tech Used
Sammy uses a number of open source projects to work properly:
- ### Android
    - Java
    - XML
    - ExoPlayer
    - Retrofit - for REST API
    - Text to Speak by Google
    - Speech-to-Text by Google
    - MVP architecture

- ### Backend
    - NodeJS
    - Web/Worker processes
        - Packages that internally use Redis - for job queues
    - Video API
        - Packages that internally use ffmpeg - Detect scenes, extract frames
        - Azure Cloud Vision
        - Tesseract - for OCR
        - Sharp
        - Express
    - Audio API
        - Packages that internally use ffmpeg - to extract audio
        - Azure cognitive speech services/DeepSpeech


### Related links
- ###### [GitHub Repository | Web](https://github.com/PlytonRexus/sammy-node)
- ###### [GitHub Repository | Video Node](https://github.com/PlytonRexus/sammy-node)
- ###### [GitHub Repository | Audio Node](https://github.com/PlytonRexus/sammy-node-audio)
- ###### [Sammy - Slides/Presentatiom](https://docs.google.com/presentation/d/1FNzMfHjVQVPnZ-rpnFb149bxvdE5WuJT0RT1AOqp2r0/edit?usp=sharing)

***
##### You can run this Android app on [Appetize.io]

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [Appetize.io]: <https://developer.android.com/studio>
   [Android Studio]: <https://developer.android.com/studio>
   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
